import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {FormBuilder, FormsModule} from '@angular/forms';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {InputChecked} from '@shared/Componenti/Input/input-checked/input-checked';
import {validazione} from '@core/utils/validazione';

@Component({
  selector: 'app-profilo-personale',
  imports: [
    FormsModule,
    TemplateTitoloSottotitolo,
    InputChecked
  ],
  templateUrl: './profilo-personale.html',
  styleUrl: './profilo-personale.css',
})

export class ProfiloPersonale implements OnChanges {

  constructor(private validator: validazione) { }

  @Output() clickSalva = new EventEmitter<ModificaDatiUtenteDTO>();
  @Input() utente!: ModificaDatiUtenteDTO;
  datiForm: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO

  erroreNome: boolean = false;
  erroreCognome: boolean = false;
  erroreEmail: boolean = false;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['utente'] && this.utente) {
      this.datiForm = {
        nome: this.utente.nome,
        cognome: this.utente.cognome,
        data: null,
        email: this.utente.email,
        nomeAzienda: null,
        sedeAzienda: null,
        pIva: null,
        idUtente: this.utente.idUtente
      }
    }
  }

  salva() {
    this.erroreNome = false;
    this.erroreCognome = false;
    this.erroreEmail = false;

    let valid = true;

    if (!this.datiForm.nome || this.datiForm.nome.trim() === '') {
      this.erroreNome = true;
      valid = false;
    }

    if (!this.datiForm.cognome || this.datiForm.cognome.trim() === '') {
      this.erroreCognome = true;
      valid = false;
    }

    if (!this.datiForm.email || this.datiForm.email.trim() === '') {
      this.erroreEmail = true;
      valid = false;
    } else {

      if(!this.validator.checkEmail(this.datiForm.email)) {
        valid = false;
        this.erroreEmail = true;
      }

    }

    if (!valid) {
      return;
    }

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

    let qualcosaCambiato = false;

    if (this.datiForm.nome !== this.utente.nome) {
      qualcosaCambiato = true;
      datiDaInviare.nome = this.datiForm.nome;
    }
    if (this.datiForm.cognome !== this.utente.cognome) {
      qualcosaCambiato = true;
      datiDaInviare.cognome = this.datiForm.cognome;
    }
    if (this.datiForm.email !== this.utente.email) {
      qualcosaCambiato = true;
      datiDaInviare.email = this.datiForm.email;
    }

    if(qualcosaCambiato) this.clickSalva.emit(datiDaInviare);
  }
}
