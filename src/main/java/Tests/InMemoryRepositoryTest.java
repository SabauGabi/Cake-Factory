package Tests;

import domain.Entity;
import exception.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.InMemoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryRepositoryTest {

    private class TestEntity extends Entity {
        private String name;
        public TestEntity(int id, String name) {
            super(id);
            this.name = name;
        }
        public TestEntity() {
            super(0);
        }
        public String getName() {
            return name;
        }
    }

    private InMemoryRepository<TestEntity> repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryRepository<>();
        repo.add(new TestEntity(1, "Item1"));
        repo.add(new TestEntity(2, "Item2"));
    }

    @Test
    void testAddSuccess() {
        repo.add(new TestEntity(3, "Item3"));
        assertEquals(3, repo.getAll().size());
        assertEquals("Item3", repo.findById(3).getName());
    }

    @Test
    void testAddDuplicateIdThrowsException() {
        assertThrows(RuntimeException.class, () -> repo.add(new TestEntity(1, "Duplicate")));
    }

    @Test
    void testUpdateSuccess() {
        TestEntity updated = new TestEntity(2, "UpdatedItem");
        repo.update(2, updated);
        assertEquals("UpdatedItem", repo.findById(2).getName());
    }

    @Test
    void testUpdateNonExistingIdThrowsException() {
        assertThrows(RuntimeException.class, () -> repo.update(99, new TestEntity(99, "Inexistent")));
    }

    @Test
    void testFindByIdSuccess() {
        TestEntity item = repo.findById(1);
        assertNotNull(item);
        assertEquals("Item1", item.getName());
    }

    @Test
    void testFindByIdNotFound() {
        TestEntity item = repo.findById(99);
        assertNull(item);
    }

    @Test
    void testGetAll() {
        List<TestEntity> all = repo.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void testDeleteSuccess() {
        repo.remove(1);
        assertEquals(1, repo.getAll().size());
        assertNull(repo.findById(1));
    }

    @Test
    void testDeleteNonExistingId() {
        assertThrows(RuntimeException.class, () -> repo.remove(99));
    }
}