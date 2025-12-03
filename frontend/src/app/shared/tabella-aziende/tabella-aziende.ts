import {Component, inject} from '@angular/core';
import {TabellaBackground} from '@shared/tabella-background/tabella-background';
import {AziendeAffiliateService} from '@core/services/adminFleetGoService/aziende-affiliate-service';
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
    for(const a of this.listaAziende){
      console.log("arrivato id", a.idAzienda)
    }
  }



  aggiornaDati() {
    this.service.richiediAziende().subscribe({
      next: (data) => {

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
