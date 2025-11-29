import { Component } from '@angular/core';
import {RouterLink} from '@angular/router';
import {Logo} from '@shared/logo/logo';

@Component({
  selector: 'app-barra-no-log',
  standalone: true,
  imports: [
    RouterLink,
    Logo
  ],
  templateUrl: './barra-no-log.html',
  styleUrl: './barra-no-log.css',
})
export class BarraNoLog {

}
