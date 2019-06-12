package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Site;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
public class SiteDaoImplTest {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void create() {
        Assertions.assertThat(siteDao.findAll()).hasSize(1);
        siteDao.save(new Site("Nouveau site"));

        Assertions.assertThat(siteDao.findAll())
                .hasSize(2)
                .extracting("name")
                .contains("Bigcorp Lyon", "Nouveau site");
    }

    @Test
    public void findById() {
        Optional<Site> site = siteDao.findById("site1");
        Assertions.assertThat(site).get().extracting("name").containsExactly("Bigcorp Lyon");
    }

    @Test
    public void findByIdShouldReturnNullWhenIdUnknown() {
        Optional<Site> site = siteDao.findById("unkwown");
        Assertions.assertThat(site).isEmpty();
    }

    @Test
    public void findAll() {
        List<Site> sites = siteDao.findAll();
        Assertions.assertThat(sites)
                .hasSize(1)
                .extracting("id", "name")
                .contains(Tuple.tuple("site1", "Bigcorp Lyon"));
    }

    @Test
    public void update() {
        Optional<Site> site = siteDao.findById("site1");
        Assertions.assertThat(site).get().extracting("name").containsExactly("Bigcorp Lyon");

        site.ifPresent(s -> {
            s.setName("Site updated");
            siteDao.save(s);
        });


        site = siteDao.findById("site1");
        Assertions.assertThat(site).get().extracting("name").containsExactly("Site updated");

    }

    @Test
    public void deleteById() {
        Site newSite = new Site("Nouveau site");
        siteDao.save(newSite);
        Assertions.assertThat(siteDao.findById(newSite.getId())).isNotNull();

        siteDao.delete(newSite);
        Assertions.assertThat(siteDao.findById(newSite.getId())).isEmpty();
    }

    @Test
    public void deleteByIdShouldThrowExceptionWhenIdIsUsedAsForeignKey() {
        Site site = (Site) siteDao.getOne("site1");
        Assertions.assertThatThrownBy(() -> {
            siteDao.delete(site);
            entityManager.flush();
        }).isExactlyInstanceOf(PersistenceException.class)
                .hasCauseExactlyInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void preventConcurrentWrite() {
        Site site = siteDao.getOne("site1");
        Assertions.assertThat(site.getVersion()).isEqualTo(0);

        // On detache cet objet du contexte de persistence
        entityManager.detach(site);
        site.setName("Site updated");

        //On force la MàJ en base (via le flush) et on vérifie que l'objet retourné et attaché à la session a été mis à jour
        Site attachedSite = siteDao.save(site);
        entityManager.flush();

        Assertions.assertThat(attachedSite.getName()).isEqualTo("Site updated");
        Assertions.assertThat(attachedSite.getVersion()).isEqualTo(1);
        //Erreur lorsqu'on ré-essai d'enregistrer le site de version 0 sur celui de version 1
        Assertions.assertThatThrownBy(() -> siteDao.save(site))
                .isExactlyInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

}