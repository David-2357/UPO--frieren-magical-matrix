
package upo.frieren.magical.matrix;

public class UPOFrierenMagicalMatrix {

    public static final int NUM_ITERATIONS = 5; // cuantas iteraciones se hacen por cada tamaño de filas y columnas
    public static final int[] NUM_ROWS = {2, 5, 10, 20, 50, 100, 500, 1000}; // array de numeros de filas
    public static final int[] NUM_COLUMNS = {2, 5, 10, 20, 50, 100, 500, 1000}; // array de numeros de columnas
    public static int MAX_NUM_RANDOM = 10; // numero maximo que puede salir al rellenar las matrices con numeros aleatorios

    @SuppressWarnings("unused") // si dejas la funcion de verResultados comentada, no se usan las matrices resultado, por eso quito los warnings
    public static void main(String[] args) {
        for(int tamIndex = 0; tamIndex < NUM_ROWS.length; tamIndex++){
            long iterativeMultiplicationTotal = 0;
            long recursiveMultiplicationTotal = 0;
            double iterativeDurationMiliseconds = 0;
            double recursiveDurationMiliseconds = 0;

            int[][] matrix1 = new int[NUM_ROWS[tamIndex]][NUM_COLUMNS[tamIndex]];
            int[][] matrix2 = new int[NUM_ROWS[tamIndex]][NUM_COLUMNS[tamIndex]];

            int[][] matrixResIt = new int[NUM_ROWS[tamIndex]][NUM_COLUMNS[tamIndex]];
            int[][] matrixResRec = new int[NUM_ROWS[tamIndex]][NUM_COLUMNS[tamIndex]];

            for(int i = 0; i < NUM_ITERATIONS; i++){
                matrix1 = generateRandomMatrix(NUM_ROWS[tamIndex], NUM_COLUMNS[tamIndex]);
                matrix2 = generateRandomMatrix(NUM_ROWS[tamIndex], NUM_COLUMNS[tamIndex]);

                long iterativeMultiplicationStart = System.nanoTime();
                matrixResIt = UPOFrierenMagicalMatrix.multiplyMatrixIterative(matrix1, matrix2);
                long iterativeMultiplicationEnd = System.nanoTime();


                long recursiveMultiplicationStart = System.nanoTime();
                matrixResRec = UPOFrierenMagicalMatrix.callMultiplyMatrixRecursive(matrix1, matrix2);
                long recursiveMultiplicationEnd = System.nanoTime();

                // Comparar para ver si los resultados de la multiplicación iterativa y recursiva son iguales
//                if (!UPOFrierenMagicalMatrix.compareMatrix(UPOFrierenMagicalMatrix.multiplyMatrixIterative(matrix1, matrix2), UPOFrierenMagicalMatrix.callMultiplyMatrixRecursive(matrix1, matrix2))) {
//                    System.out.println("Error: Los resultados de la multiplicación iterativa y recursiva no coinciden.");
//                }

                iterativeMultiplicationTotal += iterativeMultiplicationEnd - iterativeMultiplicationStart;
                recursiveMultiplicationTotal += recursiveMultiplicationEnd - recursiveMultiplicationStart;

                // ver los resultados de las multiplicaciones para verificar si están bien
                // viewResults(matrix1, matrix2, matrixResIt, matrixResRec, i, tamIndex);
            }

            iterativeDurationMiliseconds = iterativeMultiplicationTotal / 1000000.0; // entre un millón para pasar de nanosegundos a ms
            recursiveDurationMiliseconds = recursiveMultiplicationTotal / 1000000.0;

            printResults(matrix1, matrix2, matrixResIt, matrixResRec, tamIndex, (long) iterativeDurationMiliseconds, (long) recursiveDurationMiliseconds);
        }
    }

