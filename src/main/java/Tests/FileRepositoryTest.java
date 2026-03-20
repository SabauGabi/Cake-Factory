package Tests;

import domain.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.FileRepository;

import static org.junit.jupiter.api.Assertions.*;

public class FileRepositoryTest {

    private class MockFileRepository extends FileRepository<TestEntity> {

        private boolean loadCalled = false;
        private boolean saveCalled = false;

        public MockFileRepository(String fileName) {
            super(fileName);
        }

        @Override
        protected void loadData() {
            loadCalled = true;
        }

        @Override
        protected void saveData() {
            saveCalled = true;
        }

        public boolean isLoadCalled() {
            return loadCalled;
        }

        public boolean isSaveCalled() {
            return saveCalled;
        }
    }

    private class TestEntity extends Entity {
        public TestEntity(int id) {
            super(id);
        }
    }

    private MockFileRepository repo;

    @BeforeEach
    void setUp() {
        repo = new MockFileRepository("mock.txt");
    }

    @Test
    void testAddCallsSaveData() {
        repo.add(new TestEntity(1));
        assertTrue(repo.isSaveCalled());
    }

    @Test
    void testUpdateCallsSaveData() {
        repo.add(new TestEntity(2));
        repo.saveCalled = false;

        repo.update(2, new TestEntity(2));
        assertTrue(repo.isSaveCalled());
    }

    @Test
    void testDeleteCallsSaveData() {
        repo.add(new TestEntity(3));
        repo.saveCalled = false;

        repo.remove(3);
        assertTrue(repo.isSaveCalled());
    }
}