package model.data.graphe;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.util.Pair;
import model.data.persistence.*;

public class TestGraphe {

    public static void main(String[] args){
        test();
    }
    public static void test(){
        ArrayList<Pair<DPS, Competence>> DPSCompet = new ArrayList<>();
        ArrayList<DPS> dpss = new ArrayList<>();

        int[] horaire_depart1 = new int[2];
        horaire_depart1[0] = 10;
        horaire_depart1[1] = 0;
        int[] horaire_fin1 = new int[2];
        horaire_fin1[0] = 16;
        horaire_fin1[1] = 30;

        Journee jour = new Journee(1,1,2030);

        Competence comp1 = new Competence("PSC1");
        Competence comp2 = new Competence("PCS2");

        ArrayList<Competence> compList = new ArrayList<>();
        compList.add(comp1);
        compList.add(comp2);

        Site lieu = new Site("STD", "Stade", 49F, 59F);

        Sport sport = new Sport("TNS", "Tennis");

        DPS dps = new DPS(1L, horaire_depart1, horaire_fin1, jour, compList, lieu, sport);
        dpss.add(dps);

        for (int i = 0; i < compList.size(); i++){
            Pair<DPS, Competence> DpsCompetTest = new Pair<DPS,Competence>(dps, compList.get(i));
            DPSCompet.add(DpsCompetTest);
        }

        HashSet<Dispos> secDispos1 = new HashSet<>();

        ArrayList<Competence> secComp1 = new ArrayList<>();
        secComp1.add(comp1);

        Secouriste sec1 = new Secouriste(1L, "Lucien", "Martin", "07/08/2009", "martin.lucien@gmail.com", "0708091112", "8 rue des potiers", secComp1, secDispos1);
        Dispos dispo1 = new Dispos(sec1, jour, horaire_depart1, horaire_fin1);
        sec1.addDispos(dispo1);

        HashSet<Dispos> secDispos2 = new HashSet<>();

        ArrayList<Competence> secComp2 = new ArrayList<>();
        secComp2.add(comp2);

        Secouriste sec2 = new Secouriste(2L, "Jean", "Jacques", "02/02/2002", "jean.jacques@hotmail.com", "0607080910", "7 rue de la chance", secComp2, secDispos2);
        Dispos dispo2 = new Dispos(sec2, jour, horaire_depart1, horaire_fin1);
        sec2.addDispos(dispo2);

        ArrayList<Secouriste> listSec = new ArrayList<>();
        listSec.add(sec1);
        listSec.add(sec2);

        Graphe graph = new Graphe(listSec, dpss);
        ArrayList<Affectation> listAff = graph.startExhaustif();

        for(int i = 0; i < listAff.size(); i++){
            Affectation aff = listAff.get(i);
            ArrayList<Pair<Secouriste, Competence>> listSecComp = aff.getList();
            DPS idDps = aff.getIdDps();
            
            System.out.println("DPS : " + idDps.getId());
            for (int j = 0; j < listSecComp.size(); j++){
                Pair<Secouriste, Competence> pairSecComp = listSecComp.get(j);
                System.out.println(pairSecComp.getKey().toString() + " : " + pairSecComp.getValue().toString());
            }
        }
    }
}