    public static void printResults(int[][] matrix1, int[][] matrix2, int[][] matrixResIt, int[][] matrixResRec, int tamIndex, long iterativeDurationMiliseconds, long recursiveDurationMiliseconds) {
         System.out.println("\n=============\n Resultados para un tamaño de " + NUM_ROWS[tamIndex] + "x" + NUM_COLUMNS[tamIndex] + ": ");

        System.out.println("Iterativo: " + iterativeDurationMiliseconds + "ms");
        System.out.println("Recursivo: " + recursiveDurationMiliseconds + "ms");


        // Si son muchas, no mostrar las matrices, porque se llenaria la pantalla de numeros y no se veria nada
        if (NUM_ROWS[tamIndex] > 10 || NUM_COLUMNS[tamIndex] > 10) {
            System.out.println("\nMatrices demasiado grandes para mostrar, no se muestran los resultados de la multiplicación.");

        }else {
            System.out.println("\nMatriz 1: ");
            printMatrix(matrix1);

            System.out.println("\nMatriz 2: ");
            printMatrix(matrix2);

            System.out.println("\nMatriz resultado de la multiplicación iterativa: ");
            printMatrix(matrixResIt);

            System.out.println("\nMatriz resultado de la multiplicación recursiva: ");
            printMatrix(matrixResRec);
        }


        // Comparar para ver si los resultados de la multiplicación iterativa y recursiva son iguales
        if (!compareMatrix(matrixResIt, matrixResRec)) {
            System.out.println("\nError: Los resultados de la multiplicación iterativa y recursiva no coinciden.");
        } else {
            System.out.println("\nLos resultados de ambas multiplicaciones coinciden!");
        }
    }

