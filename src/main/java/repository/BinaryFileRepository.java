package repository;

import domain.Entity;
import exception.RepositoryException;

import java.io.*;
import java.util.List;

public class BinaryFileRepository<T extends Entity> extends FileRepository<T> {

    public BinaryFileRepository(String fileName) {
        super(fileName);
        loadData();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            List<T> loaded = (List<T>) ois.readObject();
            data.clear();
            data.addAll(loaded);
        } catch (FileNotFoundException e) {
            data.clear();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("Eroare la citire binar: " + e.getMessage());
        }
    }

    @Override
    protected void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scriere binar: " + e.getMessage());
        }
    }
}