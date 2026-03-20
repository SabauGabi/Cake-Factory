package exception;

import domain.Tort;

public class TortValidator {
    public void validate(Tort tort) throws RepositoryException {

        if (tort.getId() < 0) {
            throw new RepositoryException("ID-ul tortului trebuie să fie un număr pozitiv.");
        }


        if (tort.getTipulTortului() == null || tort.getTipulTortului().trim().isEmpty()) {
            throw new RepositoryException("Tipul tortului este obligatoriu.");
        }
    }
}