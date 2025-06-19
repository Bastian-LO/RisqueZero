package model.data.service;

import model.dao.SecouristeDAO;
import model.data.persistence.Secouriste;

/**
 * Classe de gestion des secouristes.
 * <p>
 * Cette classe sert d'interface métier entre les couches supérieures de l'application
 * et la couche d'accès aux données représentée ici par {@link SecouristeDAO}.
 * Elle permet d'effectuer des opérations courantes sur les objets {@link Secouriste}.
 */
public class SecouristeMngt{

    /**
     * DAO permettant d'accéder aux données des secouristes.
     */
    private final SecouristeDAO secouristeDAO = new SecouristeDAO();

    /**
     * Recherche un secouriste à partir de son identifiant.
     *
     * @param idSecouriste l'identifiant unique du secouriste à rechercher
     * @return le {@link Secouriste} correspondant à l'identifiant, ou {@code null} s'il n'existe pas
     */
    public Secouriste findSecouriste(long idSecouriste) { return secouristeDAO.findById(idSecouriste); }
}