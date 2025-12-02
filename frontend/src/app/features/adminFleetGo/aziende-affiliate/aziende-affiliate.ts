import { Component, inject } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { TabellaAziendeComponent } from '@shared/tabella-aziende/tabella-aziende';
import {FormAggiungiAdminAzienda} from '@shared/form-aggiungi-admin-azienda/form-aggiungi-admin-azienda';

@Component({
  selector: 'app-aziende-affiliate',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    TabellaAziendeComponent,
    FormAggiungiAdminAzienda
  ],
  templateUrl: './aziende-affiliate.html',
  styleUrl: './aziende-affiliate.css',
})

export class AziendeAffiliate {}
