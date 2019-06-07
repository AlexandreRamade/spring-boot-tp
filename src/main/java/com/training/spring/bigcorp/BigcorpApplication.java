package com.training.spring.bigcorp;

import com.training.spring.bigcorp.config.properties.BigCorpApplicationProperties;
import com.training.spring.bigcorp.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BigcorpApplication {

	private final static Logger LOGGER = LoggerFactory.getLogger(BigcorpApplication.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(BigcorpApplication.class, args);

		BigCorpApplicationProperties applicationInfo = context.getBean(BigCorpApplicationProperties.class);

		LOGGER.info("=============================================");
		LOGGER.info("Application [" + applicationInfo.getName() + "] - version :" + applicationInfo.getVersion());
		LOGGER.info("plus d'informations sur " + applicationInfo.getWebSiteUrl());
		LOGGER.info("===============================================");

		context.getBean(SiteService.class).findById("test");
	}

}
