package com.company.common;

public class SommetDegre {

    private Sommet sommet;
    private int degre;

    public SommetDegre(Sommet sommet, int degre) {
        this.sommet = sommet;
        this.degre = degre;
    }

    public Sommet getSommet() {
        return sommet;
    }

    public int getDegre() {
        return degre;
    }
}
