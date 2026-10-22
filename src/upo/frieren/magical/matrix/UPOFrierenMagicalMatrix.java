
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
    
}
