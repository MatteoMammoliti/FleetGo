import {AdminAziendaleDTO} from '@core/models/adminAziendaleDTO.models';
import {AziendaDTO} from '@core/models/aziendaDTO';

export interface ContenitoreDatiRegistrazioneAzienda {
  adminAziendale: AdminAziendaleDTO;
  azienda: AziendaDTO;
}
