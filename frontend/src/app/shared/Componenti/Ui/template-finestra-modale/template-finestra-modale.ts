import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-template-finestra-modale',
  standalone : true,
    imports: [
    ],
  templateUrl: './template-finestra-modale.html',
  styleUrl: './template-finestra-modale.css',
})
export class TemplateFinestraModale {
  @Input() titolo="";
  @Input() sottotitolo="";
  @Output() chiudi=new EventEmitter();
  @Output() annulla=new EventEmitter();
  @Output() conferma=new EventEmitter();

  @Input() tastoBianco="";
  @Input() tastoBlu="";

  @Input() larghezza="max-w-xl";

  @Input() confermaDisabilitata: boolean = false;
  

    


  chiusuraInCorso = false;

  private eseguiChiusura(eventEmitter: EventEmitter<any>) {
    this.chiusuraInCorso = true;
    setTimeout(() => {
      eventEmitter.emit();
    }, 250);
  }

  chiudiModale() { this.eseguiChiusura(this.chiudi); }
  annullaModale() { this.eseguiChiusura(this.annulla); }
  confermaModale() { this.eseguiChiusura(this.conferma); }
}
