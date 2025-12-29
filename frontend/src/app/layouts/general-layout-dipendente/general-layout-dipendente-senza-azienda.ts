import {Component, inject} from '@angular/core';
import { Sidebar } from '@shared/sidebar/sidebar';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {Footer} from '@shared/footer/footer';
import {AuthService} from '@core/auth/auth-service';

@Component({
  selector: 'app-general-layout-dipendente',
  imports: [Sidebar, RouterLink, RouterLinkActive, RouterOutlet, Footer],
  standalone: true,
  templateUrl: './general-layout-dipendente-senza-aziendaù.html',
  styleUrl: './general-layout-dipendente-senza-aziendaù.css',
})

export class GeneralLayoutDipendenteSenzaAzienda {
  public authService= inject(AuthService);
  private router = inject(Router);

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
