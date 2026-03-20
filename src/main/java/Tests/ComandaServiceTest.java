package Tests;

import domain.Comanda;
import domain.Tort;
import exception.RepositoryException;
import exception.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepository;
import repository.IRepository;
import exception.ComandaValidator;
import service.ComandaService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComandaServiceTest {

    private IRepository<Comanda> comandaRepo;
    private ComandaValidator comandaValidator;
    private ComandaService comandaService;
    private List<Tort> torturiValide;

    @BeforeEach
    void setUp() {
        comandaRepo = new InMemoryRepository<>();
        comandaValidator = new ComandaValidator();
        comandaService = new ComandaService(comandaRepo, comandaValidator);


        torturiValide = Arrays.asList(new Tort(10, "Mock Tort 1"), new Tort(11, "Mock Tort 2"));


        Comanda c1 = new Comanda(1, torturiValide.subList(0, 1), new Date(System.currentTimeMillis() + 100000));
        Comanda c2 = new Comanda(2, torturiValide, new Date(System.currentTimeMillis() + 200000));

        comandaService.addComanda(c1);
        comandaService.addComanda(c2);
    }



    @Test
    void testAddComandaSuccess() {
        Comanda c3 = new Comanda(3, torturiValide, new Date(System.currentTimeMillis() + 300000));
        comandaService.addComanda(c3);
        assertEquals(3, comandaService.getAll().size());
    }

    @Test
    void testAddComandaDuplicateIdThrowsRepositoryException() {
        Comanda c_dup = new Comanda(1, torturiValide, new Date(System.currentTimeMillis() + 300000));
        assertThrows(RepositoryException.class, () -> {
            comandaService.addComanda(c_dup);
        });
    }

    @Test
    void testAddComandaInvalidDataThrowsValidatorException() {

        Comanda c_invalid_date = new Comanda(3, torturiValide, new Date(System.currentTimeMillis() - 300000));
        assertThrows(RepositoryException.class, () -> {
            comandaService.addComanda(c_invalid_date);
        });


        Comanda c_invalid_list = new Comanda(3, Arrays.asList(), new Date(System.currentTimeMillis() + 300000));
        assertThrows(RepositoryException.class, () -> {
            comandaService.addComanda(c_invalid_list);
        });
    }



    @Test
    void testFindByIdSuccess() {
        Comanda comanda = comandaService.findById(2);
        assertNotNull(comanda);
        assertEquals(2, comanda.getListaTorturi().size());
    }

    @Test
    void testFindByIdNotFoundThrowsRepositoryException() {
        assertThrows(RepositoryException.class, () -> {
            comandaService.findById(99);
        });
    }



    @Test
    void testUpdateComandaSuccess() {
        List<Tort> listaNoua = torturiValide.subList(0, 1);
        Comanda comandaActualizata = new Comanda(2, listaNoua, new Date(System.currentTimeMillis() + 400000));
        comandaService.updateComanda(comandaActualizata);

        Comanda updated = comandaService.findById(2);
        assertEquals(1, updated.getListaTorturi().size());
    }

    @Test
    void testUpdateComandaNonExistingIdThrowsRepositoryException() {
        Comanda c_inexistent = new Comanda(99, torturiValide, new Date(System.currentTimeMillis() + 300000));
        assertThrows(RepositoryException.class, () -> {
            comandaService.updateComanda(c_inexistent);
        });
    }



    @Test
    void testDeleteComandaSuccess() {
        comandaService.deleteComanda(1);
        assertEquals(1, comandaService.getAll().size());

        assertThrows(RepositoryException.class, () -> {
            comandaService.findById(1);
        });
    }

    @Test
    void testDeleteComandaNonExistingIdThrowsRepositoryException() {
        assertThrows(RepositoryException.class, () -> {
            comandaService.deleteComanda(99);
        });
    }
}