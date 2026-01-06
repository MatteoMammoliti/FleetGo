import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import {NgOptimizedImage} from '@angular/common';


@Component({
  selector: 'app-banner-hero',
  standalone: true,
  imports: [RouterLink, NgOptimizedImage],
  templateUrl: './banner-hero.html',
  styleUrl: './banner-hero.css',
})
export class BannerHero implements OnInit {

  veicoliGestiti: string = "50+";

  constructor() {}

  ngOnInit() {}
}
