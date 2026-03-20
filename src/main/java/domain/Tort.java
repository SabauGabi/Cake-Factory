package domain;

import java.io.Serializable;

public class Tort extends Entity implements Serializable {
    public Tort() {
        super();
    }
    private String tipulTortului;
    private static final long serialVersionUID = 1L;

    public Tort(int id, String tipulTortului) {
        super(id);
        this.tipulTortului = tipulTortului;
    }

    public String getTipulTortului() {
        return tipulTortului;
    }

    public void setTipulTortului(String tipulTortului) {
        this.tipulTortului = tipulTortului;
    }

    @Override
    public String toString() {
        return "Tort{" +
                "id=" + getId() +
                ", tipulTortului='" + tipulTortului + '\'' +
                '}';
    }
}