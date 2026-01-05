import {Component, OnInit, ViewChild} from '@angular/core';
import {CardAutoAziendale} from '@shared/Componenti/Ui/card-auto-aziendale/card-auto-aziendale';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {IntestazioneEBackground} from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import { FlottaAdminAziendaleService } from '../../ServiceSezioneAdminAziendale/flotta-aziendale-service';
import { ModaleGestisciVeicolo } from '@features/SezioneAdminAziendale/Componenti/modali/modale-gestisci-veicolo/modale-gestisci-veicolo';
import { FlottaGlobaleService } from '../../../SezioneFleetGo/ServiceSezioneFleetGo/flotta-globale-service';
import { DettagliVeicoloAziendaleService } from '../../ServiceSezioneAdminAziendale/dettagli-veicolo-aziendale-service';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';

@Component({
  selector: 'app-flotta-admin-aziendale',
  standalone: true,
  imports: [
    CardAutoAziendale,
    CommonModule,
    FormsModule,
    IntestazioneEBackground,
    ModaleGestisciVeicolo
  ],

  templateUrl: './flotta-admin-aziendale.html',
  styleUrl: './flotta-admin-aziendale.css',
})



export class FlottaAdminAziendale implements OnInit {

  @ViewChild('mioModale') modale!: ModaleGestisciVeicolo;


  veicoli: any[] = [];

  testoRicerca: string = '';
  filtroStato: string = '';
  mostraModale= false;
  veicoloSelezionato:any=null;
  listaLuoghi:any[]=[];
  constructor(private flottaService: FlottaAdminAziendaleService, private dettagliService: DettagliVeicoloAziendaleService) {}


  loading:boolean = false;



  ngOnInit(): void {
    this.caricaVeicoli();
    this.caricaLuoghi();
  }

  caricaLuoghi() {
    this.flottaService.richiediLuoghi().subscribe({
      next: (data) => {
        this.listaLuoghi = data;
        console.log("Luoghi caricati:", this.listaLuoghi);
      },
      error: (err) => {
        console.error("Errore caricamento luoghi:", err);
      }
    });
  }


  caricaVeicoli(){
    this.flottaService.richiediVeicoliAziendali().subscribe({
      next: (data) => {
        if (data) {
          this.veicoli = data;
          console.log("Dati caricati:", this.veicoli);
        }
        else{
          console.log("Nessun veicolo trovato", this.veicoli);
        }
      },
      error: (err) => {
        console.error("Errore nel caricamento:", err);
      }
    });
  }

  get veicoliFiltrati() {
    return this.veicoli.filter(v => {
      const targa = v.targaVeicolo || '';
      const modello = v.nomeModello || '';
      const matchTesto = (targa.toLowerCase().includes(this.testoRicerca.toLowerCase()) ||
                          modello.toLowerCase().includes(this.testoRicerca.toLowerCase()));
      const stato = v.statusContrattualeVeicolo?.toUpperCase() || '';
      const matchStato = this.filtroStato ? stato === this.filtroStato?.toUpperCase() : true;

      return matchTesto && matchStato;
    });
  }

  getConteggio(stato: string): number {
    if (!this.veicoli) return 0;
    return this.veicoli.filter(v =>
      v.statusContrattualeVeicolo?.toUpperCase() === stato.toUpperCase()
    ).length;
  }

  resetFiltri() {
    this.testoRicerca = '';
    this.filtroStato = '';
  }

  apriModaleDettagli(veicoloDallaLista: any) {
    const targa = veicoloDallaLista.targaVeicolo;

    if (targa) {
      this.dettagliService.richiediVeicolo(targa).subscribe({
        next: (veicoloCompleto) => {
          this.veicoloSelezionato = veicoloCompleto;
          this.mostraModale = true;
          console.log("Dettagli completi caricati:", this.veicoloSelezionato);
        },
        error: (err) => {
          console.error("Impossibile scaricare dettagli, uso dati parziali", err);
          this.veicoloSelezionato = veicoloDallaLista;
          this.mostraModale = true;
        }
      });
    } else {
      this.veicoloSelezionato = veicoloDallaLista;
      this.mostraModale = true;
    }
  }

  chiudiModale() {
    this.mostraModale = false;
    this.veicoloSelezionato = null;
  }





  caricaDettagliVeicolo(targa: string) {
    this.loading = true;
    this.dettagliService.richiediVeicolo(targa).subscribe({
      next: (data) => {
        console.log("Dettagli completi ricevuti:", data);
        this.veicoloSelezionato = data;
        this.loading = false;
        if (this.veicoloSelezionato.luogoRitiroDeposito &&
          this.veicoloSelezionato.luogoRitiroDeposito.latitudine &&
          this.veicoloSelezionato.luogoRitiroDeposito.longitudine) {
          this.modale.initMappa();
        }
      },
      error: (err) => {
        console.error("Errore caricamento dettagli:", err);
        this.loading = false;
      }
    });
  }




  impostaLuogo(luogoScelto: LuogoDTO ) {


      if(luogoScelto){
        const veicoloAggiornato = {
          ...this.veicoloSelezionato,
          luogoRitiroDeposito: luogoScelto,
        };

        this.veicoloSelezionato.luogoRitiroDeposito = luogoScelto;
        this.dettagliService.aggiornaPosizioneVeicolo(veicoloAggiornato).subscribe({
          next: (res) => {
            this.veicoloSelezionato=veicoloAggiornato;
            console.log("Luogo salvato con successo", res);
            },
          error: (err) => {
            console.error("Errore durante il salvataggio:", err);
            alert("Impossibile salvare la sede. Riprova");
          }
        });
      }
  }


}
