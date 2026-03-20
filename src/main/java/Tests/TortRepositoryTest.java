package Tests;

import domain.Tort;
import exception.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.TextFileRepository;
import repository.TortConverter;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TortRepositoryTest {

    private static final String TEST_FILE = "torturi_test.txt";
    private IRepository<Tort> tortRepo;

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }

        tortRepo = new TextFileRepository<>(TEST_FILE, new TortConverter());

        tortRepo.add(new Tort(1, "Diplomat"));
        tortRepo.add(new Tort(2, "Ciocolata"));
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testAddAndSave() {
        Tort newTort = new Tort(3, "Amandina");
        tortRepo.add(newTort);

        assertEquals(3, tortRepo.getAll().size());

        IRepository<Tort> newRepo = new TextFileRepository<>(TEST_FILE, new TortConverter());
        assertEquals(3, newRepo.getAll().size());
        assertEquals("Amandina", newRepo.findById(3).getTipulTortului());
    }

    @Test
    void testAddDuplicateIdThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            tortRepo.add(new Tort(1, "Duplicat"));
        });
    }

    @Test
    void testUpdate() {
        Tort updatedTort = new Tort(1, "Diplomat Premium");
        tortRepo.update(1, updatedTort);

        assertEquals("Diplomat Premium", tortRepo.findById(1).getTipulTortului());

        IRepository<Tort> newRepo = new TextFileRepository<>(TEST_FILE, new TortConverter());
        assertEquals("Diplomat Premium", newRepo.findById(1).getTipulTortului());
    }

    @Test
    void testUpdateNonExistingIdThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            tortRepo.update(99, new Tort(99, "Inexistent"));
        });
    }

    @Test
    void testFindByIdSuccess() {
        Tort tort = tortRepo.findById(2);
        assertNotNull(tort);
        assertEquals("Ciocolata", tort.getTipulTortului());
    }

    @Test
    void testFindByIdNotFound() {
        Tort tort = tortRepo.findById(99);
        assertNull(tort);
    }

    @Test
    void testDeleteSuccess() {
        tortRepo.remove(2);
        assertEquals(1, tortRepo.getAll().size());

        IRepository<Tort> newRepo = new TextFileRepository<>(TEST_FILE, new TortConverter());
        assertEquals(1, newRepo.getAll().size());
        assertNull(newRepo.findById(2));
    }

    @Test
    void testDeleteNonExistingId() {
        assertThrows(RuntimeException.class, () -> tortRepo.remove(99));
    }
}