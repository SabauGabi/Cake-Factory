package Tests;

import domain.Tort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TortTest {

    @Test
    void testConstructorAndGetters() {
        Tort tort = new Tort(100, "Cheesecake");
        assertEquals(100, tort.getId());
        assertEquals("Cheesecake", tort.getTipulTortului());
    }

    @Test
    void testSetTipulTortului() {
        Tort tort = new Tort(1, "Original");
        tort.setTipulTortului("Modificat");
        assertEquals("Modificat", tort.getTipulTortului());
    }

    @Test
    void testToStringContainsDetails() {
        Tort tort = new Tort(2, "Red Velvet");
        String output = tort.toString();
        assertTrue(output.contains("id=2"));
        assertTrue(output.contains("Red Velvet"));
    }
}