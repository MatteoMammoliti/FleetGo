package it.unical.fleetgo.backend.Controller.Dipendente;

import it.unical.fleetgo.backend.Models.DTO.LuogoDTO;
import it.unical.fleetgo.backend.Models.DTO.RichiestaNoleggioDTO;
import it.unical.fleetgo.backend.Models.DTO.StatisticheDipendenteDTO;
import it.unical.fleetgo.backend.Service.DipendenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dashboardDipendente")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerHomeDipendente {
    @Autowired private DipendenteService dipendenteService;

    @GetMapping("/prossimoViaggio")
    public ResponseEntity<RichiestaNoleggioDTO> getProssimoNoleggioDipendente(HttpSession session){
        try{
            return ResponseEntity.ok(this.dipendenteService.getProssimoNoleggioDipendente((Integer) session.getAttribute("idUtente")));
        }catch(SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/statisticheDipendente")
    public ResponseEntity<StatisticheDipendenteDTO> getStatisticheDipendente(HttpSession session){
        try{
            return ResponseEntity.ok(this.dipendenteService.getStatisticheDipendente((Integer) session.getAttribute("idUtente")));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/luoghiAzienda")
    public ResponseEntity<List<LuogoDTO>> getLuoghiAzienda(HttpSession session){
        Integer idAziendaAssociata=(Integer) session.getAttribute("idAziendaAssociata");
        if(idAziendaAssociata==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try{
            return ResponseEntity.ok(this.dipendenteService.getLuoghiAziendaAssociata(idAziendaAssociata));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/richiediNome")
    public  ResponseEntity<String> getRichiediNome(HttpSession session){
        Integer idDipendente=(Integer) session.getAttribute("idUtente");
        if(idDipendente==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try{
            return ResponseEntity.ok(this.dipendenteService.getNomeDipendente(idDipendente));
        }catch (SQLException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }catch (RuntimeException e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }
}
