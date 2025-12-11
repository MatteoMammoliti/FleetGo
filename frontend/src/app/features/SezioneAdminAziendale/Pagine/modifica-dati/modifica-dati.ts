import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  FormModificaDatiAdminAziendale
} from '@features/SezioneAdminAziendale/Componenti/form-modifica-dati-admin-aziendale/form-modifica-dati-admin-aziendale';
import {ModificaDatiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/modifica-dati-service';
import {validazione} from '@core/utils/validazione';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';

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
