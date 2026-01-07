import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import {SceltaTendina} from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
import {FormsModule} from '@angular/forms';
import { InputChecked } from '@shared/Componenti/Ui/input-checked/input-checked';
import { BannerErrore } from '@shared/Componenti/Ui/banner-errore/banner-errore';

@Component({
  selector: 'app-modale-richiesta-assistenza',
  imports: [
    TemplateFinestraModale,
    SceltaTendina,
    FormsModule,
    InputChecked,
    BannerErrore
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
  erroreBanner: string = '';
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

  invio() {
    this.erroreBanner='';
    this.successoBanner='';
    this.erroreCategoria = false;
    this.erroreMessaggio = false;
    this.erroreOggetto = false;
    let valid = true;

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
        this.erroreBanner="Impossibile procedere senza un noleggio valido";
      }
    }

    if (!valid) {
      if (!this.erroreBanner) this.erroreBanner= "Compila tutti i campi obbligatori";
      return;
    }


    let datoDaInviare = "Richiesta riguardo a " + this.categoriaSelezionata;

    if (this.oggettoSelezionato && this.oggettoSelezionato.labelVisuale) {
      datoDaInviare += " riguardo a " + this.oggettoSelezionato.labelVisuale;
    }

    datoDaInviare += " con messaggio " + this.messaggio;
    this.successoBanner= "Segnalazione inviata con successo";

    this.inviaSegnalazione.emit(datoDaInviare);
  }
}
