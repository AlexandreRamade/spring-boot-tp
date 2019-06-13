package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
import com.training.spring.bigcorp.model.RealCaptor;
import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class MeasureDaoImplTest {

    @Autowired
    private MeasureDao measureDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void create() {
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.save(new Measure(Instant.now(), 2_333_666, measureDao.getOne(-1L).getCaptor()));
        Assertions.assertThat(measureDao.findAll()).hasSize(11);
    }

    @Test
    public void findById() {
        Optional<Measure> measure = measureDao.findById(-1L);
        Assertions.assertThat(measure).get().extracting("valueInWatt").containsExactly(1_000_000);
        Assertions.assertThat(measure).get().extracting("id").containsExactly(-1L);
        Assertions.assertThat(measure).get().extracting("instant").containsExactly(Instant.parse("2018-08-09T11:00:00.000Z"));
        Assertions.assertThat(measure).get().extracting("captor").extracting("name").containsExactly("Eolienne");
        Assertions.assertThat(measure).get().extracting("captor").extracting("site").extracting("name").containsExactly("Bigcorp Lyon");
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Optional<Measure> measure = measureDao.findById(-1000L);
        Assertions.assertThat(measure).isEmpty();
    }

    @Test
    public void findAll() {
        List<Measure> measures = measureDao.findAll();
        Assertions.assertThat(measures)
                .hasSize(10);
    }

    @Test
    public void update() {
        Optional<Measure> measure = measureDao.findById(-1L);
        Assertions.assertThat(measure).get().extracting("valueInWatt").containsExactly(1_000_000);
        measure.ifPresent(m -> {
            m.setValueInWatt(2_333_666);
            measureDao.save(m);
        });
        Assertions.assertThat(measure).get().extracting("valueInWatt").containsExactly(2_333_666);
    }

    @Test
    public void delete() {
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        Optional<Measure> measure = measureDao.findById(-1L);
        measure.ifPresent(m -> {
            measureDao.delete(m);
        });
        Assertions.assertThat(measureDao.findAll()).hasSize(9);
    }

    @Test
    public void preventConcurrentWrite() {
        Measure measure = measureDao.getOne(-1L);
        Assertions.assertThat(measure.getVersion()).isEqualTo(0);

        // On detache cet objet du contexte de persistence
        entityManager.detach(measure);
        measure.setValueInWatt(3_333_333);

        // On force la mise à jour en base (via le flush) et on vérifie que l'objet retourné et attaché à la session a été mis à jour
        Measure attachedMeasure = measureDao.save(measure);
        entityManager.flush();

        Assertions.assertThat(attachedMeasure.getValueInWatt()).isEqualTo(3_333_333);
        Assertions.assertThat(attachedMeasure.getVersion()).isEqualTo(1);

        //on réessaie d'enregistrer la measure, comme le numéro de version est à 0 on dois avoir une exception
        Assertions.assertThatThrownBy(() -> measureDao.save(measure))
                .isExactlyInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    public void deleteByCaptorId() {
        Assertions.assertThat(measureDao.findAll().stream().filter(m ->
                m.getCaptor().getId().equals("c1"))).hasSize(5);
        measureDao.deleteByCaptorId("c1");
        Assertions.assertThat(measureDao.findAll().stream().filter(m ->
                m.getCaptor().getId().equals("c1"))).isEmpty();
    }

}