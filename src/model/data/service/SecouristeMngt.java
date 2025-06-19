package model.data.service;

import model.dao.SecouristeDAO;
import model.data.persistence.Secouriste;

public class SecouristeMngt{
    private final SecouristeDAO secouristeDAO = new SecouristeDAO();

    public Secouriste findSecouriste(long idSecouriste) { return secouristeDAO.findById(idSecouriste); }
}