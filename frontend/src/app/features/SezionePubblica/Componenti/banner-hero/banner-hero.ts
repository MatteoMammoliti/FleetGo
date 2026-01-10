import {Component} from '@angular/core';
import {RouterLink} from '@angular/router';


@Component({
  selector: 'app-banner-hero',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './banner-hero.html',
  styleUrl: './banner-hero.css',
})
export class BannerHero {

  veicoliGestiti: string = "50+";

}
