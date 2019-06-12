package com.training.spring.bigcorp.repository;


import com.training.spring.bigcorp.model.Measure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class MeasureDaoImpl implements MeasureDao{

    @PersistenceContext
    private EntityManager em;


    @Override
    public Measure findById(Long measureId) {
        return em.find(Measure.class, measureId);
    }

    @Override
    public List<Measure> findAll() {
        return em.createQuery("SELECT m FROM Measure m INNER JOIN m.captor c", Measure.class).getResultList();
    }

    @Override
    public void persist(Measure measure) {
        em.persist(measure);
    }

    @Override
    public void delete(Measure measure) {
        em.remove(measure);
    }


}
