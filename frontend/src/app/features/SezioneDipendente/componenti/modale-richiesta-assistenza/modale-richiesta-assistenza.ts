import {Component, EventEmitter, Input, Output, ViewChild} from '@angular/core';
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
  @ViewChild('modale') finestraModale!: TemplateFinestraModale;

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
  messaggioErroreOggetto: string = 'Seleziona un riferimento';


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
    this.messaggioErroreOggetto='Seleziona un riferimento';
  }


  invio() {
    let valid = true;
    this.reset();


    if (!this.categoriaSelezionata) {
      this.erroreCategoria = true;
      valid = false;
    }


    if (!this.messaggio || this.messaggio.trim() === '') {
      this.erroreMessaggio = true;
      valid = false;
    }
    if (this.erroreMessaggio||this.erroreCategoria) {
      return;
    }

    if (this.listaOggettiDinamicaVisibile) {
      if (!this.oggettoSelezionato) {
        this.erroreOggetto = true;
        this.messaggioErroreOggetto = 'Seleziona un riferimento';
        valid = false;
      } else {

        const valoreControllo = this.oggettoSelezionato.labelVisuale
                                ? this.oggettoSelezionato.labelVisuale
                                : this.oggettoSelezionato;

        if (valoreControllo === 'Nessun noleggio trovato') {
          this.erroreOggetto = true;
          this.messaggioErroreOggetto = 'Impossibile inviare: nessun noleggio disponibile';
          valid = false;
        }
      }
    }


    if (!valid) {
      return;
    }

    let datoDaInviare = "Richiesta riguardo a " + this.categoriaSelezionata;
    if (this.oggettoSelezionato) {

      const label = this.oggettoSelezionato.labelVisuale
                    ? this.oggettoSelezionato.labelVisuale
                    : this.oggettoSelezionato;
      datoDaInviare += " riguardo a " + label;
    }
    datoDaInviare += " con messaggio " + this.messaggio;
    this.successoBanner = "Segnalazione inviata con successo";

      this.inviaSegnalazione.emit(datoDaInviare);
      this.finestraModale.chiudiModale();
      this.successoBanner = '';
  }

}
