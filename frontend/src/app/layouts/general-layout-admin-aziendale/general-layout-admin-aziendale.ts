import { Component } from '@angular/core';
import { Sidebar } from '@shared/sidebar/sidebar';
import { inject } from '@angular/core';
import { AuthService } from '@core/auth/auth-service';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {Footer} from '@shared/footer/footer';

@Component({
  selector: 'app-general-layout-admin-aziendale',
  imports: [Sidebar, RouterOutlet, RouterLink, RouterLinkActive, Footer],
  standalone: true,
  templateUrl: './general-layout-admin-aziendale.html',
  styleUrl: './general-layout-admin-aziendale.css',
})

export class GeneralLayoutAdminAziendale {

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
