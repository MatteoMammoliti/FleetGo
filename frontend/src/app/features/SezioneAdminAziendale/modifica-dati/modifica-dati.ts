import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FormBackground } from '@shared/form-background/form-background';
import {
  FormModificaDatiAdminAziendale
} from '@shared/form-modifica-dati-admin-aziendale/form-modifica-dati-admin-aziendale';
import {ModificaDatiService} from '@core/services/ServiceSezioneAdminAziendale/modifica-dati-service';
import {validazione} from '@shared/validation/validazione';
import {ModificaDatiUtenteDTO} from '@models/ModificaDatiUtenteDTO';

@Component({
  selector: 'app-modifica-dati',
  standalone: true,
  imports: [
    FormsModule,
    FormModificaDatiAdminAziendale
  ],
  templateUrl: './modifica-dati.html',
  styleUrl: './modifica-dati.css'
})
export class ModificaDatiComponent {
  errore:string="";

  constructor(private service: ModificaDatiService , validator:validazione) {
  }
  ngOnInit(){
    this.caricaDatiEsistenti();
  }

  utenteDaModificare: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO;

  modificaDatiUtente(datiAggiornati:ModificaDatiUtenteDTO){
    this.service.modificaDati(datiAggiornati).subscribe({
      next: (res) => {
        if(res) {
          this.caricaDatiEsistenti();
        }
        this.errore = 'Dati modificati con successo';
      },
      error: (err) => {
        console.log('Errore durante la modifica dati', err);
      }
    });
  }

  caricaDatiEsistenti() {

    this.service.getDati().subscribe({
      next: (datiRicevuti) => {

        console.log("Dati arrivati dal backend:", datiRicevuti);

        if(datiRicevuti) {
          this.utenteDaModificare=datiRicevuti;
        }
      },
      error: (err) => {
        console.error('Errore nel caricamento dati', err);
        this.errore = 'Impossibile caricare i dati utente';
      }
    });
  }







}
