import {UtenteDTO} from '@core/models/UtenteDTO';
import {VeicoloDTO} from '@core/models/VeicoloDTO';


export interface RichiestaNoleggioDTO {
  idRichiesta?: number;
  idDipendente?: number;
  idAziendaRiferimento?: number;
  idVeicolo?: number;
  oraInizio?: string;
  oraFine?: string;
  dataRitiro?: string;
  dataConsegna?: string;
  motivazione?: string;
  accettata?: boolean;
  utente?: UtenteDTO;
  richiestaAnnullata?: boolean;
  costoNoleggio?: number;
  statoRichiesta?: string;
  veicolo?: VeicoloDTO;
  nomeLuogo?: string;
}
