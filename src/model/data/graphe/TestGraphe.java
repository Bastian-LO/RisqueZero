package model.data.graphe;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

import javafx.util.Pair;
import model.data.persistence.*;

public class TestGraphe {

    public static void main(String[] args){
        test();
    }
    public static void test(){

        // Création du DPS 
        
        ArrayList<Pair<DPS, Competence>> DPSCompet = new ArrayList<>();

        /**Un évenement un certain jour */
        int[] horaire_departDPS1 = new int[2];
        horaire_departDPS1[0] = 10;
        horaire_departDPS1[1] = 0;
        int[] horaire_finDPS1 = new int[2];
        horaire_finDPS1[0] = 16;
        horaire_finDPS1[1] = 30;

        Journee jour1 = new Journee(1,1,2030);


        /**Un évenement sur un autre jour et autre heure */
        int[] horaire_departDPS2 = new int[2];
        horaire_departDPS2[0] = 11;
        horaire_departDPS2[1] = 0;
        int[] horaire_finDPS2 = new int[2];
        horaire_finDPS2[0] = 17;
        horaire_finDPS2[1] = 0;

        Journee jour2 = new Journee(2,2,2030);


        /**Un évenement ou c'est les mêmes horaires que le jour1 mais pas le même jour */
        int[] horaire_departDPS3 = new int[2];
        horaire_departDPS3[0] = 10;
        horaire_departDPS3[1] = 0;
        int[] horaire_finDPS3 = new int[2];
        horaire_finDPS3[0] = 16;
        horaire_finDPS3[1] = 30;

        Journee jour3 = new Journee(3,3,2030);


        /**Un évenement ou c'est le tout pareil que le jour1 (Horaire,compétences,jour)*/
        int[] horaire_departDPS4 = new int[2];
        horaire_departDPS4[0] = 10;
        horaire_departDPS4[1] = 0;
        int[] horaire_finDPS4 = new int[2];
        horaire_finDPS4[0] = 16;
        horaire_finDPS4[1] = 30;

        Journee jour4 = new Journee(1,1,2030);


        /**Un évenement ou c'est le tout pareil que le jour1 mais l'horaire change (compétences,jour) */
        int[] horaire_departDPS5 = new int[2];
        horaire_departDPS5[0] = 6;
        horaire_departDPS5[1] = 0;
        int[] horaire_finDPS5 = new int[2];
        horaire_finDPS5[0] = 9;
        horaire_finDPS5[1] = 0;

        Journee jour5 = new Journee(1,1,2030);


        /**Un évenement ou c'est le tout pareil que le jour1 mais avec un lieu différent (Horaire, compétences, jour) */
        int[] horaire_departDPS6 = new int[2];
        horaire_departDPS6[0] = 10;
        horaire_departDPS6[1] = 0;
        int[] horaire_finDPS6 = new int[2];
        horaire_finDPS6[0] = 16;
        horaire_finDPS6[1] = 30;

        Journee jour6 = new Journee(1,1,2030);


        /**Un évenement ou c'est juste les compétences qui sont différentes */
        int[] horaire_departDPS7 = new int[2];
        horaire_departDPS7[0] = 10;
        horaire_departDPS7[1] = 0;
        int[] horaire_finDPS7 = new int[2];
        horaire_finDPS7[0] = 16;
        horaire_finDPS7[1] = 30;

        Journee jour7 = new Journee(1,1,2030);


        Competence comp1 = new Competence("PSC1");
        HashSet<Competence> requis1 = new HashSet<>();
        requis1.add(comp1);
        
        Competence comp2 = new Competence("PSC2", requis1);

        Competence comp3 = new Competence("VAO1");
        HashSet<Competence> requis2 = new HashSet<>();
        requis2.add(comp3);

        Competence comp4 = new Competence("VAO2", requis2);

        Competence comp5 = new Competence("NVE");

        ArrayList<Competence> compList = new ArrayList<>();
        compList.add(comp1);
        compList.add(comp2);
        compList.add(comp3);
        compList.add(comp4);
        compList.add(comp5);

        Site lieu1 = new Site("STD", "Stade", 49F, 59F);
        Site lieu2 = new Site("VLD", "Velodrome",60F, 30F);

        Sport sport1 = new Sport("TNS", "Tennis");
        Sport sport2 = new Sport("VL", "Velo");

        ArrayList<DPS> dpss = new ArrayList<>();

        DPS dps1 = new DPS(1L, horaire_departDPS1, horaire_finDPS1, jour1, compList, lieu1, sport1);
        dpss.add(dps1);

        DPS dps2 = new DPS(2L, horaire_departDPS2, horaire_finDPS2, jour2, compList, lieu1, sport1);
        dpss.add(dps2);

        DPS dps3 = new DPS(3L, horaire_departDPS3, horaire_finDPS3, jour3, compList, lieu1, sport1);
        dpss.add(dps3);

        DPS dps4 = new DPS(4L, horaire_departDPS4, horaire_finDPS4, jour4, compList, lieu1, sport1);
        dpss.add(dps4);

        DPS dps5 = new DPS(5L, horaire_departDPS5, horaire_finDPS5, jour5, compList, lieu1, sport1);
        dpss.add(dps5);

        DPS dps6 = new DPS(6L, horaire_departDPS6, horaire_finDPS6, jour6, compList, lieu2, sport1);
        dpss.add(dps6);

        DPS dps7 = new DPS(7L, horaire_departDPS7, horaire_finDPS7, jour7, compList, lieu1, sport2);
        dpss.add(dps7);


        for (DPS dps : dpss) {
            for (Competence comp : dps.getCompetences()) {
                DPSCompet.add(new Pair<>(dps, comp));
            }
        }


        // Création des secouristes

        HashSet<Dispos> secBastianDispos = new HashSet<>();

        ArrayList<Competence> secBastianComp = new ArrayList<>();
        secBastianComp.add(comp2);
        secBastianComp.add(comp4);
        secBastianComp.add(comp5);

        int[] horaire_departSecouristeBastian = new int[2];
        horaire_departSecouristeBastian[0] = 9;
        horaire_departSecouristeBastian[1] = 30;
        int[] horaire_finSecouristeBastian = new int[2];
        horaire_finSecouristeBastian[0] = 18;
        horaire_finSecouristeBastian[1] = 30;

        Secouriste secBastian = new Secouriste(1L, "Le Ouedec", "Bastian", "03/11/2003", "LeOuedec.Bastian@gmail.com", "0708091112", "8 rue des potiers", secBastianComp, secBastianDispos);
        Dispos dispo1 = new Dispos(secBastian, jour1, horaire_departSecouristeBastian, horaire_finSecouristeBastian);
        secBastian.addDispos(dispo1);

        Dispos dispo2 = new Dispos(secBastian, jour2, horaire_departSecouristeBastian, horaire_finSecouristeBastian);
        secBastian.addDispos(dispo2);



        HashSet<Dispos> secEnrickDispos = new HashSet<>();

        ArrayList<Competence> secEnrickComp = new ArrayList<>();
        secEnrickComp.add(comp1);
        secEnrickComp.add(comp4);
        secEnrickComp.add(comp5);

        int[] horaire_departSecouristeEnrick = new int[2];
        horaire_departSecouristeEnrick[0] = 5;
        horaire_departSecouristeEnrick[1] = 30;
        int[] horaire_finSecouristeEnrick = new int[2];
        horaire_finSecouristeEnrick[0] = 12;
        horaire_finSecouristeEnrick[1] = 30;

        Secouriste secEnrick = new Secouriste(2L, "Mananjean", "Enrick", "07/09/2006", "Mananjean.Enrick@gmail.com", "0708091124", "9 rue des potiers", secEnrickComp, secEnrickDispos);
        Dispos dispo3 = new Dispos(secEnrick, jour1, horaire_departSecouristeEnrick, horaire_finSecouristeEnrick);
        secEnrick.addDispos(dispo3);

        Dispos dispo4 = new Dispos(secEnrick, jour2, horaire_departSecouristeEnrick, horaire_finSecouristeEnrick);
        secEnrick.addDispos(dispo4);


        
        HashSet<Dispos> secElwanDispos = new HashSet<>();

        ArrayList<Competence> secElwanComp = new ArrayList<>();
        secElwanComp.add(comp2);
        secElwanComp.add(comp3);
        secElwanComp.add(comp5);

        int[] horaire_departSecouristeElwan = new int[2];
        horaire_departSecouristeElwan[0] = 17;
        horaire_departSecouristeElwan[1] = 30;
        int[] horaire_finSecouristeElwan = new int[2];
        horaire_finSecouristeElwan[0] = 23;
        horaire_finSecouristeElwan[1] = 30;

        Secouriste secElwan = new Secouriste(3L, "Yvin", "Elwan", "25/12/2006", "Yvin.Elwan@gmail.com", "0708091196", "10 rue des potiers", secElwanComp, secElwanDispos);
        Dispos dispo5 = new Dispos(secElwan, jour1, horaire_departSecouristeElwan, horaire_finSecouristeElwan);
        secElwan.addDispos(dispo5);

        Dispos dispo6 = new Dispos(secElwan, jour2, horaire_departSecouristeElwan, horaire_finSecouristeElwan);
        secElwan.addDispos(dispo6);



        HashSet<Dispos> secEmileDispos = new HashSet<>();

        ArrayList<Competence> secEmileComp = new ArrayList<>();
        secEmileComp.add(comp5);

        int[] horaire_departSecouristeEmile = new int[2];
        horaire_departSecouristeEmile[0] = 8;
        horaire_departSecouristeEmile[1] = 0;
        int[] horaire_finSecouristeEmile = new int[2];
        horaire_finSecouristeEmile[0] = 19;
        horaire_finSecouristeEmile[1] = 0;

        Secouriste secEmile = new Secouriste(4L, "Thevin", "Emile", "14/07/2004", "Thevin.Emile@gmail.com", "0708091141", "11 rue des potiers", secEmileComp, secEmileDispos);
        Dispos dispo7 = new Dispos(secEmile, jour1, horaire_departSecouristeEmile, horaire_finSecouristeEmile);
        secEmile.addDispos(dispo7);

        Dispos dispo8 = new Dispos(secEmile, jour2, horaire_departSecouristeEmile, horaire_finSecouristeEmile);
        secEmile.addDispos(dispo8);



        HashSet<Dispos> secKillianDispos = new HashSet<>();

        ArrayList<Competence> secKillianComp = new ArrayList<>();
        secKillianComp.add(comp1);

        int[] horaire_departSecouristeKillian = new int[2];
        horaire_departSecouristeKillian[0] = 9;
        horaire_departSecouristeKillian[1] = 30;
        int[] horaire_finSecouristeKillian = new int[2];
        horaire_finSecouristeKillian[0] = 18;
        horaire_finSecouristeKillian[1] = 30;

        Secouriste secKillian = new Secouriste(5L, "Avril", "Killian", "07/04/2006", "Avril.Killian@gmail.com", "0708091156", "12 rue des potiers", secKillianComp, secKillianDispos);
        Dispos dispo9 = new Dispos(secKillian, jour1, horaire_departSecouristeKillian, horaire_finSecouristeKillian);
        secKillian.addDispos(dispo9);

        Dispos dispo10 = new Dispos(secKillian, jour2, horaire_departSecouristeKillian, horaire_finSecouristeKillian);
        secKillian.addDispos(dispo10);



        HashSet<Dispos> secTestDispos = new HashSet<>();

        ArrayList<Competence> secTestComp = new ArrayList<>();
        secTestComp.add(comp2);
        secTestComp.add(comp4);
        secTestComp.add(comp5);

        int[] horaire_departSecouristeTest = new int[2];
        horaire_departSecouristeTest[0] = 9;
        horaire_departSecouristeTest[1] = 30;
        int[] horaire_finSecouristeTest = new int[2];
        horaire_finSecouristeTest[0] = 18;
        horaire_finSecouristeTest[1] = 30;

        Secouriste secTest = new Secouriste(6L, "Test", "Testtt", "01/04/2000", "Test.Testtt@gmail.com", "0708091156", "13 rue des potiers", secTestComp, secTestDispos);
        Dispos dispo11 = new Dispos(secTest, jour3, horaire_departSecouristeTest, horaire_finSecouristeTest);
        secTest.addDispos(dispo11);


        ArrayList<Secouriste> listSec = new ArrayList<>();
        listSec.add(secBastian);
        listSec.add(secEnrick);
        listSec.add(secElwan);
        listSec.add(secEmile);
        listSec.add(secKillian);
        listSec.add(secTest);


        // Création du graphe

        Graphe graph = new Graphe(listSec, dpss);
        
        /**
        System.out.println("\nAlgorithme exhaustif : ");
        ArrayList<Affectation> listAff = graph.affectationExhaustive();

        for(int i = 0; i < listAff.size(); i++){
            Affectation aff = listAff.get(i);
            ArrayList<Pair<Secouriste, Competence>> listSecComp = aff.getList();
            DPS idDps = aff.getDps();
            
            System.out.println("DPS ID: " + idDps.getId());
            for (int j = 0; j < listSecComp.size(); j++){
                Pair<Secouriste, Competence> pairSecComp = listSecComp.get(j);
                System.out.println(pairSecComp.getKey().getNom() + " : " + pairSecComp.getValue());
            }
        }
    /**
        System.out.println("\nAlgorithme glouton : ");
        ArrayList<Affectation> listAffGlouton = graph.affectationGlouton();
        for(int i = 0; i < listAffGlouton.size(); i++){
            ArrayList<Pair<Secouriste, Competence>> listSecComp = aff.getList();
            DPS idDps = aff.getDps();
            
            System.out.println("DPS ID: " + idDps.getId());
            for (int j = 0; j < listSecComp.size(); j++){
                Pair<Secouriste, Competence> pairSecComp = listSecComp.get(j);
                System.out.println(pairSecComp.getKey().getNom() + " : " + pairSecComp.getValue());
            }
        }
    }

    void testExhaustif(){
        System.out.println("\nAlgorithme exhaustif, tests : Cas normal ");
    */
    }
}
