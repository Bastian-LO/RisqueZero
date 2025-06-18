package model.data.persistence;
import java.time.*;
import java.util.*;

public class Secouriste{

    //================================
    //           ATTRIBUTES
    //================================
    
    private long id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String email;
    private String tel;
    private String adresse;
    private HashSet<Dispos> disponibilites;
    private ArrayList<Competence> competences;

    public Secouriste(long id, String nom, String prenom, String dateNaissance, String email, String tel, String adresse, ArrayList<Competence> comp, HashSet<Dispos> disponibilite) throws IllegalArgumentException{
        // Checks if the parameters are valid
        if(id < 1 || nom == null || nom.trim().equals("") || prenom == null || prenom.trim().isEmpty() || dateNaissance == null || !dateNaissance.trim().matches("^\\d{2}/\\d{2}/\\d{4}$") ||
        email == null || !email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") || tel == null || 
        !tel.trim().matches("^0\\d{9}$") || adresse == null || adresse.trim().isEmpty() || comp == null || 
        comp.isEmpty() || comp.contains(null) || disponibilite == null || disponibilite.isEmpty()){
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être null ou vides");
        }

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
        this.disponibilites = disponibilite;
    }
    
    //================================
    //           GETTERS
    //================================
    public long getId(){
        return id;
    }
    
    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public ArrayList<Competence> getCompetences(){
        return new ArrayList<>(this.competences);
    }
    
    public HashSet<Dispos> getDisponibilites(){
        return new HashSet<>(this.disponibilites);
    }

