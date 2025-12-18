package it.unical.fleetgo.backend.Controller.Dipendente;

import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.VeicoloPrenotazioneDTO;
import it.unical.fleetgo.backend.Service.PrenotazioniDipendentiService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/nuovaPrenotazione")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerNuovePrenotazioni {
    @Autowired private PrenotazioniDipendentiService service;

    @GetMapping("/caricaVeicoli")
    public ResponseEntity<List<VeicoloPrenotazioneDTO>> getVeicoli(@RequestParam("ritiro") String dataRitiro, @RequestParam("consegna") String dataConsegna,
                                           @RequestParam("oraInizio")String oraInizio, @RequestParam("oraFine") String oraFine, @RequestParam("nomeLuogo") String nomeLuogo,
                                           HttpSession session){
        Integer idAziendaAssociata= (Integer) session.getAttribute("idAziendaAssociata");
        if(idAziendaAssociata==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(service.getVeicoli(idAziendaAssociata,dataRitiro,dataConsegna,oraInizio,oraFine,nomeLuogo));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/caricaLuoghi")
    public ResponseEntity<List<LuogoDTO>> getLuoghiAzienda(HttpSession session){
        Integer idAziendaAssociata= (Integer) session.getAttribute("idAziendaAssociata");
        if(idAziendaAssociata==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(service.getLuogoAzienda(idAziendaAssociata));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
