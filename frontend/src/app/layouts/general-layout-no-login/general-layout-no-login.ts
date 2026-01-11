import {Component} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {Footer} from '@shared/Footer/footer';
import {NavbarNoLog} from '@shared/Navigazione/navbar-no-log/navbar-no-log';


@Component({
  selector: 'app-general-layout-no-login',
  standalone: true,
  imports: [
    RouterOutlet,
    Footer,
    NavbarNoLog
  ],
  templateUrl: './general-layout-no-login.html',
  styleUrl: './general-layout-no-login.css',
})
export class GeneralLayoutNoLogin {
  constructor(private router: Router) {
  }

  vaiAllaDashboard() {
    this.router.navigate(['/dashboard']);
  }
}
