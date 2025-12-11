import { Component } from '@angular/core';
import { Sidebar } from '@shared/sidebar/sidebar';
import { inject } from '@angular/core';
import { AuthService } from '@core/auth/auth-service';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-general-layout-admin-aziendale',
  imports: [Sidebar, RouterOutlet, RouterLink, RouterLinkActive],
  standalone: true,
  templateUrl: './general-layout-admin-aziendale.html',
  styleUrl: './general-layout-admin-aziendale.css',
})
export class GeneralLayoutAdminAziendale {
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
