package Tests;

import domain.Comanda;
import domain.Tort;
import exception.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ComandaConverter;
import repository.IRepository;
import repository.TextFileRepository;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComandaRepositoryTest {

    private static final String TEST_FILE = "comenzi_test.txt";
    private IRepository<Comanda> comandaRepo;
    private List<Tort> mockTorturi;

    @BeforeEach
    void setUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }

        mockTorturi = Arrays.asList(new Tort(10, "T1"), new Tort(11, "T2"));

        comandaRepo = new TextFileRepository<>(TEST_FILE, new ComandaConverter());

        Comanda c1 = new Comanda(1, mockTorturi.subList(0, 1), new Date(System.currentTimeMillis() + 86400000));
        Comanda c2 = new Comanda(2, mockTorturi, new Date(System.currentTimeMillis() + 86400000 * 2));

        comandaRepo.add(c1);
        comandaRepo.add(c2);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void testAddAndSave() {
        Comanda newComanda = new Comanda(3, mockTorturi.subList(1, 2), new Date(System.currentTimeMillis() + 86400000 * 3));
        comandaRepo.add(newComanda);

        assertEquals(3, comandaRepo.getAll().size());

        IRepository<Comanda> newRepo = new TextFileRepository<>(TEST_FILE, new ComandaConverter());
        assertEquals(3, newRepo.getAll().size());
        assertNotNull(newRepo.findById(3));
    }

    @Test
    void testAddDuplicateIdThrowsException() {
        Comanda c_dup = new Comanda(1, mockTorturi, new Date(System.currentTimeMillis() + 86400000 * 4));
        assertThrows(RuntimeException.class, () -> {
            comandaRepo.add(c_dup);
        });
    }

    @Test
    void testUpdate() {
        Comanda updatedComanda = new Comanda(1, mockTorturi.subList(1, 2), new Date(System.currentTimeMillis() + 100000000));
        comandaRepo.update(1, updatedComanda);

        assertEquals(1, comandaRepo.findById(1).getListaTorturi().size());

        IRepository<Comanda> newRepo = new TextFileRepository<>(TEST_FILE, new ComandaConverter());
        assertNotNull(newRepo.findById(1));
    }

    @Test
    void testUpdateNonExistingIdThrowsException() {
        Comanda c_inexistent = new Comanda(99, mockTorturi, new Date(System.currentTimeMillis() + 5000000));
        assertThrows(RuntimeException.class, () -> {
            comandaRepo.update(99, c_inexistent);
        });
    }

    @Test
    void testFindByIdSuccess() {
        Comanda comanda = comandaRepo.findById(2);
        assertNotNull(comanda);
        assertEquals(2, comanda.getListaTorturi().size());
    }

    @Test
    void testFindByIdNotFound() {
        Comanda comanda = comandaRepo.findById(99);
        assertNull(comanda);
    }

    @Test
    void testDeleteSuccess() {
        comandaRepo.remove(2);
        assertEquals(1, comandaRepo.getAll().size());

        IRepository<Comanda> newRepo = new TextFileRepository<>(TEST_FILE, new ComandaConverter());
        assertEquals(1, newRepo.getAll().size());
        assertNull(newRepo.findById(2));
    }

    @Test
    void testDeleteNonExistingId() {
        assertThrows(RuntimeException.class, () -> comandaRepo.remove(99));
    }
}