    // Metodo para generar matrices aleatorias
    public static int[][] generateRandomMatrix(int rows, int columns){
        int[][] matrix = new int[rows][columns];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                matrix[i][j] = (int) (Math.random() * MAX_NUM_RANDOM);
            }
        }

        return matrix;
    }

    // Metodo auxiliar por si queremos comprobar
    public static void printMatrix(int[][] matrix){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < matrix.length; i++){
            sb.append("{");
            for(int j = 0; j < matrix[0].length-1; j++){
                sb.append(matrix[i][j]).append(", ");
            }
            sb.append(matrix[i][matrix[0].length-1]).append("}");
            System.out.println(sb.toString());
            sb.setLength(0);
        }
    }

    // Metodo auxiliar para comprobar si las matrices se pueden multiplicar
    public static boolean bothMatrixCanBeMultiplied(int[][] matrix1, int[][] matrix2) {
        // Comprobamos que el numero de columnas de la primera matriz sea igual al numero de filas de la segunda matriz
        if (matrix1[0].length != matrix2.length) {
            return false;
        }

        // Comprobamos la ultima por si acaso xd
        return matrix1[matrix1.length - 1].length == matrix2.length;
    }

    public static int[][] multiplyMatrixIterative(int[][] matrix1, int[][] matrix2) {
        // Validacion inicial (hecha por Fern, porque Frieren sigue dormida xd)
        if (!UPOFrierenMagicalMatrix.bothMatrixCanBeMultiplied(matrix1, matrix2)) {
            throw new IllegalArgumentException("\nMuy mal señorita Frieren,  la cantidad de columnas de la primera tiene que ser igual a la cantidad de filas que la segunda\n -Fern");
        }

        int totalRows = matrix1.length;
        int totalCols = matrix2[0].length;
        int rowsMatrix2 = matrix2.length;

        int[][] resultMatrix = new int[totalRows][totalCols];
        int[][] colsValue = new int[totalCols][rowsMatrix2];
        boolean[] didColsInit = new boolean[totalCols];

        for (int row = 0; row < totalRows; row++) {
            // Guardar la fila actual
            int[] rowValue = matrix1[row];

            for (int col = 0; col < totalCols; col++) {
                // Guardar la columna actual
                int[] colValue;

                // Obtener la lista de una columna de la segunda matriz (programacion dinamica)
                if (didColsInit[col]) {
                    colValue = colsValue[col];
                }else {
                    colValue = new int[rowsMatrix2];

                    for (int i = 0; i < matrix2[0].length; i++) {
                        colValue[i] = matrix2[i][col];
                    }

                    colsValue[col] = colValue;
                    didColsInit[col] = true;
                }

                // Multiplicarlas y sumarlas
                resultMatrix[row][col] = UPOFrierenMagicalMatrix.multiplyListsInMatrix(rowValue, colValue);
            }
        }

        return resultMatrix;
    }

    // Metodo auxiliar que multiplica y suma la fila de la primera con la columna de la segunda
    public static int multiplyListsInMatrix(int[] rowOfFirst, int[] colOfSecond) {
        int result = 0;

        for (int i = 0; i < rowOfFirst.length; i++) {
            result += rowOfFirst[i] * colOfSecond[i];
        }

        return result;
    }

    // Funcion para llamar a la recursiva, para ejecutar cosas que solo quiero que se hagan una vez
    // y tambien para no pasarle parametros que siempre tienen el mismo valor por defecto
    public static int[][] callMultiplyMatrixRecursive(int[][] matrix1, int[][] matrix2) {
        // Validacion inicial (hecha por Fern, porque Frieren sigue dormida xd)
        if (!UPOFrierenMagicalMatrix.bothMatrixCanBeMultiplied(matrix1, matrix2)) {
            throw new IllegalArgumentException("\nMuy mal señorita Frieren, la cantidad de columnas de la primera tiene que ser igual a la cantidad de filas que la segunda\n -Fern");
        }

        int totalRows = matrix1.length;
        int totalCols = matrix2[0].length;

        int[][] result = new int[totalCols][totalRows];

        multiplyColumns(matrix1, matrix2, 0, result);

        return result;
    }

    // Metodo recursivo que multiplica las columnas de la segunda matriz por todas las filas de la primera
    public static void multiplyColumns(int[][] matrix1, int[][] matrix2, int col ,int[][] result) {
        // Caso base, cuando se han terminado las columnas de grimorios
        if (col >= matrix2[0].length) {
            return;
        }

        // Sacamos la columna actual
        int[] currentColumn = new int[matrix2.length];
        for (int i = 0; i < matrix2.length; i++) {
            currentColumn[i] = matrix2[i][col];
        }

        // Multiplicamos la columna de grimorios por todas las filas de la primera matriz
        multiplyRows(matrix1, currentColumn, 0, col, result);

        // Avanzamos a la siguiente columna de grimorios
        multiplyColumns(matrix1, matrix2, col + 1, result);
    }

    // Metodo recursivo que multiplica una columna de la segunda matriz por todas las filas de la primera (complementario al primero)
    public static void multiplyRows(int[][] matrix1, int[] column, int row, int col, int[][] result) {
        // Caso base, se han acabado las filas de grimorios
        if (row >= matrix1.length) {
            return;
        }

        // Multiplicamos y sumamos la columna y fila de cada una
        int sum = 0;
        for (int k = 0; k < matrix1[0].length; k++) {
            sum += matrix1[row][k] * column[k];
        }

        result[row][col] = sum;

        // Seguimos con la siguiente fila
        multiplyRows(matrix1, column, row + 1, col, result);
    }

    // Metodo auxiliar para ver si dan el mismo resultado la iterativa y la recursiva
    public static boolean compareMatrix(int[][] matrix1, int[][] matrix2) {
        if (!UPOFrierenMagicalMatrix.bothMatrixCanBeMultiplied(matrix1, matrix2)) {
            return false;
        }

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                if (matrix1[i][j] != matrix2[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void viewResults(int[][] matrix1, int[][] matrix2, int[][] matrixResIt, int[][] matrixResRec, int index, int tamIndex){
        // Solo mostrar la multiplicacion de matrices de la primera iteracion para cada tamaño, asi no llenamos la pantalla
        if(index != 0){
            return; 
        }

        System.out.println("\n=============\n Resultados para un tamaño de " + NUM_ROWS[tamIndex] + "x" + NUM_COLUMNS[tamIndex] + ": ");

        System.out.println("\n\nMatriz 1: ");
        printMatrix(matrix1);

        System.out.println("\n\nMatriz 2: ");
        printMatrix(matrix2);

        System.out.println("\n\nMatriz resultado de la multiplicación iterativa: ");
        printMatrix(matrixResIt);
        
        System.out.println("\n\nMatriz resultado de la multiplicación recursiva: ");
        printMatrix(matrixResRec);

        // Comparar para ver si los resultados de la multiplicación iterativa y recursiva son iguales
        if (!compareMatrix(matrixResIt, matrixResRec)) {
            System.out.println("\nError: Los resultados de la multiplicación iterativa y recursiva no coinciden");
        } else {
            System.out.println("\n\nAdemás, los resultados de ambas multiplicaciones coinciden!");
        }

    }
}
