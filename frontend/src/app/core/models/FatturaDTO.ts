import {AziendaDTO} from '@core/models/AziendaDTO';
import {OffertaDTO} from '@core/models/OffertaDTO';

export interface FatturaDTO {
  numeroFattura: number;
  idAzienda: number;
  meseFattura: number;
  annoFattura: number;
  fatturaPagata: boolean;
  costo: number;
  azienda: AziendaDTO;
  idOffertaApplicata?: number;
  offertaApplicata?: OffertaDTO;
}
