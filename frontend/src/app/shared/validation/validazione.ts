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
        const data = new Date(dataNascita);
        const oggi = new Date();
        return data < oggi;
    }   


}