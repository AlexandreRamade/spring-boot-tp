package com.training.spring.bigcorp.service;


import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.RealCaptor;
import com.training.spring.bigcorp.model.Site;
import com.training.spring.bigcorp.repository.CaptorDao;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Set;

@RunWith(SpringRunner.class)
public class CaptorServiceImplTest {

    @Mock
    private CaptorDao captorDao;

    @InjectMocks
    private CaptorServiceImpl captorService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findBySiteShouldReturnNullWhenIdIsNull() {
        // Initialisation
        String siteId = null;

        // Appel du SUT
        Set<Captor> captors = captorService.findBySite(siteId);

        // Vérification
        Assertions.assertThat(captors).isEmpty();
    }

    @Test
    public void findBySite() {
        // Initialisation
        String siteId = "siteId";

        Captor expectedCaptor = new RealCaptor("Capteur A", new Site("Florange"));

        Mockito.when(captorDao.findBySiteId(siteId)).thenReturn(Arrays.asList(expectedCaptor));

        // Appel du SUT
        Set<Captor> captors = captorService.findBySite(siteId);

        // Vérification
        Assertions.assertThat(captors).hasSize(1);
        Assertions.assertThat(captors).extracting(Captor::getName).contains("Capteur A");
    }
}