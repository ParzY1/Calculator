package com.example.kalk;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Kalkulator");

        LoadingStage loadingStage = new LoadingStage(primaryStage);
        loadingStage.show();
    }

    private class LoadingStage extends Stage {

        public LoadingStage(Stage primaryStage) {
            initOwner(primaryStage);
            setResizable(false);

            StackPane stackPane = new StackPane();
            stackPane.setStyle("-fx-background-color: black;");
            Scene scene = new Scene(stackPane, 300, 400);
            scene.setFill(Color.BLACK);

            ProgressIndicator progressIndicator = new ProgressIndicator();
            progressIndicator.setStyle("-fx-progress-color: white;");
            stackPane.getChildren().add(progressIndicator);

            Label loadingLabel = new Label("PPRAKTYKI SZOBI");
            loadingLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: orange;");
            stackPane.getChildren().add(loadingLabel);
            StackPane.setAlignment(loadingLabel, Pos.CENTER);
            StackPane.setMargin(loadingLabel, new Insets(-100, 0, 0, 0));

            setScene(scene);

            // Animacja
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(stackPane.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(2), new KeyValue(stackPane.opacityProperty(), 2)),
                    new KeyFrame(Duration.seconds(3), new KeyValue(stackPane.opacityProperty(), 1)),
                    new KeyFrame(Duration.seconds(4), new KeyValue(stackPane.opacityProperty(), 1))
            );
            timeline.setOnFinished(event -> {
                close();
                showMenu(primaryStage);
                primaryStage.setTitle("Menu");
            });
            timeline.play();
        }
    }

    private void showMenu(Stage primaryStage) {
        MenuStage menuStage = new MenuStage(this.primaryStage);
        menuStage.show();
    }

    private class MenuStage extends Stage {

        public MenuStage(Stage primaryStage) {
            initOwner(primaryStage);
            setResizable(false);

            GridPane menuGridPane = new GridPane();
            menuGridPane.setAlignment(Pos.CENTER);
            menuGridPane.setHgap(10);
            menuGridPane.setVgap(10);
            menuGridPane.setPadding(new Insets(20));
            menuGridPane.setStyle("-fx-background-color: black;");

            Button calculatorButton = new Button("Przejdź do kalkulatora");
            calculatorButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
            calculatorButton.setOnAction(event -> {
                showCalculator(primaryStage);
                primaryStage.setTitle("Kalkulator");
                close();
            });
            menuGridPane.add(calculatorButton, 0, 0);

            Button emptyWindowButton = new Button("Pracownik");
            emptyWindowButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
            emptyWindowButton.setOnAction(event -> {
                showEmptyWindow(primaryStage);
                primaryStage.setTitle("Pracownik-zadania");
                close();
            });
            menuGridPane.add(emptyWindowButton, 0, 1);

            setScene(new Scene(menuGridPane, 300, 400));
        }
    }

    private void showCalculator(Stage primaryStage) {
        CalculatorStage calculatorStage = new CalculatorStage(primaryStage);
        calculatorStage.show();
    }

    private class CalculatorStage extends Stage {

        public CalculatorStage(Stage primaryStage) {
            initOwner(primaryStage);
            setResizable(false);

            GridPane mainGridPane = new GridPane();
            mainGridPane.setHgap(10);
            mainGridPane.setVgap(10);

            TextField dzialanieTextField = new TextField();
            dzialanieTextField.setPrefSize(200, 50);
            dzialanieTextField.setEditable(false);
            dzialanieTextField.setStyle("-fx-background-color: black; -fx-text-fill: white;");
            mainGridPane.add(dzialanieTextField, 0, 0, 2, 1);

            Label wynikLabel = new Label("Wynik: ");
            wynikLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
            mainGridPane.add(wynikLabel, 1, 1);

            GridPane klawiaturaGridPane = new GridPane();
            klawiaturaGridPane.setHgap(10);
            klawiaturaGridPane.setVgap(10);

            int row = 0;
            int col = 0;
            for (int i = 1; i <= 9; i++) {
                Button button = new Button(Integer.toString(i));
                button.setPrefSize(50, 50);
                button.setStyle("-fx-background-color: white;");
                button.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + button.getText()));
                klawiaturaGridPane.add(button, col, row);
                col++;
                if (col > 2) {
                    col = 0;
                    row++;
                }
            }

            Button zeroButton = new Button("0");
            zeroButton.setPrefSize(50, 50);
            zeroButton.setStyle("-fx-background-color: white;");
            zeroButton.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + zeroButton.getText()));
            klawiaturaGridPane.add(zeroButton, 0, 3);

            Button plusButton = new Button("+");
            plusButton.setPrefSize(50, 50);
            plusButton.setStyle("-fx-background-color: white;");
            plusButton.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + plusButton.getText()));
            klawiaturaGridPane.add(plusButton, 1, 3);

            Button minusButton = new Button("-");
            minusButton.setPrefSize(50, 50);
            minusButton.setStyle("-fx-background-color: white;");
            minusButton.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + minusButton.getText()));
            klawiaturaGridPane.add(minusButton, 2, 3);

            Button multiplyButton = new Button("*");
            multiplyButton.setPrefSize(50, 50);
            multiplyButton.setStyle("-fx-background-color: grey;");
            multiplyButton.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + multiplyButton.getText()));
            klawiaturaGridPane.add(multiplyButton, 3, 0);

            Button divideButton = new Button("/");
            divideButton.setPrefSize(50, 50);
            divideButton.setStyle("-fx-background-color: grey;");
            divideButton.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + divideButton.getText()));
            klawiaturaGridPane.add(divideButton, 3, 1);

            Button exponentButton = new Button("^");
            exponentButton.setPrefSize(50, 50);
            exponentButton.setStyle("-fx-background-color: grey;");
            exponentButton.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + exponentButton.getText()));
            klawiaturaGridPane.add(exponentButton, 3, 2);

            Button sqrtButton = new Button("√");
            sqrtButton.setPrefSize(50, 50);
            sqrtButton.setStyle("-fx-background-color: grey;");
            sqrtButton.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + sqrtButton.getText()));
            klawiaturaGridPane.add(sqrtButton, 3, 3);

            Button percentButton = new Button("%");
            percentButton.setPrefSize(50, 50);
            percentButton.setStyle("-fx-background-color: grey;");
            percentButton.setOnAction(event -> dzialanieTextField.setText(dzialanieTextField.getText() + percentButton.getText()));
            klawiaturaGridPane.add(percentButton, 3, 4);
            Button backButton = new Button("Powrót do menu");
            backButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
            backButton.setOnAction(event -> {
                close();
                showMenu(primaryStage);
                primaryStage.setTitle("Menu");
            });
            mainGridPane.add(backButton, 0, 5);

            Button equalsButton = new Button("=");
            equalsButton.setPrefSize(50, 50);
            equalsButton.setStyle("-fx-background-color: cyan;-fx-text-fill: white;");
            equalsButton.setOnAction(event -> {
                String dzialanie = dzialanieTextField.getText();
                if (dzialanie.contains("+")) {
                    String[] split = dzialanie.split("\\+");
                    if (split.length == 2) {
                        int a = Integer.parseInt(split[0]);
                        int b = Integer.parseInt(split[1]);
                        int wynik = a + b;
                        wynikLabel.setText("Wynik: " + wynik);
                    }
                } else if (dzialanie.contains("-")) {
                    String[] split = dzialanie.split("-");
                    if (split.length == 2) {
                        int a = Integer.parseInt(split[0]);
                        int b = Integer.parseInt(split[1]);
                        int wynik = a - b;
                        wynikLabel.setText("Wynik: " + wynik);
                    }
                } else if (dzialanie.contains("*")) {
                    String[] split = dzialanie.split("\\*");
                    if (split.length == 2) {
                        int a = Integer.parseInt(split[0]);
                        int b = Integer.parseInt(split[1]);
                        int wynik = a * b;
                        wynikLabel.setText("Wynik: " + wynik);
                    }
                } else if (dzialanie.contains("/")) {
                    String[] split = dzialanie.split("/");
                    if (split.length == 2) {
                        int a = Integer.parseInt(split[0]);
                        int b = Integer.parseInt(split[1]);
                        if (b != 0) {
                            int wynik = a / b;
                            wynikLabel.setText("Wynik: " + wynik);
                        } else {
                            wynikLabel.setText("Błąd: Dzielenie przez 0");
                        }
                    }
                } else if (dzialanie.contains("^")) {
                    String[] split = dzialanie.split("\\^");
                    if (split.length == 2) {
                        int a = Integer.parseInt(split[0]);
                        int b = Integer.parseInt(split[1]);
                        int wynik = (int) Math.pow(a, b);
                        wynikLabel.setText("Wynik: " + wynik);
                    }
                } else if (dzialanie.contains("√")) {
                    String[] split = dzialanie.split("√");
                    if (split.length == 2) {
                        int a = Integer.parseInt(split[1]);
                        double wynik = Math.sqrt(a);
                        wynikLabel.setText("Wynik: " + wynik);
                    }
                } else if (dzialanie.contains("%")) {
                    String[] split = dzialanie.split("%");
                    if (split.length == 2) {
                        int a = Integer.parseInt(split[0]);
                        int b = Integer.parseInt(split[1]);
                        double wynik = (a * b) / 100.0;
                        wynikLabel.setText("Wynik: " + wynik);
                    }
                }
            });
            klawiaturaGridPane.add(equalsButton, 2, 4);

            Button clearButton = new Button("C");
            clearButton.setPrefSize(50, 50);
            clearButton.setStyle("-fx-background-color: #6c757d;-fx-text-fill: white;");
            clearButton.setOnAction(event -> dzialanieTextField.clear());
            klawiaturaGridPane.add(clearButton, 0, 4);
            Button czyszczenieButton = new Button("AC");
            czyszczenieButton.setPrefSize(50, 50);
            czyszczenieButton.setStyle("-fx-text-fill: white; -fx-background-color: #6c757d;");
            czyszczenieButton.setOnAction(event -> {
                dzialanieTextField.setText("");
                wynikLabel.setText("Wynik: ");
            });
            klawiaturaGridPane.add(czyszczenieButton, 1, 4);

            mainGridPane.add(klawiaturaGridPane, 0, 2, 2, 1);

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(20);
            vbox.setStyle("-fx-background-color: black;");
            vbox.getChildren().addAll(mainGridPane, wynikLabel);

            Scene scene = new Scene(vbox, 240, 400);
            scene.setFill(Color.BLACK);
            setScene(scene);
        }
    }
    private void showEmptyWindow(Stage primaryStage) {
        EmptyWindowStage emptyWindowStage = new EmptyWindowStage(this.primaryStage);
        emptyWindowStage.show();
    }

    private class EmptyWindowStage extends Stage {

        public EmptyWindowStage(Stage primaryStage) {
            initOwner(primaryStage);
            setResizable(false);

            GridPane emptyWindowGridPane = new GridPane();
            emptyWindowGridPane.setAlignment(Pos.CENTER);
            emptyWindowGridPane.setHgap(10);
            emptyWindowGridPane.setVgap(10);
            emptyWindowGridPane.setPadding(new Insets(20));
            emptyWindowGridPane.setStyle("-fx-background-color: black;");

            // Dodawanie zawartości do pustego okienka
            // Tutaj możesz dodać dowolne elementy do okienka

            Button backButton = new Button("Powrót do menu");
            backButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
            backButton.setOnAction(event -> {
                close();
                showMenu(primaryStage);
                primaryStage.setTitle("Menu");
            });
            emptyWindowGridPane.add(backButton, 0, 1);

            setScene(new Scene(emptyWindowGridPane, 300, 400));
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}