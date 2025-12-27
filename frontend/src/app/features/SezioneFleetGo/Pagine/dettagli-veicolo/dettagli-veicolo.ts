import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {FlottaGlobaleService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/flotta-globale-service';
import {AziendeAffiliateService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/aziende-affiliate-service';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {ActivatedRoute} from '@angular/router';
import {GoogleMapsService} from '@core/services/google-maps-service';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import { SceltaTendina } from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
declare var google:any;

@Component({
  selector: 'app-dettagli-veicolo',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    TemplateTitoloSottotitolo,
    SceltaTendina
  ],
  templateUrl: './dettagli-veicolo.html',
  styleUrl: './dettagli-veicolo.css',
})

export class DettagliVeicolo implements OnInit {

  constructor(private veicoloService: FlottaGlobaleService,
              private route: ActivatedRoute,
              private aziendeAssociateService:AziendeAffiliateService,
              private googleMapsService:GoogleMapsService) {}

  veicolo: any = null;
  aziende:AziendaDTO[] = [];
  aziendaSelezionata:any=null;
  modificaAzienda: boolean = false;

  private map: any;
  private marker: any;
  private infoWindow: any;
  private coordsIniziali: { lat: number, lng: number } | null = null;
  private zoomIniziale: number = 15;

  ngOnInit(){
    const targa:string | null = this.route.snapshot.paramMap.get('targa');
    this.initVeicolo(targa);
  }

  caricaListaAziende() {
    this.aziendeAssociateService.richiediAziende().subscribe({
      next: (data) => this.aziende = data || [],
      error: (err) => console.error("Errore aziende:", err)
    });
  }

  initVeicolo(targa: string | null) {
    this.veicoloService.richiediVeicolo(targa).subscribe({
      next: (response) => {
        if (response) {
          this.veicolo = response;
          this.modificaAzienda = !this.veicolo.nomeAziendaAffiliata;

          const luogo = this.veicolo.luogoRitiroDeposito;
          if (this.veicolo.nomeAziendaAffiliata && luogo && luogo.latitudine && luogo.longitudine) {
            this.initMappa();
          }
        }
      },
      error: (err) => console.error("Errore caricamento veicolo:", err)
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
      }, 500);
    });
  }

  disegnaMappa(coords: { lat: number; lng: number}) {
    const mapElement = document.getElementById("google-map");

    if (!mapElement) return;

    const mapOptions = {
      zoom: this.zoomIniziale,
      center: coords,
      disableDefaultUI: true,
      zoomControl: false,
      gestureHandling: 'none',
      clickableIcons: false
    };

    this.map=new google.maps.Map(mapElement, mapOptions);

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

  abilitaCambioAzienda() {
    this.modificaAzienda = true;
    this.aziendaSelezionata = null;
    this.caricaListaAziende();
  }

  disabilitaCambioAzienda() {
    this.modificaAzienda = false;
    this.aziendaSelezionata = null;
  }

  salvaModifiche(): void {
    if (this.modificaAzienda) {

      const veicoloDaInviare: any = {
        idVeicolo: this.veicolo.idVeicolo,
        targaVeicolo: this.veicolo.targaVeicolo,
        urlImmagine: this.veicolo.urlImmagine,
        modello: this.veicolo.modello,
        tipoDistribuzioneVeicolo: this.veicolo.tipoDistribuzioneVeicolo,
        livelloCarburante: this.veicolo.livelloCarburante,
        statusContrattualeVeicolo: this.veicolo.statusContrattualeVeicolo,
        inManutenzione: this.veicolo.inManutenzione || false
      };

      if (this.aziendaSelezionata) {
        veicoloDaInviare.idAziendaAffiliata = this.aziendaSelezionata.idAzienda;
        veicoloDaInviare.nomeAziendaAffiliata = this.aziendaSelezionata.nomeAzienda;
      } else {
        veicoloDaInviare.idAziendaAffiliata = null;
        veicoloDaInviare.nomeAziendaAffiliata = null;
      }

      this.veicoloService.inviaModifiche(veicoloDaInviare).subscribe({
        next: () => {
          console.log("Salvataggio avvenuto con successo");
          this.initVeicolo(this.veicolo.targaVeicolo);
          this.modificaAzienda = false;
          this.aziendaSelezionata = null;
        },
        error: (err) => {
          console.error("Errore salvataggio:", err);
        }
      });
    }
  }

  tornaIndietro(){window.history.back();}
}
