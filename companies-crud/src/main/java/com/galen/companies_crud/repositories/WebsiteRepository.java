package com.galen.companies_crud.repositories;

import com.galen.companies_crud.entities.WebSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebsiteRepository extends JpaRepository<WebSite, Long> {
}
