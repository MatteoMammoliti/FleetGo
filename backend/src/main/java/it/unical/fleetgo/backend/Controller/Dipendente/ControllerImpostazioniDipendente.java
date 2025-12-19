package it.unical.fleetgo.backend.Controller.Dipendente;

import it.unical.fleetgo.backend.Models.DTO.ModificaDatiUtenteDTO;
import it.unical.fleetgo.backend.Models.DTO.Utente.DipendenteDTO;
import it.unical.fleetgo.backend.Service.DipendenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/impostazioniDipendente")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerImpostazioniDipendente {
    @Autowired private DipendenteService dipendenteService;

    @GetMapping("/dettagliUtente")
    public ResponseEntity<ModificaDatiUtenteDTO> dettagliUtente(HttpSession session){
        Integer idUtente= (Integer)session.getAttribute("idUtente");
        if(idUtente==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            return ResponseEntity.ok(this.dipendenteService.getDatiUtente(idUtente));
        }catch(SQLException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (RuntimeException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/applicaModifiche")
    public ResponseEntity applicaModifiche(@RequestBody ModificaDatiUtenteDTO dto,HttpSession session){
        Integer idDipendente=(Integer) session.getAttribute("idUtente");
        if(idDipendente==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        dto.setIdUtente(idDipendente);
        try{
            this.dipendenteService.modificaDatiDipendente(dto);
            return  ResponseEntity.ok().build();
        }catch(SQLException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (RuntimeException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
