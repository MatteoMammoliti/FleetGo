package it.unical.fleetgo.backend.Models.Proxy;

import it.unical.fleetgo.backend.Persistence.DAO.*;
import it.unical.fleetgo.backend.Persistence.Entity.*;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.AdminAziendale;
import it.unical.fleetgo.backend.Persistence.Entity.Utente.Dipendente;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AdminAziendaleProxy extends AdminAziendale {

    private final AziendaDAO aziendaDAO;
    private boolean aziendaCaricata = false;
    private boolean idAziendaGestitaCaricato = false;

    public AdminAziendaleProxy(AziendaDAO aziendaDAO) {

        this.aziendaDAO = aziendaDAO;
    }

    @Override
    public Integer getIdAziendaGestita(){
        if(!idAziendaGestitaCaricato){
            idAziendaGestitaCaricato = true;
            super.setIdAziendaGestita(aziendaDAO.getIdAziendaGestita(
                    this.getIdUtente()
            ));
        }
        return super.getIdAziendaGestita();
    }

    @Override
    public Azienda getAziendaGestita(){
        if(!aziendaCaricata) {
            aziendaCaricata = true;
            super.setAziendaGestita(aziendaDAO.getAziendaById(this.getIdAziendaGestita()));
        }
        return super.getAziendaGestita();
    }
}