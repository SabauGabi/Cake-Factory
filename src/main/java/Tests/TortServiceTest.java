package Tests;

import domain.Tort;
import exception.RepositoryException;
import exception.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepository;
import repository.IRepository;
import exception.TortValidator;
import service.TortService;

import static org.junit.jupiter.api.Assertions.*;

public class TortServiceTest {

    private IRepository<Tort> tortRepo;
    private TortValidator tortValidator;
    private TortService tortService;

    @BeforeEach
    void setUp() {
        tortRepo = new InMemoryRepository<>();
        tortValidator = new TortValidator();
        tortService = new TortService(tortRepo, tortValidator);

        tortService.addTort(1, "Tiramisu");
        tortService.addTort(2, "Savarina");
    }

    @Test
    void testAddTortSuccess() {
        tortService.addTort(3, "Ecler");
        assertEquals(3, tortService.getAll().size());
        assertEquals("Ecler", tortService.findById(3).getTipulTortului());
    }

    @Test
    void testAddTortDuplicateIdThrowsRepositoryException() {
        assertThrows(RepositoryException.class, () -> {
            tortService.addTort(1, "Duplicat");
        });
    }

    @Test
    void testAddTortInvalidDataThrowsValidatorException() {
        assertThrows(RepositoryException.class, () -> {
            tortService.addTort(4, null);
        });

        assertThrows(RepositoryException.class, () -> {
            tortService.addTort(-5, "Valabil");
        });
    }

    @Test
    void testFindByIdSuccess() {
        Tort tort = tortService.findById(2);
        assertNotNull(tort);
        assertEquals("Savarina", tort.getTipulTortului());
    }

    @Test
    void testFindByIdNotFoundThrowsRepositoryException() {
        assertThrows(RepositoryException.class, () -> {
            tortService.findById(99);
        });
    }

    @Test
    void testUpdateTortSuccess() {
        tortService.updateTort(1, "Tiramisu Actualizat");
        Tort updatedTort = tortService.findById(1);
        assertEquals("Tiramisu Actualizat", updatedTort.getTipulTortului());
    }

    @Test
    void testUpdateTortNonExistingIdThrowsRepositoryException() {
        assertThrows(RepositoryException.class, () -> {
            tortService.updateTort(99, "Nou");
        });
    }

    @Test
    void testUpdateTortInvalidDataThrowsValidatorException() {
        assertThrows(RepositoryException.class, () -> {
            tortService.updateTort(1, "");
        });
    }

    @Test
    void testDeleteTortSuccess() {
        tortService.deleteTort(2);
        assertEquals(1, tortService.getAll().size());

        assertThrows(RepositoryException.class, () -> {
            tortService.findById(2);
        });
    }

    @Test
    void testDeleteTortNonExistingIdThrowsRepositoryException() {
        assertThrows(RepositoryException.class, () -> {
            tortService.deleteTort(99);
        });
    }
}