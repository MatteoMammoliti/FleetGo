import {Component, inject, Output, EventEmitter} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {validazione} from '@core/utils/validazione';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';

@Component({
  selector: 'app-form-aggiungi-auto',
    imports: [
        FormsModule
    ],
  templateUrl: './form-aggiungi-auto.html',
  styleUrl: './form-aggiungi-auto.css',
})
export class FormAggiungiAuto {
  private validator = inject(validazione);

  @Output() onSalvataggio = new EventEmitter<FormData>();

  mappaErrori = {
    targaVeicolo: false,
    urlImmagine: false,
    modello: false,
    tipoDistribuzioneVeicolo: false,
    statusCondizioneVeicolo: false
  };

  targaVeicolo = '';
  urlImmagine: any = null;
  modello = '';
  tipoDistribuzioneVeicolo = '';
  statusCondizioneVeicolo = 'Disponibile';
  errore='';

  onSubmit() {
    this.reset();

    if (!this.targaVeicolo || !this.urlImmagine || !this.modello || !this.tipoDistribuzioneVeicolo || !this.statusCondizioneVeicolo) {
      this.errore = "Riempi tutti i campi";

      if (!this.targaVeicolo) this.mappaErrori.targaVeicolo = true;
      if (!this.urlImmagine) this.mappaErrori.urlImmagine = true;
      if (!this.modello) this.mappaErrori.modello = true;
      if (!this.tipoDistribuzioneVeicolo) this.mappaErrori.tipoDistribuzioneVeicolo = true;
      if (!this.statusCondizioneVeicolo) this.mappaErrori.statusCondizioneVeicolo = true;
      return;
    }

    if(!this.validator.checkTarga(this.targaVeicolo)) {
      this.errore = "Targa non valida";
      this.mappaErrori.targaVeicolo = true;
      return;
    }

    const veicolo: VeicoloDTO = {
      targaVeicolo: this.targaVeicolo,
      modello: this.modello,
      tipoDistribuzioneVeicolo: this.tipoDistribuzioneVeicolo,
      statusCondizioneVeicolo: this.statusCondizioneVeicolo,
    }

    const formData = new FormData();
    formData.append("immagineVeicolo", this.urlImmagine);
    formData.append("veicolo", new Blob([JSON.stringify(veicolo)], { type: 'application/json' }));
    this.onSalvataggio.emit(formData as any);
    this.pulisciForm();
  }

  onFileSelected(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.urlImmagine = event.target.files[0];
    }
  }

  reset() {
    this.errore='';
    this.mappaErrori = {
      targaVeicolo: false,
      urlImmagine: false,
      modello: false,
      tipoDistribuzioneVeicolo: false,
      statusCondizioneVeicolo: false
    };
  }

  pulisciForm() {
    this.targaVeicolo = '';
    this.urlImmagine = null;
    this.modello = '';
    this.tipoDistribuzioneVeicolo = '';
    this.statusCondizioneVeicolo = 'Disponibile';
  }
}
