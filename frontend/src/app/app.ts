import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {BarraNoLog} from "./layouts/barra-no-log/barra-no-log"; 
import { AuthService } from '@core/services/auth-service';
import { BarraLogFleetGo } from './layouts/barra-log-fleet-go/barra-log-fleet-go';  

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, BarraNoLog, BarraLogFleetGo],
  templateUrl: './app.html',
  standalone: true,
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
  /*public authService = inject(AuthService);*/
  constructor(public authService: AuthService) {}
}

