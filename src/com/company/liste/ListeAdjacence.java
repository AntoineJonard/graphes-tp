package com.company.liste;

import com.company.common.Sommet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeAdjacence {

    private final HashMap<Sommet, List<Integer>> listeAdjacence;

    public ListeAdjacence() {
        this.listeAdjacence = new HashMap<>();
    }

    public void addSommet(Sommet s){
        if (!this.listeAdjacence.containsKey(s))
            this.listeAdjacence.put(s,new ArrayList<>());
        else
            System.out.println("Sommet déjà existant");
    }

    public void addArrete(int s1Id, int s2Id){
        Sommet s1 = new Sommet(s1Id);
        Sommet s2 = new Sommet(s2Id);
        this.listeAdjacence.get(s1).add(s2Id);
        this.listeAdjacence.get(s2).add(s1Id);
    }

    public void removeArrete(int s1Id, int s2Id){
        Sommet s1 = new Sommet(s1Id);
        Sommet s2 = new Sommet(s2Id);
        this.listeAdjacence.get(s1).remove(s1Id);
        this.listeAdjacence.get(s2).remove(s2Id);
    }

    public void removeSommet(int sId){
        Sommet s = new Sommet(sId);
        this.listeAdjacence.remove(s);
        for (Map.Entry entry : this.listeAdjacence.entrySet()){
            ArrayList<Integer> sommetsIds = ((ArrayList<Integer>)entry.getValue());
            for (int i = 0 ; i < sommetsIds.size() ; i++){
                if (sommetsIds.get(i).equals(sId)){
                    sommetsIds.remove(sommetsIds.get(i));
                }
            }
        }
    }

    public static void main(String[] args) {
        ListeAdjacence listeAdjacence = new ListeAdjacence();

        Sommet s1 = new Sommet("1");
        Sommet s2 = new Sommet("2");

        listeAdjacence.addSommet(s1);
        listeAdjacence.addSommet(s2);

        listeAdjacence.addArrete(s1.getId(), s2.getId());

        System.out.print(listeAdjacence);
    }
}
