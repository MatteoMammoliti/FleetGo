import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-come-funziona',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './come-funziona.html',
  styleUrls: ['./come-funziona.css']
})
export class ComeFunziona {

  activeTab: 'AZIENDA' | 'DIPENDENTE' = 'AZIENDA';

  featuresAzienda = [
    {
      icon: 'bi-wallet2',
      titolo: 'Integrazione rapida',
      descrizione: 'Prenota un appuntamento e stabilisci tutto con noi. Sarai su FleetGo in un batter d\'occhio.'
    },
    {
      icon: 'bi-buildings',
      titolo: 'Gestione Centralizzata',
      descrizione: 'Controlla l’intera flotta, le scadenze e le assegnazioni da un’unica dashboard intuitiva.'
    },
    {
      icon: 'bi-file-earmark-text',
      titolo: 'Fatturazione Automatica',
      descrizione: 'Ricevi una fattura unica mensile per tutti i servizi, eliminando il caos amministrativo.'
    }
  ];

  featuresDipendente = [
    {
      icon: 'bi-buildings',
      titolo: 'Accesso semplificato',
      descrizione: 'Registra il tuo profilo ed attendi l\'approvazione presso la tua azienda. I tuoi viaggi inizieranno preso'
    },
    {
      icon: 'bi-calendar-check',
      titolo: 'Prenotazione Rapida',
      descrizione: 'Prenota l’auto che ti serve in pochi click, direttamente dal tuo smartphone o computer.'
    },
    {
      icon: 'bi-clock-history',
      titolo: 'Storico Viaggi',
      descrizione: 'Tieni traccia delle tue trasferte e giustifica le spese con un click, senza moduli cartacei.'
    }
  ];

  get featuresCorrenti() {
    return this.activeTab === 'AZIENDA' ? this.featuresAzienda : this.featuresDipendente;
  }

  setTab(tab: 'AZIENDA' | 'DIPENDENTE') {
    this.activeTab = tab;
  }
}
