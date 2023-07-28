package es.lonely.sksolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class SKSolver {
    private Stage stage;
    private List<Map<Integer,List<String>>> sumList = new ArrayList<>();

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

    public List<Map<Integer, List<String>>> markerBoard(){
        Map<Integer, List<String>> res = new HashMap<>();
        GridPane markerBoard = (GridPane) stage.getScene().lookup("#selectorBoard");
        TextField sumText = (TextField) stage.getScene().lookup("#sum");
        for (Integer pos = 0; pos<81; pos++){
            Button button = ((Button) ((StackPane) markerBoard.getChildren().get(pos)).getChildren().get(0));
            Paint value = button.getBackground().getFills().get(0).getFill();
            if(value.equals(Paint.valueOf("#00ff00")) && !button.isDisable() && !sumText.getText().equals("") && res.containsKey(Integer.parseInt(sumText.getText()))){
                List<String> values = res.get(Integer.parseInt(sumText.getText()));
                values.add(button.getId());
                button.setDisable(true);
                res.put(Integer.parseInt(sumText.getText()), values);
            }else if(value.equals(Paint.valueOf("#00ff00")) && !button.isDisable() && !sumText.getText().equals("") && !res.containsKey(Integer.parseInt(sumText.getText()))){
                List<String> ls = new ArrayList<>();
                ls.add(button.getId());
                res.put(Integer.parseInt(sumText.getText()),ls);
                button.setDisable(true);
            }
        }
        if(!res.isEmpty()){
            sumList.add(res);
        }
        return sumList;
    }

    public void undoMarker(){
        GridPane board = (GridPane) stage.getScene().lookup("#selectorBoard");
        List<Map<Integer, List<String>>> markerBoard = markerBoard();
        if(!markerBoard.isEmpty()){

            for(Integer pos = 0; pos<81; pos++){

                Button button = ((Button) ((StackPane) board.getChildren().get(pos)).getChildren().get(0));
                String buttonId = button.getId();
                Map<Integer, List<String>> lastSave = markerBoard.get(markerBoard.size()-1);
                if(lastSave.values().stream().flatMap(List::stream).toList().contains(buttonId)){
                    button.setDisable(false); 
                }

            }

            markerBoard.get(markerBoard.size()-1);
            markerBoard.remove(markerBoard.size()-1); markerBoard.forEach(x-> System.out.println(x));
        }
        
    }   
    
}
