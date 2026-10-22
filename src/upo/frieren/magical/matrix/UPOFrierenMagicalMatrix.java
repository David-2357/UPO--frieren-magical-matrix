
package upo.frieren.magical.matrix;

public class UPOFrierenMagicalMatrix {

    public static final int NUM_ITERATIONS = 5;
    public static final int[] NUM_ROWS = {2, 5, 10, 20, 50, 100};
    public static final int[] NUM_COLUMNS = {2, 5, 10, 20, 50, 100};
    public static int MAX_NUM_RANDOM = 10;

    public static void main(String[] args) {
        for(int tamIndex = 0; tamIndex < NUM_ROWS.length; tamIndex++){
            long iterativeMultiplicationTotal = 0;
            long recursiveMultiplicationTotal = 0;
            double iterativeDurationMiliseconds = 0;
            double recursiveDurationMiliseconds = 0;
            for(int i = 0; i < NUM_ITERATIONS; i++){
                int[][] matrix = generateRandomMatrix(NUM_ROWS[tamIndex], NUM_COLUMNS[tamIndex]);
                int[][] matrix2 = generateRandomMatrix(NUM_ROWS[tamIndex], NUM_COLUMNS[tamIndex]);
                long iterativeMultiplicationStart = System.nanoTime();
                // funcion de mult iterativa aqui
                long iterativeMultiplicationEnd = System.nanoTime();

                long recursiveMultiplicationStart = System.nanoTime();
                // funcion de mult recursiva aqui
                long recursiveMultiplicationEnd = System.nanoTime();

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

    public static int[][] generateRandomMatrix(int rows, int columns){
        int[][] matrix = new int[rows][columns];

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                matrix[i][j] = (int) (Math.random() * MAX_NUM_RANDOM);
            }
        }

        return matrix;
    }

    // metodo auxiliar por si queremos comprobar
    public static void printMatrix(int[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            System.out.print("\n{");
            for(int j = 0; j < matrix[0].length-1; j++){
                System.out.print(matrix[i][j] + ", ");
            }
            System.out.print(matrix[i][matrix[0].length-1] + "}");
        }
    }

    public static boolean bothMatrixAreSameLength(int[][] matrix1, int[][] matrix2) {
        // Comprobamos que la primera lista sea del mismo tamano
        if (matrix1.length != matrix2.length) {
            return false;
        }

        // Comprobamos la primera de dentro
        if (matrix1[0].length != matrix2[0].length) {
            return false;
        }

        // Comprobamos la ultima por si acaso xd
        if (matrix1[matrix1.length - 1].length != matrix2[matrix2.length - 1].length) {
            return false;
        }

        return true;
    }

    public static int[][] multiplicateMatrixIterative(int[][] matrix1, int[][] matrix2) {
        // Validacion inicial (hecha por Fern, porque Frieren sigue dormida xd)
        if (!UPOFrierenMagicalMatrix.bothMatrixAreSameLength(matrix1, matrix2)) {
            throw new IllegalArgumentException("\nMuy mal señorita Frieren, la matriz de grimorios tienen que ser del mismo tamaño\n -Fern");
        }

        int interiorLength = matrix1[0].length;
        int[][] resultMatrix = new int[matrix1.length][interiorLength];

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < interiorLength; j++) {
                resultMatrix[i][j] = matrix1[i][j] * matrix2[i][j];
            }
        }

        return resultMatrix;
    }

    public static int[][] multiplicateMatrixRecursive(int[][] matrix1, int[][] matrix2, int i, int j, int[][] result) {
        int interiorLength = matrix1[0].length;

        // Hacerlo solo en el primer caso para que sea optima
        if (i == 0 && j == 0) {
            result = new int[matrix1.length][interiorLength];

            // Validacion inicial (hecha por Fern, porque Frieren sigue dormida xd)
            if (!UPOFrierenMagicalMatrix.bothMatrixAreSameLength(matrix1, matrix2)) {
                throw new IllegalArgumentException("\nMuy mal señorita Frieren, la matriz de grimorios tienen que ser del mismo tamaño\n -Fern");
            }
        }


        if (i == matrix1.length-1 && j == interiorLength-1) {
            return result;
        }

        if (j == interiorLength-1) {
            j = 0;
            i++;
        }

        result[i][j] = matrix1[i][j] * matrix2[i][j];

        return result;
    }
}
