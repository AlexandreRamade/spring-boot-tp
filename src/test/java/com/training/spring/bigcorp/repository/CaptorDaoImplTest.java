package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Captor;
import com.training.spring.bigcorp.model.FixedCaptor;
import com.training.spring.bigcorp.model.RealCaptor;
import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import java.util.List;
import java.util.Optional;


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
        captorDao.save(new RealCaptor("New captor", site));

        Assertions.assertThat(captorDao.findAll())
                .hasSize(3)
                .extracting(Captor::getName)
                .contains("Eolienne", "Laminoire à chaud", "New captor");
    }

    @Test
    public void findById() {
        Optional<Captor> captor = captorDao.findById("c1");
        Assertions.assertThat(captor).get().extracting("id").containsExactly("c1");
        Assertions.assertThat(captor).get().extracting("name").containsExactly("Eolienne");
        Assertions.assertThat(captor).get().extracting("site").extracting("name").containsExactly("Bigcorp Lyon");
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Optional<Captor> captor = captorDao.findById("unkwown");
        Assertions.assertThat(captor).isEmpty();
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
        Optional<Captor> captor = captorDao.findById("c1");
        Assertions.assertThat(captor).get().extracting("name").containsExactly("Eolienne");

        captor.ifPresent(c -> {
            c.setName("Captor updated");
            captorDao.save(c);
        });

        captor = captorDao.findById("c1");
        Assertions.assertThat(captor).get().extracting("name").containsExactly("Captor updated");

    }

    @Test
    public void delete() {

        Captor newCaptor = new RealCaptor("Nouveau capteur", captorDao.findById("c1").get().getSite());
        captorDao.save(newCaptor);
        Assertions.assertThat(captorDao.findAll()).hasSize(3);
        captorDao.delete(newCaptor);
        Assertions.assertThat(captorDao.findAll()).hasSize(2);

    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeignKey() {
        Captor captor = captorDao.getOne("c1");
        Assertions.assertThatThrownBy(() -> {
                    captorDao.delete(captor);
                    entityManager.flush();
                }).isExactlyInstanceOf(PersistenceException.class)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }


    @Test
    public void findByExample() {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.ignoreCase().contains())
                .withMatcher("site", match -> match.contains())
                .withIgnorePaths("id", "powerSource", "defaultPowerInWatt")
                .withIgnoreNullValues();

        Captor captor = new FixedCaptor("olien", captorDao.findById("c1").get().getSite(), 10000000);
        List<Captor> captors = captorDao.findAll(Example.of(captor, matcher));

        Assertions.assertThat(captors)
                .hasSize(1)
                .extracting("id", "name")
                .containsExactly(Tuple.tuple("c1", "Eolienne"));
    }


}