import {UtenteDTO} from '@core/models/utenteDTO.model';

export interface DipendenteDTO extends UtenteDTO {
  idAziendaAffiliata?: number;
  urlImmagine?: string;
}
