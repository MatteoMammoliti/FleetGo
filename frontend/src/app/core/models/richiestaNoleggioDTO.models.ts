import {UtenteDTO} from '@core/models/utenteDTO.model';


export interface RichiestaNoleggioDTO {
idRichiestaNoleggio?:number;
idVeicolo:number;
idUtente:number;
utente: UtenteDTO;
idAzienda: number;
dataRitiro: string;
dataConsegna: string;
oraInizio: string;
oraFine: string;
motivazione:string;
richiestaAccettata: boolean;
}
