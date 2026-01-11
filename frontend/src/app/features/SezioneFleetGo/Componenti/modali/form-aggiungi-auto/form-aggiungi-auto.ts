import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {validazione} from '@core/utils/validazione';
import {VeicoloDTO} from '@core/models/VeicoloDTO';
import {SceltaTendina} from '@shared/Componenti/Input/scelta-tendina/scelta-tendina';
import {ModelloDTO} from '@core/models/ModelloDTO';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';
import {InputChecked} from '@shared/Componenti/Input/input-checked/input-checked';

@Component({
  selector: 'app-form-aggiungi-auto',
  imports: [
    FormsModule,
    SceltaTendina,
    TemplateFinestraModale,
    InputChecked
  ],
  templateUrl: './form-aggiungi-auto.html',
  styleUrl: './form-aggiungi-auto.css',
})

export class FormAggiungiAuto {
  constructor(private validator: validazione) {
  }

  @ViewChild('modale') finestraModale!: TemplateFinestraModale;

  @Input() modelli: ModelloDTO[] = [];
  @Output() onSalvataggio = new EventEmitter<VeicoloDTO>();
  @Output() onAnnulla = new EventEmitter<void>();

  opzioniAlimentazione: string[] = ['Elettrico', 'Benzina', 'Diesel', 'Ibrido'];

  mappaErrori = {
    targaVeicolo: false,
    tipoDistribuzioneVeicolo: false,
    modello: false,
  };

  targaVeicolo = '';
  idModello: number | null = null;
  tipoDistribuzioneVeicolo: any = null;
  errore = '';


  salva() {
    this.reset();

    if (!this.targaVeicolo || !this.tipoDistribuzioneVeicolo || !this.idModello) {
      this.errore = "Riempi tutti i campi";

      if (!this.targaVeicolo) this.mappaErrori.targaVeicolo = true;
      if (!this.tipoDistribuzioneVeicolo) this.mappaErrori.tipoDistribuzioneVeicolo = true;
      if (!this.idModello) this.mappaErrori.modello = true;
      return;
    }

    if (!this.validator.checkTarga(this.targaVeicolo)) {
      this.errore = "Targa non valida";
      this.mappaErrori.targaVeicolo = true;
      return;
    }

    const veicolo: VeicoloDTO = {
      targaVeicolo: this.targaVeicolo,
      idModello: this.idModello,
      tipoDistribuzioneVeicolo: this.tipoDistribuzioneVeicolo,
      inManutenzione: false
    }

    this.onSalvataggio.emit(veicolo);
    this.finestraModale.chiudiModale()
    this.pulisciForm();
  }

  reset() {
    this.errore = '';
    this.mappaErrori = {
      targaVeicolo: false,
      tipoDistribuzioneVeicolo: false,
      modello: false
    };
  }

  pulisciForm() {
    this.targaVeicolo = '';
    this.idModello = null;
    this.tipoDistribuzioneVeicolo = '';
  }
}
