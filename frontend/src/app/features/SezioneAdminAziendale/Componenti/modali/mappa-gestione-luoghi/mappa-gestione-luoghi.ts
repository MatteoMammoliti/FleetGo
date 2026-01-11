import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {LuogoDTO} from '@core/models/LuogoDTO';
import {GoogleMapsService} from '@core/services/google-maps-service';
import {CommonModule} from '@angular/common';

declare var google: any;

@Component({
  selector: 'app-mappa-gestione-luoghi',
  imports: [CommonModule],
  templateUrl: './mappa-gestione-luoghi.html',
  styleUrl: './mappa-gestione-luoghi.css',
})

export class MappaGestioneLuoghi implements OnInit, OnChanges {

  constructor(private googleMapsService: GoogleMapsService) {
  }

  @Input() luoghiSalvati: LuogoDTO[] = [];
  @Input() modalitaAggiuntaLuogo = false;
  @Output() luogoSelezionato: EventEmitter<LuogoDTO> = new EventEmitter<LuogoDTO>();

  private map: any;
  private markersLuoghiEsistenti: any [] = [];
  private barraRicerca: any;
  protected luogoDaAggiungere: LuogoDTO | null = null;
  private markerNuovoLuogo: any;

  ngOnInit() {
    this.googleMapsService.load().then(() => {
      this.initMap();
    })
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['luoghiSalvati'] && this.map) {
      this.aggiornaMarkers();
    }

    if (changes['modalitaAggiuntaLuogo'] && !this.modalitaAggiuntaLuogo) {
      this.luogoDaAggiungere = null;

      if (this.markerNuovoLuogo) this.markerNuovoLuogo.setMap(null);

      const input = document.getElementById("search-box") as HTMLInputElement;
      if (input) input.value = "";
    }
  }

  private initMap() {
    const centroItalia = {lat: 41.8719, lng: 12.5674};
    this.map = new google.maps.Map(document.getElementById("google-map"), {
      zoom: 6,
      mapTypeControl: false,
      center: centroItalia,
      streetViewControl: false
    });

    const inputRicerca = document.getElementById("search-box") as HTMLInputElement;
    this.barraRicerca = new google.maps.places.Autocomplete(inputRicerca);
    this.barraRicerca.bindTo("bounds", this.map);

    this.barraRicerca.addListener("place_changed", () => {
      const luogo = this.barraRicerca.getPlace();

      if (!luogo.geometry) return;

      if (luogo.geometry.viewport) {
        this.map.fitBounds(luogo.geometry.viewport);
      } else {
        this.map.setCenter(luogo.geometry.location);
        this.map.setZoom(17);
      }

      this.luogoDaAggiungere = {
        idLuogo: 0,
        nomeLuogo: luogo.name,
        latitudine: luogo.geometry.location.lat(),
        longitudine: luogo.geometry.location.lng(),
        idAzienda: 0
      };

      if (this.markerNuovoLuogo) this.markerNuovoLuogo.setMap(null);

      this.markerNuovoLuogo = new google.maps.Marker({
        position: luogo.geometry.location,
        map: this.map,
        title: "Nuovo Luogo",
        icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
      });
    });

    this.aggiornaMarkers();
  }

  confermaLuogoSelezionato() {
    if (this.luogoDaAggiungere) {
      this.luogoSelezionato.emit(this.luogoDaAggiungere);
      this.luogoDaAggiungere = null;
    }
  }

  private aggiornaMarkers() {
    this.markersLuoghiEsistenti.forEach(m => m.setMap(null));
    this.markersLuoghiEsistenti = [];

    if (this.luoghiSalvati) {
      this.luoghiSalvati.forEach(luogo => {
        const nuovoMarker = new google.maps.Marker({
          position: {lat: luogo.latitudine, lng: luogo.longitudine},
          map: this.map,
          title: luogo.nomeLuogo,
          icon: 'http://maps.google.com/mapfiles/ms/icons/red-dot.png'
        });

        this.markersLuoghiEsistenti.push(nuovoMarker);
      });
    }
  }
}
