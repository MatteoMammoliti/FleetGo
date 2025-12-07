import {UtenteDTO} from '@models/utenteDTO.model';

export interface DipendenteDTO extends UtenteDTO {
  idAziendaAffiliata?: number;
  urlImmagine?: string;
  patenteAccettata?: boolean;
}
