import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-bottone-pillola',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './bottone-pillola.html'
})
export class BottonePillola {
  @Input() label: string = '';
  @Input() attivo: boolean = false;
  @Input() icona: string = '';
  @Input() variante: string = 'blu'; 

  @Output() clickBtn = new EventEmitter<void>();

  ottieniClassi(): string {
    const base = 'shadow-md border ';
    const inattivo = 'bg-white text-slate-500 border-transparent hover:bg-white/80 ';

    switch (this.variante) {
      case 'giallo': 
        return this.attivo 
          ? base + 'bg-yellow-500 text-white border-yellow-500 shadow-yellow-100' 
          : inattivo + 'hover:text-yellow-600';
      
      case 'verde': 
        return this.attivo 
          ? base + 'bg-emerald-500 text-white border-emerald-500 shadow-emerald-100' 
          : inattivo + 'hover:text-emerald-600';

      case 'viola':
        return this.attivo 
          ? base + 'bg-indigo-500 text-white border-indigo-500 shadow-indigo-100' 
          : inattivo + 'hover:text-indigo-600';

      case 'grigio':
        return this.attivo 
          ? base + 'bg-slate-600 text-white border-slate-600 shadow-slate-200' 
          : inattivo + 'hover:text-slate-700';

      case 'blu':
      default: 
        return this.attivo 
          ? base + 'bg-blu text-white border-blu shadow-blue-100' 
          : inattivo + 'hover:text-blu';
    }
  }
}