package com.company.common;

/**
 * Classe utilisée pour stocker les informations d'un sommet et de son degré dans un graphe
 */
public class SommetDegre {

    private final Sommet sommet;
    private final int degre;

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
