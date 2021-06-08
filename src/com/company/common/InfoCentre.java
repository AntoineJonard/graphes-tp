package com.company.common;

import java.util.List;

public class InfoCentre {

    private int rayon;
    private List<Sommet> centres;

    public InfoCentre(int rayon, List<Sommet> centres) {
        this.rayon = rayon;
        this.centres = centres;
    }

    public int getRayon() {
        return rayon;
    }

    public void setRayon(int rayon) {
        this.rayon = rayon;
    }

    public List<Sommet> getCentres() {
        return centres;
    }

    public void setCentres(List<Sommet> centres) {
        this.centres = centres;
    }

    public int getNbCentre(){
        return centres.size();
    }

    @Override
    public String toString() {
        return "Informations centre du graphe :" +
                "\nrayon : " + rayon +
                "\nSommets du centre : " + centres;
    }
}
