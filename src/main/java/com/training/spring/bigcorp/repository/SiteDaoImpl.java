package com.training.spring.bigcorp.repository;

import com.training.spring.bigcorp.model.Site;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class SiteDaoImpl implements SiteDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Site findById(String id) {
        return em.find(Site.class, id);
    }


    @Override
    public List<Site> findAll() {
        return em.createQuery("SELECT s FROM Site s").getResultList();
    }

    @Override
    public void persist(Site site) {
        em.persist(site);
    }

    @Override
    public void delete(Site site) {
        em.remove(site);
    }


}
