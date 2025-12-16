import { Component } from '@angular/core';
import { RouterLink } from '@angular/router'; 
import {Logo} from '@shared/Componenti/Ui/logo/logo';

@Component({
  selector: 'app-navbar-no-log',
  standalone: true,
  imports: [RouterLink, Logo],
  templateUrl: './navbar-no-log.html',
  styleUrl: './navbar-no-log.css',
})
export class NavbarNoLog {

}
