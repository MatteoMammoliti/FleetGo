import {Component, OnInit, inject} from '@angular/core';
import {Form, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TabellaAuto} from '@features/SezioneFleetGo/Componenti/tabella-auto/tabella-auto';
import {FormAggiungiAuto} from '@features/SezioneFleetGo/Componenti/form-aggiungi-auto/form-aggiungi-auto';
import {FlottaGlobaleService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/flotta-globale-service';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {Router} from '@angular/router';
import { CommonModule } from '@angular/common';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import {AziendaCard} from '@features/DipendenteSenzaAzienda/Componenti/azienda-card/azienda-card';
import {AziendaDTO} from '@core/models/aziendaDTO';

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
    TemplateTitoloSottotitolo
  ],
  templateUrl: './flotta-globale.html',
  styleUrl: './flotta-globale.css',
})

export class FlottaGlobale implements OnInit{

  constructor(private service: FlottaGlobaleService, private route: Router) {}

  veicoliOriginali: VeicoloDTO[] = [];

  testoRicerca: string = '';
  filtroAzienda: AziendaDTO | null = null;
  filtroStatoVeicolo: string = '';
  aziendeInPiattaforma: AziendaDTO[] = []

  mostraModale: boolean = false;

  ngOnInit(): void {
    this.caricaDati();
    this.caricaAziende();
  }

  caricaDati() {
    this.service.richiediVeicoli().subscribe({
      next: (datiDalServer) => {
        this.veicoliOriginali = datiDalServer;
      },
      error: (err) => {
        console.error("Errore nel caricamento:", err);
      }
    });
  }

  caricaAziende() {
    this.service.richiediAziende().subscribe({
      next: (datiDalServer) => {
        if(datiDalServer) this.aziendeInPiattaforma = datiDalServer;
      }, error: (err) => {
        console.error("Errore nel caricamento:", err);
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
        (veicolo.modello?.toLowerCase().includes(this.testoRicerca.toLowerCase()) ?? false);

      return matchAzienda && matchStato && matchRicerca;
    });
  }

  gestisciVisibilitaModale() { this.mostraModale = !this.mostraModale; }

  gestisciSalvataggio(dati: FormData) {
    this.service.registraVeicolo(dati).subscribe({
      next: (response) => {
        console.log("Veicolo salvato con successo:", response);
        this.caricaDati();
        this.gestisciVisibilitaModale();
      },
      error: (err) => {
        console.error("Errore durante il salvataggio del veicolo:", err);
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
      }
    });
  }

  dettagliVeicolo(targaVeicolo: string) {
    this.route.navigate(["/dashboardFleetGo", "dettagli-veicolo", targaVeicolo]);
  }

  resettaFiltri() {
    this.filtroAzienda = {} as AziendaDTO;
    this.filtroStatoVeicolo = "";
    this.testoRicerca = "";
  }
}
