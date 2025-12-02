import { Component } from '@angular/core';
import {RouterLink, RouterOutlet} from '@angular/router';
import {Logo} from '@shared/logo/logo';

@Component({
  selector: 'app-general-layout-no-login',
  standalone: true,
  imports: [
    RouterLink,
    Logo,
    RouterOutlet
  ],
  templateUrl: './general-layout-no-login.html',
  styleUrl: './general-layout-no-login.css',
})
export class GeneralLayoutNoLogin {

}
