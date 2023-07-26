package es.lonely.sksolver;

import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public final class App extends Application {

    private boolean isClicked = false;

    @Override
    public void start(Stage stage) {
        SKSolver sksolver = new SKSolver(stage);
        BorderPane borderPane1 = new BorderPane();
        BorderPane borderPane2 = new BorderPane();
        BorderPane borderPane3 = new BorderPane();
        GridPane board = board();
        GridPane cellSelectorBoard = cellSelectorBoard();
        GridPane solveBoard = solveBoard();
        Image icon = new Image("sudoku-icon.png");
        Scene scene1 = new Scene(borderPane1);
        Scene scene2 = new Scene(borderPane2);
        Scene scene3 = new Scene(borderPane3);
        
        StackPane sp = new StackPane();
        Button solveButton = new Button();
        solveButton.setText("Solve");
        sp.setAlignment(Pos.CENTER);
        sp.setTranslateX(-50);
        sp.setScaleX(1.4);
        sp.setScaleY(1.4);
        sp.getChildren().add(solveButton);

        Button homeButton = new Button();
        homeButton.setText("Return to home");

        Button sceneChangerButton1 = new Button("Change to sum mode");
        sceneChangerButton1.setCursor(Cursor.HAND);
        sceneChangerButton1.setOnAction(e-> {stage.setScene(scene2); System.out.println("Sum mode"); borderPane2.setRight(sp);});

        Button sceneChangerButton2 = new Button("Change to number mode");
        sceneChangerButton2.setCursor(Cursor.HAND);
        sceneChangerButton2.setOnAction(e-> {stage.setScene(scene1); System.out.println("Number mode");
        int[][] matrix = sksolver.board(); 
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        };
        borderPane1.setRight(sp);});

        solveButton.setOnAction(e -> {System.out.println("Solve"); stage.setScene(scene3);});

        homeButton.setOnAction(e -> {System.out.println("Home"); borderPane1.setLeft(board()); borderPane2.setLeft(cellSelectorBoard()); stage.setScene(scene1);});
        
        VBox vb1 = new VBox();
        Text textMode1 = new Text();
        textMode1.setText("Number mode");
        vb1.setPadding(new Insets(10, 30, 0, 30));
        vb1.setSpacing(10);
        vb1.setAlignment(Pos.CENTER);
        vb1.getChildren().add(textMode1);
        vb1.getChildren().add(sceneChangerButton1);

        borderPane1.setLeft(board);
        borderPane1.setCenter(vb1);
        borderPane1.setRight(sp);

        VBox vb2 = new VBox();
        Text textMode2 = new Text();
        TextField sumText = new TextField();
        sumText.setAlignment(Pos.CENTER);
        sumText.setMaxWidth(80);
        sumText.setMaxHeight(10);

        sumText.setTextFormatter(new TextFormatter<Integer>(c -> {
            if (c.getControlNewText().matches("^(?:[1-9]\\d?|)$")) {
                return c;
            }else{
                return null;
            }}));

        Button saveButton = new Button();
        saveButton.setText("Save");

        textMode2.setText("Sum mode");
        vb2.setPadding(new Insets(10, 30, 0, 30));
        vb2.setSpacing(10);
        vb2.setAlignment(Pos.CENTER);
        vb2.getChildren().add(textMode2);
        vb2.getChildren().add(sceneChangerButton2);
        vb2.getChildren().add(sumText);
        vb2.getChildren().add(saveButton);
        
        borderPane2.setLeft(cellSelectorBoard);
        borderPane2.setCenter(vb2);

        VBox vb3 = new VBox();
        vb3.setPadding(new Insets(10, 30, 0, 30));
        vb3.setSpacing(10);
        vb3.setAlignment(Pos.CENTER);
        vb3.setTranslateY(125);
        vb3.getChildren().add(homeButton);
        borderPane3.setLeft(solveBoard);
        borderPane3.setCenter(vb3);

        scene1.getStylesheets().add("layout.css");
        scene2.getStylesheets().add("layout.css");
        scene3.getStylesheets().add("layout.css");
        stage.setScene(scene1);
        stage.setTitle("SKSolver");
        stage.getIcons().add(icon);
        stage.setWidth(800);
        stage.setHeight(500);
        stage.setResizable(false);
        stage.show();
    }

    private GridPane board(){
        GridPane board = new GridPane();
        board.setId("board");
        PseudoClass right = PseudoClass.getPseudoClass("right");
        PseudoClass bottom = PseudoClass.getPseudoClass("bottom");

        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                StackPane cell = new StackPane();
                cell.getStyleClass().add("cell");
                cell.pseudoClassStateChanged(right, col == 2 || col == 5);
                cell.pseudoClassStateChanged(bottom, row == 2 || row == 5);

                TextField text = createTextField();
                text.setId(col + "_" + row);
                cell.getChildren().add(text);
                
                board.add(cell, col, row);
            }
        }
        return board;
    }

    private GridPane cellSelectorBoard(){
        GridPane board = new GridPane();
        board.setId("selectorBoard");
        PseudoClass right = PseudoClass.getPseudoClass("right");
        PseudoClass bottom = PseudoClass.getPseudoClass("bottom");

        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                StackPane cell = new StackPane();
                cell.getStyleClass().add("cell");
                cell.pseudoClassStateChanged(right, col == 2 || col == 5);
                cell.pseudoClassStateChanged(bottom, row == 2 || row == 5);

                Button button = createMarker();
                button.setId(col + "_" + row);
                cell.getChildren().add(button);

                board.add(cell, col, row);
            }
        }
        return board;
    }

    private GridPane solveBoard(){
        GridPane board = new GridPane();

        PseudoClass right = PseudoClass.getPseudoClass("right");
        PseudoClass bottom = PseudoClass.getPseudoClass("bottom");

        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 9; row++) {
                StackPane cell = new StackPane();
                cell.getStyleClass().add("cell");
                cell.pseudoClassStateChanged(right, col == 2 || col == 5);
                cell.pseudoClassStateChanged(bottom, row == 2 || row == 5);

                TextField text = createTextField();
                text.setId(col + "_" + row);
                text.setDisable(true);
                cell.getChildren().add(text);
                
                board.add(cell, col, row);
            }
        }
        return board;
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.setAlignment(Pos.CENTER);
        // restrict input to integers:
        textField.setTextFormatter(new TextFormatter<Integer>(c -> {
            if (c.getControlNewText().matches("[1-9]?")) {
                return c;
            } else {
                return null;
            }
        }));
        return textField;
    }

    private Button createMarker(){
        Button button = new Button();
        button.setCursor(Cursor.HAND);
        button.setPrefSize(36, 36);
        button.setOnAction(e -> {isClicked = !isClicked; 
            if(isClicked){
                button.setStyle("-fx-background-color: #00ff00;");
            }else{
                button.setStyle("");
            }
        });
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
