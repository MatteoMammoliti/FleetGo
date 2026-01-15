import {Component, OnInit, ViewChild} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CommonModule} from '@angular/common';
import {TabellaAziendeComponent} from '@features/SezioneFleetGo/Componenti/tabelle/tabella-aziende/tabella-aziende';
import {
  FormAggiungiAdminAzienda
} from '@features/SezioneFleetGo/Componenti/modali/form-aggiungi-admin-azienda/form-aggiungi-admin-azienda';
import {AziendeAffiliateService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AziendaDTO} from '@core/models/AziendaDTO';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';
import {IntestazioneEBackground} from '@shared/Componenti/IntestazionePagina/intestazione-ebackground/intestazione-ebackground';
import {ContenitoreDatiRegistrazioneAzienda} from '@core/models/ContenitoreDatiRegistrazioneAzienda';

@Component({
  selector: 'app-aziende-affiliate',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    TabellaAziendeComponent,
    FormAggiungiAdminAzienda,
    BannerErrore,
    IntestazioneEBackground
  ],
  templateUrl: './aziende-affiliate.html',
  styleUrl: './aziende-affiliate.css',
})

export class AziendeAffiliate implements OnInit {

  constructor(private aziendeService: AziendeAffiliateService) {
  }

  @ViewChild('tabellaAziende') tabella!: TabellaAziendeComponent;
  @ViewChild('formAggiungiAdminAzienda') formAggiunta!: FormAggiungiAdminAzienda;

  testoRicerca: string = '';
  listaAziendeAttive: AziendaDTO[] | null = null;
  listaAziendeDisabilitate: AziendaDTO[] | null = null;

  mostraModale = false;
  mostraAziendeArchiviate = false

  erroreBanner = "";
  successoBanner = "";



  ngOnInit() {
    this.richiediAziendeAttive();
  }

  gestisciVisibilitaModaleAggiuntaAziende() {
    this.mostraModale = !this.mostraModale;
  }

  impostaVista(stato: boolean) {

    this.mostraAziendeArchiviate = stato

    if (this.mostraAziendeArchiviate) {
      this.richiediAziendeDisabilitate();
    }
  }

  get filtraAziende() {
    const listaTarget = !this.mostraAziendeArchiviate ? this.listaAziendeAttive : this.listaAziendeDisabilitate
    if (!listaTarget) {
      return null;
    }
    if (!this.testoRicerca || this.testoRicerca.trim() === '') {
      return !this.mostraAziendeArchiviate ? this.listaAziendeAttive : this.listaAziendeDisabilitate;
    }


    return listaTarget.filter(azienda => {

      const nomeAzienda = (azienda.nomeAzienda && azienda.nomeAzienda.toLowerCase().includes(this.testoRicerca.toLowerCase()));
      const partitaIva = (azienda.pIva && azienda.pIva.toLowerCase().includes(this.testoRicerca.toLowerCase()));
      return nomeAzienda || partitaIva;

    });
  }

  onAziendaAggiunta(contenitore: ContenitoreDatiRegistrazioneAzienda) {
    this.aziendeService.registraAzienda(contenitore).subscribe({
      next: (res) => {
        this.richiediAziendeAttive();
        if (this.formAggiunta) this.formAggiunta.pulisciForm();
        this.gestisciSuccesso("Azienda aggiunta con successo");
      },
      error: (err) => {
        this.gestisciErrore(err.error);

      }
    });
  }

  richiediAziendeAttive() {
    this.aziendeService.richiediAziendeAttive().subscribe({
      next: (data) => {
        if (data) {
          this.listaAziendeAttive = data;
        }
      }, error: (err) => {
        this.gestisciErrore(err.error);

      }
    });
  }

  richiediAziendeDisabilitate() {
    this.aziendeService.richiediAziendeDisabilitate().subscribe({
      next: (data) => {
        if (data) this.listaAziendeDisabilitate = data;
      }, error: (err) => {
        this.gestisciErrore(err.error);

      }
    })
  }

  disabilitaAzienda(idAzienda: number) {
    this.aziendeService.disabilitaAzienda(idAzienda).subscribe({
      next: (res) => {
        if (res) {
          this.richiediAziendeAttive();
        }
        this.gestisciSuccesso("Azienda disabilitata con successo");
      }, error: (err) => {
        this.gestisciErrore(err.error);

      }
    });
  }

  riabilitaAzienda(idAzienda: number) {
    this.aziendeService.riabilitaAzienda(idAzienda).subscribe({
      next: (res) => {
        if (res) {
          this.richiediAziendeAttive();
          this.richiediAziendeDisabilitate();
        }
        this.gestisciSuccesso("Azienda riabilitata con successo");
      }, error: (err) => {
        this.gestisciErrore(err.error);
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

