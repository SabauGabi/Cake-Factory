package repository;

import domain.Entity;
import exception.RepositoryException;

public interface EntityConverter<T extends Entity> {
    String toString(T entity);
    T fromString(String line) throws RepositoryException;
}