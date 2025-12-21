import { Component, ViewChild, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CommonModule } from '@angular/common';
import { TabellaAziendeComponent } from '@features/SezioneFleetGo/Componenti/tabella-aziende/tabella-aziende';
import { FormAggiungiAdminAzienda } from '@features/SezioneFleetGo/Componenti/form-aggiungi-admin-azienda/form-aggiungi-admin-azienda';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo'; // <--- AGGIUNTO PER GRAFICA
import { AziendeAffiliateService } from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import { AziendaDTO } from '@core/models/aziendaDTO';

@Component({
  selector: 'app-aziende-affiliate',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule,             
    TabellaAziendeComponent,
    FormAggiungiAdminAzienda,
    TemplateTitoloSottotitolo 
  ],
  templateUrl: './aziende-affiliate.html',
  styleUrl: './aziende-affiliate.css',
})
export class AziendeAffiliate implements OnInit {

  testoRicerca: string = '';
  aziendeOriginali: AziendaDTO[] = [];

  mostraModale = false;

  apriModaleAggiunta() {
    this.mostraModale = true;
  }

  chiudiModale() {
    this.mostraModale = false;
  }

  filtraAziende() {
    if (!this.testoRicerca || this.testoRicerca.trim() === '') {
      this.listaAziende = this.aziendeOriginali;
    } else {
      const term = this.testoRicerca.toLowerCase();
      this.listaAziende = this.aziendeOriginali.filter(az => 
      (az.nomeAzienda && az.nomeAzienda.toLowerCase().includes(term)) ||
      (az.pIva && az.pIva.toLowerCase().includes(term))
      );
    }
  }


  @ViewChild('tabellaAziende') tabella!: TabellaAziendeComponent;
  @ViewChild('formAggiungiAdminAzienda') formAggiunta!: FormAggiungiAdminAzienda;

  listaAziende: AziendaDTO[] = [];

  constructor(private aziendeService: AziendeAffiliateService) {}

  ngOnInit() {
    this.aggiornaDati();
  }

  onAziendaAggiunta(mod: any) {
    this.aziendeService.registraAzienda(mod.adminAziendale, mod.azienda).subscribe({
      next: (res) => {
        console.log("Azienda registrata!", res);
        
        this.aggiornaDati();
        this.chiudiModale(); 
        
        if(this.formAggiunta) this.formAggiunta.pulisciForm();
      },
      error: (err) => {
        console.error("Errore", err);
        alert("Errore durante il salvataggio: " + (err.error?.message || "Errore sconosciuto"));
      }
    });
  }

  aggiornaDati() {
    this.aziendeService.richiediAziende().subscribe({
      next: (data) => {
        console.log("ho ricevuto");
        if (data) {
          this.aziendeOriginali = data;
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
    if(!idAdmin) return;
    
    if(confirm("Sei sicuro di voler eliminare questa azienda?")) {
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
}