import { Component } from '@angular/core';

@Component({
  selector: 'app-tabella-storico-fatture',
  imports: [],
  templateUrl: './tabella-storico-fatture.html',
  styleUrl: './tabella-storico-fatture.css',
})
export class TabellaStoricoFatture {
  protected fatture: any;

  protected onDownload(numeroFattura: any) {
    
  }
}
