package com.training.spring.bigcorp.service;

import com.training.spring.bigcorp.config.Monitored;
import com.training.spring.bigcorp.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@Service("siteService")
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//@Lazy
public class SiteServiceImpl implements SiteService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SiteService.class);

    private CaptorService captorService;

    @Autowired
    private ResourceLoader ressourceLoader;

    public SiteServiceImpl() {
    }

    @Autowired
    public SiteServiceImpl(CaptorService captorService) {
       LOGGER.debug("Init SiteServiceImpl :" + this);
       this.captorService = captorService;
    }

    @Override
    @Monitored
    public Site findById(String siteId) {
        LOGGER.debug("Appel de findById :" + this);
        if (siteId == null) {
            return null;
        }

        Site site = new Site("Florange");
        site.setId(siteId);
        site.setCaptors(captorService.findBySite(siteId));
        return site;
    }

    @Override
    public void readFile(String path) {
        InputStream stream = null;
        try {
            Resource resource = ressourceLoader.getResource(path);

            stream = resource.getInputStream();
            Scanner scanner = new Scanner(stream).useDelimiter("\\n");
            while(scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        } catch (IOException e) {
            LOGGER.error("Erreur sur le chargement fichier", e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                stream = null;
            }
        }

    }
}
