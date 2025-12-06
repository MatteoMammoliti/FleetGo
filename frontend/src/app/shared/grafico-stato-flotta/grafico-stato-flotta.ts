import { Component } from '@angular/core';

import Chart from 'chart.js/auto'
import {DashboardService} from '@core/services/ServiceSezioneAdminAziendale/dashboard-service';

@Component({
  selector: 'app-grafico-stato-flotta',
  imports: [],
  templateUrl: './grafico-stato-flotta.html',
  styleUrl: './grafico-stato-flotta.css',
})

export class GraficoStatoFlotta {

  constructor(private service:DashboardService) {}

  public graficoTorta:any;
  autoDisponibili = 0;
  autoInUso = 0;
  autoInManutenzione = 0;
  totaleVeicoli= 0;

  ngOnInit(): void {
    this.settaDatiGraficoStatoFlotta()
  }

  creaGrafico(){
    this.graficoTorta=new Chart(
      'graficoStatoFlotta',
      {
        type:'pie',
        data:{
          labels:[this.autoDisponibili+' Disponibili',this.autoInUso+' In uso', this.autoInManutenzione+ " In manutenzione"],
          datasets:[
            {
              data: [this.autoDisponibili, this.autoInUso, this.autoInManutenzione],

              backgroundColor: [
                '#00A88D',
                '#002040',
                '#0070dc'
              ],
              borderWidth: 2,
              borderColor: '#ffffff'
            }],

        },
        options:{
          responsive: true,
          maintainAspectRatio: false,
          plugins:{
            legend:{
              position:'right',
              labels:{
                padding:10,
                boxWidth:10
              }

            }
          }
        }
      })
  }

  settaDatiGraficoStatoFlotta() {
    this.service.getStatoVeicoli().subscribe({
      next: (contenitore) => {
        this.autoDisponibili = contenitore.numVeicoliDisponibili;
        this.autoInUso = contenitore.numVeicoliNoleggiati;
        this.autoInManutenzione = contenitore.numVeicoliInManutenzione;

        this.totaleVeicoli = this.autoDisponibili + this.autoInUso + this.autoInManutenzione;

        if (this.graficoTorta) {
          this.graficoTorta.destroy();
        }
        this.creaGrafico();
      },
      error: (err) => console.error("Errore grafico:", err)
    });
  }
}
