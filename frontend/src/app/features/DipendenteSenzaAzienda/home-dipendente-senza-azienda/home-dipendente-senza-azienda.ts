import {Component} from '@angular/core';
import {AziendaCard} from '@features/DipendenteSenzaAzienda/Componenti/azienda-card/azienda-card';
import {FormsModule} from '@angular/forms';
import {HomeService} from '@features/DipendenteSenzaAzienda/ServiceDipendenteSenzaAzienda/home-service';
import {ContenitoreDatiAzienda} from '@core/models/ContenitoreDatiAzienda';
import {RichiestaAffiliazione} from '@features/DipendenteSenzaAzienda/Componenti/richiesta-affiliazione/richiesta-affiliazione';
import { BannerErrore} from '@shared/Componenti/Ui/banner-errore/banner-errore';
import { MessaggioCardVuota } from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import { CommonModule } from '@angular/common';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-home-dipendente-senza-azienda',
  imports: [
    AziendaCard,
    FormsModule,
    RichiestaAffiliazione,
    BannerErrore,
    CommonModule,
    MessaggioCardVuota,
    TemplateTitoloSottotitolo,
    NgClass
  ],
  templateUrl: './home-dipendente-senza-azienda.html',
  styleUrl: './home-dipendente-senza-azienda.css',
})
export class HomeDipendenteSenzaAzienda {
  constructor(private service:HomeService) {}

  testoFiltro:string="";
  aziende:ContenitoreDatiAzienda[]=[]
  aziendeFiltrate:ContenitoreDatiAzienda[]=[]
  aziendaSelezionata: ContenitoreDatiAzienda={} as ContenitoreDatiAzienda;
  richiestaInAttesa: ContenitoreDatiAzienda | null=null;

  caricamentoDati: boolean = true;
  erroreBanner: string = '';
  successoBanner: string = '';

  ngOnInit(){
    this.getRichiestaInAttesa()
  }

  filtraAziende(){
    if (!this.testoFiltro || this.testoFiltro.trim() === '') {
      this.aziendeFiltrate = this.aziende;
    } else {
      const term = this.testoFiltro.toLowerCase();
      this.aziendeFiltrate = this.aziende.filter(az =>
        az.nomeAzienda.toLowerCase().includes(term)
      );
    }
  }

  getAziende() {
    this.caricamentoDati = true;
    this.service.getAziende().subscribe({
      next: (risposta: ContenitoreDatiAzienda[]) => {
        this.aziende = risposta;
        this.aziendeFiltrate = this.aziende;
        this.caricamentoDati = false;
      },
      error: (err) => {
        this.gestisciErrore("Impossibile caricare la lista aziende.");
        this.caricamentoDati = false;
      }
    });
  }

  getRichiestaInAttesa() {
    this.caricamentoDati = true;
    this.service.getRichiestaAffiliazioneAttesa().subscribe({
      next: (risposta: ContenitoreDatiAzienda | null) => {
        this.richiestaInAttesa = risposta;
        if (this.richiestaInAttesa === null) {
          this.getAziende();
        } else {
          this.caricamentoDati = false;
        }
      },
      error: (err) => {
        this.gestisciErrore("Errore nel recupero dello stato affiliazione.");
        this.caricamentoDati = false;
      }
    });
  }

  selezionaAzienda(azienda: ContenitoreDatiAzienda) {
    this.aziendaSelezionata = azienda;
    if (window.innerWidth < 1024) {
      setTimeout(() => {
        document.getElementById('sidebar-richiesta')?.scrollIntoView({ behavior: 'smooth' });
      }, 100);
    }
  }

  gestisciInvioRichiesta(idAzienda: number) {
    if (!idAzienda) return;
    
    this.caricamentoDati = true;
    this.service.inviaRichiestaAffiliazione(idAzienda).subscribe({
      next: (risposta: any) => {
        this.gestisciSuccesso("Richiesta inviata con successo!");
        this.getRichiestaInAttesa(); 
      },
      error: (err) => {
        this.gestisciErrore("Errore nell'invio della richiesta.");
        this.caricamentoDati = false;
      }
    });
  }

  annullaRichiesta() {
    if (!this.richiestaInAttesa) return;

    this.caricamentoDati = true;
    // @ts-ignore
    this.service.annullaRichiestaInAttesa(this.richiestaInAttesa.idAzienda).subscribe({
      next: (risposta: any) => {
        this.gestisciSuccesso("Richiesta annullata correttamente.");
        this.richiestaInAttesa = null;
        this.getAziende(); 
      },
      error: (err) => {
        this.gestisciErrore("Impossibile annullare la richiesta.");
        this.caricamentoDati = false;
      }
    });
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


