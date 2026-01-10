import {Component, EventEmitter, HostListener, Output} from '@angular/core';
import {Logo} from '@shared/Navigazione/logo/logo';

@Component({
  selector: 'app-sidebar',
  imports: [Logo],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
  standalone: true
})
export class Sidebar {
  isSidebarOpen: boolean = true;
  @Output() clickLogo = new EventEmitter();

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  closeSidebar() {
    if (window.innerWidth < 1280) {
      this.isSidebarOpen = false;
    }
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    if (event.target.innerWidth < 1280) {
      this.isSidebarOpen = false;
    }
  }
}
