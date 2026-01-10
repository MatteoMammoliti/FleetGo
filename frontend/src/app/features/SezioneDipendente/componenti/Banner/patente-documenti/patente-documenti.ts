import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {ImmagineInputChecked} from '@shared/Componenti/Input/immagine-input-checked/immagine-input-checked';

@Component({
  selector: 'app-patente-documenti',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TemplateTitoloSottotitolo,
    ImmagineInputChecked
  ],
  templateUrl: './patente-documenti.html',
  styleUrl: './patente-documenti.css'
})
export class PatenteDocumentiComponent {

  @Input() datiPatente = "";
  @Output() salva = new EventEmitter<File>();

  onImmagineSelezionata(file: File) {
    this.salva.emit(file);

    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.datiPatente = e.target.result;
    };
    reader.readAsDataURL(file);
  }
}
