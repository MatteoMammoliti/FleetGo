import {Component, inject, Output, EventEmitter} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {validazione} from '@core/utils/validazione';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import { SceltaTendina } from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';

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

  @Output() onSalvataggio = new EventEmitter<FormData>();

  opzioniAlimentazione: string[] = ['Elettrica', 'Benzina', 'Diesel', 'Ibrida'];

  mappaErrori = {
    targaVeicolo: false,
    urlImmagine: false,
    modello: false,
    tipoDistribuzioneVeicolo: false,
    statusContrattuale: false
  };

  targaVeicolo = '';
  urlImmagine: any = null;
  modello = '';
  tipoDistribuzioneVeicolo = '';
  statusContrattuale = 'Disponibile';
  errore='';

  impostaAlimentazione(valore: string) {
    this.tipoDistribuzioneVeicolo = valore;
    if (valore) this.mappaErrori.tipoDistribuzioneVeicolo = false;
  }

  onSubmit() {
    this.reset();

    if (!this.targaVeicolo || !this.urlImmagine || !this.modello || !this.tipoDistribuzioneVeicolo || !this.statusContrattuale) {
      this.errore = "Riempi tutti i campi";

      if (!this.targaVeicolo) this.mappaErrori.targaVeicolo = true;
      if (!this.urlImmagine) this.mappaErrori.urlImmagine = true;
      if (!this.modello) this.mappaErrori.modello = true;
      if (!this.tipoDistribuzioneVeicolo) this.mappaErrori.tipoDistribuzioneVeicolo = true;
      if (!this.statusContrattuale) this.mappaErrori.statusContrattuale = true;
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
      statusContrattualeVeicolo: this.statusContrattuale,
      inManutenzione:false
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
      this.mappaErrori.urlImmagine = false;
    }
  }

  reset() {
    this.errore='';
    this.mappaErrori = {
      targaVeicolo: false,
      urlImmagine: false,
      modello: false,
      tipoDistribuzioneVeicolo: false,
      statusContrattuale: false
    };
  }

  pulisciForm() {
    this.targaVeicolo = '';
    this.urlImmagine = null;
    this.modello = '';
    this.tipoDistribuzioneVeicolo = '';
    this.statusContrattuale = 'Disponibile';
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }
}
