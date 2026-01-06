import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {UtenteDTO} from '@core/models/utenteDTO.model';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import { BannerErrore } from '@shared/Componenti/Ui/banner-errore/banner-errore';

@Component({
  selector: 'app-profilo-personale',
  imports: [
    FormsModule,
    TemplateTitoloSottotitolo,
    BannerErrore
  ],
  templateUrl: './profilo-personale.html',
  styleUrl: './profilo-personale.css',
})

export class ProfiloPersonale implements OnChanges {

  @Output() clickSalva=new EventEmitter<ModificaDatiUtenteDTO>()
  @Input() utente!:ModificaDatiUtenteDTO;
  datiForm:ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO

  erroreBanner : string = '';
  successoBanner : string = '';

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['utente'] && this.utente) {
      this.datiForm={
        nome:this.utente.nome,
        cognome:this.utente.cognome,
        data:null,
        email:this.utente.email,
        nomeAzienda:null,
        sedeAzienda:null,
        pIva:null,
        idUtente:this.utente.idUtente
      }
    
    }
  }

  salva() {
    this.erroreBanner='';
    this.successoBanner='';
    if (!this.datiForm.nome || this.datiForm.nome.trim() === '') {
      this.erroreBanner = "il campo nome è obbligatorio";
      return;
    }

    if (!this.datiForm.cognome || this.datiForm.cognome.trim() === '') {
      this.erroreBanner = "il campo cognome è obbligatorio";
      return;
    }

    if (!this.datiForm.email || this.datiForm.email.trim() === '') {
      this.erroreBanner = "il campo email è obbligatorio";
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.datiForm.email)) {
      this.erroreBanner = "inserisci un indirizzo email validoe";
      return;
    }

    let inviare = false;
    const datiDaInviare: ModificaDatiUtenteDTO = {
      nome: null,
      cognome: null,
      data: null,
      email: null,
      nomeAzienda: null,
      sedeAzienda: null,
      pIva: null,
      idUtente: this.utente.idUtente
    };

    if (this.datiForm.nome !== this.utente.nome) {
      inviare = true;
      datiDaInviare.nome = this.datiForm.nome;
    }
    if (this.datiForm.cognome !== this.utente.cognome) {
      inviare = true;
      datiDaInviare.cognome = this.datiForm.cognome;
    }
    if (this.datiForm.email !== this.utente.email) {
      inviare = true;
      datiDaInviare.email = this.datiForm.email;
    }

    if (inviare) {
      this.successoBanner="modifiche salvate con successo";
      setTimeout(() => this.successoBanner = '', 5000);
      this.clickSalva.emit(datiDaInviare);
    } else {
      this.erroreBanner = "nessuna modifica rilevata";
    }
  }
}
