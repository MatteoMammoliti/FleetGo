import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {
  FormModificaDatiAdminAziendale
} from '@features/SezioneAdminAziendale/Componenti/form-modifica-dati-admin-aziendale/form-modifica-dati-admin-aziendale';
import {ModificaDatiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/modifica-dati-service';
import {ModificaDatiUtenteDTO} from '@core/models/ModificaDatiUtenteDTO';
import {LuogoDTO} from '@core/models/LuogoDTO';
import {
  MappaGestioneLuoghi
} from '@features/SezioneAdminAziendale/Componenti/modali/mappa-gestione-luoghi/mappa-gestione-luoghi';
import {
  TabellaGestioneLuoghi
} from '@features/SezioneAdminAziendale/Componenti/tabelle/tabella-gestione-luoghi/tabella-gestione-luoghi';
import {NgClass} from '@angular/common';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';

@Component({
  selector: 'app-modifica-dati',
  standalone: true,
  imports: [
    FormsModule,
    FormModificaDatiAdminAziendale,
    MappaGestioneLuoghi,
    TabellaGestioneLuoghi,
    NgClass,
    BannerErrore
  ],
  templateUrl: './modifica-dati.html',
  styleUrl: './modifica-dati.css'
})


export class ModificaDatiComponent implements OnInit {

  constructor(private service: ModificaDatiService) {
  }

  errore: string = "";
  utenteDaModificare: ModificaDatiUtenteDTO = {} as ModificaDatiUtenteDTO;
  luoghiEsistenti: LuogoDTO[] = [];
  modalitaAggiunta = false;

  erroreBanner = "";
  successoBanner = "";

  ngOnInit() {
    this.caricaDatiAdmin();
    this.caricaLuoghiEsistenti();
  }

  iniziaAggiuntaLuogo() {
    this.modalitaAggiunta = !this.modalitaAggiunta;
  }

  aggiungiLuogo(luogo: LuogoDTO) {
    this.service.aggiungiLuogo(luogo).subscribe({
      next: value => {
        this.luoghiEsistenti.push(luogo);
        this.iniziaAggiuntaLuogo();
        this.caricaLuoghiEsistenti();
        this.gestisciSuccesso('Luogo aggiunto con successo!');

      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    })
  }

  modificaDatiUtente(datiAggiornati: ModificaDatiUtenteDTO) {
    this.service.modificaDati(datiAggiornati).subscribe({
      next: (res) => {
        if (res) {
          this.caricaDatiAdmin();
        }
        this.gestisciSuccesso('Dati modificati con successo!');

      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }

  caricaDatiAdmin() {
    this.service.getDati().subscribe({
      next: (datiRicevuti) => {

        if (datiRicevuti) {
          this.utenteDaModificare = datiRicevuti;
        }
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }

  caricaLuoghiEsistenti() {
    this.service.getLuoghi().subscribe({
      next: value => {
        this.luoghiEsistenti = [...value];
      },
      error: err => {
        this.gestisciErrore(err.error);
      }
    });
  }

  impostaSede(idLuogo: number) {
    this.service.impostaSedeAzienda(idLuogo).subscribe({
      next: value => {
        if (value) {
          this.caricaDatiAdmin();
          this.caricaLuoghiEsistenti();
          this.gestisciSuccesso('Sede impostata con successo!');
        }
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  eliminaLuogo(idLuogo: number) {
    this.service.eliminaLuogo(idLuogo).subscribe({
      next: value => {
        if (value) {
          this.caricaDatiAdmin();
          this.caricaLuoghiEsistenti();
          this.gestisciSuccesso('Luogo eliminato con successo!');

        }
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }


  gestisciErrore(messaggio: string) {
    this.successoBanner = '';
    this.erroreBanner = messaggio;
    setTimeout(() => this.erroreBanner = '', 5000);
  }

  gestisciSuccesso(messaggio: string) {
    this.erroreBanner = '';
    this.successoBanner = messaggio;
    setTimeout(() => this.successoBanner = '', 3000);
  }
}
