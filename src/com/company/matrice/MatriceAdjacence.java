package com.company.matrice;

import com.company.common.InfoCentre;
import com.company.common.MinDistance;
import com.company.common.Sommet;
import com.company.liste.ListeAdjacence;

import java.io.*;
import java.util.*;
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
    /** Enregister un graphe */
    public void save(String fileName) throws IOException {
        File file = new File("src/data/"+fileName);
        if (file.createNewFile()) System.out.println("Ficher créer");
        else System.out.println("Fichier deja existant, écrasement...");

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(sommetsList.size()+"\n");

        for (Sommet s : sommetsList){
            bufferedWriter.write(s.getId()+" "+s.getName()+"\n");
        }

        for (int i = 1 ; i < matrice.size(); i++){
            for (int j = 0; j < matrice.size(); j++){
                int sommetA = sommetsList.get(i).getId();
                int sommetB = sommetsList.get(j).getId();
                bufferedWriter.write(sommetA + " " + sommetB + "\n");
            }
        }

        bufferedWriter.close();

        System.out.println("Fichier correctement sauvegardé");
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

    //Exercice 2

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
        return arretes_Incluses_Dans(G2) && sommets_Inclus_Dans(G2, false);
    }
    /** Arretes et sommets strictement inclus */
    public boolean est_Sous_Graphe_De(MatriceAdjacence G2){
        return this.arretes_Incluses_Dans(G2) && this.sommets_Inclus_Dans(G2, true);
    }

    public boolean est_Sous_Graphe_Partiel(MatriceAdjacence G2){
        return sommets_Inclus_Dans(G2,true) && arretes_Incluses_Dans(G2) && nombre_Arretes() + 2 <= G2.nombre_Arretes();
    }

    /** On verifie que G est bien un sous graphe de G2, et que G est complet*/
    public boolean est_Clique_De(MatriceAdjacence G2){
        int nbSommets = sommetsList.size();
        return (nbSommets*(nbSommets-1))/2 == this.nombre_Arretes() && sommets_Inclus_Dans(G2, true);
    }

    /** On verifie que G est bien un sous graphe de G2, et que G n'a pas d'arretes */
    public boolean est_Stable_De(MatriceAdjacence G2){
        return nombre_Arretes() == 0 && sommets_Inclus_Dans(G2, false);
    }

    //Exercice 3
    public Collection<MinDistance> computeMinDistances(){
        Map<Sommet, MinDistance> minDistList = new HashMap<>();

        List<Sommet> treated; // Liste des sommets traités

        // On initialise la liste avec tous les sommets du graphe
        sommetsList.forEach(s -> minDistList.put(s, new MinDistance(s)));

        for(int i = 0; i < sommetsList.size(); i++){
            treated = new ArrayList<>();
            Sommet s = sommetsList.get(i);

            //On créer une file de données à traiter
            Queue<Sommet> parcours = new PriorityQueue<>(Comparator.comparingInt(o -> minDistList.get(s).getMinDistance(o)));
            parcours.add(s);
            treated.add(s);
            minDistList.get(s).updateMinDistance(s, 0);

            //Tant qu'il reste des sommets
            while(!parcours.isEmpty()){
                Sommet current = parcours.poll(); //On recupere la tete de la liste
                int idCurrent = sommetsList.indexOf(current);
                //On determine la distance min des suivants
                for(int j = 0; j < sommetsList.size(); j++){
                    //S'il y a une lisaison
                    if(matrice.get(idCurrent).get(j) == 1){
                        Sommet suivant = sommetsList.get(j); //On recupere le sommet suivant
                        int distance = minDistList.get(s).getMinDistance(current)+1;

                        if (minDistList.get(s).better(suivant, distance) && !treated.contains(suivant)){
                            parcours.remove(suivant);
                            minDistList.get(s).updateMinDistance(suivant, distance);
                            parcours.offer(suivant);
                            treated.add(suivant);
                        }
                    }
                }
            }
        }

        return minDistList.values();
    }

    public int diametre(){
        int maxDist = 0;

        Collection<MinDistance> minDistances = computeMinDistances();
        //Pour chaque sommet, on cherche la valeur maximale des distances minimums, et renvoie la plus grande valeur
        for(MinDistance minDist: minDistances){
            // On regarde la valeur maximale des distances a chaque sommet
            Integer maxS = minDist.getMaxMinDistance();
            // On met a jour si elle est plus grande que la précédente
            if (maxS != null && maxS > maxDist)
                maxDist = maxS;
        }
        return maxDist;
    }

    /** Renvoie un objet InfoCentre qui contient la liste des centres, le nombre de centres et le rayon */
    public InfoCentre centres(){
        ArrayList<Sommet> centres = new ArrayList<>();
        int rayon = Integer.MAX_VALUE;

        // Tout les objets contenant les infos de distance minimal d'un sommet avec les autres
        Collection<MinDistance> minDistances = computeMinDistances();

        for (MinDistance minDistance : minDistances){
            // On regard la valeur maximale des distances a chaque sommet
            Integer maxS = minDistance.getMaxMinDistance();
            // Si l'excentricité est la meme alors on ajoute a la liste des sommets du centre
            if (maxS != null && maxS == rayon){
                centres.add(minDistance.getFrom());
            }
            // Si l'excentricité est plus petite alors on met a jout la liste des centres et la nouvelle excentricité
            if (maxS != null && maxS < rayon){
                centres.clear();
                rayon = maxS;
                centres.add(minDistance.getFrom());
            }
        }
        
        return new InfoCentre(rayon, centres);
    }

    /** Renvoie une Map associant le sommet (clé) à son degré  */
    public Map<Sommet, Integer> allDegres(){
        Map<Sommet, Integer> degres = new HashMap<>();
        for(int i = 0; i < matrice.size(); i++){
            int deg = 0;
            for(int j = 0; j < i; j++){
                if(matrice.get(i).get(j) == 1) deg++;
            }
            degres.put(sommetsList.get(i), deg);
        }
        return degres;
    }

    /** Calcul le centre en fonction du degre */
    public InfoCentre centresDegres(){
        ArrayList<Sommet> centres = new ArrayList<>();
        int degreMin = Integer.MAX_VALUE;

        Map<Sommet, Integer> minDistances = allDegres();

        for (Sommet s : minDistances.keySet()){
            if (minDistances.get(s) == degreMin){
                centres.add(s);
            }
            if (minDistances.get(s) < degreMin){
                centres.clear();
                degreMin = minDistances.get(s);
                centres.add(s);
            }
        }

        return new InfoCentre(degreMin, centres);
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
        MatriceAdjacence base = new MatriceAdjacence("graphe_base.txt");
        MatriceAdjacence G = new MatriceAdjacence("graphe_clique_base.txt");
        MatriceAdjacence G1 = new MatriceAdjacence("graphe_partiel_base.txt");
        MatriceAdjacence G2 = new MatriceAdjacence("graphe_sous_base.txt");
        MatriceAdjacence G3 = new MatriceAdjacence("graphe_stable_base.txt");
        //System.out.println(G.est_Clique_De(base));
        //System.out.println(G2.est_Sous_Graphe_De(base));
        //System.out.println(G1.est_Partiel_De(base));
        //System.out.println(G3.est_Stable_De(base));
        /*Sommet s1 = new Sommet("s1");
        Sommet s2 = new Sommet("s2");
        Sommet s3 = new Sommet("s3");

        base.add_sommet(s1);
        base.add_sommet(s3);
        base.add_sommet(s2);

        base.add_arrete(s1, s2);
        base.save("testdqdqsdqdsqsd.txt");
        */
        //m.afficher();

        System.out.println(base.diametre());
        System.out.println(base.centres());
        System.out.println(base.centresDegres());
    }
}
