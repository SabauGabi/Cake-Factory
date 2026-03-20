package repository;

import domain.Tort;
import exception.RepositoryException;

public class TortConverter implements EntityConverter<Tort> {

    @Override
    public String toString(Tort tort) {
        return tort.getId() + "," + tort.getTipulTortului();
    }

    @Override
    public Tort fromString(String line) throws RepositoryException {
        try {
            String[] parts = line.split(",");
            if (parts.length != 2) {
                throw new RepositoryException("Format invalid pentru Tort: " + line);
            }
            int id = Integer.parseInt(parts[0].trim());
            String tipul = parts[1].trim();
            return new Tort(id, tipul);
        } catch (NumberFormatException e) {
            throw new RepositoryException("ID invalid in fisierul Tort: " + line);
        }
    }
}