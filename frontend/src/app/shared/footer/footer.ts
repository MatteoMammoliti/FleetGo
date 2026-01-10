import {Component} from '@angular/core';
import {Logo} from '@shared/Navigazione/logo/logo';

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
