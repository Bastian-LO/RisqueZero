package model.data.graphe;

import model.data.persistence.Secouriste;
import model.data.persistence.Competence;
import model.data.persistence.DPS;

import java.util.*;

import javafx.util.Pair;

public class Graphe {

    private ArrayList<Pair<DPS, Competence>> DPSCompet = new ArrayList<>();

    private ArrayList<Secouriste> secouristes = new ArrayList<Secouriste>();

    public int nbSommets;

    int[][] matriceAdj;

    public Graphe(ArrayList<Secouriste> secouristes, ArrayList<DPS> dispositifs) throws IllegalArgumentException {
        if (secouristes == null || dispositifs == null) {
            throw new IllegalArgumentException("Secouristes ou DPS null");
        }


        this.secouristes = secouristes;
        for (DPS dispositif : dispositifs) {
            for (Competence c : dispositif.getCompetences()) {
                DPSCompet.add(new Pair<>(dispositif, c));
            }
        }
        this.initMatriceAdjacence();
    }

    public void addSecouriste(Secouriste secouriste) throws IllegalArgumentException {
        if (secouriste == null) {
            throw new IllegalArgumentException("Secouriste null");
        }
        this.secouristes.add(secouriste);
    }

    public void addSecouristes(ArrayList<Secouriste> secouristes) throws IllegalArgumentException {
        if (secouristes == null) {
            throw new IllegalArgumentException("Secouristes null");
        }
        this.secouristes.addAll(secouristes);
    }

    public void addDPS(DPS dps) throws IllegalArgumentException {
        if (dps == null) {
            throw new IllegalArgumentException("DPS null");
        }
        for (Competence c : dps.getCompetences()) {
            this.DPSCompet.add(new Pair<>(dps, c));
        }
    }

    public void addSeveralDPS(ArrayList<DPS> dpss) throws IllegalArgumentException {
        if (dpss == null) {
            throw new IllegalArgumentException("DPS null");
        }
        for (DPS dps : dpss) {
            for (Competence c : dps.getCompetences()) {
                this.DPSCompet.add(new Pair<>(dps, c));
            }
        }
    }

    public void initMatriceAdjacence() {
        int n = this.secouristes.size();
        int m = this.DPSCompet.size();
        this.nbSommets = n + m;  // Taille réelle du graphe

        // Initialiser la matrice avec des 0
        this.matriceAdj = new int[nbSommets][nbSommets];

        // Parcourir tous les secouristes (indices 0 à n-1)
        for (int i = 0; i < n; i++) {
            Secouriste s = this.secouristes.get(i);
            // Parcourir tous les dispositifs (indices n à n+m-1 dans la matrice)
            for (int j = 0; j < m; j++) {
                Pair<DPS, Competence> p = this.DPSCompet.get(j);
                if (s.getCompetences().contains(p.getValue())) {
                    // Lier le secouriste i au dispositif j (placé à l'indice n+j)
                    matriceAdj[i][n + j] = 1;
                }
            }
        }
    }


}
