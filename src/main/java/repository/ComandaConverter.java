package repository;

import domain.Comanda;
import domain.Tort;
import exception.RepositoryException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ComandaConverter implements EntityConverter<Comanda> {

    @Override
    public String toString(Comanda comanda) {

        String tortIds = comanda.getListaTorturi().stream()
                .map(t -> String.valueOf(t.getId()))
                .collect(java.util.stream.Collectors.joining("|"));

        return comanda.getId() + "," + comanda.getData().getTime() + "," + tortIds;
    }

    @Override
    public Comanda fromString(String line) throws RepositoryException {
        try {
            String[] parts = line.split(",");
            if (parts.length < 2) {
                throw new RepositoryException("Format invalid pentru Comanda: " + line);
            }

            int id = Integer.parseInt(parts[0].trim());
            Date data = new Date(Long.parseLong(parts[1].trim()));



            List<Tort> listaTorturi = new ArrayList<>();

            return new Comanda(id, listaTorturi, data);
        } catch (NumberFormatException e) {
            throw new RepositoryException("ID/Data invalidă in fisierul Comanda: " + line);
        }
    }
}