# 🚗 FleetGo - Corporate Car Sharing Platform

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.3-green)
![Angular](https://img.shields.io/badge/Angular-17-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen)

**FleetGo** è una piattaforma web completa per la gestione del car sharing aziendale. L'applicazione permette alle aziende di gestire la propria flotta, ai dipendenti di prenotare i veicoli e agli amministratori di monitorare l'intero ecosistema.

Il progetto è stato sviluppato come elaborato finale per il corso di **Web Applications**.

---

## 📋 Indice
- [Caratteristiche Principali](#-caratteristiche-principali)
- [Architettura e Tecnologie](#-architettura-e-tecnologie)
- [Requisiti di Sistema](#-requisiti-di-sistema)
- [Installazione e Avvio](#-installazione-e-avvio)
- [Configurazione](#-configurazione)
- [Testing](#-testing)
- [Team](#-team)

---

## ✨ Caratteristiche Principali

Il sistema gestisce tre attori principali con permessi distinti:

### 👤 Dipendente
- **Prenotazione Intelligente:** Ricerca veicoli disponibili con filtri per data, ora e luogo.
- **Gestione Hub:** Visualizzazione dei veicoli su mappa interattiva (Google Maps).
- **Storico:** Visualizzazione delle prenotazioni passate e future.
- **Assistenza:** Segnalazione guasti o richieste di supporto diretto.

### 🏢 Admin Azienda
- **Gestione Flotta:** Gestione dei veicoli assegnati con impostazioni dei luoghi di ritiro e consegna.
- **Gestione Dipendenti:** Approvazione account e monitoraggio attività.
- **Fatturazione:** Generazione automatica di fatture mensili in PDF basate sull'utilizzo.
- **Pagamenti:** Integrazione con **Stripe** per il saldo delle fatture.
- **Dashboard:** Statistiche e grafici sull'utilizzo della flotta.

### 🔧 Admin FleetGo (Super Admin)
- **Gestione Globale:** Approvazione nuove aziende affiliate.
- **Manutenzione:** Monitoraggio stato globale dei veicoli.

---

## 🛠 Architettura e Tecnologie

Il progetto segue un'architettura a **microservizi monolitici** (Frontend e Backend separati ma integrabili).

### Backend (Java / Spring Boot)
- **Framework:** Spring Boot 3.2.3 (Web, Security, Mail).
- **Database:** PostgreSQL.
- **Pattern di Persistenza:** **DAO (Data Access Object)** con **JDBC puro**.
- **Sicurezza:** Spring Security con autenticazione basata su Sessioni/Cookie.
- **Librerie Esterne:**
  - `iText / OpenPDF`: Generazione PDF dinamici.
  - `Stripe Java`: Gestione pagamenti.
  - `Cloudinary`: Hosting immagini (foto auto, loghi).
  - `JavaMailSender`: Invio email transazionali (OTP, benvenuto).

### Frontend (Angular)
- **Framework:** Angular 17+ (Standalone Components).
- **Stile:** CSS3 personalizzato con layout responsivi.
- **Integrazioni:** Google Maps API.

---

## 💻 Requisiti di Sistema

- **Java JDK:** 21
- **Node.js:** 18+
- **Maven:** 3.8+
- **PostgreSQL:** 14+
