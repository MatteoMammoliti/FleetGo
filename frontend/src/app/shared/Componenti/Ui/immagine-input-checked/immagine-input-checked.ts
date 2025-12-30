import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-immagine-input-checked',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './immagine-input-checked.html'
})
export class ImmagineInputChecked {
  @Input() titolo: string = '';
  @Input() haErrore: boolean = false;
  @Input() messaggioErrore: string = 'Immagine richiesta';

  @Output() fileSelezionato = new EventEmitter<File>();

  anteprimaUrl: string | null = null;

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.fileSelezionato.emit(file);

      const reader = new FileReader();
      reader.onload = () => {
        this.anteprimaUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }
}
