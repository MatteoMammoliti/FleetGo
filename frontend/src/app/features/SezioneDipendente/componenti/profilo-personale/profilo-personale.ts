import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {UtenteDTO} from '@core/models/utenteDTO.model';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';

@Component({
  selector: 'app-profilo-personale',
  imports: [
    FormsModule
  ],
  templateUrl: './profilo-personale.html',
  styleUrl: './profilo-personale.css',
})

export class ProfiloPersonale implements OnChanges {

  @Output() clickSalva=new EventEmitter<ModificaDatiUtenteDTO>()
  @Input() utente!:ModificaDatiUtenteDTO;
  datiForm:ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO

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
    let inviare=false;
    const datiDaInviare:ModificaDatiUtenteDTO={
      nome:null,
      cognome:null,
      data:null,
      email:null,
      nomeAzienda:null,
      sedeAzienda:null,
      pIva:null,
      idUtente:this.utente.idUtente
    }

    if(this.datiForm.nome !== this.utente.nome){
      inviare=true;
      datiDaInviare.nome=this.datiForm.nome;
    }
    if(this.datiForm.cognome !== this.utente.cognome){
      inviare=true;
      datiDaInviare.cognome=this.datiForm.cognome;
    }
    if(this.datiForm.email!==this.utente.email){
      inviare=true;
      datiDaInviare.email=this.datiForm.email;
    }
    if(inviare){
      this.clickSalva.emit(datiDaInviare)
    }
  }
}
