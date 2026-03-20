package Tests;

import domain.Comanda;
import domain.Tort;
import exception.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.ComandaConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComandaConverterTest {

    private ComandaConverter converter;
    private List<Tort> mockTorturi;

    @BeforeEach
    void setUp() {
        converter = new ComandaConverter();
        mockTorturi = new ArrayList<>();
        mockTorturi.add(new Tort(10, "T1"));
        mockTorturi.add(new Tort(20, "T2"));
    }

    @Test
    void testToStringConversion() {
        Date data = new Date(1672531200000L); // 01/01/2023
        Comanda comanda = new Comanda(1, mockTorturi, data);


        String output = converter.toString(comanda);
        assertTrue(output.startsWith("1,1672531200000"));
        assertTrue(output.endsWith("10|20"));
    }

    @Test
    void testFromStringConversionSuccess() {
        long timestamp = new Date().getTime() + 100000;
        String input = "5," + timestamp + ",10|20|30";

        Comanda comanda = converter.fromString(input);

        assertEquals(5, comanda.getId());
        assertEquals(timestamp, comanda.getData().getTime());

        assertTrue(comanda.getListaTorturi().isEmpty());
    }

    @Test
    void testFromStringConversionInvalidFormatThrowsException() {

        String input = "10";
        assertThrows(RepositoryException.class, () -> converter.fromString(input));


        String input2 = "10,ABC,1";
        assertThrows(RepositoryException.class, () -> converter.fromString(input2));
    }
}