import {Component, inject, OnInit} from '@angular/core';
import {GraficoStatoFlotta} from '@shared/Componenti/Grafici/grafico-stato-flotta/grafico-stato-flotta';
import {GraficoAndamentoUtilizzo} from '@shared/Componenti/Grafici/grafico-andamento-utilizzo/grafico-andamento-utilizzo';
import {DashboardService} from '@features/SezioneAdminAziendale/ServiceSezioneAdminAziendale/dashboard-service';
import {ContenitoreStatisticheNumeriche} from '@core/models/ContenitoreStatisticheNumeriche';
import {ContenitoreDatiGraficoAndamento} from '@core/models/ContenitoreDatiGraficoAndamento.models';
import {
  CardStatisticheDashboardFleet
} from '@shared/Componenti/Ui/card-statistiche-dashboard-fleet/card-statistiche-dashboard-fleet';
import {ContenitoreDatiGraficoLuoghiAuto} from '@core/models/ContenitoreDatiGraficoLuoghiAuto.models';
import {GraficoACandelaLuoghi} from '@shared/Componenti/Grafici/grafico-a-candela-luoghi/grafico-a-candela-luoghi';

@Component({
  selector: 'app-dashboard-azienda',
  imports: [
    GraficoStatoFlotta,
    GraficoAndamentoUtilizzo,
    CardStatisticheDashboardFleet,
    GraficoACandelaLuoghi
  ],
  templateUrl: './dashboard-azienda.html',
  styleUrl: './dashboard-azienda.css',
})

export class DashboardAzienda implements OnInit{

  constructor(private service:DashboardService) {}

  ngOnInit(){}
}
