import { Component } from '@angular/core';
import { RouterLink } from '@angular/router'; 
import {Logo} from '@shared/Componenti/Ui/logo/logo';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-navbar-no-log',
  standalone: true,
  imports: [RouterLink, Logo, CommonModule],
  templateUrl: './navbar-no-log.html',
  styleUrl: './navbar-no-log.css',
})
export class NavbarNoLog {
}
