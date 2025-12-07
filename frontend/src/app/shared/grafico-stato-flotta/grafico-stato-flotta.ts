import {Component, Input, SimpleChanges} from '@angular/core';

import Chart from 'chart.js/auto'
import {DashboardService} from '@core/services/ServiceSezioneAdminAziendale/dashboard-service';
import {ContenitoreContatoriStatoVeicoli} from '@models/ContenitoreContatoriStatoVeicoli';


@Component({
  selector: 'app-grafico-stato-flotta',
  imports: [],
  templateUrl: './grafico-stato-flotta.html',
  styleUrl: './grafico-stato-flotta.css',
})

export class GraficoStatoFlotta {

  public graficoTorta: any;

  @Input({required: true}) contenitoreInput: ContenitoreContatoriStatoVeicoli = {
    numVeicoliDisponibili: 0,
    numVeicoliInManutenzione: 0,
    numVeicoliNoleggiati: 0
  };
  totaleVeicoli = 0;

  ngOnChanges(changes: SimpleChanges): void {
    console.log(this.contenitoreInput);
    this.inserisciDatiGrafico()
  }

  creaGrafico() {
    this.graficoTorta = new Chart(
      'graficoStatoFlotta',
      {
        type: 'pie',
        data: {
          labels: [this.percentuale(this.contenitoreInput.numVeicoliDisponibili) + '% Disponibili', this.percentuale(this.contenitoreInput.numVeicoliNoleggiati)+ '% In uso',  this.percentuale(this.contenitoreInput.numVeicoliInManutenzione) + "% In manutenzione"],
          datasets: [
            {
              data: [this.contenitoreInput.numVeicoliDisponibili, this.contenitoreInput.numVeicoliNoleggiati , this.contenitoreInput.numVeicoliInManutenzione],

              backgroundColor: [
                '#00A88D',
                '#002040',
                '#0070dc'
              ],
              borderWidth: 2,
              borderColor: '#ffffff'
            }],

        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'right',
              labels: {
                padding: 10,
                boxWidth: 10
              }

            }
          }
        }
      })
  }

  percentuale(valore:number){
    return valore/this.totaleVeicoli*100;
  }
  inserisciDatiGrafico() {
    this.totaleVeicoli = this.contenitoreInput.numVeicoliDisponibili + this.contenitoreInput.numVeicoliInManutenzione + this.contenitoreInput.numVeicoliNoleggiati;

    if (this.graficoTorta) {
      this.graficoTorta.destroy();
    }
    this.creaGrafico();
  }
}
