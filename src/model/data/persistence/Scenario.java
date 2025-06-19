package model.data.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import javafx.util.Pair;

public class Scenario {
    public static void main(String[] args){
        test();
    }

    public static void test(){

        System.out.println("\nJ'inscris un secouriste débutant ne disposant que d'un PSE1.");

        // Création de sa liste de compétence
        ArrayList<Competence> compList1 = new ArrayList<>();

        // Création de son ensemble de disponibilités (ici vide)
        HashSet<Dispos> dispoSet1 = new HashSet<>();

        Secouriste sec1 = new Secouriste(1L, "Dupont", "Martin", "01/05/1984", 
                    "martin.dupont@gmail.com", "0708091011", "4 rue des potiers", compList1, dispoSet1);

        // Ajout de compétences
        Competence comp1 = new Competence("PSE1");
        compList1.add(comp1);

        // Ajout de disponibilités (9h - 17h | 1 janvier 2030)
        Journee jour1 = new Journee(1, 1, 2030);
        int[] hDebutSec1 = new int[2];
        hDebutSec1[0] = 9;
        hDebutSec1[1] = 0;
        int[] hFinSec1 = new int[2];
        hFinSec1[0] = 17;
        hFinSec1[1] = 0;
        Dispos dispo1 = new Dispos(sec1, jour1, hDebutSec1, hFinSec1);
        sec1.addDispos(dispo1);

        String prenomNom1 =  sec1.getPrenom() + " " + sec1.getNom();
        System.out.println("Secouriste " + prenomNom1 + " créé");
        System.out.println("Disponibilité de " + prenomNom1 + " : " + sec1.getDisponibilites());



        System.out.println("\nJe crée un dispositif ne nécessitant qu'un secouriste avec PSE1");

        // Création des horaires
        int[] hDebutDps1 = new int[2];
        hDebutDps1[0] = 10;
        hDebutDps1[1] = 0;
        int[] hFinDps1 = new int[2];
        hFinDps1[0] = 12;
        hFinDps1[1] = 0;

        // Création de la liste de compétences
        ArrayList<Competence> listCompDps1 = new ArrayList<>();
        listCompDps1.add(comp1);

        // Création du site et du sport
        Site site1 = new Site("VLD", "Vélodrome", 49F, 59F);
        Sport sport1 = new Sport("FTB", "Football");

        // Création du DPS
        DPS dps1 = new DPS(1L, hDebutDps1, hFinDps1, jour1, listCompDps1, site1, sport1);

        System.out.println("DPS " + dps1.getId() + " : " + dps1.toString());
        System.out.println("Compétence requise : " + dps1.getCompetences());



        System.out.println("\nJ'affecte le secouriste au DPS manuellement");

        // Création de la liste des pairs Secouriste / Compétence
        ArrayList<Pair<Secouriste, Competence>> listAff = new ArrayList<>();

        // Ajout de la pair avec Martin et PSE1
        Pair<Secouriste, Competence> pair1 = new Pair<Secouriste,Competence>(sec1, comp1);
        listAff.add(pair1);

        // Création de l'objet Affectation
        Affectation affectations = new Affectation(listAff, dps1);

        for(Pair<Secouriste, Competence> pair : affectations.getList()){
            System.out.println(pair.getKey().getPrenom() + " " + pair.getKey().getNom() + " associé à " + pair.getValue());
        }
    }
}
