package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class CaptorDaoImplTest {

    @Autowired
    private CaptorDao captorDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findSiteById() {
        List<Captor> captors = captorDao.findBySiteId("site1");
        Assertions.assertThat(captors)
                .hasSize(2);

        captors = captorDao.findBySiteId("site inconu");
        Assertions.assertThat(captors).isEmpty();
    }

    @Test
    public void create() {
        Assertions.assertThat(captorDao.findAll()).hasSize(2);
        Site site = new Site("Nouveau site");
        site.setId("site1");
        captorDao.persist(new Captor("New captor", site));

        Assertions.assertThat(captorDao.findAll())
                .hasSize(3)
                .extracting(Captor::getName)
                .contains("Eolienne", "Laminoire à chaud", "New captor");
    }

    @Test
    public void findById() {
        Captor captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getId()).isEqualTo("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Eolienne");
        Assertions.assertThat(captor.getSite().getName()).isEqualTo("Bigcorp Lyon");
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Captor captor = captorDao.findById("unkwown");
        Assertions.assertThat(captor).isNull();
    }

    @Test
    public void findAll() {
        List<Captor> captors = captorDao.findAll();
        Assertions.assertThat(captors)
                .hasSize(2)
                .extracting("id", "name")
                .contains(Tuple.tuple("c1", "Eolienne"))
                .contains(Tuple.tuple("c2", "Laminoire à chaud"));
    }

    @Test
    public void update() {
        Captor captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Eolienne");

        captor.setName("Captor updated");
        captorDao.persist(captor);

        captor = captorDao.findById("c1");
        Assertions.assertThat(captor.getName()).isEqualTo("Captor updated");

    }

    @Test
    public void delete() {

        Captor captor = new Captor("Nouveau capteur", captorDao.findById("c1").getSite());
        captorDao.persist(captor);
        Assertions.assertThat(captorDao.findAll()).hasSize(3);
        captorDao.delete(captor);
        Assertions.assertThat(captorDao.findAll()).hasSize(2);

    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeignKey() {
        Captor captor = captorDao.findById("c1");
        Assertions.assertThatThrownBy(() -> {
                    captorDao.delete(captor);
                    entityManager.flush();
                }).isExactlyInstanceOf(PersistenceException.class)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

}