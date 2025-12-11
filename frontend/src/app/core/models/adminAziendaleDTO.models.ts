import {UtenteDTO} from '@core/models/utenteDTO.model';

export interface AdminAziendaleDTO extends UtenteDTO {
  idAziendaGestita?: number;
}
