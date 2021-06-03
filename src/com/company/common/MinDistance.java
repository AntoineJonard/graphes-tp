package com.company.common;

import java.util.HashMap;
/**
 * Base de données répetoriant les distances minimum de chaque sommet,
 * */
public class MinDistance {

    private Sommet from;
    private HashMap<Sommet, Integer> distances;

    public MinDistance(Sommet from) {
        this.from = from;
        this.distances = new HashMap<>();
    }

    public boolean updateMinDistance(Sommet to, int distance){
        if (distances.get(to) > distance){
            distances.put(to, distance);
            return true;
        }
        return false;
    }

    public int getMinDistance(Sommet to){
        return distances.get(to);
    }
}
