package com.training.spring.bigcorp.repository;

import java.util.List;

public interface CrudDao<T, ID> {
    //Create
    void create(T element);

    //Read
    T findById(ID id);
    List<T> findAll();

    //Update
    void update(T element);

    //Delete
    void deleteById(ID id);
}
