import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TableSortService {
  private direzioneAsc = true;
  private colonnaAttiva = '';

  sortArray<T>(array: T[], chiave: string, direzioneAttuale?: boolean) {
    if (this.colonnaAttiva === chiave) {
      this.direzioneAsc = direzioneAttuale !== undefined ? !direzioneAttuale : !this.direzioneAsc;
    } else {
      this.colonnaAttiva = chiave;
      this.direzioneAsc = true;
    }

    const sortedArray = [...array].sort((a, b) => {
      const valA = chiave.split('.').reduce((obj: any, i) => obj?.[i], a);
      const valB = chiave.split('.').reduce((obj: any, i) => obj?.[i], b);

      if (valA == null) return 1;
      if (valB == null) return -1;

      if (typeof valA === 'string' && typeof valB === 'string') {
        return this.direzioneAsc ? valA.localeCompare(valB) : valB.localeCompare(valA);
      }

      if (valA < valB) return this.direzioneAsc ? -1 : 1;
      if (valA > valB) return this.direzioneAsc ? 1 : -1;
      return 0;
    });

    return  sortedArray;
  }
}
