package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Comanda extends Entity {
    private List<Tort> listaTorturi;
    private Date data;

    public Comanda() {
        super();
        this.listaTorturi = new ArrayList<>();
        this.data = new Date();
    }

    public Comanda(int id, List<Tort> listaTorturi, Date data) {
        super(id);
        this.listaTorturi = listaTorturi;
        this.data = data;
    }
    public Comanda(int id, Date data, List<Tort> listaTorturi) {
        super(id);
        this.data = data;
        this.listaTorturi = listaTorturi;
    }

    public List<Tort> getListaTorturi() {
        return listaTorturi;
    }

    public Date getData() {
        return data;
    }

    public void setListaTorturi(List<Tort> listaTorturi) {
        this.listaTorturi = listaTorturi;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + getId() +
                ", data=" + data +
                ", torturi=" + listaTorturi.size() +
                '}';
    }
}