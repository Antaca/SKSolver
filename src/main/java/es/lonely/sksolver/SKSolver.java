package es.lonely.sksolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
       if(board!=null){

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
        }else{
            return null;
        }
    }

    public int[][] markerBoard(){
       int[][] matrix = new int[9][9]; 
       GridPane board = (GridPane) stage.getScene().lookup("#selectorBoard");
       if(board!=null){

           for (Integer pos = 0; pos < 81; pos++){
               String value = ((Button) ((StackPane) board.getChildren().get(pos)).getChildren().get(0)).getText();
               if(value.equals("")){
                   value = "0";
                }
                String valueId = ((Button) ((StackPane) board.getChildren().get(pos)).getChildren().get(0)).getId();
                int col = Integer.parseInt(String.valueOf(valueId.charAt(0)));
                int row = Integer.parseInt(String.valueOf(valueId.charAt(2)));
                int num = Integer.parseInt(value);
                matrix[row][col] = num;
            }
            return matrix;
        }else{
            return null;
        }
    }

    public List<Map<Integer, List<String>>> sumList(){
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
        List<Map<Integer, List<String>>> markerBoard = sumList();
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

    public boolean isInputValid(int[][] matrix) {
        // Verificar filas
        for (int row = 0; row < matrix.length; row++) {
            boolean[] used = new boolean[matrix.length];
            for (int col = 0; col < matrix.length; col++) {
                int num = matrix[row][col];
                if (num != 0) {
                    if (used[num - 1]) {
                        return false; // Número repetido en la fila
                    }
                    used[num - 1] = true;
                }
            }
        }
    
        // Verificar columnas
        for (int col = 0; col < matrix.length; col++) {
            boolean[] used = new boolean[matrix.length];
            for (int row = 0; row < matrix.length; row++) {
                int num = matrix[row][col];
                if (num != 0) {
                    if (used[num - 1]) {
                        return false; // Número repetido en la columna
                    }
                    used[num - 1] = true;
                }
            }
        }
    
        // Verificar zonas 3x3
        int subMatrix = (int) Math.sqrt(matrix.length);
        for (int blockRow = 0; blockRow < matrix.length; blockRow += subMatrix) {
            for (int blockCol = 0; blockCol < matrix.length; blockCol += subMatrix) {
                boolean[] used = new boolean[matrix.length];
                for (int row = 0; row < subMatrix; row++) {
                    for (int col = 0; col < subMatrix; col++) {
                        int num = matrix[blockRow + row][blockCol + col];
                        if (num != 0) {
                            if (used[num - 1]) {
                                return false; // Número repetido en la zona 3x3
                            }
                            used[num - 1] = true;
                        }
                    }
                }
            }
        }
    
        return true; // Todos los valores iniciales cumplen con las restricciones del Sudoku
    }
    
    public boolean isValid(int[][] matrix, int row, int column, int num){
        //Check if the num is in the same row
        for(int col = 0; col < matrix.length; col++){
            if(matrix[row][col] == num){
                return false;
            }        
        }
        //Check if the num is in the same column
        for(int r = 0; r < matrix.length; r++){
            if(matrix[r][column] == num){
                return false;
            }
        }
        int subMatrix = (int) Math.sqrt(matrix.length);
        int subMatrixRow = row - row % subMatrix;
        int subMatrixColumn = column - column % subMatrix;

        //Check if the num is present in the 3x3 submatrix
        for(int r = subMatrixRow; r < subMatrixRow + subMatrix; r++){
            for(int c = subMatrixColumn; c < subMatrixColumn + subMatrix; c++){
                if(matrix[r][c] == num){
                    return false;
                }            
            }
        }

        return true;
    }

    public boolean isValidSum(int[][] matrix, int row, int column, int num, List<Map<Integer,List<String>>> markerBoard){

        //Check if the num is in the same row
        for(int col = 0; col < matrix.length; col++){
            if(matrix[row][col] == num){
                return false;
            }        
        }
        //Check if the num is in the same column
        for(int r = 0; r < matrix.length; r++){
            if(matrix[r][column] == num){
                return false;
            }
        }
        int subMatrix = (int) Math.sqrt(matrix.length);
        int subMatrixRow = row - row % subMatrix;
        int subMatrixColumn = column - column % subMatrix;

        //Check if the num is present in the 3x3 submatrix
        for(int r = subMatrixRow; r < subMatrixRow + subMatrix; r++){
            for(int c = subMatrixColumn; c < subMatrixColumn + subMatrix; c++){
                if(matrix[r][c] == num){
                    return false;
                }            
            }
        }

        if(!markerBoard.isEmpty()){
            for(Map<Integer,List<String>> map : markerBoard){
                for(Map.Entry<Integer,List<String>> entry : map.entrySet()){
                    List<Integer> values = new ArrayList<>();
                    int sum = entry.getKey();
                    List<String> cells = entry.getValue();
                    int current = 0;
                    for(String cell:cells){
                        int c = Integer.parseInt(String.valueOf(cell.charAt(0)));
                        int r = Integer.parseInt(String.valueOf(cell.charAt(2)));
                        if(r == row && c == column){
                            values.add(num);
                        }else{
                            values.add(matrix[r][c]);
                        }
                    }
                    current = values.stream().reduce((x,y) -> x + y).orElse(null);
                    if(current > sum){
                        return false;
                    }
                    if(!values.contains(0)){
                        if(current != sum){
                            return false;
                        }
                        Set<Integer> setValues = new HashSet<>(values);
                        if(values.size() != setValues.size()){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean solveSudoku(int[][] matrix, int length) {
        int r = -1;
        int c = -1;
        boolean isFilled = true;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (matrix[i][j] == 0) {
                    r = i;
                    c = j;
                    isFilled = false;
                    break;
                }
            }

            if (!isFilled) {
                break;
            }
        }

        if (isFilled) {
            return true;
        }

        for (int num = 1; num <= length; num++) {
            if (isValid(matrix, r, c, num)) {
                matrix[r][c] = num;
                if (solveSudoku(matrix, length)) {
                    return true;
                }
                matrix[r][c] = 0;
            }
        }
        return false;
    }

    public boolean solveKillerSudoku(int[][] matrix, int length){
        List<Map<Integer,List<String>>> markerBoard = sumList();
        int r = -1;
        int c = -1;
        boolean isFilled = true;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (matrix[i][j] == 0) {
                    r = i;
                    c = j;
                    isFilled = false;
                    break;
                }
            }

            if (!isFilled) {
                break;
            }
        }

        if (isFilled) {
            return true;
        }

        for (int num = 1; num <= length; num++) {
            if (isValidSum(matrix, r, c, num, markerBoard)) {
                matrix[r][c] = num;
                if (solveKillerSudoku(matrix, length)) {
                    return true;
                }
                matrix[r][c] = 0;
            }
        }
        return false;
    }

    public void display(int[][] matrix) {

        GridPane board = (GridPane) stage.getScene().lookup("#solveBoard");
        if(board!=null){

           for (Integer pos = 0; pos < 81; pos++){
               String valueId = ((TextField) ((StackPane) board.getChildren().get(pos)).getChildren().get(0)).getId();
               int col = Integer.parseInt(String.valueOf(valueId.charAt(0)));
               int row = Integer.parseInt(String.valueOf(valueId.charAt(2)));
               TextField value = ((TextField) ((StackPane) board.getChildren().get(pos)).getChildren().get(0));
               value.setText(String.valueOf(matrix[row][col]));
            }
        }
    }
    
}
