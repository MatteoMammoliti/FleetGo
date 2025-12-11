import {Component, Input, SimpleChanges} from '@angular/core';

import Chart from 'chart.js/auto'
import {
  ContenitoreStatisticheNumeriche
} from '@core/models/ContenitoreStatisticheNumeriche';


@Component({
  selector: 'app-grafico-stato-flotta',
  imports: [],
  templateUrl: './grafico-stato-flotta.html',
  styleUrl: './grafico-stato-flotta.css',
})

export class GraficoStatoFlotta {

  public graficoTorta: any;

  @Input({required: true}) contenitoreInput: ContenitoreStatisticheNumeriche ={
    veicoliAssegnati: 0,
    totaleVeicoli: 0,
    totaleAziende: 0,
    veicoliDisponibili: 0,
    veicoliManutenzione: 0,
    veicoliNoleggiati: 0,
    fattureDaGenerare: 0,
    guadagnoMensile:0
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
          labels: [this.percentuale(this.contenitoreInput.veicoliDisponibili) + '% Disponibili',
            this.percentuale(this.contenitoreInput.veicoliNoleggiati)+ '% In uso',
            this.percentuale(this.contenitoreInput.veicoliManutenzione) + "% In manutenzione"],
          datasets: [
            {
              data: [this.contenitoreInput.veicoliDisponibili, this.contenitoreInput.veicoliNoleggiati , this.contenitoreInput.veicoliManutenzione],

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
    this.totaleVeicoli = this.contenitoreInput.veicoliDisponibili +
      this.contenitoreInput.veicoliManutenzione
      + this.contenitoreInput.veicoliNoleggiati;

    if (this.graficoTorta) {
      this.graficoTorta.destroy();
    }
    this.creaGrafico();
  }
}
