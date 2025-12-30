import { Component, EventEmitter, Input, Output, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TemplateFinestraModale } from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import { GoogleMapsService } from '@core/services/google-maps-service';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import { DettagliVeicoloAziendaleService } from '../../ServiceSezioneAdminAziendale/dettagli-veicolo-aziendale-service';
import { RichiestaManutenzioneDTO } from '@core/models/RichiestaManutenzioneDTO';
import { FlottaAdminAziendaleService } from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/flotta-aziendale-service';
declare var google: any;

@Component({
  selector: 'app-modale-gestisci-veicolo',
  standalone: true,
  imports: [
    CommonModule,
    TemplateFinestraModale,
    FormsModule,
  ],
  templateUrl: './modale-gestisci-veicolo.html',
  styleUrl: './modale-gestisci-veicolo.css',
})
export class ModaleGestisciVeicolo implements OnInit, OnDestroy {

  @Input() veicoloInput: any;
  @Input() listaLuoghi: LuogoDTO[] = [];
  @Output() chiudi = new EventEmitter<void>();
  veicolo: any = null;
  loading: boolean = true;
  mostraFormManutenzione: boolean = false;
  tipoManutenzioneSelezionato: string = '';

  opzioniManutenzione = [
    'Guasto meccanico',
    'Incidente',
    'Tagliando',
    'Cambio gomme',
    'Controllo elettronico',
    'altro'
  ];

  private map: any;
  private marker: any;
  private infoWindow: any;
  private coordsIniziali: { lat: number, lng: number } | null = null;
  private zoomIniziale: number = 15;

  luogoSelezionatoId: number | null = null;

  constructor(
    private googleMapsService: GoogleMapsService,
    private dettagliService: DettagliVeicoloAziendaleService,
    private flottaService: FlottaAdminAziendaleService
  ) {}

  ngOnInit() {
    if (this.veicoloInput && this.veicoloInput.targaVeicolo) {
      this.caricaDettagliVeicolo(this.veicoloInput.targaVeicolo);
    }
  }

  ngOnDestroy() {
    this.map = null;
  }

  caricaDettagliVeicolo(targa: string) {
    this.loading = true;
    this.dettagliService.richiediVeicolo(targa).subscribe({
      next: (data) => {
        console.log("Dettagli completi ricevuti:", data);
        this.veicolo = data;
        this.loading = false;
        if (this.veicolo.luogoRitiroDeposito &&
            this.veicolo.luogoRitiroDeposito.latitudine &&
            this.veicolo.luogoRitiroDeposito.longitudine) {
          this.initMappa();
        }
      },
      error: (err) => {
        console.error("Errore caricamento dettagli:", err);
        this.veicolo = this.veicoloInput;
        this.loading = false;
      }
    });
  }

  initMappa() {
    const luogo = this.veicolo.luogoRitiroDeposito;
    if (!luogo || !luogo.latitudine || !luogo.longitudine) return;

    this.coordsIniziali = { lat: luogo.latitudine, lng: luogo.longitudine };

    this.googleMapsService.load().then(() => {
      setTimeout(() => {
        if(this.coordsIniziali) {
           this.disegnaMappa(this.coordsIniziali);
        }
      }, 300);
    });
  }

  disegnaMappa(coords: { lat: number; lng: number}) {
    const mapElement = document.getElementById("google-map-modale");
    if (!mapElement) return;

    const mapOptions = {
      zoom: this.zoomIniziale,
      center: coords,
      disableDefaultUI: true,
      zoomControl: false,
      gestureHandling: 'none',
      clickableIcons: false
    };

    this.map = new google.maps.Map(mapElement, mapOptions);

    this.marker = new google.maps.Marker({
      position: coords,
      map: this.map,
      animation: google.maps.Animation.DROP,
    });

    this.infoWindow = new google.maps.InfoWindow({
      content:`<div style="padding:5px; font-weight:bold; color:#0f172a">${this.veicolo.luogoRitiroDeposito.nomeLuogo}</div>`
    });

    this.marker.addListener("click", () => {
      this.attivaMappa();
    });

    this.infoWindow.addListener('closeclick', () => {
      this.resetMappa();
    });
  }

  attivaMappa() {
    this.map.setZoom(17);
    this.map.setCenter(this.coordsIniziali);
    this.map.setOptions({ gestureHandling: 'cooperative' });
    this.infoWindow.open(this.map, this.marker);
  }

  resetMappa() {
    this.map.setZoom(this.zoomIniziale);
    this.map.setCenter(this.coordsIniziali);
    this.map.setOptions({ gestureHandling: 'none' });
    this.infoWindow.close();
  }

  impostaLuogo() {
    if (this.luogoSelezionatoId) {
      const luogoScelto = this.listaLuoghi.find(l => l.idLuogo == this.luogoSelezionatoId);
      if(luogoScelto){
         this.veicolo.luogoRitiroDeposito = luogoScelto;
         this.dettagliService.aggiornaPosizioneVeicolo(this.veicolo).subscribe({
           next: (res) => {
             console.log("Luogo salvato con successo", res);
             this.ngOnInit();
           },
           error: (err) => {
             console.error("Errore durante il salvataggio:", err);
             alert("Impossibile salvare la sede. Riprova");
           }
          });
        }
    }
  }

  inviaManutenzione() {
    if (!this.tipoManutenzioneSelezionato) return;

    const richiesta: RichiestaManutenzioneDTO = {
      idManutenzione: 0,
      idVeicolo: this.veicolo.idVeicolo,
      dataRichiesta: new Date().toISOString().split('T')[0],
      tipoManutenzione: this.tipoManutenzioneSelezionato,
    };

    this.flottaService.inviaRichiestaManutenzione(richiesta).subscribe({
      next: (res) => {
        this.mostraFormManutenzione = false;
        this.tipoManutenzioneSelezionato = '';
        this.ngOnInit();
      },
      error: (err: any) => {
        console.error(err);
        alert("Impossibile inviare la richiesta.");
      }
    });
  }
}
