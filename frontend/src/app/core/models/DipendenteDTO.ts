import {UtenteDTO} from '@core/models/UtenteDTO';

export interface DipendenteDTO extends UtenteDTO {
  idAziendaAffiliata?: number;
  urlImmagine?: string;
}
