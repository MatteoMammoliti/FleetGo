import {Component, EventEmitter, Input, Output} from '@angular/core';
import {AziendaDTO} from '@core/models/aziendaDTO';
import {CommonModule} from '@angular/common';
import {TemplateFinestraModale} from '@shared/Componenti/Ui/template-finestra-modale/template-finestra-modale';
import {IconaStato} from '@shared/Componenti/Ui/icona-stato/icona-stato';
import {MessaggioCardVuota} from '@shared/Componenti/Ui/messaggio-card-vuota/messaggio-card-vuota';
import {ANIMAZIONE_TABELLA} from '@shared/Animazioni/animazioneTabella';

@Component({
  selector: 'app-tabella-aziende',
  standalone: true,
  imports: [CommonModule, TemplateFinestraModale, IconaStato, MessaggioCardVuota],
  templateUrl: './tabella-aziende.html',
  styleUrl: './tabella-aziende.css',
  animations: [ANIMAZIONE_TABELLA]
})

export class TabellaAziendeComponent {

  @Input() listaAziende:AziendaDTO[]|null=null;
  @Input() modalitaArchivio = false;
  @Output() riabilitaAzienda = new EventEmitter<number>();
  @Output() disabilitaAzienda = new EventEmitter<number>();

  modaleDisabilitaAzienda = false;
  modaleRiabilitaAzienda = false;

  aziendaInteressataModale:any=null;
  nomeAziendaInteressataModale:any=null;

  apriModaleDisabilitaAzienda(idAzienda:any,nomeAzienda:any){
    if(idAzienda!=null){
      this.aziendaInteressataModale=idAzienda;
      this.modaleDisabilitaAzienda=true;
      this.nomeAziendaInteressataModale=nomeAzienda;
    }
  }
  chiudiModale(){
    this.aziendaInteressataModale=null;
    this.modaleDisabilitaAzienda=false;
    this.modaleRiabilitaAzienda=false;
    this.nomeAziendaInteressataModale=null;
  }
  confermaModaleDisabilitaAzienda(){
    this.disabilitaAzienda.emit(this.aziendaInteressataModale);
    this.chiudiModale();
  }

  apriModaleRiabilitaAzienda(idAzienda:any,nomeAzienda:any) {
    if (idAzienda != null) {
      this.aziendaInteressataModale = idAzienda;
      this.modaleRiabilitaAzienda= true;
      this.nomeAziendaInteressataModale=nomeAzienda;
    }
  }
  confermaModaleRiabilitaAzienda(){
    this.riabilitaAzienda.emit(this.aziendaInteressataModale);
    this.chiudiModale();
  }


  }
