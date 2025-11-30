package it.unical.fleetgo.backend.Controller;

import it.unical.fleetgo.backend.Models.DTO.VeicoloDTO;
import it.unical.fleetgo.backend.Persistence.DAO.VeicoloDAO;
import it.unical.fleetgo.backend.Service.UtenteService;
import it.unical.fleetgo.backend.Service.VeicoloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/dashboardFleetGo")
@CrossOrigin(value ="http://localhost:4200",allowCredentials = "true")
public class ControllerFleetGo {

    @Autowired
    private VeicoloService veicoloService;

    @PostMapping("/registraVeicolo")
    public ResponseEntity<String> registraVeicolo(@RequestPart("targaVeicolo") String targa,@RequestPart("modello") String modello,@RequestPart ("tipoDistribuzioneVeicolo") String distrubuzione,
                                                  @RequestPart("statusCondizioneVeicolo") String condizione) {
        System.out.println("Sono qui");
        VeicoloDTO veicoloDTO = new VeicoloDTO();
        veicoloDTO.setTargaVeicolo(targa);
        veicoloDTO.setModello(modello);
        veicoloDTO.setTipoDistribuzioneVeicolo(distrubuzione);
        veicoloDTO.setStatusCondizioneVeicolo(condizione);
        veicoloDTO.setLivelloCarburante(100);
        try{
            veicoloService.registraNuovoVeicolo(veicoloDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Veicolo registrato con successo");
        }catch(SQLException e){
            String state = e.getSQLState();
            if(state.equals("23505")){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Targa già presente");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Inserimento fallito a causa di un errore con il sistema");
        }
    }

}
