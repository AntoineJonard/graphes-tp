package com.company.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/** Génère un graphe aleatoireemnt avec une densité proba et un nombre de Sommets nombreSommets*/
public class RandomGraphe {
    private final double proba;
    private final long nombreSommets;

    public RandomGraphe(double proba, long n, String fileName) throws IOException {
        this.nombreSommets = n;
        this.proba = proba;
        this.generate(fileName);
    }

    public void generate(String fileName) throws IOException {
        //On creer le fichier
        File file = new File("src/data/"+fileName);
        if (file.createNewFile()) System.out.println("Ficher créer");
        else System.out.println("Fichier deja existant, écrasement...");

        //On ecrit le nombre de sommets
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(this.nombreSommets+"\n");

        //On ecrit les noms des sommets associes a leur valeur
        for (int i = 1; i <= nombreSommets; i++){
            bufferedWriter.write(i+" sommet"+i+"\n");
        }

        for (int i = 1 ; i <= nombreSommets; i++){
            for (int j = i; j < nombreSommets; j++){
                if(j != i){
                    if(proba >= Math.random()) bufferedWriter.write(i + " " + j +"\n");
                }
            }
        }

        bufferedWriter.close();

        System.out.println("Fichier correctement sauvegardé");
    }
}
