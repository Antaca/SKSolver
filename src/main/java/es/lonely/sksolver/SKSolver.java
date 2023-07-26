package es.lonely.sksolver;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SKSolver {
    private Stage stage;

    public SKSolver(Stage stage){
        this.stage = stage;
    }

    public int[][] board(){
       int[][] matrix = new int[9][9]; 
       GridPane board = (GridPane) stage.getScene().lookup("#board");
       for (Integer pos = 0; pos < 81; pos++){
           String value = ((TextField) ((StackPane) board.getChildren().get(pos)).getChildren().get(0)).getText();
           if(value.equals("")){
            value = "0";
           }
           String valueId = ((TextField) ((StackPane) board.getChildren().get(pos)).getChildren().get(0)).getId();
           int col = Integer.parseInt(String.valueOf(valueId.charAt(0)));
           int row = Integer.parseInt(String.valueOf(valueId.charAt(2)));
           int num = Integer.parseInt(value);
           matrix[row][col] = num;
       }
       return matrix;
    }
    
}
