package com.company.matrice;

import com.company.common.Sommet;
import com.company.liste.ListeAdjacence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatriceAdjacence {
    /** Matrice représentant les arretes */
    List<ArrayList<Integer>> matrice;
    /** Liste des sommets liés à leur Identifiant */
    ArrayList<Sommet> sommetsList;

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

    /** Initialise le graphe */
    public void graphe_vide(){
        matrice = new ArrayList<>();
        sommetsList = new ArrayList<>();
    }

    /** On ajoute s à la liste des association. s est alors lié à son indice.
    Si la matrice est vide, on ajoute une nouvelle ligne que l'on initialise à 0. */
    public void add_sommet(Sommet s){
        if(matrice.isEmpty()){
            sommetsList.add(s);
            matrice.add(new ArrayList<>());
            matrice.get(0).add(0);
        } else if(!sommetsList.contains(s)){
            sommetsList.add(s);
            matrice.forEach(l -> l.add(0));
            matrice.add(new ArrayList<>());
            for(int i = 0; i < matrice.get(0).size(); i++){
                matrice.get(matrice.size()-1).add(0);
            }
        } else {
            System.err.println("Sommet deja présent");
        }
    }

    public void add_arrete(Sommet i, Sommet j){
        //On recupere les indices des sommets i et j
        int indiceI = sommetsList.indexOf(i);
        int indiceJ = sommetsList.indexOf(j);

        matrice.get(indiceI).set(indiceJ, 1);
        matrice.get(indiceJ).set(indiceI, 1);
    }

    public void add_arrete(Integer i, Integer j){
        int indiceI = -1;
        int indiceJ = -1;

        for(int k = 0; k < sommetsList.size(); k++) {
            if(sommetsList.get(k).getId() == i) indiceI = k;
            else if(sommetsList.get(k).getId() == j) indiceJ = k;
        }

        matrice.get(indiceI).set(indiceJ, 1);
        matrice.get(indiceJ).set(indiceI, 1);
    }

    public void supp(Sommet i, Sommet j){
        int indiceI = sommetsList.indexOf(i);
        int indiceJ = sommetsList.indexOf(j);

        matrice.get(indiceI).set(indiceJ, 0);
        matrice.get(indiceJ).set(indiceI, 0);
    }

    /**On verifie pour chaque sommet s'il est voisin du sommet actuel */
    public int est_voisin(Sommet i, Sommet j){
        int indiceI = sommetsList.indexOf(i);
        int indiceJ = sommetsList.indexOf(j);

        ArrayList<Integer> ligne = matrice.get(indiceI);
        for(int k = 0; k < ligne.size(); k++){
            if(ligne.get(k) == 1){
                Integer indiceSommet = k;
                if(matrice.get(k).get(indiceJ) == 1) return 1;
            }
        }
        return 0;
    }

    /** On ajoute que la matrice triangulaire inferieur */
    public ListeAdjacence toListe(){
        ListeAdjacence listeAdjacence = new ListeAdjacence();
        for(int i = 0; i < matrice.size(); i++){
            listeAdjacence.addSommet(sommetsList.get(i));
            for(int j = 0; j < i; j++){
                if(matrice.get(i).get(j) == 1) listeAdjacence.addArrete(i, j);
            }
        }
        return listeAdjacence;
    }

    /** Renvoie true si les sommets du graphe actuel sont inclus dans le graphe passe en parametres, faux sinon*/
    public boolean sommets_Inclus_Dans(MatriceAdjacence G, boolean strict){
        /* On recupere les noms des sommets actuels*/
        List<String> nomSommets = sommetsList.stream().map(Sommet::getName).collect(Collectors.toList());
        /* On recupere les noms des sommets du graphe passes en parametres*/
        List<String> nomSommetsAutre = G.sommetsList.stream().map(Sommet::getName).collect(Collectors.toList());

        /*Si strict alors renvoie true si G inclus dans G' et G' pas inclus dans G*/
        if (strict) return nomSommetsAutre.containsAll(nomSommets) && !nomSommets.containsAll(nomSommetsAutre);
        /* Renvoie true si G inclus G', peut renvoyer true si G = G'*/
        return nomSommetsAutre.containsAll(nomSommets);
    }

    /** Compte le nombre d'arretes du graphe */
    public int nombre_Arretes(){
        int nbArcs = 0;
        for(int i = 1; i < matrice.size(); i++){
            for(int j = 0; j < i; j++){
                if(matrice.get(i).get(j) == 1) nbArcs++;
            }
        }
        return nbArcs;
    }

    /** On verifie si le premier graphe est inclus dans G, ensuite on compte les arretes de G et
     * on verifie si nbArcs < nbArcsG
     * */
    public boolean arretes_Incluses_Dans(MatriceAdjacence G2){
        int nbArcs = 0;
        int nbArcsG = 0;

        //Recuperer les deux noms pour chaque arrete
        for(int i = 1; i < matrice.size(); i++){
            for(int j = 0; j < i; j++){
                int id1 = -1;
                int id2 = -1;
                if(matrice.get(i).get(j) == 1){
                    nbArcs++;
                    String name1 = sommetsList.get(i).getName();
                    String name2 = sommetsList.get(j).getName();

                    //Trouver les id des sommmets qui ont ces noms dans le graphe G
                    for (int k = 0; k < G2.sommetsList.size(); k++) {
                        if(G2.sommetsList.get(k).getName().equals(name1)) {id1 = k;}
                        else if(G2.sommetsList.get(k).getName().equals(name2)) {id2 = k;}
                    }
                    if(id1 == -1 || id2 == -1) return false;

                    //On s'arrete si l'arrete n'est pas presente
                    if(G2.matrice.get(id1).get(id2) != 1 || G2.matrice.get(id2).get(id1) != 1 ) {  return false;}
                }
            }
        }

        //On verifie si nbArcs < NbArcsG
        nbArcsG = G2.nombre_Arretes();
        return nbArcs < nbArcsG;
    }
    /** Sommet strictement inclus, arretes incluses */
    public boolean est_Partiel_De(MatriceAdjacence G2){
        return this.arretes_Incluses_Dans(G2) && sommets_Inclus_Dans(G2, false);
    }
    /** Arretes et sommets strictement inclus */
    public boolean est_Sous_Graphe_De(MatriceAdjacence G2){
        return this.arretes_Incluses_Dans(G2) && this.sommets_Inclus_Dans(G2, true);
    }

    /** */
    /*public est_Sous_Graphe_Partiel(MatriceAdjacence G2){

    }*/

    /** On verifie que G2 est bien un sous graphe de G, et que G2 est complet*/
    public boolean est_Clique_De(MatriceAdjacence G2){
        int nbSommets = G2.sommetsList.size();
        return (nbSommets*(nbSommets-1))/2 == G2.nombre_Arretes() && G2.est_Sous_Graphe_De(this);
    }

    /** On verifie que G2 est bien un sous graphe de G, et que G2 n'a pas d'arretes */
    public boolean est_Stable_De(MatriceAdjacence G2){
        return G2.nombre_Arretes() == 0 && G2.est_Sous_Graphe_De(this);
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
        MatriceAdjacence G = new MatriceAdjacence("graphe2.txt");
        System.out.println(m.arretes_Incluses_Dans(G));
        //m.afficher();


    }
}
