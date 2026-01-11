import {LuogoDTO} from '@core/models/LuogoDTO';

export interface VeicoloDTO {
  idVeicolo?: number;
  targaVeicolo?: string;
  urlImmagine?: string;
  idModello?: number;
  nomeModello?: string;
  tipoDistribuzioneVeicolo?: string;
  statusContrattualeVeicolo?: string;
  inManutenzione?: boolean;
  nomeAziendaAffiliata?: string;
  idAziendaAffiliata?: number;
  luogoRitiroDeposito?: LuogoDTO;
}
