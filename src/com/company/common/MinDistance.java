package com.company.common;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;

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

    public void updateMinDistance(Sommet to, int distance){
        if (distances.get(to) == null || (distances.get(to) != null && distances.get(to) > distance)){
            distances.put(to, distance);
        }
    }

    public boolean better(Sommet to, int distance){
        return distances.get(to) == null || (distances.get(to) != null && distances.get(to) > distance);
    }

    public boolean isInit(Sommet to){
        return distances.get(to) != null ;
    }

    public int getMinDistance(Sommet to){
        return distances.get(to);
    }

    public  Integer getMaxMinDistance(){
        Optional<Integer> max = distances.values().stream().max(Comparator.comparingInt(i -> i));
        return max.orElse(null);
    }

    public Sommet getFrom() {
        return from;
    }
}
