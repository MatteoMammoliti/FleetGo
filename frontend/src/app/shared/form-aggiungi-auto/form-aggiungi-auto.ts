import {Component, inject, Output, EventEmitter} from '@angular/core';
import {FormBackground} from "@shared/form-background/form-background";
import {FormsModule} from "@angular/forms";
import {FlottaGlobaleService} from '@core/services/ServiceSezioneFleetGo/flotta-globale-service';
import {validazione} from '@shared/validation/validazione';

@Component({
  selector: 'app-form-aggiungi-auto',
    imports: [
        FormBackground,
        FormsModule
    ],
  templateUrl: './form-aggiungi-auto.html',
  styleUrl: './form-aggiungi-auto.css',
})
export class FormAggiungiAuto {
  private validator = inject(validazione);
  
  @Output() onSalvataggio = new EventEmitter<FormData>();

  mappaErrori = {
    targaVeicolo: false, urlImmagine: false, modello: false, tipoDistribuzioneVeicolo: false,
    statusCondizioneVeicolo: false
  };
  targaVeicolo = '';
  urlImmagine: any=null;
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

    const formData = new FormData();
    formData.append('targaVeicolo', this.targaVeicolo);
    formData.append('Immagine', this.urlImmagine);
    formData.append('modello', this.modello);
    formData.append('tipoDistribuzioneVeicolo', this.tipoDistribuzioneVeicolo);
    formData.append('statusCondizioneVeicolo', this.statusCondizioneVeicolo);

    this.onSalvataggio.emit(formData);
  }

  onFileSelected(event: any) {
    if (event.target.files && event.target.files.lenght > 0) {
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