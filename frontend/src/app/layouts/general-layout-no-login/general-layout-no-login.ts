import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {Logo} from '@shared/Componenti/Ui/logo/logo';
import {Footer} from '@shared/footer/footer';
import { NavbarNoLog } from '@shared/Componenti/Ui/navbar-no-log/navbar-no-log';


@Component({
  selector: 'app-general-layout-no-login',
  standalone: true,
  imports: [
    RouterLink,
    Logo,
    RouterOutlet,
    Footer,
    NavbarNoLog
  ],
  templateUrl: './general-layout-no-login.html',
  styleUrl: './general-layout-no-login.css',
})
export class GeneralLayoutNoLogin {

}
