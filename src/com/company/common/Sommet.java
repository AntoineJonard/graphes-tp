package com.company.common;

import java.util.Objects;

/**
 * Objet contenant les informations d'un sommet
 */
public class Sommet {

    private int id;
    private String name;
    /**
     * Utilisé pour générer des ids automatiquement si on fait le graphe a la main
     */
    private static int CPT_ID = 0;

    public Sommet(String name) {
        this.id = CPT_ID++;
        this.name = name;
    }

    public Sommet(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Sommet(int id) {
        this.id = id;
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
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sommet sommet = (Sommet) o;
        return id == sommet.id;
    }

    public boolean sameName(Sommet s){
        return this.getName().equals(s.getName());
    }

    @Override
    public String toString() {
        return "\nSommet "+getName()+" d'id "+id;
    }
}
