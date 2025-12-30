import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-supporto-fleetgo',
  templateUrl: './supporto-fleetgo.html',
  styleUrl: './supporto-fleetgo.css',
})
export class SupportoFleetgo {

  @Output() apriModale = new EventEmitter<void>();

}
