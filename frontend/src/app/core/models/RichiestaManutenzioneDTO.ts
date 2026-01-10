import {VeicoloDTO} from '@core/models/veicoloDTO.model';

export interface RichiestaManutenzioneDTO {
  idManutenzione: number;
  idAdminAzienda?: number;
  idVeicolo: number;
  dataRichiesta: string;
  tipoManutenzione: string;
  accettata?: boolean;
  completata?: boolean;
  veicolo?: VeicoloDTO;
}
