package model.data.service;
import model.dao.*;
public class DAOMngt {
    private static final AffectationDAO affectationDAO = new AffectationDAO();
    private static final CompetenceDAO competenceDAO = new CompetenceDAO();
    private static final DisposDAO disposDAO= new DisposDAO();
    private static final DPSDAO dpsDAO = new DPSDAO();
    private static final SecouristeDAO secouristeDAO = new SecouristeDAO();
    private static final SiteDAO siteDAO = new SiteDAO();
    private static final SportDAO sportDAO = new SportDAO();
    private static final UserDAO userDAO = new UserDAO();
    
    public static AffectationDAO getAffectationDAO() {
        return affectationDAO;
    }
    
    public static CompetenceDAO getCompetenceDAO() {
        return competenceDAO;
    }
    
    public static DisposDAO getDisposDAO() {
        return disposDAO;
    }
    
    public static DPSDAO getDPSDAO() {
        return dpsDAO;
    }

    public static SecouristeDAO getSecouristeDAO() {
        return secouristeDAO;
    }
    
    public static SiteDAO getSiteDAO() {
        return siteDAO;
    }
    
    public static SportDAO getSportDAO() {
        return sportDAO;
    }
    
    public static UserDAO getUserDAO() {
        return userDAO;
    }


    

}