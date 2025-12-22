import {UtenteDTO} from '@core/models/utenteDTO.model';

export interface RichiestaAffiliazioneAziendaDTO {
  utente?: UtenteDTO,
  idUtente?: number,
  idAzienda: number,
  accettata?: boolean,
  dataAccettazione?: string
}
