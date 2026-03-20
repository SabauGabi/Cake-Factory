package Tests;

import domain.Comanda;
import domain.Tort;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComandaTest {

    @Test
    void testConstructorAndGetters() {
        List<Tort> lista = new ArrayList<>();
        lista.add(new Tort(1, "T1"));
        Date data = new Date();

        Comanda comanda = new Comanda(1, lista, data);

        assertEquals(1, comanda.getId());
        assertEquals(lista, comanda.getListaTorturi());
        assertEquals(data, comanda.getData());
    }

    @Test
    void testSetListaTorturi() {
        Comanda comanda = new Comanda(1, new ArrayList<>(), new Date());
        List<Tort> nouaLista = new ArrayList<>();
        nouaLista.add(new Tort(2, "T2"));

        comanda.setListaTorturi(nouaLista);
        assertEquals(1, comanda.getListaTorturi().size());
        assertEquals("T2", comanda.getListaTorturi().get(0).getTipulTortului());
    }

    @Test
    void testSetData() {
        Comanda comanda = new Comanda(1, new ArrayList<>(), new Date(0));
        Date nouaData = new Date(1000);

        comanda.setData(nouaData);
        assertEquals(1000, comanda.getData().getTime());
    }

    @Test
    void testNoArgConstructorInitializesList() {
        Comanda comanda = new Comanda();
        assertNotNull(comanda.getListaTorturi());
        assertTrue(comanda.getListaTorturi().isEmpty());
    }
}