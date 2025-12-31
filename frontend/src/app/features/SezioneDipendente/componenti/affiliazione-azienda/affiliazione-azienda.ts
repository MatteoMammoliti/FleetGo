import {Component, Input} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ImpostazioniService } from '../../ServiceSezioneDipendente/impostazioni-service';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo';
import { TemplateFinestraModale } from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import { MessaggioCardVuota } from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import {AuthService} from '@core/auth/auth-service';

@Component({
  selector: 'app-affiliazione-azienda',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TemplateTitoloSottotitolo,
    TemplateFinestraModale,
    MessaggioCardVuota
  ],
  templateUrl: './affiliazione-azienda.html',
  styleUrl: './affiliazione-azienda.css',
})
export class AffiliazioneAzienda {

  @Input() datiAzienda: any = {};

  constructor(
    private impostazioniService: ImpostazioniService,
    private authService: AuthService,
  ) {}

  mostraModaleConferma: boolean = false;

  apriModaleDisdetta() {
    this.mostraModaleConferma = true;
  }

  confermaDisdetta() {
    this.impostazioniService.abbandonaAzienda().subscribe({
      next: () => {
        this.mostraModaleConferma = false;
        this.authService.logout();
      },
      error: (err) => {
        console.error(err);
        alert("Errore durante la disdetta.");
        this.mostraModaleConferma = false;
      }
    });
  }
}
