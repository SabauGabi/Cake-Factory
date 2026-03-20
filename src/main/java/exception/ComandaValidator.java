package exception;

import domain.Comanda;

import java.util.Date;

public class ComandaValidator {
    public void validate(Comanda comanda) throws RepositoryException {

        if (comanda.getId() <= 0) {
            throw new RepositoryException("ID-ul comenzii trebuie să fie un număr pozitiv.");
        }


        if (comanda.getListaTorturi() == null || comanda.getListaTorturi().isEmpty()) {
            throw new RepositoryException("O comandă trebuie să conțină cel puțin un tort.");
        }


    }
}