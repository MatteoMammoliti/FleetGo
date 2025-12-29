import {Component, OnInit} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TabellaAuto} from '@features/SezioneFleetGo/Componenti/tabella-auto/tabella-auto';
import {FormAggiungiAuto} from '@features/SezioneFleetGo/Componenti/form-aggiungi-auto/form-aggiungi-auto';
import {FlottaGlobaleService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/flotta-globale-service';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {Router} from '@angular/router';
import { CommonModule } from '@angular/common';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {ModelloDTO} from '@core/models/ModelloDTO';
import {FormAggiungiModello} from '@features/SezioneFleetGo/Componenti/form-aggiungi-modello/form-aggiungi-modello';
import {CardModello} from '@features/SezioneFleetGo/Componenti/card-modello/card-modello';
import {AziendeAffiliateService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import {BannerErrore} from "@shared/Componenti/Ui/banner-errore/banner-errore";

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
        TemplateTitoloSottotitolo,
        FormAggiungiModello,
        CardModello,
        BannerErrore
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
        console.error("Errore nel caricamento:", err);
        this.erroreBanner="Errore nel caricamento dei veicoli"
      }
    });
  }

  caricaModelli() {
    this.service.richiediModelli().subscribe({
      next: (datiDalServer) => {
        if(datiDalServer) {
          console.log(datiDalServer);
          this.listaModelli = datiDalServer;
        }
      }, error: (err) => {
        console.error("Errore nel caricamento:", err);
        this.erroreBanner=("Errore nel caricamento dei modelli");
      }
    })
  }

  caricaAziende() {
    this.aziendeService.richiediAziendeAttive().subscribe({
      next: (datiDalServer) => {
        if(datiDalServer) this.aziendeInPiattaforma = datiDalServer;
      }, error: (err) => {
        console.error("Errore nel caricamento:", err);
        this.erroreBanner=("Errore nel caricamento delle aziende attive");
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
  gestisciVisibilitaModaleInserimentoModello() { this.mostraModaleInserimentoModello = !this.mostraModaleInserimentoModello; }

  gestisciSalvataggio(veicolo: VeicoloDTO) {
    this.service.registraVeicolo(veicolo).subscribe({
      next: (response) => {
        console.log("Veicolo salvato con successo:", response);
        this.caricaDati();
        this.gestisciVisibilitaModale();
        this.resettaFiltri();
      },
      error: (err) => {
        console.error("Errore durante il salvataggio del veicolo:", err);
        this.erroreBanner=("Errore durante il salvataggio del veicolo targato: " + veicolo.targaVeicolo);
      }
    });
  }

  gestisciEliminazione(targaVeicolo: string) {
    this.service.rimuoviVeicolo(targaVeicolo).subscribe({
      next: (response) => {
        console.log("Veicolo eliminato con successo:", response);
        this.caricaDati();
      },
      error: (err) => {
        console.error("Errore durante l'eliminazione del veicolo:", err);
        this.erroreBanner=("Errore durante l'eliminazione del veicolo targato: " + targaVeicolo);
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
      }, error: (err) => {
        console.log(err);
        this.erroreBanner=("Errore durante la registrazione del modello.");
      }
    })
  }

  eliminaModello(idModello: number) {
    this.service.eliminaModello(idModello).subscribe({
      next: (response) => {
        if(response) {
          this.caricaModelli();
        }
      }, error: (err) => {
        console.log(err);
        this.erroreBanner=("Errore durante l'eliminazione del modello "+ idModello);
      }
    })
  }
}
