import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import {SceltaTendina} from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-modale-richiesta-assistenza',
  imports: [
    TemplateFinestraModale,
    SceltaTendina,
    FormsModule
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
  errore = "";
  erroreTendina= false;

  cambioCategoria(categoria: string) {
    this.categoriaSelezionata = categoria;
    this.oggettoSelezionato = null;
    this.segnalaCategoriaCambiata.emit(this.categoriaSelezionata);
  }

  invio() {
    this.errore = "";
    let datoDaInviare: string | null = null;

    if(!this.categoriaSelezionata || !this.messaggio) {
      this.errore = "Compila tutti i campi obbligatori";
      this.erroreTendina=true;
      return;
    }

    datoDaInviare = "Richiesta riguardo a " + this.categoriaSelezionata;

    if(this.listaOggettiDinamicaVisibile && !this.oggettoSelezionato) {
      this.errore = "Seleziona un riferimento di una prenotazione";
      return;
    }

    if(this.oggettoSelezionato?.labelVisuale === 'Nessun noleggio trovato') {
      this.errore = "Non è possibile inviare una richiesta senza un noleggio di riferimento";
      return;
    }

    if (this.oggettoSelezionato && this.oggettoSelezionato.labelVisuale) {
      datoDaInviare += " riguardo a " + this.oggettoSelezionato.labelVisuale;
    }

    datoDaInviare += " con messaggio " + this.messaggio;

    if(datoDaInviare) {
      this.erroreTendina=false;
      this.inviaSegnalazione.emit(datoDaInviare);
    }
  }
}
