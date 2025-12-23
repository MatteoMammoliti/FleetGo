import {Component, OnInit, inject} from '@angular/core';
import {Form, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TabellaAuto} from '@features/SezioneFleetGo/Componenti/tabella-auto/tabella-auto';
import {FormAggiungiAuto} from '@features/SezioneFleetGo/Componenti/form-aggiungi-auto/form-aggiungi-auto';
import {FlottaGlobaleService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/flotta-globale-service';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {Router} from '@angular/router';
import { CommonModule } from '@angular/common';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';

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
  private service= inject(FlottaGlobaleService);
  private route = inject(Router);

  veicoliOriginali: VeicoloDTO[] = [];
  listaVeicoli:VeicoloDTO[]=[];
  testoRicerca: string = '';
  mostraModale: boolean = false;

  ngOnInit(): void {
    this.caricaDati();
  }

  caricaDati() {
    this.service.richiediVeicoli().subscribe({
      next: (datiDalServer) => {
        this.veicoliOriginali = datiDalServer;
        this.filtraVeicoli();
      },
      error: (err) => {
        console.error("Errore nel caricamento:", err);
      }
    });
  }

  filtraVeicoli() {
    if (!this.testoRicerca || this.testoRicerca.trim() === '') {
      this.listaVeicoli = this.veicoliOriginali;
    } else {
      const term = this.testoRicerca.toLowerCase();
      this.listaVeicoli = this.veicoliOriginali.filter(v => 
        (v.targaVeicolo && v.targaVeicolo.toLowerCase().includes(term)) ||
        (v.modello && v.modello.toLowerCase().includes(term)) ||
        (v.nomeAziendaAffiliata && v.nomeAziendaAffiliata.toLowerCase().includes(term))
      );
    }
  }

  apriModale() {
    this.mostraModale = true;
  }
  
  chiudiModale() {
    this.mostraModale = false;
  } 

  gestisciSalvataggio(dati: FormData) {
    this.service.registraVeicolo(dati).subscribe({
      next: (response) => {
        console.log("Veicolo salvato con successo:", response);
        this.caricaDati();
        this.chiudiModale();
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
}
