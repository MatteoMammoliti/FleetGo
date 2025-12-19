import { Component } from '@angular/core';
import {VeicoloCard} from '@features/SezioneDipendente/componenti/veicolo-card/veicolo-card';
import {VeicoloPrenotazioneDTO} from '@core/models/veicoloPrenotazioneDTO';
import {LuogoDTO} from '@core/models/luogoDTO.models';
import {FormsModule} from '@angular/forms';
import {CreaPrenotazioneService} from '@features/SezioneDipendente/ServiceSezioneDipendente/crea-prenotazione-service';
import {FiltriRicerca} from '@features/SezioneDipendente/componenti/filtri-ricerca/filtri-ricerca';
import {DatiFiltriNuovaPrenotazione} from '@core/models/DatiFiltriNuovaPrenotazione';
import {ContenitoreFormNuovaRichiestaNoleggio} from '@core/models/ContenitoreFormNuovaRichiestaNoleggio';
import {
  RichiestaNoleggioForm
} from '@features/SezioneDipendente/componenti/richiesta-noleggio-form/richiesta-noleggio-form';
import {RichiestaNoleggioDTO} from '@core/models/richiestaNoleggioDTO.models';

@Component({
  selector: 'app-crea-prenotazione',
  imports: [
    VeicoloCard,
    FormsModule,
    FiltriRicerca,
    RichiestaNoleggioForm
  ],
  templateUrl: './crea-prenotazione.html',
  styleUrl: './crea-prenotazione.css',
})
export class CreaPrenotazione {
  listaVeicoli:VeicoloPrenotazioneDTO[]=[]
  listaLuoghi:LuogoDTO[]=[]
  nomeLuogoSelezionato:string = '';
  veicoloSelezionato:VeicoloPrenotazioneDTO={} as VeicoloPrenotazioneDTO;
  tariffaAlMinuto:number=0.15
  costoStimato:number=0;

  dataInizio: string = '';
  dataFine: string = '';
  oraInizio: string = '';
  oraFine: string = '';

  mostraModale:ContenitoreFormNuovaRichiestaNoleggio | null=null

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
      },
      error:(err)=>console.error("Errore nel caricamento dei veicoli")
    })

  }

  clickApplicaFiltri(dati:DatiFiltriNuovaPrenotazione){
    this.dataInizio=dati.dataInizio;
    this.dataFine=dati.dataFine;
    this.oraFine=dati.oraFine;
    this.oraInizio=dati.oraInizio;
    this.calcolaCostoTotale();
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
  clickPrenotaOra(veicolo:VeicoloPrenotazioneDTO){
    this.veicoloSelezionato=veicolo;
    const dati:ContenitoreFormNuovaRichiestaNoleggio={
      veicolo:this.veicoloSelezionato,
      dataInizio:this.dataInizio,
      dataFine:this.dataFine,
      oraInizio:this.oraInizio,
      oraFine:this.oraFine,
      motivazione:""
    }
    this.mostraModale=dati;
  }

  chiudiModale() {
    this.mostraModale=null;
  }

  inviaPrenotazione(dati: ContenitoreFormNuovaRichiestaNoleggio) {
    const daInviare:RichiestaNoleggioDTO={
      idVeicolo:dati.veicolo.idVeicolo,
      dataRitiro:dati.dataInizio,
      dataConsegna:dati.dataFine,
      oraInizio:dati.oraInizio,
      oraFine:dati.oraFine,
      motivazione:dati.motivazione
    }
    this.service.inviaRichiestaNoleggio(daInviare).subscribe({
      next:(risposta:string)=>{
        console.log(risposta)
        this.chiudiModale();
      },
      error:(err)=>
      {
        console.error(err)}
    })

  }

  calcolaCostoTotale(): void {
    if (!this.dataInizio || !this.oraInizio || !this.dataFine || !this.oraFine) {
      this.costoStimato=0;
    }
    const start = new Date(`${this.dataInizio}T${this.oraInizio}`);
    const end = new Date(`${this.dataFine}T${this.oraFine}`);

    const differenzaMs = end.getTime() - start.getTime();

    const minutiTotali = differenzaMs / (1000 * 60);

    if (minutiTotali < 0) {
      console.error("La data di fine è precedente all'inizio!");
      this.costoStimato=0;
    }

    console.log(`Durata noleggio: ${minutiTotali} minuti`);

    this.costoStimato= minutiTotali * this.tariffaAlMinuto;
  }

  protected readonly event = event;
  protected readonly confirm = confirm;
}
