import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import {SceltaTendina} from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
import {FormsModule} from '@angular/forms';
import { InputChecked } from '@shared/Componenti/Ui/input-checked/input-checked';

@Component({
  selector: 'app-modale-richiesta-assistenza',
  imports: [
    TemplateFinestraModale,
    SceltaTendina,
    FormsModule,
    InputChecked,
  ],
  templateUrl: './modale-richiesta-assistenza.html',
  styleUrl: './modale-richiesta-assistenza.css',
})

export class ModaleRichiestaAssistenza {

  @Output() chiudiModale = new EventEmitter<void>();
  @Output() inviaSegnalazione = new EventEmitter<string | null>();
  @Output() segnalaCategoriaCambiata = new EventEmitter<string>();

  @Input() caricamentoDati = false;
  @Input() categorieContatto: string[] = [];
  @Input() listaOggettiDinamica: any[] = [];
  @Input() listaOggettiDinamicaVisibile = false;

  oggettoSelezionato: any = null;
  categoriaSelezionata: any = null;
  messaggio = "";
  erroreCategoria: boolean = false;
  erroreMessaggio: boolean = false;
  erroreOggetto: boolean = false;
  successoBanner: string = '';

  cambioCategoria(categoria: string) {
    this.erroreCategoria=false;
    this.categoriaSelezionata = categoria;
    this.oggettoSelezionato = null;
    this.erroreOggetto = false;
    this.segnalaCategoriaCambiata.emit(this.categoriaSelezionata);
  }

  cambioOggetto(oggetto: any) {
    this.erroreOggetto=false;
    this.oggettoSelezionato=oggetto;
  }

  chiudi() {
    this.reset();
    this.listaOggettiDinamicaVisibile=false;
    this.oggettoSelezionato=null;
    this.categoriaSelezionata=null;
    this.listaOggettiDinamica=[];
    this.chiudiModale.emit();
  }

  reset() {
    this.successoBanner='';
    this.erroreCategoria = false;
    this.erroreMessaggio = false;
    this.erroreOggetto = false;
  }

  invio() {
    let valid = true;
    this.reset();

    if(!this.categoriaSelezionata) {
      this.erroreCategoria = true;
      valid = false;
    }

    if(!this.messaggio || this.messaggio.trim() === '') {
      this.erroreMessaggio = true;
      valid = false;
    }

    if(this.listaOggettiDinamicaVisibile) {
      if (!this.oggettoSelezionato) {
        this.erroreOggetto=true;
        valid = false; 
      }
      else if (this.oggettoSelezionato.labelVisuale === 'Nessun noleggio trovato') {
        valid = false;
      }
    }

    if (!valid) {
      return;
    }


    let datoDaInviare = "Richiesta riguardo a " + this.categoriaSelezionata;

    if (this.oggettoSelezionato && this.oggettoSelezionato.labelVisuale) {
      datoDaInviare += " riguardo a " + this.oggettoSelezionato.labelVisuale;
    }

    datoDaInviare += " con messaggio " + this.messaggio;
    this.successoBanner= "Segnalazione inviata con successo";

    setTimeout(() => {
      this.inviaSegnalazione.emit(datoDaInviare);
      this.successoBanner = '';
    }, 1500);
  }
}
