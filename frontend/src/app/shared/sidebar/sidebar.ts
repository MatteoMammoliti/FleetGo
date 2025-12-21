import { Component, HostListener} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Logo} from '@shared/Componenti/Ui/logo/logo';

@Component({
  selector: 'app-sidebar',
  imports: [Logo,CommonModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
  standalone: true
})
export class Sidebar {
  isSidebarOpen: boolean = true;

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    if (event.target.innerWidth < 1024) {
      this.isSidebarOpen = false;
    }
  }
}
