import {Component, EventEmitter, Output} from '@angular/core';
import {ModelloDTO} from '@core/models/ModelloDTO';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-form-aggiungi-modello',
  imports: [
    FormsModule
  ],
  templateUrl: './form-aggiungi-modello.html',
})

export class FormAggiungiModello {

  @Output() confermaAggiunta = new EventEmitter<FormData>();
  @Output() onAnnulla = new EventEmitter<void>();

  nomeModello: string = "";
  fileImmagine: File | null = null;

  onConferma() {

    if(this.nomeModello && this.fileImmagine) {
      const modello: ModelloDTO = {
        nomeModello: this.nomeModello
      }

      const obj = new FormData();
      obj.append('modello', new Blob([JSON.stringify(modello)], { type: 'application/json' }));
      obj.append("immagine", this.fileImmagine)

      this.confermaAggiunta.emit(obj);
    }
  }

  onFileSelected(event:any) {
    const file = event.target.files[0];
    if (file) {
      this.fileImmagine = file;
    }
  }
}
