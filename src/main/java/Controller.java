import domain.Comanda;
import domain.Tort;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ComandaService;
import service.TortService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Controller {

    private TortService tortService;
    private ComandaService comandaService;

    @FXML private TextField tortIdField;
    @FXML private TextField tortTipField;
    @FXML private TableView<Tort> tortTable;
    @FXML private TableColumn<Tort, Integer> colTortId;
    @FXML private TableColumn<Tort, String> colTortTip;

    @FXML private TextField comandaIdField;
    @FXML private TextField comandaTorturiField;
    @FXML private TableView<Comanda> comandaTable;
    @FXML private TableColumn<Comanda, Integer> colComandaId;
    @FXML private TableColumn<Comanda, String> colComandaData;
    @FXML private TableColumn<Comanda, String> colComandaTorturi;

    @FXML private ListView<String> statisticiZiList;
    @FXML private ListView<String> statisticiLunaList;
    @FXML private ListView<String> statisticiTopList;

    private ObservableList<Tort> torturiList = FXCollections.observableArrayList();
    private ObservableList<Comanda> comenziList = FXCollections.observableArrayList();

    public void setServices(TortService tortService, ComandaService comandaService) {
        this.tortService = tortService;
        this.comandaService = comandaService;
        loadData();
    }

    private void loadData() {
        torturiList.setAll(tortService.getAll());
        comenziList.setAll(comandaService.getAll());

        tortTable.setItems(torturiList);
        comandaTable.setItems(comenziList);
        refreshStats();
    }

    @FXML
    public void initialize() {
        colTortId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTortTip.setCellValueFactory(new PropertyValueFactory<>("tipulTortului"));

        colComandaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colComandaData.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getData().toString()));
        colComandaTorturi.setCellValueFactory(cell -> {
            String torturi = cell.getValue().getListaTorturi().stream()
                    .map(Tort::getTipulTortului)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(torturi);
        });
    }

    @FXML
    void btnAddTort(ActionEvent event) {
        try {
            int id = Integer.parseInt(tortIdField.getText());
            String tip = tortTipField.getText();
            tortService.addTort(id, tip);
            loadData();
            clearTortFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnUpdateTort(ActionEvent event) {
        try {
            int id = Integer.parseInt(tortIdField.getText());
            String tip = tortTipField.getText();
            tortService.updateTort(id, tip);
            loadData();
            clearTortFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnDeleteTort(ActionEvent event) {
        try {
            int id = Integer.parseInt(tortIdField.getText());
            tortService.deleteTort(id);
            loadData();
            clearTortFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnAddComanda(ActionEvent event) {
        try {
            int id = Integer.parseInt(comandaIdField.getText());
            List<Tort> torturi = parseTorturiIds(comandaTorturiField.getText());

            Comanda c = new Comanda(id, torturi, new Date());
            comandaService.addComanda(c);
            loadData();
            clearComandaFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnUpdateComanda(ActionEvent event) {
        try {
            int id = Integer.parseInt(comandaIdField.getText());
            List<Tort> torturi = parseTorturiIds(comandaTorturiField.getText());

            Comanda c = new Comanda(id, torturi, new Date());
            comandaService.updateComanda(c);
            loadData();
            clearComandaFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnDeleteComanda(ActionEvent event) {
        try {
            int id = Integer.parseInt(comandaIdField.getText());
            comandaService.deleteComanda(id);
            loadData();
            clearComandaFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    void btnRefreshStats(ActionEvent event) {
        refreshStats();
    }

    private void refreshStats() {
        statisticiZiList.getItems().clear();
        Map<String, Long> peZi = comandaService.getTorturiPerZi();
        peZi.forEach((k, v) -> statisticiZiList.getItems().add(k + ": " + v + " torturi"));

        statisticiLunaList.getItems().clear();
        Map<String, Long> peLuna = comandaService.getTorturiPerLuna();
        peLuna.forEach((k, v) -> statisticiLunaList.getItems().add(k + ": " + v + " torturi"));

        statisticiTopList.getItems().clear();
        Map<String, Long> top = comandaService.getCeleMaiComandateTorturi();
        top.forEach((k, v) -> statisticiTopList.getItems().add(k + ": " + v + " buc"));
    }

    private List<Tort> parseTorturiIds(String text) {
        List<Tort> result = new ArrayList<>();
        String[] ids = text.split(",");
        for (String idStr : ids) {
            try {
                int tortId = Integer.parseInt(idStr.trim());
                result.add(tortService.findById(tortId));
            } catch (Exception e) {
            }
        }
        if (result.isEmpty()) throw new RuntimeException("Niciun ID de tort valid introdus!");
        return result;
    }

    private void clearTortFields() {
        tortIdField.clear();
        tortTipField.clear();
    }

    private void clearComandaFields() {
        comandaIdField.clear();
        comandaTorturiField.clear();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }
}