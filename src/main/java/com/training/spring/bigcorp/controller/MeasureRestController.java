package com.training.spring.bigcorp.controller;

import com.training.spring.bigcorp.controller.dto.MeasureDto;
import com.training.spring.bigcorp.model.*;
import com.training.spring.bigcorp.repository.CaptorDao;
import com.training.spring.bigcorp.service.measure.FixedMeasureService;
import com.training.spring.bigcorp.service.measure.RealMeasureService;
import com.training.spring.bigcorp.service.measure.SimulatedMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measures/captors/{captorId}/last/hours/{nbHours}")
public class MeasureRestController {

    @Autowired
    private CaptorDao captorDao;

    @Autowired
    private SimulatedMeasureService simulatedMeasureService;

    @Autowired
    private FixedMeasureService fixedMeasureService;

    @Autowired
    private RealMeasureService realMeasureService;

    @GetMapping
    public List<MeasureDto> findWithCaptorIdAndLastHours(@PathVariable String captorId, @PathVariable Integer nbHours) {
        Captor captor = captorDao.findById(captorId).orElseThrow(IllegalArgumentException::new);

        if (captor.getPowerSource() == PowerSource.SIMULATED) {
            return simulatedMeasureService.readMeasures(((SimulatedCaptor) captor),
                    Instant.now().minus(Duration.ofHours(nbHours)).truncatedTo(ChronoUnit.MINUTES),
                    Instant.now().truncatedTo(ChronoUnit.MINUTES),
                    MeasureStep.ONE_MINUTE)
                    .stream()
                    .map(m -> new MeasureDto(m.getInstant(),
                            m.getValueInWatt()))
                    .collect(Collectors.toList());
        } else if (captor.getPowerSource() == PowerSource.FIXED) {
            return fixedMeasureService.readMeasures(((FixedCaptor) captor),
                    Instant.now().minus(Duration.ofHours(nbHours)).truncatedTo(ChronoUnit.MINUTES),
                    Instant.now().truncatedTo(ChronoUnit.MINUTES),
                    MeasureStep.ONE_MINUTE)
                    .stream()
                    .map(m -> new MeasureDto(m.getInstant(),
                            m.getValueInWatt()))
                    .collect(Collectors.toList());
        } else if (captor.getPowerSource() == PowerSource.FIXED) {
            return realMeasureService.readMeasures(((RealCaptor) captor),
                    Instant.now().minus(Duration.ofHours(nbHours)).truncatedTo(ChronoUnit.MINUTES),
                    Instant.now().truncatedTo(ChronoUnit.MINUTES),
                    MeasureStep.ONE_MINUTE)
                    .stream()
                    .map(m -> new MeasureDto(m.getInstant(),
                            m.getValueInWatt()))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }


    }


}
