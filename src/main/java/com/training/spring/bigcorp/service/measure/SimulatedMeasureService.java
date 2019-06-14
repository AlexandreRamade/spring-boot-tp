package com.training.spring.bigcorp.service.measure;

import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.MeasureStep;
import com.training.spring.bigcorp.model.SimulatedCaptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * Capteur simulé avec une puissance temporairement fixée à 12 MW
 * @see MeasureService
 */
@Service("simulatedMeasureService")
public class SimulatedMeasureService implements MeasureService<SimulatedCaptor> {

    private RestTemplate restTemplate;

    public SimulatedMeasureService(RestTemplateBuilder builder) {
        this.restTemplate = builder.setConnectTimeout(Duration.ofSeconds(1)).build();
    }

    @Override
    public List<Measure> readMeasures(SimulatedCaptor captor, Instant start, Instant end, MeasureStep step) {
            checkReadMeasuresArgs(captor, start, end, step);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8090/measures")
                    .path("")
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .queryParam("min", captor.getMinPowerInWatt())
                    .queryParam("max", captor.getMaxPowerInWatt())
                    .queryParam("step", step.getDurationInSecondes());


            Measure[] measures = this.restTemplate.getForObject(builder.toUriString(), Measure[].class);


        return Arrays.asList(measures);
    }
}
