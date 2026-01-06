import { ApplicationConfig, LOCALE_ID, importProvidersFrom, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withInMemoryScrolling } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { registerLocaleData } from '@angular/common';
import localeIt from '@angular/common/locales/it';
import { routes } from './app.routes';

registerLocaleData(localeIt);

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withInMemoryScrolling({
      anchorScrolling: 'enabled',
      scrollPositionRestoration: 'enabled'
    })),
    importProvidersFrom(FormsModule),
    provideHttpClient(),
    provideCharts(withDefaultRegisterables()),

    { provide: LOCALE_ID, useValue: 'it-IT' }
  ]
};
