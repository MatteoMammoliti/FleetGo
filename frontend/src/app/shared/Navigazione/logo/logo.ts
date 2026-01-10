import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-logo',
  imports: [],
  standalone: true,
  templateUrl: './logo.html',
  styleUrl: './logo.css',
})
export class Logo {

  @Output() clickLogo = new EventEmitter();
}
