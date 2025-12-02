import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {GeneralLayoutNoLogin} from "./layouts/general-layout-no-login/general-layout-no-login";
import { AuthService } from '@core/services/auth-service';
import { GeneralLayoutFleetGo } from './layouts/general-layout-fleet-go/general-layout-fleet-go';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  standalone: true,
  styleUrl: './app.css'
})
export class App {}

