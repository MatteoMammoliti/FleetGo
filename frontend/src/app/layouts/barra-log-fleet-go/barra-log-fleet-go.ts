import { Component } from '@angular/core';
import { Sidebar } from '@shared/sidebar/sidebar';
import { inject } from '@angular/core';
import { AuthService } from '@core/services/auth-service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-barra-log-fleet-go',
  imports: [Sidebar],
  standalone: true,
  templateUrl: './barra-log-fleet-go.html',
  styleUrl: './barra-log-fleet-go.css',
})
export class BarraLogFleetGo {
  public authService= inject(AuthService);
  private router = inject(Router);

  logout() {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
