package com.dom.ws.rest.bot.service.interfaces;

import java.util.List;

public interface BaseService<T> {
    T create(T entity);
    T update(T entity);
    void delete(String id);
    T findById(String id);
    List<T> findAll();
}