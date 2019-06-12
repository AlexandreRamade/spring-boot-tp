package com.training.spring.bigcorp.repository;

import java.util.List;

public interface CrudDao<T, ID> {

    //Read
    T findById(ID id);
    List<T> findAll();

    //Persist
    void persist(T element);

    //Delete
    void delete(T element);
}
