import {Component, Input} from '@angular/core';
import {DashboardFleetGoService} from '@features/SezioneFleetGo/ServiceSezioneFleetGo/dashboardFleetGo-service';
import {FatturaDaGenerareDTO} from '@core/models/FatturaDaGenerareDTO';

@Component({
  selector: 'app-fatture-da-generare',
  imports: [],
  standalone:true,
  templateUrl: './fatture-da-generare.html',
  styleUrl: './fatture-da-generare.css',
})
export class FattureDaGenerare {
  constructor(private fleetGoService:DashboardFleetGoService) {}

  @Input() fatture:FatturaDaGenerareDTO[] = []

}
