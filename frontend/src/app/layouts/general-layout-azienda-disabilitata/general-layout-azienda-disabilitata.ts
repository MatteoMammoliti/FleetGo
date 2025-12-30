import { Component } from '@angular/core';
import {Footer} from "@shared/footer/footer";
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {Sidebar} from "@shared/sidebar/sidebar";
import {AuthService} from '@core/auth/auth-service';
import {BannerErrore} from "@shared/Componenti/Ui/banner-errore/banner-errore";

@Component({
  selector: 'app-general-layout-azienda-disabilitata',
    imports: [
        Footer,
        RouterLink,
        RouterLinkActive,
        RouterOutlet,
        Sidebar,
        BannerErrore
    ],
  templateUrl: './general-layout-azienda-disabilitata.html',
  styleUrl: './general-layout-azienda-disabilitata.css',
})
export class GeneralLayoutAziendaDisabilitata {

  constructor(private authService: AuthService, private router: Router) {}

  erroreBanner='';

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.router.navigate(['/login']);
        this.gestisciErrore(err.error);
      }
    });
  }

  gestisciErrore(messaggio: string) {
    this.erroreBanner = messaggio;
    setTimeout(() => this.erroreBanner = '', 5000);
  }

}
