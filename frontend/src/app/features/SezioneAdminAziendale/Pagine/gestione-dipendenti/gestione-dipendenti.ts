import {Component, OnInit} from '@angular/core';
import { ElencoDipendenti } from '@features/SezioneAdminAziendale/Componenti/elenco-dipendenti/elenco-dipendenti';
import {DipendenteDTO} from '@core/models/dipendenteDTO.models';
import {DipendentiService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dipendenti-service';
import {ModaleDettagliDipendente} from '@features/SezioneAdminAziendale/Componenti/modali/modale-dettagli-dipendente/modale-dettagli-dipendente';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {BannerRichiesteAffiliazione} from '@features/SezioneAdminAziendale/Componenti/banner-richieste-affiliazione/banner-richieste-affiliazione';
import {RichiestaAffiliazioneAziendaDTO} from '@core/models/RichiestaAffiliazioneAziendaDTO.models';
import {ModaleRichiesteAffiliazione} from '@features/SezioneAdminAziendale/Componenti/modali/modale-richieste-affiliazione/modale-richieste-affiliazione';
import {TemplateTitoloSottotitolo} from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';

@Component({
  selector: 'app-gestione-dipendenti',
  standalone: true,
  imports: [ElencoDipendenti, ModaleDettagliDipendente, BannerRichiesteAffiliazione, ModaleRichiesteAffiliazione, TemplateTitoloSottotitolo, TemplateFinestraModale],
  templateUrl: './gestione-dipendenti.html',
  styleUrl: './gestione-dipendenti.css'
})

export class GestioneDipendentiComponent implements OnInit{

  constructor(private service: DipendentiService) { }

  listaDipendentiAzienda: DipendenteDTO[] = [];
  richiesteNoleggio: RichiestaNoleggioDTO[] = [];
  richiesteAffiliazione: RichiestaAffiliazioneAziendaDTO[] = [];

  modaleDettaglioDipendenteVisibile = false;
  dipendenteDaVisualizzare: DipendenteDTO = {} as DipendenteDTO;

  modaleRichiesteAffiliazione = false;
  OnRimuovi: boolean=false;
  idDipendenteDaRimuovere:any= null;

  ngOnInit() {
    this.getDipendenti();
    this.getRichiesteAffiliazione();
  }

  getDipendenti() {
    this.service.getDipendenti().subscribe({
      next: (response: DipendenteDTO[]) => {
        this.listaDipendentiAzienda = response;
      },
      error: (error) => {
        console.error('Errore durante il recupero dei dipendenti:', error);
      }
    })
  }

  getRichiesteNoleggio(idDipendente: number | undefined) {

    if(!idDipendente){
      this.richiesteNoleggio = [];
      return;
    }

    this.service.getRichiesteNoleggio(idDipendente).subscribe({
      next: value => {
        if(value) this.richiesteNoleggio = value;
      }, error: err => {
        console.error(err);
        this.richiesteNoleggio = [];
      }
    })
  }

  getRichiesteAffiliazione() {
    this.service.getRichiesteAffiliazione().subscribe({
      next: value => {
        if(value) this.richiesteAffiliazione = value || [];
      }, error: err => { console.error(err); }
    })
  }

  rimuoviDipendente() {
    if(this.idDipendenteDaRimuovere){
      this.service.rimuoviDipendente(this.idDipendenteDaRimuovere).subscribe({
        next: (response) => {
          this.getDipendenti();
          this.chiudiModaleRimuovi();
        },
        error: (error) => {
          console.error('Errore durante la rimozione del dipendente:', error);
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

  accettaRichiestaAffiliazione(idDipendente: number){
    this.service.rispondiRichiesta(idDipendente, true).subscribe({
      next: value => {
        if(value) {
          this.getRichiesteAffiliazione();
          this.getDipendenti();
        }
      }, error: error => {console.error(error); }
    })
  }

  rifiutaRichiestaAffiliazione(idDipendente: number){
    this.service.rispondiRichiesta(idDipendente, false).subscribe({
      next: value => {
        if(value) {
          this.getRichiesteAffiliazione();
          this.getDipendenti();
        }
      }, error: error => {console.error(error); }
    })
  }

  apriModaleRichiesteAffiliazione() {
    this.modaleRichiesteAffiliazione = true;
    this.getRichiesteAffiliazione();
  }

  chiudiModaleRichiestaAffiliazione() {
    this.modaleRichiesteAffiliazione = false;
  }

  apriModaleRimuovi(idDipendente:number ) {
    this.idDipendenteDaRimuovere=idDipendente;
    this.OnRimuovi = true;
  }
  chiudiModaleRimuovi() {
    this.idDipendenteDaRimuovere=null;
    this.OnRimuovi = false;
  }
}
