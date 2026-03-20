package domain;

import java.io.Serializable;

public abstract class Entity implements Serializable {
    private int id;
    private static final long serialVersionUID = 1L;

    public Entity() {

    }
    public Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}