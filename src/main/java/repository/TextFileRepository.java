package repository;

import domain.Entity;
import exception.RepositoryException;

import java.io.*;

public class TextFileRepository<T extends Entity> extends FileRepository<T> {

    private EntityConverter<T> converter;

    public TextFileRepository(String fileName, EntityConverter<T> converter) {
        super(fileName);
        this.converter = converter;
        loadData();
    }

    @Override
    protected void loadData() {
        data.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    T entity = converter.fromString(line);
                    data.add(entity);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            throw new RepositoryException("Eroare la citire fisier: " + e.getMessage());
        }
    }

    @Override
    protected void saveData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (T entity : data) {
                bw.write(converter.toString(entity));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("Eroare la scriere fisier: " + e.getMessage());
        }
    }
}