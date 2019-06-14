package com.training.spring.bigcorp.service.measure;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.MeasureStep;

import java.time.Instant;
import java.util.List;

public interface MeasureService<T extends Captor> {

    /**
     * Retourne effectuant une série de mesure à la fréquence indiquée durant une periode donnée
     * Le nombre de mesure dépend de la durée entre les instants start et stop,
     * ainsi que de la fréquence indiquée par le step.
     * @see Measure
     * @see Instant
     * @see MeasureStep
     *
     * @param captor
     * @param start
     * @param end
     * @param step
     *
     * @return liste de Measure
     */
    List<Measure> readMeasures(T captor, Instant start, Instant end, MeasureStep step);

    /**
     * Verifie qu'aucun des paramètres n'est null et que la date de début (start) est avant celle de fin (end)
     * Leve une IllegalArgumentException dans le cas contraire
     * @see IllegalArgumentException
     *
     * @param captor
     * @param start
     * @param end
     * @param step
     */
    default void checkReadMeasuresArgs(T captor, Instant start, Instant end, MeasureStep step) {
        if(captor == null) {
            throw new IllegalArgumentException("captor is required");
        }
        if(start == null) {
            throw new IllegalArgumentException("start is required");
        }
        if(end == null) {
            throw new IllegalArgumentException("end is required");
        }
        if(step == null) {
            throw new IllegalArgumentException("step is required");
        }
        if(start.isAfter(end)) {
            throw new IllegalArgumentException("start must be before end");
        }
    }

}
