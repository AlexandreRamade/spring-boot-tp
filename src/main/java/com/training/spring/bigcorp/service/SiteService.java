package com.training.spring.bigcorp.service;

import com.training.spring.bigcorp.model.Site;

public interface SiteService {
    Site findById(String siteId);
}
