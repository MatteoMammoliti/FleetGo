import {Component, Input} from '@angular/core';
import Chart from 'chart.js/auto';
import {RichiestaNoleggioDTO} from '@models/richiestaNoleggioDTO.models';

@Component({
  selector: 'app-grafico-andamento-utilizzo',
  imports: [],
  templateUrl: './grafico-andamento-utilizzo.html',
  styleUrl: './grafico-andamento-utilizzo.css',
})
export class GraficoAndamentoUtilizzo {
  public graficoAndamentoUtilizzo: any;
  listaNoleggi: RichiestaNoleggioDTO[] = [];
  lunghezzaMese:number=0;

  ngOnInit(): void {
    this.creaGrafico();
  }


  creaGrafico() {
    const etichetteGiorni=this.dammiEtichetteMeseCorrente();

    const dati:number[]=this.caricaDatiGrafico(etichetteGiorni.length);

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

  caricaDatiGrafico(numeroGiorni:number){
    const spesaMensilePerGiorni:number[]=[];
    for(let i=0; i<numeroGiorni; i++){
        spesaMensilePerGiorni.push(this.generaNumeroRandom(i*10,(i+1)*10%6));
    }
    return spesaMensilePerGiorni;
  }
  generaNumeroRandom(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }


  dammiEtichetteMeseCorrente() {
    const oggi = new Date();
    const mese= oggi.getMonth()+1;
    const anno = oggi.getFullYear();

    const giorniDelMese=new Date(anno,mese,0).getDate();
    this.lunghezzaMese=giorniDelMese;
    const giorni: string[]=[];
    for(let i=1;i<=giorniDelMese;i++){
      if (i<10){
        giorni.push("0"+(i).toString());
      }
      else{
        giorni.push((i).toString());
      }
    }
    return giorni;
}

}
