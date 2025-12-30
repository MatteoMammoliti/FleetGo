import { Component } from '@angular/core';
import { Sidebar } from '@shared/sidebar/sidebar';
import { inject } from '@angular/core';
import { AuthService } from '@core/auth/auth-service';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {Footer} from '@shared/footer/footer';
import {validazione} from '@core/utils/validazione';
import {BannerErrore} from '@shared/Componenti/Ui/banner-errore/banner-errore';


@Component({
  selector: 'app-general-layout-fleet-go',
  imports: [Sidebar, RouterOutlet, RouterLink, RouterLinkActive, Footer, BannerErrore],
  standalone: true,
  templateUrl: './general-layout-fleet-go.html',
  styleUrl: './general-layout-fleet-go.css',
})
export class GeneralLayoutFleetGo {

  constructor(private authService: AuthService, private router: Router) {}

  erroreBanner='';

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.gestisciErrore(err.error)
        this.router.navigate(['/login']);
      }
    });
  }

  gestisciErrore(messaggio: string) {
    this.erroreBanner = messaggio;
    setTimeout(() => this.erroreBanner = '', 5000);
  }
}
