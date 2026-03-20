import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import domain.Comanda;
import domain.Tort;
import repository.*;
import service.TortService;
import service.ComandaService;
import exception.TortValidator;
import exception.ComandaValidator;
import utils.Settings;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Settings.loadSettings();
        String repoType = Settings.getProperty("Repository");
        String dbLocation = Settings.getProperty("DatabaseLocation");

        IRepository<Tort> tortRepo;
        IRepository<Comanda> comandaRepo;

        if ("database".equalsIgnoreCase(repoType)) {
            tortRepo = new TortDbRepository(dbLocation);
            comandaRepo = new ComandaDbRepository(dbLocation);
        } else {
            tortRepo = new InMemoryRepository<>();
            comandaRepo = new InMemoryRepository<>();
        }

        TortService tortService = new TortService(tortRepo, new TortValidator());
        ComandaService comandaService = new ComandaService(comandaRepo, new ComandaValidator());

        if ("database".equalsIgnoreCase(repoType)) {

            generateTorturi(tortService);

            List<Tort> torturiDinDb = tortService.getAll();
            if (!torturiDinDb.isEmpty()) {
                generateComenzi(comandaService, torturiDinDb);
            } else {
                System.err.println("Nu s-au putut citi torturi din DB pentru a genera comenzi.");
            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setServices(tortService, comandaService);

        primaryStage.setTitle("Cofetarie Manager");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    private void generateTorturi(TortService tortService) {
        String[] tipuriBaza = {"Ciocolata", "Vanilie", "Fructe", "Morcovi", "Lamaie", "Caramel", "Cafea", "Fistic"};
        String[] tipuriExtra = {"Cu frisca", "Glazurat", "Regal", "Diplomat", "Suprem", "Delice", "Crunchy", "Bio"};

        for (int i = 1; i <= 10; i++) {

            boolean exista = true;
            try {
                tortService.findById(i);
            } catch (Exception e) {
                exista = false;
            }

            if (!exista) {
                String tip = tipuriBaza[ThreadLocalRandom.current().nextInt(tipuriBaza.length)] + " " +
                        tipuriExtra[ThreadLocalRandom.current().nextInt(tipuriExtra.length)];
                try {
                    tortService.addTort(i, tip);
                    System.out.println("Generat Tort ID " + i);
                } catch (Exception e) {
                    System.err.println("Eroare generare tort " + i + ": " + e.getMessage());
                }
            }
        }
    }

    private void generateComenzi(ComandaService comandaService, List<Tort> torturiDisponibile) {
        int nrComenziExistente = comandaService.getAll().size();
        int cateMaiTrebuie = 100 - nrComenziExistente;

        int startId = 1;
        for(Comanda c : comandaService.getAll()) {
            if(c.getId() >= startId) startId = c.getId() + 1;
        }

        System.out.println("Generez inca " + cateMaiTrebuie + " comenzi...");

        for (int i = 0; i < cateMaiTrebuie; i++) {
            int currentId = startId + i;

            int nrTorturi = ThreadLocalRandom.current().nextInt(1, 4);
            List<Tort> torturiComanda = new ArrayList<>();
            for (int k = 0; k < nrTorturi; k++) {
                torturiComanda.add(torturiDisponibile.get(ThreadLocalRandom.current().nextInt(torturiDisponibile.size())));
            }


            long now = System.currentTimeMillis();
            long anInUrma = now - (365L * 24 * 60 * 60 * 1000);
            long dataRandom = ThreadLocalRandom.current().nextLong(anInUrma, now);

            try {
                comandaService.addComanda(new Comanda(currentId, torturiComanda, new Date(dataRandom)));
            } catch (Exception e) {
                System.err.println("Eroare generare comanda " + currentId + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}