    public ArrayList<String> getCompetencesIntitules(){
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < this.getCompetences().size(); i++){
            Competence compCurr = this.getCompetences().get(i);
            ret.add(compCurr.getIntitule());
        }
        return ret;
    }

    //================================
    //           SETTERS
    //================================
    public void setId(long id){
        this.id = id;
    }

    public void setNom(String nom) throws IllegalArgumentException{
        if(nom == null || nom.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        if(prenom == null || prenom.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.prenom = prenom;
    }

    public void setDateNaissance(String dateNaissance) {
        if(dateNaissance == null || !dateNaissance.trim().matches("^\\d{2}/\\d{2}/\\d{4}$")){
            throw new IllegalArgumentException();
        }
        this.dateNaissance = dateNaissance;
    }

    public void setEmail(String email) {
        if(email == null || !email.trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")){
            throw new IllegalArgumentException();
        }
        this.email = email;
    }

    public void setTel(String tel) {
        if(tel == null || !tel.trim().matches("^0\\d{9}$")){
            throw new IllegalArgumentException();
        }
        this.tel = tel;
    }

    public void setAdresse(String adresse) {
        if(adresse == null || adresse.trim().equals("")){
            throw new IllegalArgumentException();
        }
        this.adresse = adresse;
    }

    public void addDispos(Dispos newDispo){
        // pré-condition
        if (newDispo == null || !newDispo.getSecouriste().equals(this)){
            throw new IllegalArgumentException("addDispos : paramètre invalide");
        }

        HashSet<Dispos> currDispos = this.getDisponibilites();  // Copie des dispos

        boolean existeDeja = false;
        boolean debutInclus = false;
        boolean finInclus = false;

        LocalTime debutNewDispo = newDispo.toLocalTime(newDispo.getHeureDebut());
        LocalTime finNewDispo = newDispo.toLocalTime(newDispo.getHeureFin());

        ArrayList<Dispos> disposDebutInclus = new ArrayList<>();
        ArrayList<Dispos> disposFinInclus = new ArrayList<>();

        for(Dispos dispo : currDispos){
            if (!existeDeja){
                LocalTime debutDispo = dispo.toLocalTime(dispo.getHeureDebut());
                LocalTime finDispo = dispo.toLocalTime(dispo.getHeureFin());

                debutInclus = debutNewDispo.isAfter(debutDispo) && debutNewDispo.isBefore(finDispo);
                finInclus = finNewDispo.isAfter(debutDispo) && finNewDispo.isBefore(finDispo);
                boolean memeJour = dispo.getDate().equals(newDispo.getDate());

                if(debutInclus && finInclus && memeJour){
                    existeDeja = true;
                } else if (debutInclus && !finInclus && memeJour){
                    disposDebutInclus.add(dispo);
                } else if (!debutInclus && finInclus && memeJour){
                    disposFinInclus.add(dispo);
                }
            }
        }

        if(!existeDeja && disposDebutInclus.isEmpty() && disposFinInclus.isEmpty()){
            this.getDisponibilites().add(newDispo);
        } else if (!existeDeja){
            Duration diffHDebutMax = Duration.ofSeconds(0L);
            Duration diffHFinMax = Duration.ofSeconds(0L);


            // Récupération de la disponibilité la plus étandue
            Dispos dispoDebutInclus = null;
            for(int i = 0; i < disposDebutInclus.size(); i++){
                Dispos dispoCurr = disposDebutInclus.get(i);
                LocalTime finDispo = dispoCurr.toLocalTime(dispoCurr.getHeureFin());

                Duration diffHoraireFin = Duration.between(finNewDispo, finDispo);
                if(diffHoraireFin.getSeconds() > diffHFinMax.getSeconds()){
                    diffHFinMax = diffHoraireFin;
                    dispoDebutInclus = new Dispos(dispoCurr.getSecouriste(), dispoCurr.getDate(), dispoCurr.getHeureDebut(), dispoCurr.getHeureFin());
                }
            }

            Dispos dispoFinInclus = null;
            for(int i = 0; i < disposFinInclus.size(); i++){
                Dispos dispoCurr = disposFinInclus.get(i);
                LocalTime debutDispo = dispoCurr.toLocalTime(dispoCurr.getHeureDebut());

                Duration diffHoraireDebut = Duration.between(debutDispo, debutNewDispo);
                if(diffHoraireDebut.getSeconds() > diffHDebutMax.getSeconds()){
                    diffHDebutMax = diffHoraireDebut;
                    dispoFinInclus = new Dispos(dispoCurr.getSecouriste(), dispoCurr.getDate(), dispoCurr.getHeureDebut(), dispoCurr.getHeureFin());
                }
            }

            if(dispoFinInclus != null && dispoDebutInclus != null){
                LocalTime debutDispo = dispoFinInclus.toLocalTime(dispoFinInclus.getHeureDebut());
                LocalTime finDispo = dispoDebutInclus.toLocalTime(dispoDebutInclus.getHeureFin());

                debutInclus = debutNewDispo.isAfter(debutDispo) && debutNewDispo.isBefore(finDispo);
                finInclus = finNewDispo.isAfter(debutDispo) && finNewDispo.isBefore(finDispo);
            } else {
                debutInclus = false;
                finInclus = false;
            }

            if(debutInclus && !finInclus && dispoDebutInclus != null){
                Dispos dispoFinale = new Dispos(this, newDispo.getDate().toLocalDate(), dispoDebutInclus.toLocalTime(dispoDebutInclus.getHeureDebut()), finNewDispo);
                this.getDisponibilites().remove(dispoDebutInclus);
                this.getDisponibilites().add(dispoFinale);
            } else if (!debutInclus && finInclus && dispoFinInclus != null){
                Dispos dispoFinale = new Dispos(this, newDispo.getDate().toLocalDate(), debutNewDispo, dispoFinInclus.toLocalTime(dispoFinInclus.getHeureFin()));
                this.getDisponibilites().remove(dispoFinInclus);
                this.getDisponibilites().add(dispoFinale);
            }
        }
    }

    @Override
    public boolean equals(Object obj){
        boolean ret = false;

        if (this == obj){
            ret = true;
        } else if (obj instanceof Secouriste){
            Secouriste autre = (Secouriste) obj;
            if(this.getId() == autre.getId() && this.getPrenom().equals(autre.getPrenom()) && this.getNom().equals(autre.getNom()) &&
            this.getDateNaissance().equals(autre.getDateNaissance()) && this.getAdresse().equals(autre.getAdresse()) && 
            this.getEmail().equals(autre.getEmail()) && this.getTel().equals(autre.getTel()) && 
            this.getDisponibilites().equals(autre.getDisponibilites()) && this.getCompetences().equals(autre.getCompetences())){
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public int hashCode(){
        int ret;
        ret = Objects.hash(getId(), getNom(), getPrenom(), getDateNaissance(), getAdresse(), getEmail(), getTel(), getDisponibilites(), getCompetences());
        return ret;
    }
}