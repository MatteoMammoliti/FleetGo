import {Component, SimpleChanges, ViewChild} from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TabellaAziendeComponent } from '@features/SezioneFleetGo/Componenti/tabella-aziende/tabella-aziende';
import {FormAggiungiAdminAzienda} from '@features/SezioneFleetGo/Componenti/form-aggiungi-admin-azienda/form-aggiungi-admin-azienda';
import {AziendeAffiliateService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AdminAziendaleDTO} from '@core/models/adminAziendaleDTO.models';
import {AziendaDTO} from '@core/models/aziendaDTO';

@Component({
  selector: 'app-aziende-affiliate',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    TabellaAziendeComponent,
    FormAggiungiAdminAzienda
  ],
  templateUrl: './aziende-affiliate.html',
  styleUrl: './aziende-affiliate.css',
})

export class AziendeAffiliate {
  constructor(private aziendeService: AziendeAffiliateService) {
  }

  @ViewChild('tabellaAziende') tabella!: TabellaAziendeComponent;

  @ViewChild('formAggiungiAdminAzienda') formAggiunta!: FormAggiungiAdminAzienda;

  ngOnInit() {
    this.aggiornaDati();
  }





  listaAziende:AziendaDTO[]=[];

  onAziendaAggiunta(mod:any) {
    this.aziendeService.registraAzienda(mod.adminAziendale, mod.azienda).subscribe({
      next: (res) => {
        console.log("Azienda registrata!", res);
        this.aggiornaDati();
        this.formAggiunta.pulisciForm();
      },
      error: (err) => {
        console.error("Errore", err);
      }
    });
  }


  aggiornaDati() {
    this.aziendeService.richiediAziende().subscribe({
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
    console.log("sono in elimina padre" + idAdmin);
    this.aziendeService.eliminaAzienda(idAdmin).subscribe({
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
