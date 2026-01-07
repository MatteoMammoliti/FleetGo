import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-scelta-tendina',
  standalone:true,
  imports: [
    FormsModule
  ],
  templateUrl: './scelta-tendina.html',
  styleUrl: './scelta-tendina.css',
})
export class SceltaTendina {
  @Input() lista:any[] = [];
  @Input() valore:any=null;
  @Input() placeholder:string = "";
  @Input() chiaveVisuale:string="";
  @Input() chiaveUtilizzata:string="";
  @Input() haErrore=false;
  @Output() valoreChange = new EventEmitter<any>();
  @Input() messaggioErrore: string = '';
  @Input() titolo: string = "";

  aggiorna(nuovoVal:any){
    this.valore = nuovoVal;
    this.valoreChange.emit(this.valore);
  }
}
