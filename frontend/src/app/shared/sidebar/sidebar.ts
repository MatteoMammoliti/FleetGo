import { Component, HostListener} from '@angular/core';
import {Logo} from '@shared/Componenti/Ui/logo/logo';

@Component({
  selector: 'app-sidebar',
  imports: [Logo],
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
    if (event.target.innerWidth < 1280) {
      this.isSidebarOpen = false;
    }
  }
}
