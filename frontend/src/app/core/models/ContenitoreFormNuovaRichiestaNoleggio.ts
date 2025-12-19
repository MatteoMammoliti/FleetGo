import {VeicoloPrenotazioneDTO} from '@core/models/veicoloPrenotazioneDTO';

export interface ContenitoreFormNuovaRichiestaNoleggio{
  veicolo:VeicoloPrenotazioneDTO,
  dataInizio:string,
  dataFine:string,
  oraInizio:string,
  oraFine:string
  motivazione:string
}
