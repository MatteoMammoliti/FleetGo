import {UtenteDTO} from '@models/utenteDTO.model';

export interface AdminAziendaleDTO extends UtenteDTO {
  idAziendaGestita?: number;
}
