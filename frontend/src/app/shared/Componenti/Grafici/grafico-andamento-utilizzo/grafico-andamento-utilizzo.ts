import {Component, Input} from '@angular/core';
import Chart from 'chart.js/auto';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';
import {ContenitoreDatiGraficoAndamento} from '@core/models/ContenitoreDatiGraficoAndamento.models';

@Component({
  selector: 'app-grafico-andamento-utilizzo',
  imports: [],
  templateUrl: './grafico-andamento-utilizzo.html',
  styleUrl: './grafico-andamento-utilizzo.css',
})
export class GraficoAndamentoUtilizzo {
  public graficoAndamentoUtilizzo: any;
  lunghezzaMese:number=0;

  ngOnInit(): void {
    this.creaGrafico();
  }

  @Input() datiGrafico:ContenitoreDatiGraficoAndamento= {} as ContenitoreDatiGraficoAndamento;

  creaGrafico() {
    const etichetteGiorni=this.datiGrafico.listaGiorni;

    const dati:number[]=this.datiGrafico.listaSpese;

    this.graficoAndamentoUtilizzo = new Chart(
      'graficoUtilizzo',
      {
        type: 'line',
        data: {
          labels: etichetteGiorni,
          datasets: [
            {
              label: 'Spese mensili (€)',
              data: dati,
              borderColor: '#00A88D',
              backgroundColor: 'rgba(0, 168, 141, 0.2)',
              borderWidth: 2,
              tension: 0.4,
              fill: true
            }]
        }
      })
  }

}
