import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {Logo} from '@shared/Componenti/Ui/logo/logo';
import {Footer} from '@shared/footer/footer';

@Component({
  selector: 'app-general-layout-no-login',
  standalone: true,
  imports: [
    RouterLink,
    Logo,
    RouterOutlet,
    Footer
  ],
  templateUrl: './general-layout-no-login.html',
  styleUrl: './general-layout-no-login.css',
})
export class GeneralLayoutNoLogin {

}
