import {Component, inject} from '@angular/core';
import {FormAutenticazione} from "@shared/form-background/form-autenticazione";
import {FormsModule} from "@angular/forms";
import {FlottaGlobaleService} from '@core/services/adminFleetGoService/flotta-globale-service';
import {Router} from '@angular/router';
import {validazione} from '@shared/validation/validazione';

@Component({
  selector: 'app-form-cerca-auto',
    imports: [
        FormAutenticazione,
        FormsModule
    ],
  templateUrl: './form-cerca-auto.html',
  styleUrl: './form-cerca-auto.css',
})
export class FormCercaAuto {

  private auth=inject(FlottaGlobaleService);
  private router=inject(Router);
  private validatore=inject(validazione);

  mappaErrori = {
    targaVeicolo: false,
    urlImmagine: false,
    modello: false,
    tipoDistribuzioneVeicolo: false,
    statusCondizioneVeicolo: false,
  };

  targaVeicolo = '';
  urlImmagine:any = null;
  modello = '';
  tipoDistribuzioneVeicolo = '';
  statusCondizioneVeicolo = '';

  errore='';

  onSubmit() {
    this.reset()
    if(!this.targaVeicolo || !this.urlImmagine || !this.modello || !this.tipoDistribuzioneVeicolo || !this.statusCondizioneVeicolo){
      this.errore="Riempi tutti i campi";
      this.mappaErrori.targaVeicolo = this.targaVeicolo == '';
      this.mappaErrori.urlImmagine = this.urlImmagine == null;
      this.mappaErrori.modello = this.modello == '';
      this.mappaErrori.tipoDistribuzioneVeicolo = this.tipoDistribuzioneVeicolo == '';
      this.mappaErrori.statusCondizioneVeicolo = this.statusCondizioneVeicolo == '';
      return
    }

    if(!this.validatore.checkTarga(this.targaVeicolo)){
      this.errore='Targa non valida';
      this.mappaErrori.targaVeicolo=true;
      return;
    }
    if(this.urlImmagine==null){
      this.errore='Inserisci un immagine';
      this.mappaErrori.urlImmagine=true;
      return;
    }
    if (this.modello==''){
      this.errore='Inserisci un modello valido'
      this.mappaErrori.modello=true;
      return;
    }
    if(this.tipoDistribuzioneVeicolo==''){
      this.errore='Seleziona un tipo di distribuzione';
      this.mappaErrori.tipoDistribuzioneVeicolo=true;
      return;
    }
    if(this.statusCondizioneVeicolo==''){
      this.errore='Seleziona un stato del veicolo';
      this.mappaErrori.statusCondizioneVeicolo=true;
      return;
    }


    const formData = new FormData();

    formData.append('targaVeicolo', this.targaVeicolo);
    formData.append('modello', this.modello);
    formData.append('tipoDistribuzioneVeicolo', this.tipoDistribuzioneVeicolo);
    formData.append('statusCondizioneVeicolo', this.statusCondizioneVeicolo);
    formData.append('immagineVeicolo', this.urlImmagine);

    console.log("Form valido, invio dati...");

    this.auth.registraVeicolo(formData).subscribe({
      next: (response) => {
        this.pulisciForm();
      },
      error: (error) => {
        console.error('Errore durante la registrazione:', error);
      }

    })
  }
  reset(){
    this.errore = '';
    this.mappaErrori = {
      targaVeicolo: false,
      urlImmagine: false,
      modello: false,
      tipoDistribuzioneVeicolo: false,
      statusCondizioneVeicolo: false,
    };
  }

  pulisciForm(){
    this.targaVeicolo = '';
    this.urlImmagine = null;
    this.modello = '';
    this.tipoDistribuzioneVeicolo = '';
    this.statusCondizioneVeicolo = '';
    this.reset();
  }

  onFileSelected(event: any) {
    this.urlImmagine = event.target.files[0];
  }
}
