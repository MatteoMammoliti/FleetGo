import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IntestazioneEBackground } from '@shared/Componenti/Ui/intestazione-ebackground/intestazione-ebackground';
import { ProfiloPersonale } from '../../componenti/profilo-personale/profilo-personale';
import { PatenteDocumentiComponent } from '../../componenti/patente-documenti/patente-documenti';
import { AffiliazioneAzienda } from '../../componenti/affiliazione-azienda/affiliazione-azienda';
import { ImpostazioniService } from '../../ServiceSezioneDipendente/impostazioni-service';

@Component({
  selector: 'app-impostazioni-dipendente',
  standalone: true,
  imports: [
    CommonModule,
    IntestazioneEBackground,
    ProfiloPersonale,
    PatenteDocumentiComponent,
    AffiliazioneAzienda
  ],
  templateUrl: './impostazioni-dipendente.html',
  styleUrl: './impostazioni-dipendente.css',
})
export class ImpostazioniDipendenteComponent implements OnInit {
  
  tabSelezionata: string = 'Profilo';
  sezione: string = 'Profilo';
  utente: any = {}; 

  constructor(private impostazioniService: ImpostazioniService) {}

  ngOnInit() {
    this.caricaDatiUtente();
  }

  caricaDatiUtente() {
    this.impostazioniService.getDipendente().subscribe({
      next: (datiServer) => {
        this.utente = datiServer;
        
        const utenteStorage = localStorage.getItem('utente'); 
        
        if (utenteStorage) {
          try {
            const datiCompleti = JSON.parse(utenteStorage);
            
            if (!this.utente.urlImmagine) {
              this.utente.urlImmagine = datiCompleti.urlImmagine || datiCompleti.imgPatente || datiCompleti.immagine;
            }
            if (!this.utente.numeroPatente) this.utente.numeroPatente = datiCompleti.numeroPatente;
            if (!this.utente.scadenzaPatente) this.utente.scadenzaPatente = datiCompleti.scadenzaPatente;
            if (!this.utente.nomeAzienda) this.utente.nomeAzienda = datiCompleti.nomeAzienda;
            if (!this.utente.sedeAzienda) this.utente.sedeAzienda = datiCompleti.sedeAzienda;
            if (!this.utente.pIva) this.utente.pIva = datiCompleti.pIva;

            console.log("DATI RECUPERATI DAL LOGIN:");
            console.log("- Foto:", this.utente.urlImmagine ? 'OK' : 'Mancante');
            console.log("- Azienda:", this.utente.nomeAzienda ? this.utente.nomeAzienda : 'Mancante');

          } catch (e) {
            console.error("Errore lettura localStorage", e);
          }
        }
      },
      error: (err) => console.error("Errore caricamento dati:", err)
    });
  }

  cambiaTab(tab: string) {
    this.tabSelezionata = tab;
    this.sezione = tab;
  }

  estraiDatiPatente() {
    return {
      numeroPatente: this.utente.numeroPatente,
      scadenzaPatente: this.utente.scadenzaPatente,
      urlImmagine: this.utente.urlImmagine
    };
  }

  aggiornaPatente(datiPatente: any) {
    const payload = {
        ...this.utente,
        numeroPatente: datiPatente.numeroPatente,
        scadenzaPatente: datiPatente.scadenzaPatente
    };

    this.impostazioniService.inviaModifiche(payload).subscribe({
        next: () => {
            alert('Modifiche inviate!'); 
            this.aggiornaLocalStorage(payload);
        },
        error: (err: any) => {
          console.error(err);
          alert("Dati inviati. Nota: L'aggiornamento della foto potrebbe richiedere verifiche amministrative.");
        }
    });
  }

  aggiornaFotoPatente(file: File) {
    const reader = new FileReader();
    reader.onload = (e: any) => {
      const base64String = e.target.result;
      this.utente.urlImmagine = base64String;
      this.utente.imgPatente = base64String; 
    };
    reader.readAsDataURL(file);
  }

  aggiornaLocalStorage(nuoviDati: any) {
    const utenteStorage = localStorage.getItem('utente');
    if (utenteStorage) {
      const datiCompleti = JSON.parse(utenteStorage);
      const datiAggiornati = { ...datiCompleti, ...nuoviDati };
      localStorage.setItem('utente', JSON.stringify(datiAggiornati));
    }
  }

  inviaModifiche(dati: any) {
    this.impostazioniService.inviaModifiche(dati).subscribe({
      next: () => {
        alert("Profilo aggiornato!");
        this.aggiornaLocalStorage(dati);
        this.caricaDatiUtente();
      },
      error: (err) => alert("Errore aggiornamento profilo")
    });
  }
}