import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-banner-noleggi-da-approvare',
  imports: [],
  templateUrl: './banner-noleggi-da-approvare.html',
  styleUrl: './banner-noleggi-da-approvare.css',
})
export class BannerNoleggiDaApprovare {

  @Input() noleggiDaApprovare: number = 0;
  @Output() apriModale = new EventEmitter<void>();

}
