import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-intestazione-ebackground',
  imports: [],
  templateUrl: './intestazione-ebackground.html',
  styleUrl: './intestazione-ebackground.css',
})
export class IntestazioneEBackground {
  @Input() titolo = "";

}
