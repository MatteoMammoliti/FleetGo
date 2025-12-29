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
  @Input() titolo='Operazione Fallita'
  @Input() icona='bi-exclamation-triangle';
}
