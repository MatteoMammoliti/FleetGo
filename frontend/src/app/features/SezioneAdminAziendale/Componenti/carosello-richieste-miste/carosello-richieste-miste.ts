import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {Router} from '@angular/router';
import {NgClass} from '@angular/common';

interface ItemCarosello{
  tipo: "NOLEGGIO" | "AFFILIAZIONE" | "VUOTO" | "FATTURE";
  titolo: string;
  contatore: number;
  colore: string;
  icona: string;
  rotta: string;
}

@Component({
  selector: 'app-carosello-richieste-miste',
  imports: [
    NgClass
  ],
  templateUrl: './carosello-richieste-miste.html',
  styleUrl: './carosello-richieste-miste.css',
})

export class CaroselloRichiesteMiste implements OnInit, OnChanges, OnDestroy{

  constructor(private router: Router) {}

  @Input() contatoreRichiesteNoleggioInAttesa = 0;
  @Input() contatoreRichiesteAffiliazioneInAttesa = 0;
  @Input() contatoreFattureDaPagare = 0;

  slides: ItemCarosello[] = [];
  indiceCorrente = 0;
  intervalloRotazione: any;

  ngOnInit() { this.startRotation(); }

  ngOnChanges(changes: SimpleChanges) { this.caricaSlide(); }

  ngOnDestroy() { if(this.intervalloRotazione) clearInterval(this.intervalloRotazione) }

  startRotation() {
    this.intervalloRotazione = setInterval(() => {
      if(this.slides.length > 1) {
        this.avanzaCarosello();
      }
    }, 4000)
  }

  avanzaCarosello() {
    if(this.slides.length > 0) {
      this.indiceCorrente = (this.indiceCorrente + 1) % this.slides.length;
    }
  }

  caricaSlide() {
    this.slides = [];

    if(this.contatoreRichiesteAffiliazioneInAttesa > 0) {
      const item: ItemCarosello = {
        tipo: "AFFILIAZIONE",
        titolo: "Richieste di affiliazione in attesa",
        contatore: this.contatoreRichiesteAffiliazioneInAttesa,
        colore: "blue-theme",
        icona: "bi-buildings-fill",
        rotta: '/dashboardAzienda/dipendenti'
      }

      this.slides.push(item);
    }

    if(this.contatoreRichiesteNoleggioInAttesa > 0) {
      const item: ItemCarosello = {
        tipo: "NOLEGGIO",
        titolo: "Richieste di noleggio in attesa",
        contatore: this.contatoreRichiesteNoleggioInAttesa,
        colore: "purple-theme",
        icona: "bi-car-front-fill",
        rotta: '/dashboardAzienda/prenotazioni'
      }

      this.slides.push(item);
    }

    if(this.contatoreFattureDaPagare > 0) {
      const item: ItemCarosello = {
        tipo: "FATTURE",
        titolo: "Numero di fatture da pagare",
        contatore: this.contatoreFattureDaPagare,
        colore: "blue-theme",
        icona: "bi-car-front-fill",
        rotta: '/dashboardAzienda/storico-fatture'
      }

      this.slides.push(item);
    }

    if(this.slides.length == 0) {
      const item: ItemCarosello = {
        tipo: "VUOTO",
        titolo: "Stai andando alla grande, la tua azienda è gestita",
        contatore: this.contatoreRichiesteAffiliazioneInAttesa,
        colore: "green-theme",
        icona: "bi-check-circle-fill",
        rotta: "/dashboardAzienda/dashboard"
      }

      this.slides.push(item);
    }
  }

  naviga() {
    const rotta = this.slides[this.indiceCorrente].rotta;
    this.router.navigate([rotta]);
  }
}
