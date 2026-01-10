import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-template-titolo-sottotitolo',
  imports: [],
  templateUrl: './template-titolo-sottotitolo.html',
  styleUrl: './template-titolo-sottotitolo.css',
})
export class TemplateTitoloSottotitolo {
  @Input() titolo: string = "";
  @Input() sottotitolo: string = "";
  @Input() testoDestro: string = "";
  @Input() iconaDestra = "bi-building-fill";
}
