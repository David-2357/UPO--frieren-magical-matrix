
package upo.frieren.magical.matrix;

public class UPOFrierenMagicalMatrix {

    public static final int NUM_ITERATIONS = 5;
    public static final int[] NUM_ROWS = {2, 5, 10, 20, 50, 100, 1000};
    public static final int[] NUM_COLUMNS = {2, 5, 10, 20, 50, 100, 1000};
    public static int MAX_NUM_RANDOM = 10;

    public static void main(String[] args) {
        for(int tamIndex = 0; tamIndex < NUM_ROWS.length; tamIndex++){
            long iterativeMultiplicationTotal = 0;
            long recursiveMultiplicationTotal = 0;
            double iterativeDurationMiliseconds = 0;
            double recursiveDurationMiliseconds = 0;
            for(int i = 0; i < NUM_ITERATIONS; i++){
                int[][] matrix1 = generateRandomMatrix(NUM_ROWS[tamIndex], NUM_COLUMNS[tamIndex]);
                int[][] matrix2 = generateRandomMatrix(NUM_ROWS[tamIndex], NUM_COLUMNS[tamIndex]);
                long iterativeMultiplicationStart = System.nanoTime();
                UPOFrierenMagicalMatrix.multiplyMatrixIterative(matrix1, matrix2);
                long iterativeMultiplicationEnd = System.nanoTime();

                long recursiveMultiplicationStart = System.nanoTime();
                UPOFrierenMagicalMatrix.callMultiplyMatrixRecursive(matrix1, matrix2);
                long recursiveMultiplicationEnd = System.nanoTime();

                // Comparar para ver si los resultados de la multiplicación iterativa y recursiva son iguales
                if (!UPOFrierenMagicalMatrix.compareMatrix(UPOFrierenMagicalMatrix.multiplyMatrixIterative(matrix1, matrix2), UPOFrierenMagicalMatrix.callMultiplyMatrixRecursive(matrix1, matrix2))) {
                    System.out.println("Error: Los resultados de la multiplicación iterativa y recursiva no coinciden.");
                }

                iterativeMultiplicationTotal += iterativeMultiplicationEnd - iterativeMultiplicationStart;
                recursiveMultiplicationTotal += recursiveMultiplicationEnd - recursiveMultiplicationStart;
            }

            iterativeDurationMiliseconds = iterativeMultiplicationTotal / 1000000.0;
            recursiveDurationMiliseconds = recursiveMultiplicationTotal / 1000000.0;

            System.out.println("\n====================\nTotal de tiempo para " + NUM_ITERATIONS + " iteraciones para una matriz de tamaño " + NUM_ROWS[tamIndex] + "x" + NUM_COLUMNS[tamIndex] + ": ");
            System.out.println("Iterativo: " + iterativeDurationMiliseconds + "ms");
            System.out.println("Recursivo: " + recursiveDurationMiliseconds + "ms");
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
        for(int i = 0; i < matrix.length; i++){
            System.out.print("\n{");
            for(int j = 0; j < matrix[0].length-1; j++){
                System.out.print(matrix[i][j] + ", ");
            }
            System.out.print(matrix[i][matrix[0].length-1] + "}");
        }
    }

    // Metodo auxiliar para comprobar si las matrices se pueden multiplicar
    public static boolean bothMatrixCanBeMultiplied(int[][] matrix1, int[][] matrix2) {
        // Comprobamos que el numero de columnas de la primera matriz sea igual al numero de filas de la segunda matriz
        if (matrix1[0].length != matrix2.length) {
            return false;
        }

        // Comprobamos la ultima por si acaso xd
        if (matrix1[matrix1.length - 1].length != matrix2.length) {
            return false;
        }

        return true;
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

    // Metodo auxilirar que multiplica y suma la fila de la primera con la columna de la segunda
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

        MultiplyColumns(matrix1, matrix2, 0, result);

        return result;
    }

    // Metodo recursivo que multiplica las columnas de la segunda matriz por todas las filas de la primera
    public static void MultiplyColumns(int[][] matrix1, int[][] matrix2, int col ,int[][] result) {
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
        MultiplyColumns(matrix1, matrix2, col + 1, result);
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
}
