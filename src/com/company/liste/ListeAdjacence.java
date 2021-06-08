package com.company.liste;

import com.company.common.InfoCentre;
import com.company.common.MinDistance;
import com.company.common.Sommet;
import com.company.common.SommetDegre;
import com.company.matrice.MatriceAdjacence;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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
        System.out.println("Réussite de la création du graphe");
    }

    /**
     * Le fichier se trouvera dans le réperoite src/data du projet
     * @param fileName nom du fichier qui sera créer;
     */
    public void save(String fileName) throws IOException {

        File file = new File("src/data/"+fileName);
        if (file.createNewFile()){
            System.out.println("Ficher créer");
        }else{
            System.out.println("Fichier deja existant, écrasement...");
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(listeAdjacence.size()+"\n");

        for (Sommet s : listeAdjacence.keySet()){
            bufferedWriter.write(s.getId()+" "+s.getName()+"\n");
        }
        for (Map.Entry<Sommet, List<Integer>> sa : listeAdjacence.entrySet()){
            for (Integer extremite : sa.getValue()){
                bufferedWriter.write(sa.getKey().getId()+" "+extremite+"\n");
            }
        }

        bufferedWriter.close();

        System.out.println("Fichier correctement sauvegardé");
    }

    /** Ajoute un sommet au graphe**/
    public void addSommet(Sommet s){
        if (!this.listeAdjacence.containsKey(s))
            this.listeAdjacence.put(s,new ArrayList<>());
        else
            System.out.println("Sommet déjà existant");
    }

    /** Ajoute une arrete entre les deux sommets du graphe spécifiés par leurs ids **/
    public void addArrete(int s1Id, int s2Id){
        Sommet s1 = new Sommet(s1Id);
        Sommet s2 = new Sommet(s2Id);
        this.listeAdjacence.get(s1).add(s2Id);
        this.listeAdjacence.get(s2).add(s1Id);
    }

    /** Enleve l'arrete entre les deux sommets du graphe spécifiés par leurs ids **/
    public void removeArrete(Integer s1Id, Integer s2Id){
        Sommet s1 = new Sommet(s1Id);
        Sommet s2 = new Sommet(s2Id);
        this.listeAdjacence.get(s1).remove(s2Id);
        this.listeAdjacence.get(s2).remove(s1Id);
    }

    /** Enleve du graphe le sommet spécifié par son id **/
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

    /** récupérer l'objet sommet du graphe à l'aide de son id **/
    public Sommet getSommetById(Integer id){
        for (Sommet s : listeAdjacence.keySet()){
            if (s.getId() == id){
                return  s;
            }
        }
        return null;
    }

    /** Existe t-il une arrete entre les deux sommets identifié en argument ? **/
    public boolean isVoisin(Integer s1Id, Integer s2Id){
        Sommet s1 = new Sommet(s1Id);
        if (listeAdjacence.get(s1)==null)
            return false;
        return listeAdjacence.get(s1).contains(s2Id);
    }

    /** Retourne un objet matrice correspondant au graphe courant **/
    public MatriceAdjacence toMatrice(){
        MatriceAdjacence matriceAdjacence = new MatriceAdjacence();

        for(Sommet s : listeAdjacence.keySet()){
            matriceAdjacence.add_sommet(s);
        }

        for(Map.Entry<Sommet,List<Integer>> entry : listeAdjacence.entrySet()){
            Sommet from = entry.getKey();
            List<Integer> to = entry.getValue();
            for (Integer id: to)
                matriceAdjacence.add_arrete(from.getId(),id);
        }

        return matriceAdjacence;
    }

    /** Les sommets du graphe courant sont_ils inclus dans le graphe passé en paramètre ?
     * @param g2 le graphe de référence
     * @param stricte L'inclusion doit-elle être stricte ?
     */
    public boolean sommetsInclusDans(ListeAdjacence g2, boolean stricte){
        Set<Sommet> mySommets = listeAdjacence.keySet();
        Set<Sommet> otherSommets = g2.listeAdjacence.keySet();
        // Mapping pour récupérer les des listes de noms (les comparaisons se faisant en fonction des noms et non des ids
        Set<String> mySommetsNames = mySommets.stream().map(Sommet::getName).collect(Collectors.toSet());
        Set<String> othersSommetsNames = otherSommets.stream().map(Sommet::getName).collect(Collectors.toSet());
        if (stricte)
            return othersSommetsNames.containsAll(mySommetsNames) && !mySommetsNames.containsAll(othersSommetsNames);
        return othersSommetsNames.containsAll(mySommetsNames);
    }

    /** Les arretes du graphe courant sont-elles incluses dans le graphe passé en paramètre ?
     * @param g2 le graphe de référence
     */
    public boolean arretesInclusesDans(ListeAdjacence g2){
        // passe a false si on ne trouve pas une arrete dans l'itération
        boolean stillTotallIncluded = true;

        for(Map.Entry<Sommet,List<Integer>> entry1 : listeAdjacence.entrySet()){
            for(Map.Entry<Sommet,List<Integer>> entry2 : g2.listeAdjacence.entrySet()){
                if (entry1.getKey().sameName(entry2.getKey())){
                    // Mapping pour récupérer les des listes de noms (les comparaisons se faisant en fonction des noms et non des ids
                    Set<String> sommets1 = entry1.getValue().stream().map(sommetId->getSommetById(sommetId).getName()).collect(Collectors.toSet());
                    Set<String> sommets2 = entry2.getValue().stream().map(sommetId->g2.getSommetById(sommetId).getName()).collect(Collectors.toSet());
                    if (!sommets2.containsAll(sommets1)) return false;
                    if (!sommets1.containsAll(sommets2)) stillTotallIncluded = false;
                }
            }
        }
        return !stillTotallIncluded;
    }

    /** Le graphe courant est il partiel de celui passé en argument ?
     * On vérifie donc que tout les sommets sont dans les deux graphes et que les arretes du courant sont incluses
     */
    public boolean estPartielDe(ListeAdjacence g2){
        return sommetsInclusDans(g2,false) && arretesInclusesDans(g2) && getNbSommet()==g2.getNbSommet();
    }

    /** Le graphe courant est-il sous graphe de celui passé en paramètre ?
     *  On vérifie donc une inclusion stricte des sommets et une inclusion des arretes
     * **/
    public boolean estSousGrapheDe(ListeAdjacence g2){
        return sommetsInclusDans(g2,true) && arretesInclusesDans(g2);
    }

    /** Nombre d'arretes d'un graphe **/
    public int getNBArretes(){
        int nbArrete = 0;
        for(Map.Entry<Sommet,List<Integer>> entry : listeAdjacence.entrySet()){
            nbArrete += entry.getValue().size();
        }
        return nbArrete / 2;
    }

    /** Nombre de sommet d'un graphe **/
    public int getNbSommet(){
        return listeAdjacence.size();
    }

    /** Le graphe courant est-il sous graphe partiel de celui passé en paramètre ?
     *  On vérifie donc une inclusion stricte des sommets et une inclusion des arretes
     *  On vérifie également que le nombre d'arrete correspond a un sous graphe partiel : dans la mesure ou deux inclusion
     *  stricte des arretes doivent être faites pour obtenir d'abord le sous graphe puis le partiel, le sous graphe partiel
     *  a au moins deux arretes en moins.
     * **/
    public boolean estSousGraphePartielDe(ListeAdjacence g2){
        return sommetsInclusDans(g2,true) && arretesInclusesDans(g2) && getNBArretes() + 2 <= g2.getNBArretes();
    }

    /** Le graphe courant est-il une clique du graphe passé en argument ?**/
    public boolean estCliqueDe(ListeAdjacence g2){
        if (!sommetsInclusDans(g2,true) )
            return false;

        int nbSommets = listeAdjacence.size();

        // Formule de vérification d'un graphe complet
        return (nbSommets*(nbSommets-1))/2 == getNBArretes();
    }

    /** Le graphe courant est-il un stable du graphe passé en argument ?**/
    public boolean estStableDe(ListeAdjacence g2){
        // On vérifie que aucune arrete n'existe
        return sommetsInclusDans(g2,false) && getNBArretes()==0;
    }

    /** Parcous en largueur pour calculer les distances min de tous les sommets à tous les sommets **/
    public Collection<MinDistance> computeMinDistances(){

        // Liste des sommets et des distances à tous les autres
        Map<Sommet, MinDistance> minDistances = new HashMap<>();

        List<Sommet> treated;

        // Initialisation de la liste avec tous les sommets du graphe
        for (Sommet s : listeAdjacence.keySet())
            minDistances.put(s,new MinDistance(s));

        // On itère sur tous les sommet pour faire un parcours en largueur dont le point de départ sera tour à tour les sommets du graphe
        for (Sommet s : listeAdjacence.keySet()){
            treated = new ArrayList<>();
            // File des sommets a traité dans le parcours
            // On trie dans la file en fonction des distance pour assurer une répidité de calcul
            Queue<Sommet> parcours = new PriorityQueue<>(Comparator.comparingInt(o -> minDistances.get(s).getMinDistance(o)));
            // initialisation de la file avec le sommet de départ
            parcours.add(s);
            treated.add(s);
            minDistances.get(s).updateMinDistance(s,0);
            // Tant qu'il y a des sommets à traités
            while(!parcours.isEmpty()){
                Sommet current = parcours.poll();

                // On determine la distance min des suivants la tete de file en fonction de la distance min de la tete de file
                for (Integer suivantId : listeAdjacence.get(current)){
                    Sommet suivant = getSommetById(suivantId);

                    int distance = minDistances.get(s).getMinDistance(current)+1;

                    if (minDistances.get(s).better(suivant, distance) && !treated.contains(suivant)){
                        parcours.remove(suivant);
                        minDistances.get(s).updateMinDistance(suivant, distance);
                        parcours.offer(suivant);
                        treated.add(suivant);
                    }
                }
            }
        }

        return minDistances.values();
    }

    /**
     * Calcul le diametre du graphe courant à l'aide de l'objet minDistance
     * @return le Diametre du graphe courant
     */
    public int computeDiametre(){
        // Distance maximale qui sera mise a jour
        int maxDistance = 0;

        // Tout les objets contenant les infos de distance minimal d'un sommet avec les autres
        Collection<MinDistance> minDistances = computeMinDistances();

        for (MinDistance minDistance : minDistances){
            // On regard la valeur maximale des distances a chaque sommet
            Integer maxS = minDistance.getMaxMinDistance();
            // On met a jout si elle est plus grande que la précédente
            if (maxS != null && maxS > maxDistance)
                maxDistance = maxS;
        }
        return maxDistance;
    }

    /**
     * Calcul le centre d'un graphe selon son excentricité
     * @return Un objet contenant toutes les informations du centre (les sommets, l'excentricité, le nombre de sommet)
     */
    public InfoCentre computeCentre(){
        // On initialise le rayon au maximum
        int rayon = Integer.MAX_VALUE;
        List<Sommet> centres = new ArrayList<>();

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

    /**
     * Calcul le degré de tous les sommets du graphe
     * @return La liste des sommets auquel on a associé un degré
     */
    public List<SommetDegre> computeDegres(){
        HashMap<Sommet, Integer> degres = new HashMap<>();
        // Map chaque entrée vers un objet contenant le sommet et et la taille de la liste des sommets auquels il est lié
        return listeAdjacence.entrySet().stream().map(sommetListEntry -> new SommetDegre(sommetListEntry.getKey(),sommetListEntry.getValue().size())).collect(Collectors.toList());
    }

    /**
     * Calcul le centre d'un graphe selon son degré
     */
    public InfoCentre computeCentreDegres(){
        // On initialise le rayon au maximum
        int rayon = Integer.MAX_VALUE;
        List<Sommet> centres = new ArrayList<>();

        // Tout les sommets et leurs degrés
        List<SommetDegre> sommetDegres = computeDegres();

        for (SommetDegre sommetDegre : sommetDegres){
            // Si le degré est la meme alors on ajoute a la liste des sommets du centre
            if (sommetDegre.getDegre() == rayon){
                centres.add(sommetDegre.getSommet());
            }
            // Si le degré est plus petit alors on met a jout la liste des centres et le nouveau degré minimum
            if (sommetDegre.getDegre() < rayon){
                centres.clear();
                rayon = sommetDegre.getDegre();
                centres.add(sommetDegre.getSommet());
            }
        }

        return new InfoCentre(rayon, centres);
    }



    public static void main(String[] args) throws IOException {
//        ListeAdjacence listeAdjacence = new ListeAdjacence();
//
//        Sommet s1 = new Sommet("1");
//        Sommet s2 = new Sommet("2");
//
//        listeAdjacence.addSommet(s1);
//        listeAdjacence.addSommet(s2);
//
//        listeAdjacence.addArrete(s1.getId(), s2.getId());
//        //listeAdjacence.removeArrete(s1.getId(), s2.getId());
//
//        System.out.println("1 et 2 sont voisins ? "+listeAdjacence.isVoisin(s1.getId(), s2.getId()));
//
//        ListeAdjacence listeAdjacenceFromFile = new ListeAdjacence("graphe.txt");
//        MatriceAdjacence ma = listeAdjacenceFromFile.toMatrice();
//        System.out.println(ma);

//        ListeAdjacence base = new ListeAdjacence("graphe_base.txt");
//        ListeAdjacence clique = new ListeAdjacence("graphe_clique_base.txt");
//        ListeAdjacence stable = new ListeAdjacence("graphe_stable_base.txt");
//        ListeAdjacence partiel = new ListeAdjacence("graphe_partiel_base.txt");
//        ListeAdjacence sous = new ListeAdjacence("graphe_sous_base.txt");
//        ListeAdjacence souspartiel = new ListeAdjacence("graphe_sous_partiel_base.txt");
//        System.out.println("Clique : "+clique.estCliqueDe(base));
//        System.out.println("Stable : "+stable.estStableDe(base));
//        System.out.println("Partiel : "+partiel.estPartielDe(base));
//        System.out.println("Sous graphe : "+sous.estSousGrapheDe(base));
//        System.out.println("Sous graphe partiel : "+souspartiel.estSousGraphePartielDe(base));

        ListeAdjacence base = new ListeAdjacence("graphe_base.txt");
        base.computeMinDistances();
        System.out.println(base.computeDiametre());
        System.out.println(base.computeCentre());
        System.out.println(base.computeCentreDegres());

    }
}
