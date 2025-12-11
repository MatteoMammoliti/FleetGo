import {Component, inject} from '@angular/core';
import {GraficoStatoFlotta} from '@shared/Componenti/Grafici/grafico-stato-flotta/grafico-stato-flotta';
import {GraficoAndamentoUtilizzo} from '@shared/Componenti/Grafici/grafico-andamento-utilizzo/grafico-andamento-utilizzo';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';
import {ContenitoreDatiGraficoAndamento} from '@core/models/ContenitoreDatiGraficoAndamento.models';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [
    GraficoStatoFlotta,
    GraficoAndamentoUtilizzo
  ],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})
export class DashboardAzienda {

  constructor(private service:DashboardService) {}

  ngOnInit(){
    this.settaDatiGraficoStatoFlotta();
    //SOLO PER TEST
    this.dammiEtichetteMeseCorrente();
    this.caricaDatiGrafico(this.lunghezzaMeseCorrente);
  }



  contenitoreInputGraficoStatoFlotta: ContenitoreStatisticheNumeriche = {
      veicoliAssegnati: 0,
      totaleVeicoli: 0,
      totaleAziende: 0,
      veicoliNoleggiati: 0,
      veicoliManutenzione:0,
      veicoliDisponibili:0
    }

  settaDatiGraficoStatoFlotta() {
    this.service.getStatoVeicoli().subscribe({
      next: (contenitore) => {
       this.contenitoreInputGraficoStatoFlotta=contenitore;
      },
      error: (err) => console.error("Errore grafico:", err)
    });
  }



  //GRAFICO ANDAMENTO SPESE MENSILE DA COLLEGARE solo test

  lunghezzaMeseCorrente:number=0;
  datiGraficoAndamento: ContenitoreDatiGraficoAndamento = {} as ContenitoreDatiGraficoAndamento;

  caricaDatiGrafico(numeroGiorni:number){
    const spesaMensilePerGiorni:number[]=[];
    for(let i=0; i<numeroGiorni; i++){
      spesaMensilePerGiorni.push(this.generaNumeroRandom(i*10,(i+1)*10%6));
    }
    this.datiGraficoAndamento.listaSpese=spesaMensilePerGiorni;
  }
  generaNumeroRandom(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  dammiEtichetteMeseCorrente() {
    const oggi = new Date();
    const mese= oggi.getMonth()+1;
    const anno = oggi.getFullYear();

    const giorniDelMese=new Date(anno,mese,0).getDate();
    this.lunghezzaMeseCorrente=giorniDelMese;
    const giorni: string[]=[];
    for(let i=1;i<=giorniDelMese;i++){
      if (i<10){
        giorni.push("0"+(i).toString());
      }
      else{
        giorni.push((i).toString());
      }
    }
    this.datiGraficoAndamento.listaGiorni=giorni;
  }




}
