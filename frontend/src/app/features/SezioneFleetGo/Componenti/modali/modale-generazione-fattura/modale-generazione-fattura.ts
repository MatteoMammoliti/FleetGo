import {Component, EventEmitter, Input, Output, SimpleChanges} from '@angular/core';
import {FatturaDaGenerareDTO} from '@core/models/FatturaDaGenerareDTO';
import {OffertaDTO} from '@core/models/offertaDTO.models';
import {FormsModule} from '@angular/forms';
import {DecimalPipe} from '@angular/common';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import {SceltaTendina} from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';

@Component({
  selector: 'app-modale-generazione-fattura',
  imports: [
    FormsModule,
    DecimalPipe,
    TemplateFinestraModale,
    SceltaTendina
  ],
  templateUrl: './modale-generazione-fattura.html',
  styleUrl: './modale-generazione-fattura.css',
})
export class ModaleGenerazioneFattura {

  @Input() modaleVisibile = false;
  @Input() fattura: FatturaDaGenerareDTO = {} as FatturaDaGenerareDTO;
  @Input() offerteAttive: OffertaDTO[] = [];
  @Output() chiudiPagina = new EventEmitter<void>();
  @Output() confermaGenerazione = new EventEmitter<FatturaDaGenerareDTO>();
  offertaSelezionata: any = null;
  prezzoScontato = 0;
  scontoDaSottrarre = 0;
  ngOnChanges(changes: SimpleChanges){
    if (changes['fattura'] && this.fattura) {
          this.calcolaSconto();
    }
  }

  chiudiModale() { this.chiudiPagina.emit(); }

  confermaGenerazioneFattura() {
    const fattura = {
      ...this.fattura,
      idOffertaApplicata: this.offertaSelezionata ? this.offertaSelezionata.idOfferta : undefined,
      offertaApplicata: this.offertaSelezionata ? this.offertaSelezionata : undefined
    }

    this.confermaGenerazione.emit(fattura);
  }

  calcolaSconto() {
    if (this.offertaSelezionata) {
      this.prezzoScontato = 0;
      this.scontoDaSottrarre = (this.fattura.costoTotale * this.offertaSelezionata.percentualeSconto) / 100;
      this.prezzoScontato = this.fattura.costoTotale - this.scontoDaSottrarre;
    } else {
      this.scontoDaSottrarre = 0;
      this.prezzoScontato = this.fattura.costoTotale;
    }
  }
}
