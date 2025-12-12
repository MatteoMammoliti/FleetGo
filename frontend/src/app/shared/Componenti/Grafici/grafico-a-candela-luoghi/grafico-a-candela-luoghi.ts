import {Component, Input, SimpleChanges} from '@angular/core';
import {ContenitoreDatiGraficoLuoghiAuto} from '@core/models/ContenitoreDatiGraficoLuoghiAuto.models';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-grafico-a-candela-luoghi',
  imports: [],
  templateUrl: './grafico-a-candela-luoghi.html',
  styleUrl: './grafico-a-candela-luoghi.css',
})
export class GraficoACandelaLuoghi {

@Input() dati: ContenitoreDatiGraficoLuoghiAuto[]=[];


  ngOnChanges(changes: SimpleChanges) {
    if (this.dati && this.dati.length > 0) {
      this.creaGrafico();
    }
  }

  grafico:any;

  creaGrafico() {
    const labels = this.dati.map(d => d.nomeLuogo);
    const datiDisponibili = this.dati.map(d => d.numeroVeicoliDisponibili);
    const datiOccupati = this.dati.map(d => d.numeroVeicoliOccupati);

    if (this.grafico) {
      this.grafico.destroy();
    }
    this.grafico = new Chart('graficoLuoghiRitiro', {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Disponibili',
            data: datiDisponibili,
            backgroundColor: '#00A88D',
            borderRadius: 4,
          },
          {
            label: 'Occupati',
            data: datiOccupati,
            backgroundColor: '#002040',
            borderRadius: 4,
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'bottom',
            labels: { usePointStyle: true, color: '#4b5563' }
          },
        },
        scales: {
          x: {
            stacked: true,
            grid: { display: false }
          },
          y: {
            stacked: true,
            beginAtZero: true,
          }
        }
      }
    });
  }



}
