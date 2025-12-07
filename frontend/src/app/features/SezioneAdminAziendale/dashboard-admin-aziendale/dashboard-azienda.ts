import { Component } from '@angular/core';
import {FormBackground} from '@shared/form-background/form-background';
import {GraficoStatoFlotta} from '@shared/grafico-stato-flotta/grafico-stato-flotta';
import {GraficoAndamentoUtilizzo} from '@shared/grafico-andamento-utilizzo/grafico-andamento-utilizzo';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [
    FormBackground,
    GraficoStatoFlotta,
    GraficoAndamentoUtilizzo
  ],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})
export class DashboardAzienda {

oreDiUtilizzo=3.400;



}
