package Tests;

import domain.Tort;
import exception.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BinaryFileRepository;
import repository.IRepository;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryFileRepositoryTest {

    private static final String TEST_FILE = "torturi_test.bin";
    private IRepository<Tort> tortRepo;

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }

        tortRepo = new BinaryFileRepository<>(TEST_FILE);

        tortRepo.add(new Tort(1, "BinaryCake1"));
        tortRepo.add(new Tort(2, "BinaryCake2"));
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testAddAndSave() {
        Tort newTort = new Tort(3, "BinaryCake3");
        tortRepo.add(newTort);

        assertEquals(3, tortRepo.getAll().size());

        IRepository<Tort> newRepo = new BinaryFileRepository<>(TEST_FILE);
        assertEquals(3, newRepo.getAll().size());
        assertNotNull(newRepo.findById(3));
        assertEquals("BinaryCake3", newRepo.findById(3).getTipulTortului());
    }

    @Test
    void testUpdate() {
        Tort updatedTort = new Tort(1, "BinaryUpdated");
        tortRepo.update(1, updatedTort);

        assertEquals("BinaryUpdated", tortRepo.findById(1).getTipulTortului());

        IRepository<Tort> newRepo = new BinaryFileRepository<>(TEST_FILE);
        assertEquals("BinaryUpdated", newRepo.findById(1).getTipulTortului());
    }

    @Test
    void testDeleteSuccess() {
        tortRepo.remove(2);
        assertEquals(1, tortRepo.getAll().size());

        IRepository<Tort> newRepo = new BinaryFileRepository<>(TEST_FILE);
        assertEquals(1, newRepo.getAll().size());
        assertNull(newRepo.findById(2));
    }

    @Test
    void testAddDuplicateIdThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            tortRepo.add(new Tort(1, "Duplicat"));
        });
    }

    @Test
    void testUpdateNonExistingIdThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            tortRepo.update(99, new Tort(99, "Inexistent"));
        });
    }
}