import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-banner-errore',
  imports: [],
  templateUrl: './banner-errore.html',
  styleUrl: './banner-errore.css',
})
export class BannerErrore {
  @Output() chiudi= new EventEmitter();


  @Input() errore: string = '';
  @Input() titoloErrore='Operazione Fallita'
  @Input() iconaErrore='bi-exclamation-triangle';

  @Input() successo='';
  @Input() titoloSuccesso="Operazione Eseguita"
  @Input() iconaSuccesso="bi-check2"

}
