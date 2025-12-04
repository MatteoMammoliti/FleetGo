import { Component } from '@angular/core';
import { Sidebar } from '@shared/sidebar/sidebar';

@Component({
  selector: 'app-general-layout-dipendente',
  imports: [Sidebar],
  standalone: true,
  templateUrl: './general-layout-dipendente.html',
  styleUrl: './general-layout-dipendente.css',
})

export class GeneralLayoutDipendente {}
