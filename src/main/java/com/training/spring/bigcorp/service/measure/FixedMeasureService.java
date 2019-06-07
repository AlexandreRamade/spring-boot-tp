package com.training.spring.bigcorp.service.measure;

import com.training.spring.bigcorp.config.properties.BigCorpApplicationProperties;
import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.MeasureStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Capteur virtuel avec une puissance fixée à 10 MW
 * @see MeasureService
 */
@Service("fixedMeasureService")
public class FixedMeasureService implements MeasureService {

    @Autowired
    private BigCorpApplicationProperties bigCorpApplicationProperties;

    @Override
    public List<Measure> readMeasures(Captor captor, Instant start, Instant end, MeasureStep step) {
        List<Measure> measures = new ArrayList<Measure>();

            checkReadMeasuresArgs(captor, start, end, step);

            Instant current = start;

            while (current.isBefore(end)) {
                measures.add(new Measure(current, bigCorpApplicationProperties.getMeasure().getDefaultFixed(), captor));
                current = current.plusSeconds(step.getDurationInSecondes());
            }

        return measures;
    }
}
