import {Component, Input, ViewChild} from '@angular/core';
import {LuogoDTO} from '@core/models/LuogoDTO';
import {GoogleMapsService} from '@core/services/google-maps-service';
import {GoogleMap, MapInfoWindow, MapMarker} from '@angular/google-maps';

@Component({
  selector: 'app-mappa-hub',
  imports: [
    GoogleMap,
    MapMarker,
    MapInfoWindow
  ],
  templateUrl: './mappa-hub.html',
  styleUrl: './mappa-hub.css',
})
export class MappaHub {
  constructor(private googleService: GoogleMapsService) {
  }

  @ViewChild(MapInfoWindow) infoWindow!: MapInfoWindow;
  defaultZoom = 5.5;
  defaultCenter = {lat: 41.9028, lng: 12.4964};

  apiCaricata = false
  center!: google.maps.LatLngLiteral;
  mapOptions!: google.maps.MapOptions;
  zoom = 5.5;
  @Input() luoghiAzienda: LuogoDTO[] = []
  hubSelezionato: LuogoDTO | null = null

  ngOnInit() {
    this.googleService.load().then(() => {
      this.inizializzaMappa();
      this.apiCaricata = true;
    }).catch(err => {
      console.error("Errore nel caricamento di google maps")
    })

  }

  inizializzaMappa() {
    this.center = this.defaultCenter;
    this.mapOptions = {
      disableDefaultUI: true,
      zoomControl: false,
      scrollwheel: false,
      disableDoubleClickZoom: true,
      gestureHandling: 'none',
      mapTypeId: google.maps.MapTypeId.ROADMAP
    };
  }

  openInfo(mark: MapMarker, hub: LuogoDTO) {
    this.hubSelezionato = hub;
    this.center = {lat: hub.latitudine, lng: hub.longitudine};
    this.zoom = 12
    this.infoWindow.open(mark)
  }

  resetMappa() {
    this.hubSelezionato = null;
    this.center = this.defaultCenter;
    this.zoom = this.defaultZoom;
  }
}
