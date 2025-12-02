import {Component} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FormAutenticazione} from '@shared/form-background/form-autenticazione';
import {TabellaAuto} from '@shared/tabella-auto/tabella-auto';
import {FormCercaAuto} from '@shared/form-cerca-auto/form-cerca-auto';



@Component({
  selector: 'app-flotta-globale',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    FormAutenticazione,
    TabellaAuto,
    FormCercaAuto
  ],
  templateUrl: './flotta-globale.html',
  styleUrl: './flotta-globale.css',
})
export class FlottaGlobale {

}
