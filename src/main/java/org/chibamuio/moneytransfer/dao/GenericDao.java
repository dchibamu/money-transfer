package org.chibamuio.moneytransfer.dao;

import java.io.Serializable;
import java.util.Optional;

public interface GenericDao<T, ID extends Serializable> {
    void persist(final T entity);
    Optional<T> update(final T entity);
    Optional<T> findOne(final ID id);
    void delete(final ID id);
}
