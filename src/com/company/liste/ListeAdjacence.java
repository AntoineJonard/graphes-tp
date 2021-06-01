package com.company.liste;

import com.company.common.Sommet;
import com.company.common.SommetNamed;
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

    public Sommet getSommetById(Integer id){
        for (Sommet s : listeAdjacence.keySet()){
            if (s.getId() == id){
                return  s;
            }
        }
        return null;
    }

    public boolean isVoisin(Integer s1Id, Integer s2Id){
        Sommet s1 = new Sommet(s1Id);
        if (listeAdjacence.get(s1)==null)
            return false;
        return listeAdjacence.get(s1).contains(s2Id);
    }

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

    public boolean sommetsInclusDans(ListeAdjacence g2, boolean stricte){
        Set<Sommet> mySommets = listeAdjacence.keySet();
        Set<Sommet> otherSommets = g2.listeAdjacence.keySet();
        Set<String> mySommetsNames = mySommets.stream().map(Sommet::getName).collect(Collectors.toSet());
        Set<String> othersSommetsNames = otherSommets.stream().map(Sommet::getName).collect(Collectors.toSet());
        if (stricte)
            return othersSommetsNames.containsAll(mySommetsNames) && !mySommetsNames.containsAll(othersSommetsNames);
        return othersSommetsNames.containsAll(mySommetsNames);
    }

    public boolean arretesInclusesDans(ListeAdjacence g2){
        boolean stillTotallIncluded = true;

        for(Map.Entry<Sommet,List<Integer>> entry1 : listeAdjacence.entrySet()){
            for(Map.Entry<Sommet,List<Integer>> entry2 : g2.listeAdjacence.entrySet()){
                if (entry1.getKey().sameName(entry2.getKey())){
                    Set<String> sommets1 = entry1.getValue().stream().map(sommetId->getSommetById(sommetId).getName()).collect(Collectors.toSet());
                    Set<String> sommets2 = entry2.getValue().stream().map(sommetId->g2.getSommetById(sommetId).getName()).collect(Collectors.toSet());
                    if (!sommets2.containsAll(sommets1)) return false;
                    if (!sommets1.containsAll(sommets2)) stillTotallIncluded = false;
                }
            }
        }
        return !stillTotallIncluded;
    }

    public boolean estPartielDe(ListeAdjacence g2){
        return sommetsInclusDans(g2,false) && arretesInclusesDans(g2) && getNbSommet()==g2.getNbSommet();
    }

    public boolean estSousGrapheDe(ListeAdjacence g2){
        return sommetsInclusDans(g2,true) && arretesInclusesDans(g2);
    }

    public int getNBArretes(){
        int nbArrete = 0;
        for(Map.Entry<Sommet,List<Integer>> entry : listeAdjacence.entrySet()){
            nbArrete += entry.getValue().size();
        }
        return nbArrete / 2;
    }

    public int getNbSommet(){
        return listeAdjacence.size();
    }


    public boolean estSousGraphePartielDe(ListeAdjacence g2){
        return sommetsInclusDans(g2,true) && arretesInclusesDans(g2) && getNBArretes() + 2 <= g2.getNBArretes();
    }

    public boolean estCliqueDe(ListeAdjacence g2){
        if (!sommetsInclusDans(g2,true) )
            return false;

        int nbSommets = listeAdjacence.size();

        return (nbSommets*(nbSommets-1))/2 == getNBArretes();
    }

    public boolean estStableDe(ListeAdjacence g2){
        return sommetsInclusDans(g2,false) && getNBArretes()==0;
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

        ListeAdjacence base = new ListeAdjacence("graphe_base.txt");
        ListeAdjacence clique = new ListeAdjacence("graphe_clique_base.txt");
        ListeAdjacence stable = new ListeAdjacence("graphe_stable_base.txt");
        ListeAdjacence partiel = new ListeAdjacence("graphe_partiel_base.txt");
        ListeAdjacence sous = new ListeAdjacence("graphe_sous_base.txt");
        ListeAdjacence souspartiel = new ListeAdjacence("graphe_sous_partiel_base.txt");
        System.out.println("Clique : "+clique.estCliqueDe(base));
        System.out.println("Stable : "+stable.estStableDe(base));
        System.out.println("Partiel : "+partiel.estPartielDe(base));
        System.out.println("Sous graphe : "+sous.estSousGrapheDe(base));
        System.out.println("Sous graphe partiel : "+souspartiel.estSousGraphePartielDe(base));

    }
}
