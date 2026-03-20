package ui;

import domain.Comanda;
import domain.Tort;
import exception.RepositoryException;

import service.ComandaService;
import service.TortService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleUI {
    private final TortService tortService;
    private final ComandaService comandaService;
    private final Scanner scanner;

    public ConsoleUI(TortService tortService, ComandaService comandaService) {
        this.tortService = tortService;
        this.comandaService = comandaService;
        this.scanner = new Scanner(System.in);
    }

    private void showMenu() {
        System.out.println("\n--- MENIU ---");
        System.out.println("1. Adaugă Tort");
        System.out.println("2. Actualizează Tort");
        System.out.println("3. Afișează toate Torturile");
        System.out.println("4. Caută Tort după ID");
        System.out.println("5. Șterge Tort după ID");
        System.out.println("6. Adaugă Comandă");
        System.out.println("7. Actualizează Comandă");
        System.out.println("8. Caută Comandă după ID");
        System.out.println("9. Șterge Comandă (DELETE)");
        System.out.println("10. Afișează toate Comenzile");
        System.out.println("0. Ieșire");
        System.out.print("Alegeți o opțiune: ");
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.err.println("Intrare invalidă. Vă rugăm introduceți un număr întreg.");
            scanner.next();
            System.out.print(prompt);
        }
        int result = scanner.nextInt();
        scanner.nextLine();
        return result;
    }

    private List<Tort> getTorturiFromInput(String idsStr) {
        return Arrays.stream(idsStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try {
                        return tortService.findById(Integer.parseInt(s));
                    } catch (RepositoryException e) {
                        System.err.println("Avertisment: Tortul cu ID " + s + " nu a fost găsit și va fi ignorat.");
                        return null;
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }



    private void handleAddComanda() {
        try {
            int id = readInt("ID Comandă: ");
            System.out.print("Introduceți ID-urile torturilor dorite (separate prin virgulă, ex: 1,3,5): ");
            String idsStr = scanner.nextLine();

            List<Tort> torturiComandate = getTorturiFromInput(idsStr);

            Comanda comanda = new Comanda(id, torturiComandate, new Date(System.currentTimeMillis() + 86400000));
            comandaService.addComanda(comanda);

            System.out.println("Comandă adăugată cu succes. Torturi incluse: " + torturiComandate.size());

        } catch (RepositoryException e) {
            System.err.println("Eroare de validare: " + e.getMessage());
        }
         catch (Exception e) {
            System.err.println("Eroare neașteptată la adăugarea comenzii.");
        }
    }

    private void handleUpdateComanda() {
        try {
            int id = readInt("ID Comandă de actualizat: ");
            System.out.print("Introduceți NOILE ID-uri ale torturilor (separate prin virgulă): ");
            String idsStr = scanner.nextLine();

            List<Tort> listaTorturiNoi = getTorturiFromInput(idsStr);


            Comanda comandaExistenta = comandaService.findById(id);

            Comanda comandaActualizata = new Comanda(id, listaTorturiNoi, comandaExistenta.getData());

            comandaService.updateComanda(comandaActualizata);
            System.out.println("Comanda " + id + " actualizată cu succes.");

        } catch (RepositoryException e) {
            System.err.println("Eroare tranzacțională: " + e.getMessage());
        }
    }

    private void handleFindByIdComanda() {
        try {
            int id = readInt("Introduceți ID-ul comenzii căutate: ");
            Comanda comanda = comandaService.findById(id);
            System.out.println("Comandă găsită: " + comanda);
        } catch (RepositoryException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleDeleteComanda() {
        try {
            int id = readInt("Introduceți ID-ul comenzii de șters: ");
            comandaService.deleteComanda(id);
            System.out.println("Comanda cu ID-ul " + id + " a fost ștearsă cu succes.");
        } catch (RepositoryException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleGetAllComenzi() {
        System.out.println("\n--- Lista Comenzilor ---");
        List<Comanda> comenzi = this.comandaService.getAll();
        if (comenzi.isEmpty()) {
            System.out.println("Nu există comenzi înregistrate.");
        } else {
            comenzi.forEach(System.out::println);
        }
    }


    private void handleAddTort() {
        try {
            int id = readInt("Introduceți ID-ul tortului (Nou): ");
            System.out.print("Introduceți tipul tortului: ");
            String tip = scanner.nextLine();

            tortService.addTort(id, tip);
            System.out.println("Tort adăugat cu succes.");

        } catch (RepositoryException e) {
            System.err.println("Eroare (Validare/Repository): " + e.getMessage());
        }
    }

    private void handleUpdateTort() {
        try {
            int id = readInt("Introduceți ID-ul tortului de actualizat: ");
            System.out.print("Introduceți noul tip al tortului: ");
            String tip = scanner.nextLine();

            tortService.updateTort(id, tip);
            System.out.println("Tort actualizat cu succes.");

        } catch (RepositoryException e) {
            System.err.println("Eroare de tranzacție: " + e.getMessage());
        }
    }

    private void handleGetAllTorturi() {
        System.out.println("\n--- Lista Torturilor ---");
        List<Tort> torturi = tortService.getAll();
        if (torturi.isEmpty()) {
            System.out.println("Nu există torturi înregistrate.");
        } else {
            torturi.forEach(System.out::println);
        }
    }

    private void handleFindByIdTort() {
        try {
            int id = readInt("Introduceți ID-ul tortului căutat: ");
            Tort tort = tortService.findById(id);
            System.out.println("Tort găsit: " + tort);
        } catch (RepositoryException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleDeleteTort() {
        try {
            int id = readInt("Introduceți ID-ul tortului de șters: ");
            tortService.deleteTort(id);
            System.out.println("Tortul cu ID-ul " + id + " a fost șters cu succes.");
        } catch (RepositoryException e) {
            System.err.println(e.getMessage());
        }
    }



    public void run() {
        int option;
        do {
            showMenu();
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1: handleAddTort(); break;
                    case 2: handleUpdateTort(); break;
                    case 3: handleGetAllTorturi(); break;
                    case 4: handleFindByIdTort(); break;
                    case 5: handleDeleteTort(); break;
                    case 6: handleAddComanda(); break;
                    case 7: handleUpdateComanda(); break;
                    case 8: handleFindByIdComanda(); break;
                    case 9: handleDeleteComanda(); break;
                    case 10: handleGetAllComenzi(); break;
                    case 0: System.out.println("Bye"); break;
                    default: System.out.println("Opțiune invalidă. Încercați din nou.");
                }
            } else {
                System.out.println("Intrare invalidă. Vă rugăm introduceți un număr.");
                scanner.nextLine();
                option = -1;
            }
        } while (option != 0);
    }
}