# 🏠 HomeGuard
**Sviluppo di una piattaforma IoT per la sicurezza domestica**  

> 📚 Questo progetto è stato sviluppato come **tesi di laurea in Informatica** presso l’Università di Bologna.

## 📌 Descrizione
HomeGuard è un sistema IoT per il monitoraggio e la sicurezza domestica.  
Il progetto combina **hardware ESP32**, un **backend serverless su AWS** e un’**app Android in Kotlin** per offrire un sistema di allarme semplice, automatico e sicuro.  

L’obiettivo principale è ridurre al minimo l’interazione manuale: l’allarme si attiva/disattiva automaticamente in base alla posizione degli utenti, garantendo un’esperienza intuitiva e senza manutenzione.  

---

## 🔧 Componenti principali
### 1. **Hardware (ESP32)**
- Allarme con:  
  - Sensore magnetico per la porta  
  - Lettore NFC (tessere RFID registrabili)  
  - Buzzer sonoro  
  - Display OLED  
- Fotocamera **ESP32-CAM** per foto di sicurezza  

### 2. **Cloud Backend (AWS)**
- **IoT Core** → connessione sicura dei dispositivi  
- **DynamoDB** → database NoSQL serverless  
- **Lambda** → logica serverless per gestione eventi  
- **SNS** → notifiche push  
- **Cognito** → autenticazione e gestione utenti  
- **Amplify + AppSync** → API GraphQL e integrazione con l’app mobile  

### 3. **Applicazione Android (Kotlin + Jetpack Compose)**
- Gestione utenti e dispositivi collegati  
- Attivazione/disattivazione allarme (manuale o automatica via geofencing)  
- Registrazione e utilizzo di tessere NFC  
- Visualizzazione in tempo reale di:  
  - Stato allarme  
  - Chi è in casa  
  - Record degli allarmi (foto, orari, eventi)  

---

## 🔒 Sicurezza
- Comunicazioni cifrate con **TLS** e certificati **X.509**  
- Accesso controllato tramite **IAM** e policy AWS  
- Threat model basato su **STRIDE** per mitigare attacchi comuni (spoofing, tampering, DoS, ecc.)  

---

## 🚀 Possibili sviluppi futuri
- Integrazione di ulteriori sensori (impronte digitali, sensori di movimento, rilevatori di gas/fumo)  
- Estensione a sistemi di smart home (gestione luci, elettrodomestici)  
- Dashboard web per il monitoraggio remoto
