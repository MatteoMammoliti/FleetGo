import {UtenteDTO} from '@core/models/utenteDTO.model';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';


export interface RichiestaNoleggioDTO {
  idRichiesta:number;
  idDipendente:number;
  idAziendaRiferimento?: number;
  idVeicolo:number;
  utente?: UtenteDTO;
  idAzienda: number;
  dataRitiro: string;
  dataConsegna: string;
  oraInizio: string;
  oraFine: string;
  motivazione:string;
  accettata: boolean;
  richiestaAnnullata:boolean;
  statoRichiesta:string;
  costoNoleggio:number;
  veicolo?: VeicoloDTO;
}
