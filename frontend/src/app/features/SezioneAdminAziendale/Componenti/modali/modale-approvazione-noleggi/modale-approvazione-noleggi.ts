import {Component, EventEmitter, Input, Output} from '@angular/core';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {DatePipe} from '@angular/common';
import {RisoluzioneConfilittiNoleggio} from '@core/models/RisoluzioneConfilittiNoleggio';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';

@Component({
  selector: 'app-modale-approvazione-noleggi',
  imports: [
    DatePipe,
    TemplateFinestraModale
  ],
  templateUrl: './modale-approvazione-noleggi.html',
  styleUrl: './modale-approvazione-noleggi.css',
})

export class ModaleApprovazioneNoleggi {

  @Input() modaleAperto = false;
  @Input() richiesteNoleggioDaApprovare: RichiestaNoleggioDTO[] = [];
  @Output() chiudiModale = new EventEmitter<void>();
  @Output() approvaRichiesta = new EventEmitter<number>();
  @Output() rifiutaRichiesta = new EventEmitter<number>();
  @Output() accettazioneConRifiuto = new EventEmitter<RisoluzioneConfilittiNoleggio>();

  conflittiTrovati: RichiestaNoleggioDTO[] = [];
  richiestaInAnalisi: RichiestaNoleggioDTO = {} as RichiestaNoleggioDTO;
  mostraAlertConfiltti = false;

  tentaAccettazione(richiesta: RichiestaNoleggioDTO) {
    this.conflittiTrovati = this.trovaConflitti(richiesta);

    if(this.conflittiTrovati.length > 0){
      this.richiestaInAnalisi = richiesta;
      this.mostraAlertConfiltti = true;
      this.confermaApprovazione();
      return;
    }
    this.approvaRichiesta.emit(richiesta.idRichiesta);
  }

  confermaApprovazione(){

    let idRichiesteDaRifiutare: number[] = [];
    for(const richieta of this.conflittiTrovati){
      if (richieta.idRichiesta != null) {
        idRichiesteDaRifiutare.push(richieta.idRichiesta);
      }
    }

    const dto: RisoluzioneConfilittiNoleggio = {
      idRichiestaDaApprovare: this.richiestaInAnalisi.idRichiesta ? this.richiestaInAnalisi.idRichiesta : undefined,
      idRichiesteDaRifiutare: idRichiesteDaRifiutare
    }

    if(dto.idRichiestaDaApprovare) this.accettazioneConRifiuto.emit(dto);
    this.annullaApprovazione();
  }

  annullaApprovazione() {
    this.richiestaInAnalisi = {} as RichiestaNoleggioDTO;
    this.mostraAlertConfiltti = false;
    this.conflittiTrovati = [];
  }

  private trovaConflitti(target: RichiestaNoleggioDTO): RichiestaNoleggioDTO[] {
    return this.richiesteNoleggioDaApprovare.filter(req => {

      if (req.idRichiesta === target.idRichiesta) return false;

      const inizioA = new Date(`${req.dataRitiro}T${req.oraInizio}`);
      const fineA = new Date(`${req.dataConsegna}T${req.oraFine}`);

      const inizioB = new Date(`${target.dataRitiro}T${target.oraInizio}`);
      const fineB = new Date(`${target.dataConsegna}T${target.oraFine}`);

      const sovrapposizione = inizioA < fineB && fineA > inizioB;

      if (!sovrapposizione) return false;

      return req.veicolo?.idVeicolo === target.veicolo?.idVeicolo;
    });
  }
}
