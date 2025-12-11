import {LuogoDTO} from '@core/models/luogoDTO.models';

export interface VeicoloDTO {
  idVeicolo?: number;
  targaVeicolo: string;
  UrlImmagine?: string;
  modello?: string;
  tipoDistribuzioneVeicolo?: string;
  livelloCarburante?: number;
  statusCondizioneVeicolo?: string;
  nomeAziendaAffiliata?:string;
  luogoRitiroDeposito?:LuogoDTO;
  idAziendaAffiliata?:number;
}
