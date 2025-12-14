import { Component } from '@angular/core';
import { Sidebar } from '@shared/sidebar/sidebar';
import { inject } from '@angular/core';
import { AuthService } from '@core/auth/auth-service';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {Footer} from '@shared/footer/footer';


@Component({
  selector: 'app-general-layout-fleet-go',
  imports: [Sidebar, RouterOutlet, RouterLink, RouterLinkActive, Footer],
  standalone: true,
  templateUrl: './general-layout-fleet-go.html',
  styleUrl: './general-layout-fleet-go.css',
})
export class GeneralLayoutFleetGo {
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
