package repository;

import domain.Entity;
import java.util.List;

public interface IRepository<T extends Entity> {
    void add(T entity);
    void remove(int id);
    void update(int id, T entity);
    T findById(int id);
    List<T> getAll();
}