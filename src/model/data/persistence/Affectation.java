package model.data.persistence;

import java.util.HashSet;

public class Affectation {

    private HashSet<Secouriste> idSec;
    private HashSet<DPS> idDps;
    private Competence competence;

    public Affectation(HashSet<Secouriste> idSecouristes, HashSet<DPS> idDpsEntrant, Competence competence) throws IllegalArgumentException{
        if (idSecouristes == null || idSecouristes.isEmpty() || idSecouristes.contains(null)
         || idDpsEntrant == null || idDpsEntrant.isEmpty() || idDpsEntrant.contains(null)
         || competence == null || competence.getIntitule().isEmpty()){
            throw new IllegalArgumentException("Affectation : paramètres invalides.");
        }
        this.idSec =  (HashSet<Secouriste>) idSecouristes.clone();
        this.idDps = (HashSet<DPS>) idDps.clone();
        this.competence = new Competence(competence.getIntitule(), competence.getRequis());
    }
    public HashSet<Secouriste> getIdSec(){
        return (HashSet<Secouriste>) idSec.clone();
    }

    public void setIdSec(HashSet<Secouriste> newIdSec){
        if(newIdSec == null || newIdSec.isEmpty() || newIdSec.contains(null)){
            throw new IllegalArgumentException("setIdSec : paramètre invalide");
        }
        this.idSec = newIdSec;
    }

    public Competence getCompetence(){
        return new Competence(this.competence.getIntitule(), this.competence.getRequis());
    }

    public void setCompetence(Competence newCompetence){
        if(newCompetence == null || newCompetence.getIntitule().trim().equals("")){
            throw new IllegalArgumentException("setCompetence : paramètre invalide");
        }
        this.competence = newCompetence;
    }

    public HashSet<DPS> getIdDps(){
        return (HashSet<DPS>) idDps.clone();
    }

    public void setIdDps(HashSet<DPS> newIdDps){
        if(newIdDps == null || newIdDps.isEmpty() || newIdDps.contains(null)){
            throw new IllegalArgumentException("setIdDps : paramètre invalide");
        }
        this.idDps = newIdDps;
    }
}
