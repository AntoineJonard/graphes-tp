package com.company.liste;

import com.company.common.Sommet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListeAdjacence {

    private final HashMap<Sommet, List<Integer>> listeAdjacence;

    public ListeAdjacence() {
        this.listeAdjacence = new HashMap<>();
    }

    /**
     * Le fichier doit se trouver dans le réperoite src/data du projet
     * @param fileName nom du fichier
     */
    public ListeAdjacence(String fileName) throws IOException {
        this();
        File file = new File("src/data/"+fileName);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String line = bufferedReader.readLine();

        int nbSommets = Integer.parseInt(line);

        for (int i = 0 ; i < nbSommets ; i++){
            line = bufferedReader.readLine();
            String[] infosSommet = line.split("\\s+");
            addSommet(new Sommet(Integer.parseInt(infosSommet[0]), infosSommet[1]));
        }

        while ((line = bufferedReader.readLine()) != null){
            String[] arrete = line.split("\\s+");
            addArrete(Integer.parseInt(arrete[0]),Integer.parseInt(arrete[1]));
        }
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

    public void removeArrete(Integer s1Id, Integer s2Id){
        Sommet s1 = new Sommet(s1Id);
        Sommet s2 = new Sommet(s2Id);
        this.listeAdjacence.get(s1).remove(s2Id);
        this.listeAdjacence.get(s2).remove(s1Id);
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

    public boolean isVoisin(Integer s1Id, Integer s2Id){
        Sommet s1 = new Sommet(s1Id);
        return listeAdjacence.get(s1).contains(s2Id);
    }

    public static void main(String[] args) throws IOException {
        ListeAdjacence listeAdjacence = new ListeAdjacence();

        Sommet s1 = new Sommet("1");
        Sommet s2 = new Sommet("2");

        listeAdjacence.addSommet(s1);
        listeAdjacence.addSommet(s2);

        listeAdjacence.addArrete(s1.getId(), s2.getId());
        //listeAdjacence.removeArrete(s1.getId(), s2.getId());

        System.out.print(listeAdjacence.isVoisin(s1.getId(), s2.getId()));

        ListeAdjacence listeAdjacenceFromFile = new ListeAdjacence("graphe.txt");

        System.out.print(listeAdjacenceFromFile.isVoisin(4, 5));
    }
}
