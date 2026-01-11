import {UtenteDTO} from '@core/models/UtenteDTO';
import {AziendaDTO} from '@core/models/AziendaDTO';

export interface AdminAziendaleDTO extends UtenteDTO {
  idAziendaGestita?: number;
  aziendaGestita?: AziendaDTO;
}
