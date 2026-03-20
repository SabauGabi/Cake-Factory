package repository;

import domain.Entity;
import exception.RepositoryException;
import java.util.ArrayList;
import java.util.List;

public class InMemoryRepository<T extends Entity> implements IRepository<T> {

    protected List<T> data = new ArrayList<>();

    public InMemoryRepository() {
    }

    @Override
    public void add(T entity) {
        if (findById(entity.getId()) != null) {
            throw new RepositoryException("Entitatea cu ID-ul " + entity.getId() + " exista deja.");
        }
        data.add(entity);
    }

    @Override
    public void remove(int id) {
        if (findById(id) == null) {
            throw new RepositoryException("Entitatea cu ID-ul " + id + " nu exista.");
        }
        data.removeIf(e -> e.getId() == id);
    }

    @Override
    public void update(int id, T entity) {
        if (findById(id) == null) {
            throw new RepositoryException("Entitatea cu ID-ul " + id + " nu exista.");
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == id) {
                data.set(i, entity);
                return;
            }
        }
    }

    @Override
    public T findById(int id) {
        for (T entity : data) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(data);
    }
}