import {OffertaDTO} from '@core/models/offertaDTO.models';

export interface FatturaDaGenerareDTO{
  idAzienda:number;
  nomeAzienda:string;
  anno:string;
  mese:string;
  numeroNoleggi:number;
  costoTotale:number;
  idOffertaApplicata?: number;
  offertaApplicata?: OffertaDTO;
}
