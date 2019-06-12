package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.Measure;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class MeasureDaoImplTest {

    @Autowired
    private MeasureDao measureDao;


    @Test
    public void create() {
        Captor captor = new Captor("Eolienne", new Site("site"));
        captor.setId("c1");
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.persist(new Measure(Instant.now(), 2_333_666, captor));
        Assertions.assertThat(measureDao.findAll()).hasSize(11);
    }

    @Test
    public void findById() {
        Measure measure = measureDao.findById(-1L);
        Assertions.assertThat(measure.getValueInWatt()).isEqualTo(1_000_000);
        Assertions.assertThat(measure.getId()).isEqualTo(-1L);
        Assertions.assertThat(measure.getInstant()).isEqualTo(Instant.parse("2018-08-09T11:00:00.000Z"));
        Assertions.assertThat(measure.getCaptor().getName()).isEqualTo("Eolienne");
        Assertions.assertThat(measure.getCaptor().getSite().getName()).isEqualTo("Bigcorp Lyon");
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Measure measure = measureDao.findById(-1000L);
        Assertions.assertThat(measure).isNull();
    }

    @Test
    public void findAll() {
        List<Measure> measures = measureDao.findAll();
        Assertions.assertThat(measures)
                .hasSize(10);
    }

    @Test
    public void update() {
        Measure measure = measureDao.findById(-1L);
        Assertions.assertThat(measure.getValueInWatt()).isEqualTo(1_000_000);
        measure.setValueInWatt(2_333_666);
        measureDao.persist(measure);
        measure = measureDao.findById(-1L);
        Assertions.assertThat(measure.getValueInWatt()).isEqualTo(2_333_666);
    }

    @Test
    public void deleteById() {
        Assertions.assertThat(measureDao.findAll()).hasSize(10);
        measureDao.delete(measureDao.findById(-1L));
        Assertions.assertThat(measureDao.findAll()).hasSize(9);
    }
}