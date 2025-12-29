import { Component } from '@angular/core';
import {Footer} from "@shared/footer/footer";
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {Sidebar} from "@shared/sidebar/sidebar";
import {AuthService} from '@core/auth/auth-service';

@Component({
  selector: 'app-general-layout-azienda-disabilitata',
    imports: [
        Footer,
        RouterLink,
        RouterLinkActive,
        RouterOutlet,
        Sidebar
    ],
  templateUrl: './general-layout-azienda-disabilitata.html',
  styleUrl: './general-layout-azienda-disabilitata.css',
})
export class GeneralLayoutAziendaDisabilitata {

  constructor(private authService: AuthService,
              private router: Router,) {}

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        console.log("Logout avvenuto con successo sul backend.");
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error("Errore durante il logout:", err);
        this.router.navigate(['/login']);
      }
    });
  }

}
