package com.company.common;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

/**
 * Objet répertoriant les distances minimum de chaque sommet à tous les sommets
 * */
public class MinDistance {

    private Sommet from;
    private HashMap<Sommet, Integer> distances;

    public MinDistance(Sommet from) {
        this.from = from;
        this.distances = new HashMap<>();
    }

    /**
     * Met a jour la distance minimal si elle est plus petite que l'actuel
     * @param to Le sommet vers lequel on regard la min distance
     * @param distance la nouvelle distance
     */
    public void updateMinDistance(Sommet to, int distance){
        if (distances.get(to) == null || (distances.get(to) != null && distances.get(to) > distance)){
            distances.put(to, distance);
        }
    }

    /**
     * La distance en paramétres est elle mieux que la courante
     */
    public boolean better(Sommet to, int distance){
        return distances.get(to) == null || (distances.get(to) != null && distances.get(to) > distance);
    }

    public boolean isInit(Sommet to){
        return distances.get(to) != null ;
    }

    public int getMinDistance(Sommet to){
        return distances.get(to);
    }

    /**
     * Retourne le maximum des distances minimums d'un sommet vers les autres
     * @return la distance maximum
     */
    public  Integer getMaxMinDistance(){
        Optional<Integer> max = distances.values().stream().max(Comparator.comparingInt(i -> i));
        return max.orElse(null);
    }

    public Sommet getFrom() {
        return from;
    }
}
