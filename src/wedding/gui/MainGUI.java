package wedding.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import wedding.model.*;
import wedding.strategy.*; // ако имаш стратегии
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainGUI extends Application {

    // Главни структури
    private final List<Family> allFamilies = new ArrayList<>(); // master list
    private final List<Table> tables = new ArrayList<>(); // result of arrangeAll()
    private final SeatingManager manager = new SeatingManager(); // държи forbidden pairs

    private ObservableList<GuestRow> data; // за TableView
    private ObservableList<String> forbiddenViewData; // за ListView на забраните

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🎉 Wedding Organiser 🎉");

        // ===== Заглавие =====
        Label titleLabel = new Label("🎉 Wedding organiser 🎉");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: darkblue;");

        // ===== TableView (mains) =====
        TableView<GuestRow> tableView = new TableView<>();
        TableColumn<GuestRow, String> tableCol = new TableColumn<>("Маса");
        tableCol.setCellValueFactory(cellData -> cellData.getValue().tableNameProperty());

        TableColumn<GuestRow, String> familyCol = new TableColumn<>("Семейство");
        familyCol.setCellValueFactory(cellData -> cellData.getValue().familyNameProperty());

        TableColumn<GuestRow, Integer> countCol = new TableColumn<>("Брой гости");
        countCol.setCellValueFactory(cellData -> cellData.getValue().guestCountProperty().asObject());

        tableView.getColumns().addAll(tableCol, familyCol, countCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ===== Forbidden list view =====
        Label forbiddenLabel = new Label("Забранени комбинации:");
        ListView<String> forbiddenListView = new ListView<>();
        forbiddenViewData = FXCollections.observableArrayList();
        forbiddenListView.setItems(forbiddenViewData);
        forbiddenListView.setPrefHeight(120);

        // ===== Controls: Add family, Add forbidden, Strategy choice, Arrange =====
        Button addFamilyBtn = new Button("Добави семейство");
        Button addForbiddenBtn = new Button("Забрани комбинация");
        ChoiceBox<String> strategyChoice = new ChoiceBox<>(FXCollections.observableArrayList(
                "По име (Alphabetical)", "По големина (By size)"
        ));
        strategyChoice.setValue("По големина (By size)");
        Button arrangeBtn = new Button("🔄 Подреди масите");

        HBox controls = new HBox(10, addFamilyBtn, addForbiddenBtn, strategyChoice, arrangeBtn);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(8));

        // ===== Layout left (table + forbidden) =====
        VBox left = new VBox(10, titleLabel, tableView, controls, forbiddenLabel, forbiddenListView);
        left.setPadding(new Insets(12));
        left.setPrefWidth(680);
        left.setStyle("-fx-background-color: #f7fbff; -fx-border-color: #d0e6ff; -fx-border-width: 1;");

        // ===== Initial data (example) =====
        allFamilies.add(new Family("Иванови", 4));
        allFamilies.add(new Family("Петрови", 3));
        allFamilies.add(new Family("Георгиеви", 6));
        // initial arrange
        arrangeAll(strategyChoice.getValue());
        data = FXCollections.observableArrayList();
        updateTableView(data);
        tableView.setItems(data);

        // ===== Button actions =====

        // Add family - opens dialog
        addFamilyBtn.setOnAction(e -> openAddFamilyDialog());

        // Add forbidden pair - dialog with two choiceboxes populated from allFamilies
        addForbiddenBtn.setOnAction(e -> openAddForbiddenDialog(forbiddenListView));

        // Arrange using selected strategy, taking forbidden pairs into account
        arrangeBtn.setOnAction(e -> {
            String strategy = strategyChoice.getValue();
            arrangeAll(strategy);
            // Обновяваме observable list по сигурен начин
            if (data == null) data = FXCollections.observableArrayList();
            updateTableView(data);
        });


        // ===== Scene =====
        BorderPane root = new BorderPane();
        root.setCenter(left);
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 760, 520);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ===== Popup: Add family =====
    private void openAddFamilyDialog() {
        Dialog<Family> dialog = new Dialog<>();
        dialog.setTitle("Добавяне на семейство");
        dialog.setHeaderText("Въведи име и брой гости");

        Label nameLabel = new Label("Име:");
        TextField nameField = new TextField();
        nameField.setPromptText("Иванови");

        Label countLabel = new Label("Брой:");
        Spinner<Integer> spinner = new Spinner<>(1, 20, 2);

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(10));
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(countLabel, 0, 1);
        grid.add(spinner, 1, 1);

        dialog.getDialogPane().setContent(grid);
        ButtonType addBtn = new ButtonType("Добави", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == addBtn) {
                String n = nameField.getText().trim();
                int c = spinner.getValue();
                if (!n.isEmpty()) return new Family(n, c);
            }
            return null;
        });

        dialog.showAndWait().ifPresent(family -> {
            allFamilies.add(family);
            // използваме текущата стойност на strategyChoice (ако е достъпна)
            // но start() има strategyChoice само там — лесен вариант: по подразбиране използваме By size,
            //By size:
            arrangeAll("По големина (By size)");
            // обновяваме data (същият екземпляр)
            if (data == null) data = FXCollections.observableArrayList();
            updateTableView(data);
        });

    }

    // ===== Popup: Add forbidden pair =====
    private void openAddForbiddenDialog(ListView<String> forbiddenListView) {
        if (allFamilies.size() < 2) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Трябват поне 2 семейства, за да добавиш забрана.");
            a.showAndWait();
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Добави забрана между две фамилии");
        dialog.setHeaderText("Избери две фамилии, които не трябва да седят на една маса");

        ChoiceBox<String> aBox = new ChoiceBox<>(FXCollections.observableArrayList(getFamilyNames()));
        ChoiceBox<String> bBox = new ChoiceBox<>(FXCollections.observableArrayList(getFamilyNames()));
        aBox.setValue(aBox.getItems().get(0));
        bBox.setValue(bBox.getItems().get(0));

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Фамилия A:"), 0, 0);
        grid.add(aBox, 1, 0);
        grid.add(new Label("Фамилия B:"), 0, 1);
        grid.add(bBox, 1, 1);

        dialog.getDialogPane().setContent(grid);
        ButtonType addBtn = new ButtonType("Забрани", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == addBtn) {
                String sa = aBox.getValue();
                String sb = bBox.getValue();
                if (sa.equals(sb)) {
                    Alert al = new Alert(Alert.AlertType.ERROR, "Трябва да избереш две различни фамилии.");
                    al.showAndWait();
                    return null;
                }
                manager.addForbiddenPair(sa, sb);
                // update forbidden list view
                forbiddenViewData.add(sa + " ⟂ " + sb);
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ===== Helper: returns list of family names =====
    private List<String> getFamilyNames() {
        List<String> names = new ArrayList<>();
        for (Family f : allFamilies) names.add(f.getName());
        return names;
    }

    // ===== Arrange all families into tables (greedy), respecting forbidden pairs and chosen strategy =====
    private void arrangeAll(String strategyName) {
        // Reset tables completely
        tables.clear();

        // Work on a copy of all families
        List<Family> working = new ArrayList<>(allFamilies);

        // Strategy: exact matching on provided choice strings (avoid contains ambiguity)
        if ("По име (Alphabetical)".equals(strategyName)) {
            working.sort(Comparator.comparing(Family::getName));
        } else {
            // default: By size descending
            working.sort(Comparator.comparingInt(Family::getGuestCount).reversed());
        }

        int nextId = 1;
        for (Family f : working) {
            boolean placed = false;
            // try to place to existing tables
            for (Table t : tables) {
                // check forbidden pair with any existing family on this table
                boolean forbidden = false;
                for (GuestComponent gc : t.getGuests()) {
                    if (gc instanceof Family) {
                        Family exist = (Family) gc;
                        if (manager.isForbidden(f.getName(), exist.getName())) {
                            forbidden = true;
                            break;
                        }
                    }
                }
                // verify capacity and family count constraints
                if (!forbidden && t.getTotalGuests() + f.getGuestCount() <= 10 && t.getNumberOfFamilies() < 2) {
                    t.add(f);
                    placed = true;
                    break;
                }
            }
            if (!placed) {
                Table newT = new Table("Маса " + nextId++);
                newT.add(f);
                tables.add(newT);
            }
        }
    }


    // ===== Update TableView from current tables list =====
    private void updateTableView(ObservableList<GuestRow> data) {
        if (data == null) return;
        data.clear();
        for (Table t : tables) {
            for (GuestComponent gc : t.getGuests()) {
                if (gc instanceof Family) {
                    Family fam = (Family) gc;
                    data.add(new GuestRow(t.getName(), fam));
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
