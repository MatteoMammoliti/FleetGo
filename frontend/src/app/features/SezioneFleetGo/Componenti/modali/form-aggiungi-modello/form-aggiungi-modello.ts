import {Component, EventEmitter, Output, ViewChild} from '@angular/core';
import {ModelloDTO} from '@core/models/ModelloDTO';
import {FormsModule} from '@angular/forms';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import {InputChecked} from '@shared/Componenti/Ui/input-checked/input-checked';
import {ImmagineInputChecked} from '@shared/Componenti/Ui/immagine-input-checked/immagine-input-checked';

@Component({
  selector: 'app-form-aggiungi-modello',
  imports: [
    FormsModule,
    TemplateFinestraModale,
    InputChecked,
    ImmagineInputChecked
  ],
  templateUrl: './form-aggiungi-modello.html',
})

export class FormAggiungiModello {
  @ViewChild('modale') finestraModale!: TemplateFinestraModale;

  @Output() confermaAggiunta = new EventEmitter<FormData>();
  @Output() onAnnulla = new EventEmitter<void>();

  nomeModello: string = "";
  fileImmagine: File | null = null;

  erroreNome: boolean = false;
  erroreImmagine: boolean = false;

  onConferma() {
    this.erroreNome = !this.nomeModello;
    this.erroreImmagine = !this.fileImmagine;

    if(!this.erroreNome && !this.erroreImmagine) {
      const modello: ModelloDTO = {
        nomeModello: this.nomeModello
      }

      const obj = new FormData();
      obj.append('modello', new Blob([JSON.stringify(modello)], { type: 'application/json' }));
      if (this.fileImmagine) {
        obj.append("immagine", this.fileImmagine)
      }
      this.confermaAggiunta.emit(obj);
      this.finestraModale.chiudiModale();
      this.resetForm();
    }
  }

  onFileSelected(file:File) {
      this.fileImmagine = file;
      this.erroreImmagine = false;
  }

  private resetForm() {
    this.nomeModello = "";
    this.fileImmagine = null;
    this.erroreNome = false;
    this.erroreImmagine = false;
  }
}
