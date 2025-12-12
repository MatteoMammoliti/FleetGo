import {AziendaDTO} from '@core/models/aziendaDTO';

export interface FatturaDTO {
  numeroFattura: number;
  idAzienda: number;
  meseFattura: number;
  annoFattura: number;
  fatturaPagata: boolean;
  costo: number;
  azienda: AziendaDTO;
}
