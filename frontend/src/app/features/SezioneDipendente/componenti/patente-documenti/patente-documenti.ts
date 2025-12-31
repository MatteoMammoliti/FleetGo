import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import { ImmagineInputChecked } from '@shared/Componenti/Ui/immagine-input-checked/immagine-input-checked';

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
  
  @Input() datiPatente: any = {}; 
  @Output() salva = new EventEmitter<any>();
  @Output() cambioImmagine = new EventEmitter<File>();

  onImmagineSelezionata(file: File) {
    this.cambioImmagine.emit(file);

    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.datiPatente.urlImmagine = e.target.result;
    };
    reader.readAsDataURL(file);
  }

  isScaduta(dataScadenza: string): boolean {
    if (!dataScadenza) return false;
    const scadenza = new Date(dataScadenza);
    const oggi = new Date();
    return scadenza < oggi;
  }
}