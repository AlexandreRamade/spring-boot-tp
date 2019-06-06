package com.training.spring.bigcorp.service.measure;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.MeasureStep;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Capteur simulé avec une puissance temporairement fixée à 12 MW
 * @see MeasureService
 */
@Service("simulatedMeasureService")
public class SimulatedMeasureService implements MeasureService {

    @Value("${bigcorp.measure.default-simulated}")
    private Integer defaultValue;

    @Override
    public List<Measure> readMeasures(Captor captor, Instant start, Instant end, MeasureStep step) {
        List<Measure> measures = new ArrayList<Measure>();

            checkReadMeasuresArgs(captor, start, end, step);

            Instant current = start;

            while (current.isBefore(end)) {
                measures.add(new Measure(current, defaultValue, captor));
                current = current.plusSeconds(step.getDurationInSecondes());
            }


        return measures;
    }
}
