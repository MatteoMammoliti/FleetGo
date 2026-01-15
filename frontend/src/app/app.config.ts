import {
  ApplicationConfig,
  importProvidersFrom, LOCALE_ID,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';
import localeIt from '@angular/common/locales/it';
import {provideRouter, withInMemoryScrolling} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {provideHttpClient} from '@angular/common/http';
import {provideAnimations} from '@angular/platform-browser/animations';

import {routes} from './app.routes';
import {provideCharts, withDefaultRegisterables} from 'ng2-charts';
import {registerLocaleData} from '@angular/common';
registerLocaleData(localeIt);
export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes, withInMemoryScrolling({
      anchorScrolling: 'enabled',
      scrollPositionRestoration: 'enabled'
    })),
    importProvidersFrom(FormsModule),
    provideHttpClient(),
    provideCharts(withDefaultRegisterables()),
    provideAnimations(),
    { provide: LOCALE_ID, useValue: 'it-IT' }
  ]
};
