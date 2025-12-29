import { Component, ViewChild, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { TabellaAziendeComponent } from '@features/SezioneFleetGo/Componenti/tabella-aziende/tabella-aziende';
import { FormAggiungiAdminAzienda } from '@features/SezioneFleetGo/Componenti/form-aggiungi-admin-azienda/form-aggiungi-admin-azienda';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import { AziendeAffiliateService } from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import { AziendaDTO } from '@core/models/aziendaDTO';
import {BannerErrore} from '@shared/Componenti/Ui/banner-errore/banner-errore';

@Component({
  selector: 'app-aziende-affiliate',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    TabellaAziendeComponent,
    FormAggiungiAdminAzienda,
    TemplateTitoloSottotitolo,
    BannerErrore
  ],
  templateUrl: './aziende-affiliate.html',
  styleUrl: './aziende-affiliate.css',
})

export class AziendeAffiliate implements OnInit {

  @ViewChild('tabellaAziende') tabella!: TabellaAziendeComponent;
  @ViewChild('formAggiungiAdminAzienda') formAggiunta!: FormAggiungiAdminAzienda;

  testoRicerca: string = '';
  listaAziendeAttive: AziendaDTO[] = [];
  listaAziendeDisabilitate: AziendaDTO[] = [];

  mostraModale = false;
  mostraAziendeArchiviate = false

  erroreBanner = "";

  constructor(private aziendeService: AziendeAffiliateService) {}

  ngOnInit() { this.richiediAziendeAttive(); }

  gestisciVisibilitaModaleAggiuntaAziende() { this.mostraModale = !this.mostraModale; }

  impostaVista(stato: boolean) {

    this.mostraAziendeArchiviate = stato

    if(this.mostraAziendeArchiviate) {
      this.richiediAziendeDisabilitate();
    }
  }

  get filtraAziende() {

    if (!this.testoRicerca || this.testoRicerca.trim() === '') {
      return !this.mostraAziendeArchiviate ? this.listaAziendeAttive : this.listaAziendeDisabilitate;
    }

    const listaTarget = !this.mostraAziendeArchiviate ? this.listaAziendeAttive : this.listaAziendeDisabilitate

    return listaTarget.filter(azienda => {

      const nomeAzienda = (azienda.nomeAzienda && azienda.nomeAzienda.toLowerCase().includes(this.testoRicerca.toLowerCase()));
      const partitaIva = (azienda.pIva && azienda.pIva.toLowerCase().includes(this.testoRicerca.toLowerCase()));
      return nomeAzienda || partitaIva;

    });
  }

  onAziendaAggiunta(mod: any) {
    this.aziendeService.registraAzienda(mod.adminAziendale, mod.azienda).subscribe({
      next: (res) => {
        this.richiediAziendeAttive();
        this.gestisciVisibilitaModaleAggiuntaAziende();
        if(this.formAggiunta) this.formAggiunta.pulisciForm();
      },
      error: (err) => {
        console.error("Errore", err);
        this.erroreBanner = "Non è stato possibile aggiungere l'azienda";
      }
    });
  }

  richiediAziendeAttive() {
    this.aziendeService.richiediAziendeAttive().subscribe({
      next: (data) => {
        if (data) { this.listaAziendeAttive = data; }
      }, error: (err) => {
        this.erroreBanner = "Non è stato possibile caricare le aziende attive.";
      }
    });
  }

  richiediAziendeDisabilitate() {
    this.aziendeService.richiediAziendeDisabilitate().subscribe({
      next: (data) => {
        if(data) this.listaAziendeDisabilitate = data;
      }, error: (err) => {
        this.erroreBanner = "Non è stato possibile caricare le aziende disabilitate.";
      }
    })
  }

  disabilitaAzienda(idAzienda: number) {
    this.aziendeService.disabilitaAzienda(idAzienda).subscribe({
      next: (res) => {
        if(res) {
          this.richiediAziendeAttive();
        }
      }, error: (err) => {
          this.erroreBanner = "Non è stato possibile disabilitare l'azienda " + idAzienda;
      }
    });
  }

  riabilitaAzienda(idAzienda: number) {
    this.aziendeService.riabilitaAzienda(idAzienda).subscribe({
      next: (res) => {
        if(res) {
          this.richiediAziendeAttive();
          this.richiediAziendeDisabilitate();
        }
      }, error: (err) => {
        this.erroreBanner = "Non è stato possibile riabilitare l'azienda " + idAzienda;
      }
    });
  }
}
