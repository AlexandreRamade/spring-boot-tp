package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteDao extends JpaRepository<Site, String> {
}
