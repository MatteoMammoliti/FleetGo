import { trigger, transition, style, animate, query, stagger } from '@angular/animations';

export const ANIMAZIONE_TABELLA = trigger('animazioneTabella', [
  transition('* => *', [
    query(':enter', [
      style({ opacity: 0, transform: 'translateY(-10px)' }),
      stagger(50, [
        animate('300ms ease-out', style({ opacity: 1, transform: 'none' }))
      ])
    ], { optional: true })
  ])
]);


