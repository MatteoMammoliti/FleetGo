import { Component } from '@angular/core';
import { DipendentiService } from '@core/services/ServiceSezioneAdminAziendale/dipendenti-service';
import { DipendenteDTO } from '@models/dipendenteDTO.models';
import { ActivatedRoute, Router } from '@angular/router';
import { inject } from '@angular/core';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-dettagli-dipendente',
  imports: [CommonModule, FormsModule],
  templateUrl: './dettagli-dipendente.html',
  styleUrl: './dettagli-dipendente.css',
})
export class DettagliDipendente implements OnInit {
  private service = inject(DipendentiService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  dipendente: DipendenteDTO | null = null;
  loading = true;

  ngOnInit() {
    const idCatturato = this.route.snapshot.paramMap.get('id');
    if (idCatturato) {
      this.caricaDati(idCatturato);
    }
  }
  caricaDati(id: string) {
    this.service.getDipendenteID(id).subscribe({
      next: (data) => {
        this.dipendente = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Errore nel caricamento dei dati:', err);
        this.loading = false;
      } 
    });
  }
  salvaModifiche() {
    if (!this.dipendente) {
      return;
    }
    this.service.aggiornaDipendente(this.dipendente).subscribe({
      next: () => {
        this.tornaIndietro();
      },
      error: (err) => {
        console.error('Errore nel salvataggio delle modifiche:', err);
      }
    });
  }
  tornaIndietro() {
    this.router.navigate(['/dashboardAzienda/dipendenti']);
  }
}

