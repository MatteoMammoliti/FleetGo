import {Component, OnInit} from '@angular/core';
import {ElencoDipendenti} from '@features/SezioneAdminAziendale/Componenti/tabelle/elenco-dipendenti/elenco-dipendenti';
import {DipendenteDTO} from '@core/models/DipendenteDTO';
import {DipendentiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dipendenti-service';
import {
  ModaleDettagliDipendente
} from '@features/SezioneAdminAziendale/Componenti/modali/modale-dettagli-dipendente/modale-dettagli-dipendente';
import {RichiestaNoleggioDTO} from '@core/models/RichiestaNoleggioDTO';
import {
  BannerRichiesteAffiliazione
} from '@features/SezioneAdminAziendale/Componenti/banner/banner-richieste-affiliazione/banner-richieste-affiliazione';
import {RichiestaAffiliazioneAziendaDTO} from '@core/models/RichiestaAffiliazioneAziendaDTO';
import {
  ModaleRichiesteAffiliazione
} from '@features/SezioneAdminAziendale/Componenti/modali/modale-richieste-affiliazione/modale-richieste-affiliazione';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/IntestazionePagina/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';
import {BannerErrore} from "@shared/Componenti/Banner/banner-errore/banner-errore";

@Component({
  selector: 'app-gestione-dipendenti',
  standalone: true,
  imports: [ElencoDipendenti, ModaleDettagliDipendente, BannerRichiesteAffiliazione, ModaleRichiesteAffiliazione, TemplateTitoloSottotitolo, TemplateFinestraModale, BannerErrore],
  templateUrl: './gestione-dipendenti.html',
  styleUrl: './gestione-dipendenti.css'
})

export class GestioneDipendentiComponent implements OnInit {


  constructor(private service: DipendentiService) {
  }

  listaDipendentiAzienda: DipendenteDTO[] | null = null;
  richiesteNoleggio: RichiestaNoleggioDTO[] | null = null;
  richiesteAffiliazione: RichiestaAffiliazioneAziendaDTO[] = [];

  modaleDettaglioDipendenteVisibile = false;
  dipendenteDaVisualizzare: DipendenteDTO = {} as DipendenteDTO;

  modaleRichiesteAffiliazione = false;
  OnRimuoviDipendente: boolean = false;
  idDipendenteDaRimuovere: any = null;

  onRifiutaRichiesta: boolean = false;

  erroreBanner = "";
  successoBanner = "";


  ngOnInit() {
    this.getDipendenti();
    this.getRichiesteAffiliazione();
  }

  getDipendenti() {
    this.service.getDipendenti().subscribe({
      next: (response: DipendenteDTO[]) => {
        this.listaDipendentiAzienda = response;
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    })
  }

  getRichiesteNoleggio(idDipendente: number | undefined) {

    if (!idDipendente) {
      this.richiesteNoleggio = [];
      return;
    }

    this.service.getRichiesteNoleggio(idDipendente).subscribe({
      next: value => {
        if (value) this.richiesteNoleggio = value;
      }, error: err => {
        this.gestisciErrore(err.error);
        this.richiesteNoleggio = [];
      }
    })
  }

  getRichiesteAffiliazione() {
    this.service.getRichiesteAffiliazione().subscribe({
      next: value => {
        if (value) this.richiesteAffiliazione = value || [];
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  rimuoviDipendente() {
    if (this.idDipendenteDaRimuovere) {
      this.service.rimuoviDipendente(this.idDipendenteDaRimuovere).subscribe({
        next: (response) => {
          this.getDipendenti();
          this.chiudiModaleRimuovi();
          this.gestisciSuccesso("Dipendente rimosso con successo!");
        },
        error: (err) => {
          this.gestisciErrore(err.error);
        }
      });
    }
  }

  apriDettagliDipendente(dipendente: DipendenteDTO) {
    this.dipendenteDaVisualizzare = dipendente;
    this.modaleDettaglioDipendenteVisibile = true;
    this.getRichiesteNoleggio(dipendente.idUtente);
  }

  chiudiModale() {
    this.modaleDettaglioDipendenteVisibile = false;
  }

  accettaRichiestaAffiliazione(idDipendente: number) {
    this.service.rispondiRichiesta(idDipendente, true).subscribe({
      next: value => {
        if (value) {
          this.getRichiesteAffiliazione();
          this.getDipendenti();
          this.gestisciSuccesso("Affiliazione accettata con successo!");
        }
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  rifiutaRichiestaAffiliazione() {
    this.service.rispondiRichiesta(this.idDipendenteDaRimuovere, false).subscribe({
      next: value => {
        if (value) {
          this.getRichiesteAffiliazione();
          this.getDipendenti();
          this.chiudiModaleRifiuta()
          this.gestisciSuccesso("Affiliazione rifiutata con successo!");
        }
      }, error: err => {
        this.gestisciErrore(err.error);
      }
    })
  }

  apriModaleRichiesteAffiliazione() {
    this.modaleRichiesteAffiliazione = true;
    this.getRichiesteAffiliazione();
  }

  chiudiModaleRichiestaAffiliazione() {
    this.modaleRichiesteAffiliazione = false;
  }

  apriModaleRimuovi(idDipendente: number) {
    this.idDipendenteDaRimuovere = idDipendente;
    this.OnRimuoviDipendente = true;
  }

  chiudiModaleRimuovi() {
    this.idDipendenteDaRimuovere = null;
    this.OnRimuoviDipendente = false;
  }

  apriModaleRifiuta(idDipendente: number) {
    this.idDipendenteDaRimuovere = idDipendente;
    this.onRifiutaRichiesta = true;
  }

  chiudiModaleRifiuta() {
    this.idDipendenteDaRimuovere = null;
    this.onRifiutaRichiesta = false;
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
