package repository;

import domain.Entity;

public abstract class FileRepository<T extends Entity> extends InMemoryRepository<T> {
    protected String fileName;

    public FileRepository(String fileName) {
        this.fileName = fileName;
    }

    protected abstract void loadData();
    protected abstract void saveData();

    @Override
    public void add(T entity) {
        super.add(entity);
        saveData();
    }

    @Override
    public void remove(int id) {
        super.remove(id);
        saveData();
    }

    @Override
    public void update(int id, T entity) {
        super.update(id, entity);
        saveData();
    }
}