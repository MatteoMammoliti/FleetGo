import {Component} from '@angular/core';
import {Sidebar} from '@shared/Navigazione/sidebar/sidebar';
import {Router, RouterOutlet} from '@angular/router';
import {Footer} from '@shared/Footer/footer';
import {AuthService} from '@core/auth/auth-service';
import {BannerErrore} from '@shared/Componenti/Banner/banner-errore/banner-errore';

@Component({
  selector: 'app-general-layout-dipendente',
  imports: [Sidebar, RouterOutlet, Footer, BannerErrore],
  standalone: true,
  templateUrl: './general-layout-dipendente-senza-azienda.html',
  styleUrl: './general-layout-dipendente-senza-azienda.css',
})

export class GeneralLayoutDipendenteSenzaAzienda {
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
    this.router.navigate(['/dashboard']);
  }
}
