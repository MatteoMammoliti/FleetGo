import {LuogoDTO} from '@core/models/luogoDTO.models';

export interface VeicoloDTO {
  idVeicolo?: number;
  targaVeicolo?: string;
  urlImmagine?: string;
  idModello?: number;
  nomeModello?: string;
  tipoDistribuzioneVeicolo?: string;
  statusContrattualeVeicolo?: string;
  inManutenzione?:boolean;
  nomeAziendaAffiliata?:string;
  idAziendaAffiliata?:number;
  luogoRitiroDeposito?:LuogoDTO;
}
