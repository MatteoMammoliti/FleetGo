import {Component} from '@angular/core';
import {Sidebar} from '@shared/Navigazione/sidebar/sidebar';
import {AuthService} from '@core/auth/auth-service';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {Footer} from '@shared/footer/footer';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';

@Component({
  selector: 'app-general-layout-admin-aziendale',
  imports: [Sidebar, RouterOutlet, RouterLink, RouterLinkActive, Footer, BannerErrore],
  standalone: true,
  templateUrl: './general-layout-admin-aziendale.html',
  styleUrl: './general-layout-admin-aziendale.css',
})

export class GeneralLayoutAdminAziendale {

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
    this.router.navigate(['/dashboardAzienda/dashboard']);
  }
}
