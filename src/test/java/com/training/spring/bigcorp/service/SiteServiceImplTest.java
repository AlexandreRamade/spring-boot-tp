package com.training.spring.bigcorp.service;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Site;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SiteServiceImplTest.SiteServiceTestConfiguration.class})
public class SiteServiceImplTest {

    @Configuration
    @ComponentScan("com.training.spring.bigcorp.service")
    static class SiteServiceTestConfiguration{}

    @Autowired
    private SiteService siteService;

    @Rule
    public OutputCapture output = new OutputCapture();

    @Test
    public void readFileFromURL() {
        siteService.readFile("url:https://dev-mind.fr/lorem.txt");
        assertThat(output.toString().contains("Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
    }

    @Test
    public void readFileFromClasspath() {
        siteService.readFile("classpath:lorem.txt");
        assertThat(output.toString().contains("Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
    }

    @Test
    public void readFileFromSystem() {
        siteService.readFile("file:///C:/lorem.txt");
        assertThat(output.toString().contains("Lorem ipsum dolor sit amet, consectetur adipiscing elit"));
    }


    @Mock
    private CaptorService captorService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByIdShouldReturnNullWhenIdIsNull(){
        // Initialisation
        String siteId = null;

        // Appel du SUT
        Site site = siteService.findById(siteId);

        // Vérification
        assertThat(site).isNull();
    }

    @Test
    public void findById(){
        // Initialisation
        String siteId = "siteId";
        Set<Captor> expectedCpators = Collections.singleton(new Captor("Capteur A"));
        Mockito.when(captorService.findBySite(siteId)).thenReturn(expectedCpators);

        // Appel du SUT
        Site site = siteService.findById(siteId);

        // Vérification
        assertThat(site.getId()).isEqualTo(siteId);
        assertThat(site.getName()).isEqualTo("Florange");
        assertThat(site.getCaptors()).isEqualTo(expectedCpators);
    }











}