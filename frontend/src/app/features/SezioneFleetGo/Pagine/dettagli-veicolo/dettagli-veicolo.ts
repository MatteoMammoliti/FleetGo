import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {AziendaDTO} from '@core/models/aziendaDTO';
import { SceltaTendina } from '@shared/Componenti/Ui/scelta-tendina/scelta-tendina';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
declare var google:any;

@Component({
  selector: 'app-dettagli-veicolo',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    SceltaTendina,
    TemplateFinestraModale
  ],
  templateUrl: './dettagli-veicolo.html',
  styleUrl: './dettagli-veicolo.css',
})

export class DettagliVeicolo  {

  @Output() chiudi = new EventEmitter<void>();

  @Output() dissocia = new EventEmitter<any>();
  @Output() associa = new EventEmitter<any>();
  @Output() dissociaAssocia=new EventEmitter<{veicolo: any, nuovaAzienda: any}>();

  @Input() veicolo: any = null;
  @Input() aziende: AziendaDTO[] = [];
  aziendaSelezionata: any = null;

  dissociaSelezionata = false;

  private map: any;
  private marker: any;
  private infoWindow: any;
  private coordsIniziali: { lat: number, lng: number } | null = null;
  private zoomIniziale: number = 15;

  disegnaMappa(coords: { lat: number; lng: number }) {
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

    this.map = new google.maps.Map(mapElement, mapOptions);

    this.marker = new google.maps.Marker({
      position: coords,
      map: this.map,
      animation: google.maps.Animation.DROP,
    });

    this.infoWindow = new google.maps.InfoWindow({
      content: `<div style="padding:5px; font-weight:bold; color:#0f172a">${this.veicolo.luogoRitiroDeposito.nomeLuogo}</div>`
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
    this.map.setOptions({gestureHandling: 'cooperative'});
    this.infoWindow.open(this.map, this.marker);
  }

  resetMappa() {
    this.map.setZoom(this.zoomIniziale);
    this.map.setCenter(this.coordsIniziali);
    this.map.setOptions({gestureHandling: 'none'});
    this.infoWindow.close();
  }

  associaVeicoloAzienda() {
    this.associa.emit(this.aziendaSelezionata);
  }

  dissociaVeicoloAzienda() {
    this.dissocia.emit(this.veicolo);
  }

  salva() {
    if(this.dissociaSelezionata&&this.aziendaSelezionata){
        this.dissociaAssocia.emit({ veicolo: this.veicolo, nuovaAzienda: this.aziendaSelezionata });
    }
    else if (this.dissociaSelezionata) {
      this.dissociaVeicoloAzienda();
    }
    else if (this.aziendaSelezionata) {
      this.associaVeicoloAzienda();
    }
    this.chiudiModale()
  }

  chiudiModale(){
    this.dissociaSelezionata = false;
    this.aziendaSelezionata=null;
    this.veicolo=null;
    this.aziende=[];
    this.chiudi.emit();
  }

}
