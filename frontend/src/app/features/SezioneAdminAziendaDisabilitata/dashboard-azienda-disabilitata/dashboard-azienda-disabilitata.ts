import {Component, OnInit} from '@angular/core';
import {ModaleRichiestaAppuntamento} from '@features/SezioneAdminAziendale/Componenti/modali/modale-richiesta-appuntamento/modale-richiesta-appuntamento';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-dashboard-azienda-disabilitata',
  imports: [
    ModaleRichiestaAppuntamento
  ],
  templateUrl: './dashboard-azienda-disabilitata.html',
  styleUrl: './dashboard-azienda-disabilitata.css',
})

export class DashboardAziendaDisabilitata implements OnInit {

  modaleRichiestaAppuntamentoVisibile = false;
  appuntamentoRichiesto = false;

  constructor(private dashboardService: DashboardService,
              private router: Router) {}

  ngOnInit() {
    const statoRichiesta = localStorage.getItem('appuntamento_inviato');
    if (statoRichiesta === 'true') {
      this.appuntamentoRichiesto = true;
    }
  }

  gestisciVisibilitaModale() { this.modaleRichiestaAppuntamentoVisibile = !this.modaleRichiestaAppuntamentoVisibile; }

  richiediAppuntamento() {
    this.dashboardService.inoltraRichiestaDiAppuntamento().subscribe({
      next: data => {
        if(data) {
          this.appuntamentoRichiesto = true;
          localStorage.setItem('appuntamento_inviato', 'true');

          setTimeout(() => {
            this.gestisciVisibilitaModale();
          }, 3000);
        }
      },
      error: error => { console.error(error); }
    });
  }

  vaiAlleFatture() {
    this.router.navigate(['/azienda-disabilitata/storico-fatture']);
  }
}
