import {Component} from '@angular/core';
import {AziendaCard} from '@features/DipendenteSenzaAzienda/Componenti/azienda-card/azienda-card';
import {FormsModule} from '@angular/forms';
import {HomeService} from '@features/DipendenteSenzaAzienda/ServiceDipendenteSenzaAzienda/home-service';
import {ContenitoreDatiAzienda} from '@core/models/ContenitoreDatiAzienda';
import {
  RichiestaAffiliazione
} from '@features/DipendenteSenzaAzienda/Componenti/richiesta-affiliazione/richiesta-affiliazione';
@Component({
  selector: 'app-home-dipendente-senza-azienda',
  imports: [
    AziendaCard,
    FormsModule,
    RichiestaAffiliazione
  ],
  templateUrl: './home-dipendente-senza-azienda.html',
  styleUrl: './home-dipendente-senza-azienda.css',
})
export class HomeDipendenteSenzaAzienda {
  constructor(private service:HomeService) {}

  testoFiltro:string="";
  aziende:ContenitoreDatiAzienda[]=[]
  aziendeFiltrate:ContenitoreDatiAzienda[]=[]
  aziendaSelezionata: ContenitoreDatiAzienda={} as ContenitoreDatiAzienda;
  richiestaInAttesa: ContenitoreDatiAzienda | null=null;

  ngOnInit(){
    this.getRichiestaInAttesa()
  }



  filtraAziende(){
    if (!this.testoFiltro || this.testoFiltro.trim() === '') {
      this.aziendeFiltrate = this.aziende;
    } else {
      const term = this.testoFiltro.toLowerCase();
      this.aziendeFiltrate = this.aziende.filter(az =>
        az.nomeAzienda.toLowerCase().includes(term)
      );
    }
  }
  getAziende(){
    this.service.getAziende().subscribe({
      next:(risposta:ContenitoreDatiAzienda[])=>{
        console.log("Sono qu")
        this.aziende=risposta;
        this.aziendeFiltrate=this.aziende;
      },
      error:(err)=>console.error("Errore nel caricare le aziende")
    })
  }
  getRichiestaInAttesa(){
    this.service.getRichiestaAffiliazioneAttesa().subscribe({
      next:(risposta:ContenitoreDatiAzienda | null)=>{
        this.richiestaInAttesa=risposta
        if(this.richiestaInAttesa===null){
          this.getAziende();
        }
      },
      error:(err)=>console.error("Errore nel caricare richiesta in attesa")
    });
  }


  selezionaAzienda(azienda: ContenitoreDatiAzienda) {
    this.aziendaSelezionata=azienda;
  }

  gestisciInvioRichiesta(azienda: number) {
    if(azienda==null){
      console.log("ss")
      return
    }
    this.service.inviaRichiestaAffiliazione(azienda).subscribe({
      next:(risposta:any)=>{
        this.ngOnInit();
      },
      error:(err)=> console.error("Errore nell'invio")
    })

  }

  annullaRichiesta() {
    // @ts-ignore
    this.service.annullaRichiestaInAttesa(this.richiestaInAttesa.idAzienda).subscribe({
      next:(risposta:any)=>{
        this.ngOnInit();
      },
      error:(err)=>console.error("Errore annulla")
    })
  }
}
