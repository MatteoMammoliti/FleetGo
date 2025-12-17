import { Component } from '@angular/core';
import {RouterLink} from '@angular/router';
import {Logo} from '@shared/Componenti/Ui/logo/logo';

@Component({
  selector: 'app-footer',
  imports: [
    Logo
  ],
  templateUrl: './footer.html',
  styleUrl: './footer.css',
})
export class Footer {
  protected annoAttuale = new Date().getFullYear();
}
