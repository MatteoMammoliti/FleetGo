import {Component, EventEmitter, Input, OnChanges, OnDestroy, Output, SimpleChanges} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TemplateFinestraModale} from '@shared/Componenti/Modali/template-finestra-modale/template-finestra-modale';
import {GoogleMapsService} from '@core/services/google-maps-service';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {
  DettagliVeicoloAziendaleService
} from '../../../ServiceSezioneAdminAziendale/dettagli-veicolo-aziendale-service';
import {RichiestaManutenzioneDTO} from '@core/models/RichiestaManutenzioneDTO';
import {
  FlottaAdminAziendaleService
} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/flotta-aziendale-service';
import {IconaStato} from '@shared/Componenti/Banner/icona-stato/icona-stato';
import {SceltaTendina} from '@shared/Componenti/Input/scelta-tendina/scelta-tendina';

declare var google: any;

@Component({
  selector: 'app-modale-gestisci-veicolo',
  standalone: true,
  imports: [
    CommonModule,
    TemplateFinestraModale,
    FormsModule,
    IconaStato,
    SceltaTendina,
  ],
  templateUrl: './modale-gestisci-veicolo.html',
  styleUrl: './modale-gestisci-veicolo.css',
})

export class ModaleGestisciVeicolo implements OnChanges, OnDestroy {

  @Output() chiudi = new EventEmitter<void>();
  @Output() richiediVeicolo = new EventEmitter<string>();
  @Output() inviaLuogo = new EventEmitter<LuogoDTO>();
  @Output() inviaRichiestaManutenzione = new EventEmitter<RichiestaManutenzioneDTO>();

  @Input() loading: any;
  @Input() mostraFormManutenzione: boolean = false;
  @Input() veicoloInput: any;
  @Input() listaLuoghi: LuogoDTO[] = [];

  tipoManutenzioneSelezionato: any = null;
  richiestaManutenzione: RichiestaManutenzioneDTO | null = null;
  veicolo: any = null;


  opzioniManutenzione = [
    'Guasto meccanico',
    'Incidente',
    'Tagliando',
    'Cambio gomme',
    'Controllo elettronico',
    'altro'
  ];
  luogoSelezionatoId: number | null = null;
  erroreSelezioneManutenzione: boolean = false;
  private map: any;
  private marker: any;
  private infoWindow: any;
  private coordsIniziali: { lat: number, lng: number } | null = null;
  private zoomIniziale: number = 15;

  constructor(
    private googleMapsService: GoogleMapsService,
    private dettagliService: DettagliVeicoloAziendaleService,
    private flottaService: FlottaAdminAziendaleService
  ) {
  }


  ngOnInit() {
    this.caricaManutenzione();
    this.initMappa()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['veicoloInput'] && this.veicoloInput) {
      this.veicolo = this.veicoloInput;

      if (this.veicolo.luogoRitiroDeposito) this.initMappa();
    }
  }

  ngOnDestroy() {
    this.map = null;
  }

  caricaManutenzione() {
    this.dettagliService.richiediManutenzioneVeicolo(this.veicoloInput.idVeicolo).subscribe({
      next: data => {
        if (data) {
          console.log("ho ricevuto", data);
          this.richiestaManutenzione = data;
        }
      }, error: error => console.log(error)
    })
  }


  initMappa() {
    const luogo = this.veicolo.luogoRitiroDeposito;
    if (!luogo || !luogo.latitudine || !luogo.longitudine) return;

    this.coordsIniziali = {lat: luogo.latitudine, lng: luogo.longitudine};

    this.googleMapsService.load().then(() => {
      setTimeout(() => {
        if (this.coordsIniziali) {
          this.disegnaMappa(this.coordsIniziali);
        }
      }, 300);
    });
  }

  disegnaMappa(coords: { lat: number; lng: number }) {
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


  impostaLuogo() {
    const luogoScelto = this.listaLuoghi.find(l => l.idLuogo == this.luogoSelezionatoId);
    this.inviaLuogo.emit(luogoScelto);
  }


  inviaManutenzione() {
    if (!this.tipoManutenzioneSelezionato || this.tipoManutenzioneSelezionato === '') {
      this.erroreSelezioneManutenzione = true;
      return;
    }

    this.erroreSelezioneManutenzione = false;

    const richiesta: RichiestaManutenzioneDTO = {
      idManutenzione: 0,
      idVeicolo: this.veicolo.idVeicolo,
      dataRichiesta: new Date().toISOString().split('T')[0],
      tipoManutenzione: this.tipoManutenzioneSelezionato,
    };

    this.erroreSelezioneManutenzione = false;

    this.flottaService.inviaRichiestaManutenzione(richiesta).subscribe({
      next: (res) => {
        this.mostraFormManutenzione = false;
        this.tipoManutenzioneSelezionato = null;
        this.caricaManutenzione();
        this.ngOnInit();
        this.initMappa();
      },
      error: (err: any) => {
        console.error(err);
        alert("Impossibile inviare la richiesta.");
      }
    });
  }

  annullaRichiesta(richiesta: RichiestaManutenzioneDTO) {
    this.dettagliService.annullaRichiesta(richiesta).subscribe({
      next: (res) => {
        if (res) {
          this.ngOnInit();
          this.initMappa();
        }
      }, error: (err) => console.error(err)
    })
  }


}
