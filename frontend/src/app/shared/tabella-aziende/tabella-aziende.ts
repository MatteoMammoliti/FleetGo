import {Component, inject} from '@angular/core';
import {TabellaBackground} from '@shared/tabella-background/tabella-background';
import {AziendeAffiliateService} from '@core/services/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AziendaDTO} from '@models/aziendaDTO';

@Component({
  selector: 'app-tabella-aziende',
  standalone: true,
  imports: [
    TabellaBackground
  ],
  templateUrl: './tabella-aziende.html',
  styleUrl: './tabella-aziende.css'
})
export class TabellaAziendeComponent {

  private service: AziendeAffiliateService = inject(AziendeAffiliateService);
  constructor() {}

  listaAziende:AziendaDTO[] = [];

  ngOnInit(): void {
    this.aggiornaDati();
  }

  aggiornaDati() {
    this.service.richiediAziende().subscribe({
      next: (data) => {
        console.log("ho ricevuto")
        if(data) {
          this.listaAziende = data;
          console.log("Dati caricati:", this.listaAziende);
        }
      },
      error: (err) => {
        console.error("Errore nel caricamento:", err);
      }
    });
  }

  elimina(idAdmin: number | undefined) {
    this.service.eliminaAzienda(idAdmin).subscribe({
      next: (data) => {
        console.log(data);
        this.aggiornaDati();
      },
      error: (err) => {
        console.error("Errore nell'eliminazione:", err);
      }
    });
  }
}
