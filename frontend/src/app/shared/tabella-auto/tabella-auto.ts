import {Component, inject} from '@angular/core';
import {VeicoloDTO} from '@models/veicoloDTO.model';
import {FlottaGlobaleService} from '@core/services/ServiceSezioneFleetGo/flotta-globale-service';
import {TabellaBackground} from "@shared/tabella-background/tabella-background";
import {AziendaDTO} from '@models/aziendaDTO';
import {FormsModule} from '@angular/forms';
import {AziendeAffiliateService} from '@core/services/ServiceSezioneFleetGo/aziende-affiliate-service';
import {Route, Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-tabella-auto',
  imports: [
    TabellaBackground,
    FormsModule
  ],
  templateUrl: './tabella-auto.html',
  styleUrl: './tabella-auto.css',
})
export class TabellaAuto {

  private service= inject(FlottaGlobaleService);
  private serviceAziende:AziendeAffiliateService = inject(AziendeAffiliateService)
  private route:Router = inject(Router)

  filtroTarga = "";
  filtroModello= "";
  filtroStato = "";
  filtroAzienda = "";

  listaAziendeInPiattaforma: AziendaDTO[] = [];
  listaVeicoli:VeicoloDTO[]=[];

  constructor() {}

  ngOnInit(): void {
    this.aggiornaDati();
    this.caricaAziende();
  }

  aggiornaDati(){
    this.service.richiediVeicoli().subscribe({
      next: (response) => {
        if (response) {
          this.listaVeicoli = response;
          console.log("Dati caricati:", this.listaVeicoli);
        }
        else{
          console.log("Nessun veicolo trovato", this.listaVeicoli);
        }
      },
      error: (err) => {
        console.error("Errore nel caricamento:", err);
      }
    });
  }

  caricaAziende() {
    this.serviceAziende.richiediAziende().subscribe({
      next: (response) => {
        if (response) {
          this.listaAziendeInPiattaforma = response;
        }
      },
      error: (err) => {
        console.error("Errore nel caricamento:", err);
      }
    });
  }

  get veicoliFiltrati(): VeicoloDTO[] {
    return this.listaVeicoli.filter(veicolo => {

      const targa = !this.filtroTarga ||
        (veicolo.targaVeicolo && veicolo.targaVeicolo.toLowerCase().includes(this.filtroTarga.toLowerCase()));

      const modello = !this.filtroModello ||
        (veicolo.modello && veicolo.modello.toLowerCase().includes(this.filtroModello.toLowerCase()));

      const stato = !this.filtroStato ||
        (veicolo.statusCondizioneVeicolo && veicolo.statusCondizioneVeicolo.toLowerCase().includes(this.filtroStato.toLowerCase()));

      const azienda = !this.filtroAzienda ||
        (veicolo.nomeAziendaAssociato && veicolo.nomeAziendaAssociato.toLowerCase().includes(this.filtroAzienda.toLowerCase()));

      return targa && modello && stato && azienda;
    });
  }

  rimuoviVeicolo(targaVeicolo: string) {
    this.service.rimuoviVeicolo(targaVeicolo).subscribe({
      next: (response) => {
        this.aggiornaDati();
      },
      error: (err) => {
        console.error("Errore durante la rimozione:", err);
      }
    })
  }

  apriDettaglioVeicolo(targaVeicolo: string) {
    this.route.navigate(["/dashboardFleetGo","dettagli-veicolo",targaVeicolo])

  }
}
