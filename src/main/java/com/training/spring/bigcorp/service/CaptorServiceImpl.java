package com.training.spring.bigcorp.service;

import com.training.spring.bigcorp.config.Monitored;
import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.repository.CaptorDao;
import com.training.spring.bigcorp.service.measure.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CaptorServiceImpl implements CaptorService{

    /**
     * Measures with fixed captor
     */
    private MeasureService fixedMeasureService;

    /**
     * Measures with real captor
     */
    private MeasureService realMeasureService;

    /**
     * Measure with simulated captor
     */
    private MeasureService simulatedMeasureService;


    private CaptorDao captorDao;


    public CaptorServiceImpl() {
    }

    @Autowired
    public CaptorServiceImpl(@Qualifier("fixedMeasureService") MeasureService fixedMeasureService,
                             @Qualifier("realMeasureService") MeasureService realMeasureService,
                             @Qualifier("simulatedMeasureService") MeasureService simulatedMeasureService) {
        this.fixedMeasureService = fixedMeasureService;
        this.realMeasureService = realMeasureService;
        this.simulatedMeasureService = simulatedMeasureService;
    }

    @Override
    @Monitored
    public Set<Captor> findBySite(String siteId) {
        Set<Captor> captors = new HashSet<>();
        if (siteId == null) {
            return captors;
        } else {
            captors = captorDao.findBySiteId(siteId).stream().collect(Collectors.toSet());
        }
        return captors;
    }


}
