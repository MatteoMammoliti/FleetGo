import { Component } from '@angular/core';
import { RouterLink} from '@angular/router'; 
import {CommonModule} from "@angular/common";
import {Logo} from "../logo/logo";

@Component({
  selector: 'app-navbar-no-log',
  standalone: true,
  imports: [RouterLink, CommonModule, Logo],
  templateUrl: './navbar-no-log.html',
  styleUrl: './navbar-no-log.css',
})
export class NavbarNoLog {
  sidebarVisibile: boolean = false;

  toggleSidebar(){
    this.sidebarVisibile = !this.sidebarVisibile;
  }
}
