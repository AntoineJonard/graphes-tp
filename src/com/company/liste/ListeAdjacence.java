package com.company.liste;

import com.company.common.Sommet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeAdjacence {

    private final HashMap<Sommet, List<Sommet>> listeAdjacence;

    public ListeAdjacence() {
        this.listeAdjacence = new HashMap<>();
    }

    public void addSommet(Sommet s){
        this.listeAdjacence.put(s,new ArrayList<>());
    }

    public void addArrete(Sommet s1, Sommet s2){
        this.listeAdjacence.get(s1).add(s2);
        this.listeAdjacence.get(s2).add(s1);
    }

    public void removeArrete(Sommet s1, Sommet s2){
        this.listeAdjacence.get(s1).remove(s2);
        this.listeAdjacence.get(s2).remove(s1);
    }

    public void removeSommet(Sommet s){
        this.listeAdjacence.remove(s);
        for (Map.Entry entry : this.listeAdjacence.entrySet()){
            ArrayList<Sommet> sommets = ((ArrayList<Sommet>)entry.getValue());
            for (int i = 0 ; i < sommets.size() ; i++){
                if (sommets.get(i).equals(s)){
                    sommets.remove(sommets.get(i));
                }
            }
        }
    }

    public static void main(String[] args) {
        ListeAdjacence listeAdjacence = new ListeAdjacence();

        listeAdjacence.addSommet(new Sommet("1"));
    }
}
