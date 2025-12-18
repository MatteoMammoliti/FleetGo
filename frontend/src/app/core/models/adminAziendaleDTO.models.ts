import {UtenteDTO} from '@core/models/utenteDTO.model';
import {AziendaDTO} from '@core/models/aziendaDTO';

export interface AdminAziendaleDTO extends UtenteDTO {
  idAziendaGestita?: number;
  aziendaGestita?: AziendaDTO;
}
