import {Component, computed, input} from '@angular/core';
import {ChartConfiguration, ChartData} from 'chart.js';
import {BaseChartDirective} from 'ng2-charts';

@Component({
  selector: 'app-grafico-torta-flotta',
  standalone: true,
  imports: [
    BaseChartDirective
  ],
  templateUrl: './grafico-torta-flotta.html',
  styleUrl: './grafico-torta-flotta.css',
})
export class GraficoTortaFlotta {

  inUso = input.required<number>();
  disponibili = input.required<number>();
  inManutenzione = input.required<number>();

  public chartType: 'doughnut' = 'doughnut';

  public chartOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    cutout: '85%',
    plugins: {
      legend: {
        display: false
      }
    }
  };

  public chartData = computed<ChartData<'doughnut'>>(() => ({
    labels: ['In uso', 'Disponibili', 'In manutenzione'],
    datasets: [{
      data: [this.inUso(), this.disponibili(), this.inManutenzione()],
      backgroundColor: ['#0061FF', '#0F172A', '#E2E8F0'],
      hoverBackgroundColor: ['#0052d9', '#020617', '#cbd5e1'],
      borderWidth: 2,
      borderColor: '#ffffff',
      borderRadius: 4
    }]
  }));

  totale = computed(() => this.inUso() + this.disponibili() + this.inManutenzione());
}
