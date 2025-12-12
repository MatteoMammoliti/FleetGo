import {Component, OnInit, inject} from '@angular/core';
import {Form, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TabellaAuto} from '@features/SezioneFleetGo/Componenti/tabella-auto/tabella-auto';
import {FormAggiungiAuto} from '@features/SezioneFleetGo/Componenti/form-aggiungi-auto/form-aggiungi-auto';
import {FlottaGlobaleService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/flotta-globale-service';
import {VeicoloDTO} from '@core/models/veicoloDTO.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-flotta-globale',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    TabellaAuto,
    FormAggiungiAuto
  ],
  templateUrl: './flotta-globale.html',
  styleUrl: './flotta-globale.css',
})

export class FlottaGlobale implements OnInit{
  private service= inject(FlottaGlobaleService);
  private route = inject(Router);
  listaVeicoli:VeicoloDTO[]=[];

  ngOnInit(): void {
    this.caricaDati();
  }

  caricaDati() {
    this.service.richiediVeicoli().subscribe({
      next: (datiDalServer) => {
        this.listaVeicoli = datiDalServer;
      },
      error: (err) => {
        console.error("Errore nel caricamento:", err);
      }
    });
  }

  gestisciSalvataggio(dati: FormData) {
    this.service.registraVeicolo(dati).subscribe({
      next: (response) => {
        console.log("Veicolo salvato con successo:", response);
        this.caricaDati();
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
