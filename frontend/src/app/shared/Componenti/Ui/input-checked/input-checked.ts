import {Component, EventEmitter, Input, Output} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-input-checked',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './input-checked.html',
})
export class InputChecked {

  @Input() titolo: string = '';
  @Input() type: string = 'text';
  @Input() placeholder: string = '';
  @Input() valore: any;
  @Input() haErrore: boolean = false;
  @Input() messaggioErrore: string = 'Campo richiesto';

  @Output() valoreChange = new EventEmitter<any>();
  @Output() suInput = new EventEmitter<void>();

  onModelChange(nuovoValore: any) {
    this.valoreChange.emit(nuovoValore);
    this.suInput.emit();
  }
}
