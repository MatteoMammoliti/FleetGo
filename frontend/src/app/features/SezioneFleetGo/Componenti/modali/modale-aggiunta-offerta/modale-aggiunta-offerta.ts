import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {FormsModule} from '@angular/forms';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';
import {InputChecked} from '@shared/Componenti/Input/input-checked/input-checked';
import {ImmagineInputChecked} from '@shared/Componenti/Input/immagine-input-checked/immagine-input-checked';

@Component({
  selector: 'app-modale-aggiunta-offerta',
  imports: [
    FormsModule,
    TemplateFinestraModale,
    InputChecked,
    ImmagineInputChecked,
  ],
  templateUrl: './modale-aggiunta-offerta.html',
  styleUrl: './modale-aggiunta-offerta.css',
})
export class ModaleAggiuntaOfferta {

  @ViewChild('modale') finestraModale!: TemplateFinestraModale;

  @Input() paginaVisibile: boolean = false;
  @Output() chiudiPagina = new EventEmitter<void>();
  @Output() salva = new EventEmitter<FormData>();

  titoloOfferta = "";
  descrizioneOfferta = "";
  scontoPercentuale: number | null = null;
  dataScadenza: any;
  immagineCopertina: any;

  mappaErrori = {
    titoloOfferta: false,
    descrizioneOfferta: false,
    scontoPercentuale: false,
    dataScadenza: false,
    immagineCopertina: false
  };

  nuovaOfferta: OffertaDTO = {} as OffertaDTO;

  chiudiFinestraModale() {
    this.chiudiPagina.emit();
    this.pulisciForm();
  }

  salvaOfferta() {
    this.resetErrori();

    if (!this.titoloOfferta || !this.descrizioneOfferta || !this.scontoPercentuale || !this.dataScadenza || !this.immagineCopertina) {

      if (!this.titoloOfferta) this.mappaErrori.titoloOfferta = true;
      if (!this.descrizioneOfferta) this.mappaErrori.descrizioneOfferta = true;
      if (!this.scontoPercentuale) this.mappaErrori.scontoPercentuale = true;
      if (!this.dataScadenza) this.mappaErrori.dataScadenza = true;
      if (!this.immagineCopertina) this.mappaErrori.immagineCopertina = true;
      return;
    }

    if (this.scontoPercentuale < 1 || this.scontoPercentuale > 100) {
      this.mappaErrori.scontoPercentuale = true;
      return;
    }

    const oggi = new Date();
    const dataSelezionata = new Date(this.dataScadenza);
    if (dataSelezionata <= oggi) {
      this.mappaErrori.dataScadenza = true;
      return;
    }

    const formData = new FormData();
    this.nuovaOfferta = {
      descrizioneOfferta: this.descrizioneOfferta,
      nomeOfferta: this.titoloOfferta,
      percentualeSconto: this.scontoPercentuale,
      scadenza: this.dataScadenza
    }

    formData.append("offerta", new Blob([JSON.stringify(this.nuovaOfferta)], {type: 'application/json'}));
    formData.append("immagine", this.immagineCopertina);

    this.salva.emit(formData);
    this.finestraModale.chiudiModale();
    this.pulisciForm();
    this.resetErrori();
  }

  onFileSelected(file: File) {
    if (file) {
      this.immagineCopertina = file;
      this.mappaErrori.immagineCopertina = false;
    }
  }

  resetErrori() {
    this.mappaErrori = {
      titoloOfferta: false,
      descrizioneOfferta: false,
      scontoPercentuale: false,
      dataScadenza: false,
      immagineCopertina: false
    };
  }

  pulisciForm() {
    this.titoloOfferta = "";
    this.descrizioneOfferta = "";
    this.scontoPercentuale = null;
    this.dataScadenza = null;
    this.immagineCopertina = null;
    this.resetErrori();
  }
}
