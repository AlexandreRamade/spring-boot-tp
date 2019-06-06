package com.training.spring.bigcorp.service;

import com.training.spring.bigcorp.model.Captor;

import java.util.Set;

public interface CaptorService {
    Set<Captor> findBySite(String siteId);
}
