package service;

import domain.Comanda;
import domain.Tort;
import repository.IRepository;
import exception.RepositoryException;
import exception.ComandaValidator;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComandaService {
    private final IRepository<Comanda> comandaRepository;
    private final ComandaValidator comandaValidator;

    public ComandaService(IRepository<Comanda> comandaRepository, ComandaValidator comandaValidator) {
        this.comandaRepository = comandaRepository;
        this.comandaValidator = comandaValidator;
    }

    public void addComanda(Comanda comanda) throws RepositoryException {
        comandaValidator.validate(comanda);
        comandaRepository.add(comanda);
    }

    public List<Comanda> getAll() {
        return comandaRepository.getAll();
    }

    public Comanda findById(int id) throws RepositoryException {
        Comanda comanda = comandaRepository.findById(id);
        if (comanda == null) {
            throw new RepositoryException("Comanda cu ID-ul " + id + " nu a fost gasita.");
        }
        return comanda;
    }

    public void updateComanda(Comanda comanda) throws RepositoryException {
        comandaValidator.validate(comanda);
        if (comandaRepository.findById(comanda.getId()) == null) {
            throw new RepositoryException("Comanda cu ID-ul " + comanda.getId() + " nu exista si nu poate fi actualizata.");
        }
        comandaRepository.update(comanda.getId(), comanda);
    }

    public void deleteComanda(int id) throws RepositoryException {
        if (comandaRepository.findById(id) == null) {
            throw new RepositoryException("Nu se poate sterge. Comanda cu ID-ul " + id + " nu exista.");
        }
        comandaRepository.remove(id);
    }

    public Map<String, Long> getTorturiPerZi() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return comandaRepository.getAll().stream()
                .collect(Collectors.groupingBy(
                        c -> sdf.format(c.getData()),
                        Collectors.summingLong(c -> c.getListaTorturi().size())
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Long> getTorturiPerLuna() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return comandaRepository.getAll().stream()
                .collect(Collectors.groupingBy(
                        c -> sdf.format(c.getData()),
                        Collectors.summingLong(c -> c.getListaTorturi().size())
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Long> getCeleMaiComandateTorturi() {
        return comandaRepository.getAll().stream()
                .flatMap(c -> c.getListaTorturi().stream())
                .collect(Collectors.groupingBy(
                        Tort::getTipulTortului,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}