import { Component } from '@angular/core';

import Chart from 'chart.js/auto'


@Component({
  selector: 'app-grafico-stato-flotta',
  imports: [],
  templateUrl: './grafico-stato-flotta.html',
  styleUrl: './grafico-stato-flotta.css',
})
export class GraficoStatoFlotta {
  public graficoTorta:any;

  totaleVeicoli=0;

  datiGrafico = {
    autoDisponibili:0,
    autoInUso:0,
    autoInManutenzione:0
  }

  ngOnInit(): void {
    this.settaDatiGraficoStatoFlotta()
    this.sommaTotaleVeicoli();
    this.creaGrafico();
  }
  creaGrafico(){
    this.graficoTorta=new Chart(
      'graficoStatoFlotta',
      {
        type:'pie',
        data:{
          labels:[this.datiGrafico.autoDisponibili+' Disponibili',this.datiGrafico.autoInUso+' In uso',this.datiGrafico.autoInManutenzione+ " In manutenzione"],
          datasets:[
            {
              data: [this.datiGrafico.autoDisponibili, this.datiGrafico.autoInUso, this.datiGrafico.autoInManutenzione],

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


  settaDatiGraficoStatoFlotta(){
    //solo per testare ora
    this.datiGrafico.autoDisponibili = 10;
    this.datiGrafico.autoInUso = 4;
    this.datiGrafico.autoInManutenzione = 2;

  }
  sommaTotaleVeicoli(){
    this.totaleVeicoli=this.datiGrafico.autoDisponibili+this.datiGrafico.autoInUso+this.datiGrafico.autoInManutenzione;
  }

}
