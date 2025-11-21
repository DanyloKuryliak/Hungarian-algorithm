package com.example;

public class Example {
    public static void main(String[] args) {
        int[][] costMatrix1 = {
            {9, 2, 7},
            {6, 4, 3},
            {5, 8, 1}
        };
        
        runExample(costMatrix1);
        
        int[][] costMatrix2 = {
            {1, 2},
            {2, 1}
        };
        
        runExample(costMatrix2);
    }
    
    private static void runExample(int[][] costMatrix) {
        printMatrix(costMatrix);
        
        HungarianAlgorithm solver = new HungarianAlgorithm(costMatrix);
        int[] assignment = solver.solve();
        int totalCost = solver.getTotalCost(costMatrix);
        
        for (int i = 0; i < assignment.length; i++) {
            System.out.printf("Робітник %d -> Робота %d (вартість: %d)\n", 
                i + 1, assignment[i] + 1, costMatrix[i][assignment[i]]);
        }
        
        System.out.printf("Сумарна вартість: %d\n", totalCost);
        
        if (costMatrix.length <= 10) {
            BruteForceSolver.Result bruteForceResult = 
                BruteForceSolver.bruteForceSolve(costMatrix);
            System.out.printf("Brute-force: %d\n", bruteForceResult.totalCost);
        }
        System.out.println();
    }
    
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.printf("%4d ", value);
            }
            System.out.println();
        }
    }
}
