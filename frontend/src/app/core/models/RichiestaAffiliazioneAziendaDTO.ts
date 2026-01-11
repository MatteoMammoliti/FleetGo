import {UtenteDTO} from '@core/models/UtenteDTO';

export interface RichiestaAffiliazioneAziendaDTO {
  utente?: UtenteDTO,
  idUtente?: number,
  idAzienda: number,
  accettata?: boolean,
  dataAccettazione?: string
}
