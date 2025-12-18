import { Component } from '@angular/core';
import {VeicoloCard} from '@features/SezioneDipendente/componenti/veicolo-card/veicolo-card';
import {VeicoloPrenotazioneDTO} from '@core/models/veicoloPrenotazioneDTO';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {FormsModule} from '@angular/forms';
import {CreaPrenotazioneService} from '@features/SezioneDipendente/ServiceSezioneDipendente/crea-prenotazione-service';
import {FiltriRicerca} from '@features/SezioneDipendente/componenti/filtri-ricerca/filtri-ricerca';
import {DatiFiltriNuovaPrenotazione} from '@core/models/DatiFiltriNuovaPrenotazione';

@Component({
  selector: 'app-crea-prenotazione',
  imports: [
    VeicoloCard,
    FormsModule,
    FiltriRicerca
  ],
  templateUrl: './crea-prenotazione.html',
  styleUrl: './crea-prenotazione.css',
})
export class CreaPrenotazione {
  listaVeicoli:VeicoloPrenotazioneDTO[]=[]
  listaLuoghi:LuogoDTO[]=[]
  nomeLuogoSelezionato:string = '';

  dataInizio: string = '';
  dataFine: string = '';
  oraInizio: string = '';
  oraFine: string = '';

  dataOggiCompleta:Date=new Date()
  dateDomaniCompleta:Date=new Date(+1);

  constructor(private service:CreaPrenotazioneService) {
  }
  ngOnInit(){
    this.impostaDateDefault()
    this.getListaLuoghi()
  }

  getListaLuoghi(){
    this.service.richiediLuoghi().subscribe({
      next:(risposta:LuogoDTO[])=>{
        this.listaLuoghi=risposta;
        console.log(risposta)
        this.nomeLuogoSelezionato=this.listaLuoghi[0].nomeLuogo;
        this.getRichiediVeicolo(this.dataInizio,this.dataFine,this.oraInizio,this.oraFine)
      },
      error:(err)=>console.error("Errore nel caricamento dei luoghi")
    })
  }

  getRichiediVeicolo(dataInizio:string,dataFine:string,oraInizio:string,oraFine:string){
    this.service.richiediVeicoli(this.nomeLuogoSelezionato,dataInizio,dataFine,oraInizio,oraFine).subscribe({
      next:(risposta:VeicoloPrenotazioneDTO[])=>{
        this.listaVeicoli=risposta;
        console.log(risposta)
      },
      error:(err)=>console.error("Errore nel caricamento dei veicoli")
    })

  }

  clickApplicaFiltri(dati:DatiFiltriNuovaPrenotazione){
    this.dataInizio=dati.dataInizio;
    this.dataFine=dati.dataFine;
    this.oraFine=dati.oraFine;
    this.oraInizio=dati.oraInizio;
    this.getRichiediVeicolo(this.dataInizio,this.dataFine,this.oraInizio,this.oraFine);
  }

  private impostaDateDefault() {
    const oggi = new Date();
    const domani = new Date(oggi);

    domani.setDate(oggi.getDate() + 1);

    const dataStringa = this.formattaData(domani);
    this.dataInizio = dataStringa;
    this.dataFine = dataStringa;

    this.oraInizio = "09:00";
    this.oraFine = "18:00";
  }

  private formattaData(date: Date): string {
    const anno = date.getFullYear();
    const mese = (date.getMonth() + 1).toString().padStart(2, '0');
    const giorno = date.getDate().toString().padStart(2, '0');
    return `${anno}-${mese}-${giorno}`;
  }
  clickCambioLuogo(){
    this.getRichiediVeicolo(this.dataInizio,this.dataFine,this.oraInizio,this.oraFine)
  }

}
