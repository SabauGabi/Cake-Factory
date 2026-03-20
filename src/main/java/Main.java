import ui.ConsoleUI;
import domain.Comanda;
import domain.Tort;
import repository.*;
import service.TortService;
import service.ComandaService;
import exception.TortValidator;
import exception.ComandaValidator;
import utils.Settings;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Settings.loadSettings();
        String repoType = Settings.getProperty("Repository");
        String dbLocation = Settings.getProperty("DatabaseLocation");
        String tortFile = Settings.getProperty("TortFile");
        String comandaFile = Settings.getProperty("ComandaFile");

        IRepository<Tort> tortRepo;
        IRepository<Comanda> comandaRepo;

        if ("database".equalsIgnoreCase(repoType)) {
            tortRepo = new TortDbRepository(dbLocation);
            comandaRepo = new ComandaDbRepository(dbLocation);

        } else if ("binary".equalsIgnoreCase(repoType)) {
            tortRepo = new BinaryFileRepository<>(tortFile);
            comandaRepo = new BinaryFileRepository<>(comandaFile);

        } else if ("text".equalsIgnoreCase(repoType)) {
            tortRepo = new TextFileRepository<>(tortFile, new TortConverter());
            comandaRepo = new TextFileRepository<>(comandaFile, new ComandaConverter());

        } else {
            tortRepo = new InMemoryRepository<>();
            comandaRepo = new InMemoryRepository<>();

            List<Tort> initialTorturi = Arrays.asList(
                    new Tort(1, "Tort de Ciocolată"),
                    new Tort(2, "Tort Diplomat"),
                    new Tort(3, "Tort de Morcovi"),
                    new Tort(4, "Red Velvet"),
                    new Tort(5, "Tiramisu")
            );

            List<Comanda> initialComenzi = Arrays.asList(
                    new Comanda(1, initialTorturi.subList(0, 1), new Date(System.currentTimeMillis() + 86400000)),
                    new Comanda(2, initialTorturi.subList(1, 3), new Date(System.currentTimeMillis() + 86400000 * 2)),
                    new Comanda(3, initialTorturi.subList(3, 4), new Date(System.currentTimeMillis() + 86400000 * 3)),
                    new Comanda(4, initialTorturi, new Date(System.currentTimeMillis() + 86400000 * 4)),
                    new Comanda(5, initialTorturi.subList(4, 5), new Date(System.currentTimeMillis() + 86400000 * 5))
            );

            for (Tort t : initialTorturi) {
                tortRepo.add(t);
            }
            for (Comanda c : initialComenzi) {
                comandaRepo.add(c);
            }
        }

        TortValidator tortValidator = new TortValidator();
        ComandaValidator comandaValidator = new ComandaValidator();

        TortService tortService = new TortService(tortRepo, tortValidator);
        ComandaService comandaService = new ComandaService(comandaRepo, comandaValidator);

        ConsoleUI ui = new ConsoleUI(tortService, comandaService);
        ui.run();
    }
}