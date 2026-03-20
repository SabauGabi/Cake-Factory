package Tests;

import domain.Entity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    private class ConcreteEntity extends Entity {
        public ConcreteEntity(int id) {
            super(id);
        }
    }

    @Test
    void testGetId() {
        ConcreteEntity e = new ConcreteEntity(10);
        assertEquals(10, e.getId());
    }

    @Test
    void testSetId() {
        ConcreteEntity e = new ConcreteEntity(1);
        e.setId(20);
        assertEquals(20, e.getId());
    }


}