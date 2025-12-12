import {LuogoDTO} from '@core/models/luogoDTO.models';

export interface VeicoloDTO {
  idVeicolo?: number;
  targaVeicolo: string;
  UrlImmagine?: string;
  modello?: string;
  tipoDistribuzioneVeicolo?: string;
  livelloCarburante?: number;
  statusContrattualeVeicolo?: string;
  inManutenzione:boolean;
  nomeAziendaAffiliata?:string;
  idAziendaAffiliata?:number;
  luogoRitiroDeposito?:LuogoDTO;
}
