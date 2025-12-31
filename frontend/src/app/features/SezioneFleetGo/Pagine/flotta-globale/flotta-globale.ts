import {Component, OnInit} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TabellaAuto} from '@features/SezioneFleetGo/Componenti/tabelle/tabella-auto/tabella-auto';
import {FormAggiungiAuto} from '@features/SezioneFleetGo/Componenti/modali/form-aggiungi-auto/form-aggiungi-auto';
import {FlottaGlobaleService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/flotta-globale-service';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {Router} from '@angular/router';
import { CommonModule } from '@angular/common';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {ModelloDTO} from '@core/models/ModelloDTO';
import {FormAggiungiModello} from '@features/SezioneFleetGo/Componenti/modali/form-aggiungi-modello/form-aggiungi-modello';
import {CardModello} from '@features/SezioneFleetGo/Componenti/card/card-modello/card-modello';
import {AziendeAffiliateService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import {BannerErrore} from "@shared/Componenti/Ui/banner-errore/banner-errore";
import {IntestazioneEBackground} from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';

@Component({
  selector: 'app-flotta-globale',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    TabellaAuto,
    FormAggiungiAuto,
    CommonModule,
    FormsModule,
    FormAggiungiModello,
    CardModello,
    BannerErrore,
    IntestazioneEBackground,
    TemplateFinestraModale
  ],
  templateUrl: './flotta-globale.html',
  styleUrl: './flotta-globale.css',
})

export class FlottaGlobale implements OnInit{

  constructor(private service: FlottaGlobaleService,
              private route: Router,
              private aziendeService: AziendeAffiliateService) {}

  veicoliOriginali: VeicoloDTO[] = [];

  testoRicerca: string = '';
  filtroAzienda: AziendaDTO | null = null;
  filtroStatoVeicolo: string = '';
  aziendeInPiattaforma: AziendaDTO[] = []

  mostraModale: boolean = false;
  mostraModaleInserimentoModello = false;

  listaModelli: ModelloDTO[] = [];
  erroreBanner="";
  successoBanner="";
  tendina: boolean=false;
  icona='bi-arrow-down-short';

  modaleCheck=false;
  modelloInteressato:any;

  ngOnInit(): void {
    this.resettaFiltri()
    this.caricaDati();
    this.caricaModelli();
    this.caricaAziende();
  }

  caricaDati() {
    this.service.richiediVeicoli().subscribe({
      next: (datiDalServer) => {
        this.veicoliOriginali = datiDalServer;
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }

  caricaModelli() {
    this.service.richiediModelli().subscribe({
      next: (datiDalServer) => {
        if(datiDalServer) {
          this.listaModelli = datiDalServer;
        }
      }, error: (err) => {
        this.gestisciErrore(err.error);
      }
    })
  }

  caricaAziende() {
    this.aziendeService.richiediAziendeAttive().subscribe({
      next: (datiDalServer) => {
        if(datiDalServer) this.aziendeInPiattaforma = datiDalServer;
      }, error: (err) => {
        this.gestisciErrore(err.error);
      }
    })
  }

  get veicoliFiltrati() {
    return this.veicoliOriginali.filter(veicolo => {

      let matchAzienda = true;

      if(this.filtroAzienda && this.filtroAzienda.idAzienda) {
        matchAzienda = veicolo.idAziendaAffiliata == this.filtroAzienda.idAzienda;
      }

      let matchStato = true;
      if (this.filtroStatoVeicolo && this.filtroStatoVeicolo !== '') {
        if (this.filtroStatoVeicolo === 'MANUTENZIONE') {
          matchStato = veicolo.inManutenzione || veicolo.statusContrattualeVeicolo === 'MANUTENZIONE';
        } else {
          matchStato = veicolo.statusContrattualeVeicolo?.toLowerCase() === this.filtroStatoVeicolo.toLowerCase();
        }
      }

      const matchRicerca =
          (veicolo.targaVeicolo?.toLowerCase().includes(this.testoRicerca.toLowerCase()) ?? false) ||
        (veicolo.nomeModello?.toLowerCase().includes(this.testoRicerca.toLowerCase()) ?? false);

      return matchAzienda && matchStato && matchRicerca;
    });
  }

  gestisciVisibilitaModale() { this.mostraModale = !this.mostraModale; }
  gestisciVisibilitaModaleInserimentoModello() { this.mostraModaleInserimentoModello = !this.mostraModaleInserimentoModello;}

  gestisciSalvataggio(veicolo: VeicoloDTO) {
    this.service.registraVeicolo(veicolo).subscribe({
      next: (response) => {
        this.caricaDati();
        this.gestisciVisibilitaModale();
        this.resettaFiltri();
        this.gestisciSuccesso("Veicolo registrato con successo");
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }


  gestisciEliminazione(targaVeicolo: string) {
    this.service.rimuoviVeicolo(targaVeicolo).subscribe({
      next: (response) => {
        this.caricaDati();
        this.gestisciSuccesso("Veicolo rimosso con successo");
      },
      error: (err) => {
        this.gestisciErrore(err.error);
      }
    });
  }

  dettagliVeicolo(targaVeicolo: string) {
    this.route.navigate(["/dashboardFleetGo", "dettagli-veicolo", targaVeicolo]);
  }

  resettaFiltri() {
    this.filtroAzienda = null;
    this.filtroStatoVeicolo = "";
    this.testoRicerca = "";
  }

  aggiungiModello(formData: FormData) {
    this.service.registraModello(formData).subscribe({
      next: (response) => {
        if(response) {
          this.caricaModelli();
          this.gestisciVisibilitaModaleInserimentoModello();
        }
        this.gestisciSuccesso("Modello registrato con successo");
      }, error: (err) => {
        this.gestisciErrore(err.error);
      }
    })
  }

  eliminaModello(idModello: number) {
    this.service.eliminaModello(idModello).subscribe({
      next: (response) => {
        if(response) {
          this.caricaModelli();
        }
        this.gestisciSuccesso("Modello eliminato con successo");
      }, error: (err) => {
        this.gestisciErrore(err.error);
      }
    })
  }
  gestisciTendina(){
    this.tendina=!this.tendina
    if(this.tendina){
      this.icona='bi-arrow-up-short';
    }
    else {
      this.icona='bi-arrow-down-short';
    }
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

  apriModaleCheck(idModelloInteressato:number){
    this.modelloInteressato=idModelloInteressato;
    this.modaleCheck=true;
  }

  confermaModale() {
    this.modaleCheck=false;
    if(this.modelloInteressato!=null){
      this.eliminaModello(this.modelloInteressato)
    }
    this.modelloInteressato=null;
  }

  chiudiModale() {
    this.modaleCheck=false;
    this.modelloInteressato=null;
  }
}
