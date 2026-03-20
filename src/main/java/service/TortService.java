package service;

import domain.Tort;
import repository.IRepository;
import exception.RepositoryException;
import exception.TortValidator;

import java.util.List;

public class TortService {
    private final IRepository<Tort> tortRepository;
    private final TortValidator tortValidator;

    public TortService(IRepository<Tort> tortRepository, TortValidator tortValidator) {
        this.tortRepository = tortRepository;
        this.tortValidator = tortValidator;
    }

    public void addTort(int id, String tipulTortului) throws RepositoryException {
        Tort tort = new Tort(id, tipulTortului);
        tortValidator.validate(tort);
        tortRepository.add(tort);
    }

    public void updateTort(int id, String tipulTortului) throws RepositoryException {
        Tort tort = new Tort(id, tipulTortului);
        tortValidator.validate(tort);

        if (tortRepository.findById(id) == null) {
            throw new RepositoryException("Tortul cu ID-ul " + id + " nu exista.");
        }
        tortRepository.update(id, tort);
    }

    public List<Tort> getAll() {
        return tortRepository.getAll();
    }

    public Tort findById(int id) throws RepositoryException {
        Tort tort = tortRepository.findById(id);
        if (tort == null) {
            throw new RepositoryException("Tortul cu ID-ul " + id + " nu a fost gasit in baza de date.");
        }
        return tort;
    }

    public void deleteTort(int id) throws RepositoryException {
        if (tortRepository.findById(id) == null) {
            throw new RepositoryException("Nu se poate sterge. Tortul cu ID-ul " + id + " nu exista.");
        }
        tortRepository.remove(id);
    }
}