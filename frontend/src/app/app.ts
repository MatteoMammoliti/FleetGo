import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {BarraNoLog} from "./layouts/barra-no-log/barra-no-log";

@Component({
  selector: 'app-root',
    imports: [RouterOutlet, BarraNoLog],
  templateUrl: './app.html',
  standalone: true,
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
