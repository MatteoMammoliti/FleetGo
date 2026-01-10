import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-template-card-con-riga',
  imports: [],
  templateUrl: './template-card-con-riga.html',
  styleUrl: './template-card-con-riga.css',
})
export class TemplateCardConRiga {
  @Input() rosso=false;
}
