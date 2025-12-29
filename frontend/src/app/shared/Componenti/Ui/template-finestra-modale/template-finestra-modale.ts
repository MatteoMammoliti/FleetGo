import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-template-finestra-modale',
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

}
