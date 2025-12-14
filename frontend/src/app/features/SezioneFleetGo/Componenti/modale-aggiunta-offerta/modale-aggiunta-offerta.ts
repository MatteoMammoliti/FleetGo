import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {FormsModule} from '@angular/forms';
import {validazione} from '@core/utils/validazione';

@Component({
  selector: 'app-modale-aggiunta-offerta',
  imports: [
    FormsModule,
  ],
  templateUrl: './modale-aggiunta-offerta.html',
  styleUrl: './modale-aggiunta-offerta.css',
})
export class ModaleAggiuntaOfferta {

  private validator = inject(validazione);

  @Input() paginaVisibile: boolean = false;
  @Output() chiudiPagina = new EventEmitter<void>();
  @Output() salva = new EventEmitter<FormData>();

  titoloOfferta = "";
  descrizioneOfferta = "";
  scontoPercentuale: number | null = null;
  dataScadenza: any;
  immagineCopertina: any;
  errore = '';

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
      this.errore = "Riempi tutti i campi obbligatori";

      if (!this.titoloOfferta) this.mappaErrori.titoloOfferta = true;
      if (!this.descrizioneOfferta) this.mappaErrori.descrizioneOfferta = true;
      if (!this.scontoPercentuale) this.mappaErrori.scontoPercentuale = true;
      if (!this.dataScadenza) this.mappaErrori.dataScadenza = true;
      if (!this.immagineCopertina) this.mappaErrori.immagineCopertina = true;
      return;
    }

    if (this.scontoPercentuale < 1 || this.scontoPercentuale > 100) {
      this.errore = "Lo sconto deve essere compreso tra 1% e 100%";
      this.mappaErrori.scontoPercentuale = true;
      return;
    }

    const oggi = new Date();
    const dataSelezionata = new Date(this.dataScadenza);
    if (dataSelezionata <= oggi) {
      this.errore = "La data di scadenza deve essere futura";
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

    formData.append("offerta", new Blob([JSON.stringify(this.nuovaOfferta)], { type: 'application/json' }));
    formData.append("immagine", this.immagineCopertina);

    this.salva.emit(formData);
    this.pulisciForm();
  }

  onFileSelected(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.immagineCopertina = event.target.files[0];
      this.mappaErrori.immagineCopertina = false;
    }
  }

  resetErrori() {
    this.errore = '';
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
