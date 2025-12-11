import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class validazione {

  checkEmail(email: string): boolean {
      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      return emailRegex.test(email);
  }
  checkPassword(password: string): boolean {
      return true;
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
      return passwordRegex.test(password);
  }

  checkNome(nome: string): boolean {
      const nomeRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,}$/;
      return nomeRegex.test(nome);
  }

  checkCognome(cognome: string): boolean {
      const cognomeRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ' -]{2,}$/;
      return cognomeRegex.test(cognome);
  }

  checkDataNascita(dataNascita: string): boolean {
    if (!dataNascita) return false;

    const nascita = new Date(dataNascita);
    const oggi = new Date();
    const dataMaggiorenne = new Date();

    dataMaggiorenne.setFullYear(oggi.getFullYear() - 18);
    nascita.setHours(0, 0, 0, 0);
    dataMaggiorenne.setHours(0, 0, 0, 0);

    return nascita <= dataMaggiorenne;
  }

  checkTarga(targa:string){
    targa = targa.toUpperCase();
    const targaRegex = /^[A-Z]{2}[0-9]{3}[A-Z]{2}$/;
    return targaRegex.test(targa);
  }

  checkPartitaIva(piva: string): boolean {
      const pivaRegex = /^[0-9]{11}$/;
      return pivaRegex.test(piva);
  }
}
