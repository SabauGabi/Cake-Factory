# 🍰 Cake Factory Management System

## 📝 Descriere
Acest proiect este o aplicație desktop dezvoltată în **Java**, concepută pentru gestionarea eficientă a unei **fabrici de torturi**. Aplicația utilizează o interfață grafică modernă (**JavaFX**) și o arhitectură robustă, fiind un exemplu practic de aplicare a principiilor de **Clean Code** și **Separation of Concerns**.

---

## ✨ Funcționalități Cheie
- **Interfață Grafică (JavaFX):** Utilizarea fișierelor FXML pentru separarea designului de logica de control.
- **Arhitectură Stratificată (3-Tier):** Decuplarea completă a straturilor de Domain, Repository și Service.
- **Persistență Polimorfică:** Gestiunea datelor prin stocare în memorie sau în fișiere (Text/Binary).
- **Procesare Modernă:** Filtrarea și sortarea inventarului de torturi folosind **Java Streams API**.
- **Validare Date:** Sistem custom de excepții pentru asigurarea integrității datelor introduse.
- **Unit Testing:** Validarea logicii de business prin teste automate cu **JUnit 5**.

---

## 🛠️ Stack Tehnologic
- **Limbaj:** Java 17+
- **GUI:** JavaFX 17 (Scene Builder & CSS)
- **Unit Testing:** JUnit 5
- **Build Tool:** Maven 
- **Versionare:** Git

---

## 📂 Structura Proiectului
Organizarea pachetelor respectă modelul **Layered Architecture**:

```text
├── domain/       # Entități (ex: Cake) și reguli de validare
├── repository/   # Gestiunea datelor (Interfețe, FileRepo, MemoryRepo)
├── service/      # Logica de business (CakeService)
├── ui/           # Interfața JavaFX (Controllere și fișiere FXML)
└── Main.java     # Punctul de lansare a aplicației
