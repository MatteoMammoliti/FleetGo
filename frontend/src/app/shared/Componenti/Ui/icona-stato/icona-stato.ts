import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-icona-stato',
  imports: [],
  templateUrl: './icona-stato.html',
  styleUrl: './icona-stato.css',
})
export class IconaStato {
  @Input() stato='verde';
  @Input() testo:any;

}
