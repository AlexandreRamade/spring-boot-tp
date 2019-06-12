package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaptorDao extends JpaRepository<Captor, String> {

    List<Captor> findBySiteId(String siteId);
}
