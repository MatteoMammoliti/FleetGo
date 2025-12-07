import {AziendaDTO} from '@models/aziendaDTO';
import {AdminAziendaleDTO} from '@models/adminAziendaleDTO.models';

export interface AggiuntaNuovoAdminDTO{
  adminAziendale:AdminAziendaleDTO;
  azienda:AziendaDTO;
}
