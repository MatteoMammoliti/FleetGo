import {AdminAziendaleDTO} from '@core/models/AdminAziendaleDTO';
import {AziendaDTO} from '@core/models/AziendaDTO';

export interface ContenitoreDatiRegistrazioneAzienda {
  adminAziendale: AdminAziendaleDTO;
  azienda: AziendaDTO;
}
