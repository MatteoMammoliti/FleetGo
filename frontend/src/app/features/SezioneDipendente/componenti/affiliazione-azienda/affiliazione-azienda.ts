import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router'; 
import { ImpostazioniService } from '../../ServiceSezioneDipendente/impostazioni-service';
import { TemplateTitoloSottotitolo } from '@shared/Componenti/Ui/template-titolo-sottotitolo/template-titolo-sottotitolo'; // Importa il componente UI
import { TemplateFinestraModale } from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import { MessaggioCardVuota } from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
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
    private router: Router
  ) {}

  disdiciAffiliazione() {
    const conferma = confirm(
      "SEI SICURO DI VOLER LASCIARE L'AZIENDA?\n\n" +
      "Perderai l'accesso a tutti i vantaggi aziendali.\n" +
      "Verrai immediatamente disconnesso."
    );

    if (conferma) {
      this.impostazioniService.abbandonaAzienda().subscribe({
        next: () => {
          alert("Affiliazione rimossa con successo. Verrai reindirizzato al login.");
          localStorage.removeItem('idAziendaAffiliata');
          this.router.navigate(['/autenticazione/login']);
        },
        error: (err) => {
          console.error(err);
          alert("Errore durante la disdetta. Riprova più tardi.");
        }
      });
    }
  }
  mostraModaleConferma: boolean = false;
  apriModaleDisdetta() {
    this.mostraModaleConferma = true;
  }
  confermaDisdetta() {
    this.impostazioniService.abbandonaAzienda().subscribe({
      next: () => {
        this.mostraModaleConferma = false;
        localStorage.removeItem('idAziendaAffiliata');
        localStorage.removeItem('utente'); 
        this.router.navigate(['/autenticazione/login']);
      },
      error: (err) => {
        console.error(err);
        alert("Errore durante la disdetta.");
        this.mostraModaleConferma = false;
      }
    });
  }
}


