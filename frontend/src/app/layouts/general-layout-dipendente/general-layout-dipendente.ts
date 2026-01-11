import {Component} from '@angular/core';
import {Sidebar} from '@shared/Navigazione/sidebar/sidebar';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {Footer} from '@shared/Footer/footer';
import {AuthService} from '@core/auth/auth-service';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';

@Component({
  selector: 'app-general-layout-dipendente',
  imports: [Sidebar, RouterLink, RouterLinkActive, RouterOutlet, Footer, BannerErrore],
  standalone: true,
  templateUrl: './general-layout-dipendente.html',
  styleUrl: './general-layout-dipendente.css',
})

export class GeneralLayoutDipendente {

  constructor(private authService: AuthService, private router: Router) {
  }

  erroreBanner = '';

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

  vaiAllaDashboard() {
    this.router.navigate(['/dashboardDipendente/homepage']);
  }
}
