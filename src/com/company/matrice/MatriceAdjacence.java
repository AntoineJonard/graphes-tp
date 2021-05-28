package com.company.matrice;

import com.company.common.Sommet;
import com.company.liste.ListeAdjacence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MatriceAdjacence {
    ArrayList<ArrayList<Integer>> matrice;
    ArrayList<Sommet> association;

    public MatriceAdjacence(){
        graphe_vide();
    }

    public MatriceAdjacence(String fileName) throws IOException {
        graphe_vide();
        File file = new File("src/data/"+fileName);

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String line = bufferedReader.readLine();

        int nbSommets = Integer.parseInt(line);

        for (int i = 0 ; i < nbSommets ; i++){
            line = bufferedReader.readLine();
            String[] infosSommet = line.split("\\s+");
            add_sommet(new Sommet(Integer.parseInt(infosSommet[0]), infosSommet[1]));
        }

        while ((line = bufferedReader.readLine()) != null){
            String[] arrete = line.split("\\s+");
            add_arrete(Integer.parseInt(arrete[0]),Integer.parseInt(arrete[1]));
        }
    }

    public void graphe_vide(){
        matrice = new ArrayList<>();
        association = new ArrayList<>();
    }

    public void add_sommet(Sommet s){
        //On ajoute s à la liste des association. s est alors lié à son indice.
        //Si la matrice est vide, on ajoute une nouvelle ligne que l'on initialise à 0.
        if(matrice.isEmpty()){
            association.add(s);
            matrice.add(new ArrayList<Integer>());
            matrice.get(0).add(0);
        } else if(!association.contains(s)){
            association.add(s);
            matrice.forEach(l -> l.add(0));
            matrice.add(new ArrayList<Integer>());
            for(int i = 0; i < matrice.get(0).size(); i++){
                matrice.get(matrice.size()-1).add(0);
            }
        } else {
            System.err.println("Sommet deja présent");
        }
    }

    public void add_arrete(Sommet i, Sommet j){
        //On recupere les indices des sommets i et j
        int indiceI = association.indexOf(i);
        int indiceJ = association.indexOf(j);

        matrice.get(indiceI).set(indiceJ, 1);
        matrice.get(indiceJ).set(indiceI, 1);
    }

    public void add_arrete(Integer i, Integer j){
        int indiceI = -1;
        int indiceJ = -1;

        for(int k = 0; k < association.size(); k++) {
            if(association.get(k).getId() == i) indiceI = k;
            else if(association.get(k).getId() == j) indiceJ = k;
        }

        matrice.get(indiceI).set(indiceJ, 1);
        matrice.get(indiceJ).set(indiceI, 1);
    }

    public void supp(Sommet i, Sommet j){
        int indiceI = association.indexOf(i);
        int indiceJ = association.indexOf(j);

        matrice.get(indiceI).set(indiceJ, 0);
        matrice.get(indiceJ).set(indiceI, 0);
    }

    public int est_voisin(Sommet i, Sommet j){
        int indiceI = association.indexOf(i);
        int indiceJ = association.indexOf(j);

        ArrayList<Integer> ligne = matrice.get(indiceI);
        for(int k = 0; k < ligne.size(); k++){
            if(ligne.get(k) == 1){
                Integer indiceSommet = k;
                if(matrice.get(k).get(indiceJ) == 1) return 1;
            }
        }
        return 0;
    }

    public ListeAdjacence toListe(){
        ListeAdjacence listeAdjacence = new ListeAdjacence();

        for(int i = 0; i < matrice.size(); i++){
            listeAdjacence.addSommet(association.get(i));
            for(int j = 0; j < i; j++){
                if(matrice.get(i).get(j) == 1) listeAdjacence.addArrete(i, j);
            }
        }
        return listeAdjacence;
    }

    public void afficher() {
        for (int i = 1; i < matrice.size(); i++) {
            for (int j = 0; j < matrice.get(i).size(); j++) {
                System.out.print(matrice.get(i).get(j));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        MatriceAdjacence m = new MatriceAdjacence("graphe.txt");
        Sommet s1 = new Sommet("1");
        Sommet s2 = new Sommet("2");

        m.add_sommet(s1);
        m.add_sommet(s2);
        m.add_arrete(s1, s2);

        m.afficher();
        ListeAdjacence l = m.toListe();


    }
}
