package com.example.kalk;

import com.example.kalk.Employee;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.function.Predicate;

public class Pracownik extends Application {

    private ListView<Employee> employeeListView;
    private TableView<Employee.Task> taskTableView;
    private ObservableList<Employee> employees;
    private Employee selectedEmployee;

    private static final String EMPLOYEES_FILE_PATH = "src/pracownicy.txt";
    public static ObservableList<Employee.Task> tasks;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Management");
        employees = loadDataFromFile("src/pracownicy.txt");
        tasks = loadTasksFromFile("src/zadania.txt");
        VBox leftPanel = createLeftPanel();
        VBox rightPanel = createRightPanel();
        leftPanel.setStyle("-fx-background-color: black;");
        rightPanel.setStyle("-fx-background-color: black;");

        BorderPane root = new BorderPane();
        root.setLeft(leftPanel);
        root.setCenter(rightPanel);
        Scene scene = new Scene(root, 800, 400);
        scene.setFill(Color.BLACK);
        root.setStyle("-fx-background-color: black;");
        primaryStage.setScene(scene);
        primaryStage.show();
        if (selectedEmployee == null) {
            taskTableView.setItems(FXCollections.emptyObservableList()); // Wyczyszczenie zawartości tabeli z zadaniami
        }
    }

    private VBox createLeftPanel() {
        Label listaPracownikowLabel = new Label("Lista pracowników");
        listaPracownikowLabel.setStyle("-fx-text-fill: orange;");
        VBox leftPanel = new VBox(listaPracownikowLabel);

        employeeListView = new ListView<>(employees);
        employeeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateEmployeeDetails(newVal);
            }
        });

        leftPanel.getChildren().add(employeeListView);

        TableView<Employee> employeeTableView = new TableView<>();
        TableColumn<Employee, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Employee, String> nameColumn = new TableColumn<>("Imię");
        TableColumn<Employee, Integer> unfinishedTasksColumn = new TableColumn<>("Liczba niezakończonych zadań");
        employeeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateEmployeeDetails(newVal);
            } else {
                updateEmployeeDetails(null);
            }
        });
        employeeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedEmployee = newVal;
            updateEmployeeDetails(newVal);
        });

        selectedEmployee = null;
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        unfinishedTasksColumn.setCellValueFactory(new PropertyValueFactory<>("niezakonczoneDzialania"));

        employeeTableView.getColumns().addAll(idColumn, nameColumn, unfinishedTasksColumn,);
        employeeTableView.setItems(employees);
        leftPanel.getChildren().add(employeeTableView);

        Button addButton = new Button("+");
        addButton.setOnAction(e -> showAddEmployeeDialog());

        Button removeButton = new Button("-");
        removeButton.setOnAction(e -> removeSelectedEmployee());

        HBox buttonBox = new HBox(addButton, removeButton);
        buttonBox.setSpacing(10);
        leftPanel.getChildren().add(buttonBox);

        employeeTableView.setRowFactory(tv ->
        {
            TableRow<Employee> row = new TableRow<>();
            Button removeButtonForRow = new Button("Usuń");
            removeButtonForRow.setOnAction(e ->
            {
                Employee employee = row.getItem();
                if (employee != null)
                {
                    employees.remove(employee);
                    saveEmployeesToFile();
                }
            });
            row.setGraphic(removeButtonForRow);
            return row;
        });

        return leftPanel;
    }


    private void updateEmployeeDetails(Employee employee) {
        if (employee != null) {
            idField.setText(String.valueOf(employee.getId()));
            imieField.setText(employee.getName());
            niezakonczoneDzialaniaField.setText(String.valueOf(employee.getNiezakonczoneDzialania()));
            showTasksForEmployee(employee.getId());
        } else {
            idField.setText("");
            imieField.setText("");
            niezakonczoneDzialaniaField.setText("");
            taskTableView.setItems(FXCollections.emptyObservableList());
        }
    }


    private Label idField;
    private Label imieField;
    private Label niezakonczoneDzialaniaField;
    private VBox createRightPanel() {
        VBox rightPanel = new VBox();
        rightPanel.setSpacing(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setMaxWidth(400);

        Label szczegolyPracownikaLabel = new Label("Szczegóły pracownika");
        szczegolyPracownikaLabel.setStyle("-fx-text-fill: orange;");
        rightPanel.getChildren().add(szczegolyPracownikaLabel);

        GridPane detailsPane = new GridPane();
        detailsPane.setHgap(10);
        detailsPane.setVgap(10);

        Label idLabel = new Label("ID:");
        idLabel.setStyle("-fx-text-fill: white;");
        Label imieLabel = new Label("Imię:");
        imieLabel.setStyle("-fx-text-fill: white;");
        Label niezakonczoneDzialaniaLabel = new Label("Liczba niezakończonych zadań:");
        niezakonczoneDzialaniaLabel.setStyle("-fx-text-fill: white;");

        idField = new Label();
        idField.setStyle("-fx-text-fill: white;");
        imieField = new Label();
        imieField.setStyle("-fx-text-fill: white;");
        niezakonczoneDzialaniaField = new Label();
        niezakonczoneDzialaniaField.setStyle("-fx-text-fill: white;");

        detailsPane.add(idLabel, 0, 0);
        detailsPane.add(imieLabel, 0, 1);
        detailsPane.add(niezakonczoneDzialaniaLabel, 0, 2);
        detailsPane.add(idField, 1, 0);
        detailsPane.add(imieField, 1, 1);
        detailsPane.add(niezakonczoneDzialaniaField, 1, 2);

        taskTableView = new TableView<>(tasks);
        taskTableView.setMinWidth(400);
        TableColumn<Employee.Task, Integer> taskIdColumn = new TableColumn<>("ID");
        TableColumn<Employee.Task, String> taskCategoryColumn = new TableColumn<>("Kategoria zadania");
        TableColumn<Employee.Task, String> taskDescriptionColumn = new TableColumn<>("Opis zadania");


        taskIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        taskCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        taskTableView.getColumns().addAll(taskIdColumn, taskCategoryColumn, taskDescriptionColumn);

        rightPanel.getChildren().addAll(detailsPane, taskTableView);
        rightPanel.setStyle("-fx-background-color: black;");
        szczegolyPracownikaLabel.setStyle("-fx-text-fill: orange;");

        return rightPanel;
    }


    private ObservableList<Employee> loadDataFromFile(String filename) {
        File file = new File(filename);
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");

                if (parts.length >= 3) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        String name = parts[1];
                        int unfinishedTasks = Integer.parseInt(parts[2]);
                        Employee employee = new Employee(id, name, unfinishedTasks);
                        employees.add(employee);
                    } catch (NumberFormatException e) {

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private ObservableList<Employee.Task> loadTasksFromFile(String filename1) {
        File file = new File(filename1);

        ObservableList<Employee.Task> tasks = FXCollections.observableArrayList();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");

                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    String category = parts[1];
                    String description = parts[2];
                    int idworker = Integer.parseInt(parts[3]);
                    Employee.Task task = new Employee.Task(id, category, description, idworker);
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private void showAddEmployeeDialog() {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Dodaj pracownika");
        dialog.setHeaderText("Wprowadź dane nowego pracownika");

        ButtonType addButton = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Imię");
        TextField unfinishedTasksField = new TextField();
        unfinishedTasksField.setPromptText("Liczba niezakończonych zadań");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Imię:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Liczba niezakończonych zadań:"), 0, 2);
        grid.add(unfinishedTasksField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> idField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int unfinishedTasks = Integer.parseInt(unfinishedTasksField.getText());

                Employee employee = new Employee(id, name, unfinishedTasks);
                employees.add(employee);
                saveEmployeesToFile();
                return employee;
            }
            return null;
        });
        dialog.showAndWait();
    }
    private void saveEmployeesToFile() {
        try (PrintWriter writer = new PrintWriter(new File(EMPLOYEES_FILE_PATH))) {
            for (Employee employee : employees) {
                writer.println(employee.getId() + " " + employee.getName() + " " + employee.getNiezakonczoneDzialania());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void removeSelectedEmployee() {
        Employee selectedEmployee = employeeListView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            employees.remove(selectedEmployee);
            saveEmployeesToFile();
        }
    }
    private void showTasksForEmployee(int employeeId) {
        Predicate<Employee.Task> filterByEmployeeId = task -> task.getId() == employeeId;
        taskTableView.setItems(tasks.filtered(filterByEmployeeId));
    }
}
