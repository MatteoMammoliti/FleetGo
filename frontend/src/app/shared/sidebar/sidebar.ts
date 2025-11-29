import { Component } from '@angular/core';
import { ɵEmptyOutletComponent } from "@angular/router";
import {NgOptimizedImage} from '@angular/common';
import {Logo} from '@shared/logo/logo';

@Component({
  selector: 'app-sidebar',
  imports: [
    NgOptimizedImage,
    Logo
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
  standalone: true
})
export class Sidebar {

}
