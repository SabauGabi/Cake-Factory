package Tests;

import domain.Tort;
import exception.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.TortConverter;

import static org.junit.jupiter.api.Assertions.*;

public class TortConverterTest {

    private TortConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TortConverter();
    }

    @Test
    void testToStringConversion() {
        Tort tort = new Tort(5, "Lava Cake");
        String expected = "5,Lava Cake";
        assertEquals(expected, converter.toString(tort));
    }

    @Test
    void testFromStringConversionSuccess() {
        String input = "10,Tarta cu Fructe";
        Tort tort = converter.fromString(input);

        assertEquals(10, tort.getId());
        assertEquals("Tarta cu Fructe", tort.getTipulTortului());
    }

    @Test
    void testFromStringConversionInvalidFormatThrowsException() {

        String input = "20";
        assertThrows(RepositoryException.class, () -> converter.fromString(input));

        String input2 = "30,Tip,Extra";
        assertThrows(RepositoryException.class, () -> converter.fromString(input2));
    }

    @Test
    void testFromStringConversionInvalidIdThrowsException() {
        String input = "ABC,Tip";
        assertThrows(RepositoryException.class, () -> converter.fromString(input));
    }
}