import {AziendaDTO} from '@core/models/aziendaDTO';
import {OffertaDTO} from '@core/models/offertaDTO.models';

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
