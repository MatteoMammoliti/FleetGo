import {AdminAziendaleDTO} from '@models/adminAziendaleDTO.models';
import {AziendaDTO} from '@models/aziendaDTO';

export interface ContenitoreDatiRegistrazioneAzienda {
  adminAziendale: AdminAziendaleDTO;
  azienda: AziendaDTO;
}
