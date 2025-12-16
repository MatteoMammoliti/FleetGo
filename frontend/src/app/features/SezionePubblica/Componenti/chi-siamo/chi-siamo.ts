import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-chi-siamo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chi-siamo.html',
  styleUrl: './chi-siamo.css',
})
export class ChiSiamo implements OnInit, OnDestroy {
  images: string[] = [
    'logo-marca-auto/bmw-logo.png',
    'logo-marca-auto/fiat-logo.png',
    'logo-marca-auto/jeep-logo.png',
    'logo-marca-auto/mercedes-logo.png',
  ];

  indiceCorrente=0;
  intervallo:any;

  ngOnInit() {
    this.startCarosello();
  }

  ngOnDestroy() {
      if (this.intervallo) {
        clearInterval(this.intervallo);
      }
  }

  startCarosello() {
    this.intervallo = setInterval(() => {
      this.indiceCorrente = (this.indiceCorrente + 1) % this.images.length;
    }, 4000);
  }

}



