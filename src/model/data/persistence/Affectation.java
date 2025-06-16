package model.data.persistence;

import java.util.ArrayList;
import javafx.util.Pair;

public class Affectation {

    private ArrayList<Pair<Secouriste, Competence>> listSecComp;
    private DPS idDps;

    public Affectation(ArrayList<Pair<Secouriste, Competence>> list, DPS idDpsEntrant) throws IllegalArgumentException{
        if (list == null || list.isEmpty() || list.contains(null) || idDpsEntrant == null){
            throw new IllegalArgumentException("Affectation : paramètres invalides.");
        }

        for (Pair<Secouriste, Competence> pair : list){
            if(pair == null || pair.getKey() == null || pair.getValue() == null || pair.getValue().getIntitule().isEmpty()){
                throw new IllegalArgumentException("Affectation : paramètres invalides.");
            }
        }

        this.listSecComp = cloneList(list); // Appel d'une méthode vérifiant le paramètre et renvoyant une nouvelle ArrayList

        this.idDps = new DPS(idDpsEntrant.getId(), idDpsEntrant.getHoraireDepart(), idDpsEntrant.getHoraireFin(), 
                            idDpsEntrant.getDateEvt(), idDpsEntrant.getCompetences(), idDpsEntrant.getLieu(), 
                            idDpsEntrant.getSport());
    }


    public ArrayList<Pair<Secouriste, Competence>> getList(){
        ArrayList<Pair<Secouriste, Competence>> newList = cloneList(this.listSecComp);

        return newList;
    }

    public void setList(ArrayList<Pair<Secouriste, Competence>> newList){
        this.listSecComp = cloneList(newList);
    }

    public DPS getIdDps(){
        return new DPS(this.idDps.getId(), this.idDps.getHoraireDepart(), this.idDps.getHoraireFin(), 
                            this.idDps.getDateEvt(), this.idDps.getCompetences(), this.idDps.getLieu(), 
                            this.idDps.getSport());
    }

    public void setIdDps(DPS newIdDps){
        if(newIdDps == null){
            throw new IllegalArgumentException("setIdDps : paramètre invalide");
        }
        this.idDps = new DPS(newIdDps.getId(), newIdDps.getHoraireDepart(), newIdDps.getHoraireFin(), 
                            newIdDps.getDateEvt(), newIdDps.getCompetences(), newIdDps.getLieu(), 
                            newIdDps.getSport());
    }

    private ArrayList<Pair<Secouriste, Competence>> cloneList(ArrayList<Pair<Secouriste, Competence>> listOrig) throws IllegalArgumentException{
        if (listOrig == null || listOrig.isEmpty() || listOrig.contains(null)){
            throw new IllegalArgumentException("Affectation : paramètres invalides.");
        }

        ArrayList<Pair<Secouriste, Competence>> newList = new ArrayList<>();
        for (Pair<Secouriste, Competence> pair : listOrig) {
            // Copie défensive du secouriste de la pair
            Secouriste secOrig = pair.getKey();
            Secouriste cloneSecouriste = new Secouriste(secOrig.getId(), secOrig.getNom(), secOrig.getPrenom(), 
                                                        secOrig.getDateNaissance(), secOrig.getEmail(), secOrig.getTel(), 
                                                        secOrig.getAdresse(), secOrig.getCompetences(), secOrig.getDisponibilites());
            // Copie défensive de la compétence de la pair
            Competence compOrig = pair.getValue();
            Competence cloneCompetence = new Competence(compOrig.getIntitule(), compOrig.getRequis());
            // Ajout de la pair à la liste 
            this.listSecComp.add(new Pair<>(cloneSecouriste, cloneCompetence));
        }

        return newList;
    }
}
