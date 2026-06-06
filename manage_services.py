#!/usr/bin/env python3
import argparse
import json
import os
import signal
import socket
import subprocess
import sys
import threading
import time
from pathlib import Path

ROOT = Path(__file__).resolve().parent
STATE_FILE = ROOT / ".services_state.json"
SERVICES = [
    ("registry-server", ROOT / "registry-server", 8761),
    ("config-server", ROOT / "config-server", 7777),
    ("report-ms", ROOT / "report-ms", 7070),
    ("companies-crud", ROOT / "companies-crud", 8081),
    ("gateway", ROOT / "gateway", 4040),
]


def wait_port(port, timeout=90):
    end = time.time() + timeout
    while time.time() < end:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
            sock.settimeout(1)
            if sock.connect_ex(("127.0.0.1", port)) == 0:
                return True
        time.sleep(1)
    return False


def stream_logs(name, proc):
    for line in iter(proc.stdout.readline, ""):
        print(f"[{name}] {line}", end="")


def mvn_cmd(service_dir):
    if os.name == "nt":
        return [str((service_dir / "mvnw.cmd").resolve()), "spring-boot:run"]
    return [str((service_dir / "mvnw").resolve()), "spring-boot:run"]


def compose_up_db():
    subprocess.run(["docker", "compose", "up", "-d", "db"], cwd=ROOT, check=True)


def compose_stop_db():
    subprocess.run(["docker", "compose", "stop", "db"], cwd=ROOT, check=False)


def kill_pid(pid):
    if os.name == "nt":
        subprocess.run(["taskkill", "/PID", str(pid), "/T", "/F"], check=False, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    else:
        try:
            os.kill(pid, signal.SIGTERM)
        except ProcessLookupError:
            return


def start_all():
    if STATE_FILE.exists():
        print("State file exists. Run stop first.")
        return 1
    compose_up_db()
    if not wait_port(5432, 60):
        print("Database did not become ready on port 5432.")
        return 1
    procs, state = [], {"services": []}
    for name, service_dir, port in SERVICES:
        proc = subprocess.Popen(mvn_cmd(service_dir), cwd=service_dir, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, text=True, bufsize=1)
        threading.Thread(target=stream_logs, args=(name, proc), daemon=True).start()
        procs.append((name, proc))
        state["services"].append({"name": name, "pid": proc.pid})
        if not wait_port(port, 120):
            print(f"{name} did not become ready on port {port}.")
            break
    STATE_FILE.write_text(json.dumps(state, indent=2), encoding="utf-8")
    try:
        while True:
            dead = [name for name, proc in procs if proc.poll() is not None]
            if dead:
                print("Detected stopped service:", ", ".join(dead))
                break
            time.sleep(1)
    except KeyboardInterrupt:
        print("Stopping services...")
    finally:
        for _, proc in procs:
            if proc.poll() is None:
                kill_pid(proc.pid)
        compose_stop_db()
        if STATE_FILE.exists():
            STATE_FILE.unlink()
    return 0


def stop_all():
    if not STATE_FILE.exists():
        print("No state file found. Stopping db container only.")
        compose_stop_db()
        return 0
    state = json.loads(STATE_FILE.read_text(encoding="utf-8"))
    for svc in state.get("services", []):
        kill_pid(svc.get("pid"))
        print(f"Stopped {svc.get('name')} (pid {svc.get('pid')})")
    compose_stop_db()
    STATE_FILE.unlink(missing_ok=True)
    return 0


def status_all():
    print("DB listening on 5432:", "yes" if wait_port(5432, 1) else "no")
    for name, _, port in SERVICES:
        print(f"{name} listening on {port}:", "yes" if wait_port(port, 1) else "no")
    return 0


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("command", choices=["start", "stop", "restart", "status"])
    args = parser.parse_args()
    if args.command == "start":
        return start_all()
    if args.command == "stop":
        return stop_all()
    if args.command == "restart":
        stop_all()
        return start_all()
    return status_all()


if __name__ == "__main__":
    sys.exit(main())