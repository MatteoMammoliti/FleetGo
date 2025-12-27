import {Component, inject, Output, EventEmitter, Input} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {validazione} from '@core/utils/validazione';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import { SceltaTendina } from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
import {ModelloDTO} from '@core/models/ModelloDTO';

@Component({
  selector: 'app-form-aggiungi-auto',
    imports: [
        FormsModule,
        SceltaTendina
    ],
  templateUrl: './form-aggiungi-auto.html',
  styleUrl: './form-aggiungi-auto.css',
})

export class FormAggiungiAuto {
  private validator = inject(validazione);

  @Input() modelli: ModelloDTO[] = [];
  @Output() onSalvataggio = new EventEmitter<VeicoloDTO>();
  @Output() onAnnulla = new EventEmitter<void>();

  opzioniAlimentazione: string[] = ['Elettrica', 'Benzina', 'Diesel', 'Ibrida'];

  mappaErrori = {
    targaVeicolo: false,
    tipoDistribuzioneVeicolo: false,
    modello: false,
  };

  targaVeicolo = '';
  idModello = 0;
  tipoDistribuzioneVeicolo = '';
  errore='';

  impostaAlimentazione(valore: string) {
    this.tipoDistribuzioneVeicolo = valore;
    if (valore) this.mappaErrori.tipoDistribuzioneVeicolo = false;
  }

  onSubmit() {
    this.reset();

    if (!this.targaVeicolo || !this.tipoDistribuzioneVeicolo || !this.idModello) {
      this.errore = "Riempi tutti i campi";

      if (!this.targaVeicolo) this.mappaErrori.targaVeicolo = true;
      if (!this.tipoDistribuzioneVeicolo) this.mappaErrori.tipoDistribuzioneVeicolo = true;
      if (!this.idModello) this.mappaErrori.modello = true;
      return;
    }

    if(!this.validator.checkTarga(this.targaVeicolo)) {
      this.errore = "Targa non valida";
      this.mappaErrori.targaVeicolo = true;
      return;
    }

    const veicolo: VeicoloDTO = {
      targaVeicolo: this.targaVeicolo,
      idModello: this.idModello,
      tipoDistribuzioneVeicolo: this.tipoDistribuzioneVeicolo,
      inManutenzione:false
    }

    this.onSalvataggio.emit(veicolo);
    this.pulisciForm();
  }

  reset() {
    this.errore='';
    this.mappaErrori = {
      targaVeicolo: false,
      tipoDistribuzioneVeicolo: false,
      modello: false
    };
  }

  pulisciForm() {
    this.targaVeicolo = '';
    this.idModello = 0;
    this.tipoDistribuzioneVeicolo = '';
  }
}
