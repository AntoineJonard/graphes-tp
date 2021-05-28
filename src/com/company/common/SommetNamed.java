package com.company.common;

import java.util.Objects;
/* Classe pour pouvoir comparer les sommets sur leurs noms*/
public class SommetNamed {

    private int id;
    private String name;

    public SommetNamed(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SommetNamed that = (SommetNamed) o;
        return Objects.equals(name, that.name);
    }

    public Sommet toSommet(){
        return new Sommet(getId(),getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
