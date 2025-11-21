package com.example;

public class PerformanceMeasurement {
    
    public static void main(String[] args) {
        int[][] test1 = {
            {9, 2, 7},
            {6, 4, 3},
            {5, 8, 1}
        };
        
        MeasurementResult result1 = measurePerformance(test1);
        MeasurementResult result1bf = measureBruteForce(test1);
        
        System.out.printf("Тест 1 (3x3):\n");
        System.out.printf("Угорський: %.6f мс, вартість: %d\n", result1.time, result1.cost);
        System.out.printf("Brute-force: %.6f мс, вартість: %d\n", result1bf.time, result1bf.cost);
        System.out.printf("Прискорення: %.2fx\n\n", result1bf.time / result1.time);
        
        int[][] test2 = {
            {7, 53, 183, 439, 863},
            {497, 383, 563, 79, 973},
            {287, 63, 343, 169, 583},
            {627, 343, 773, 959, 943},
            {767, 473, 103, 699, 303}
        };
        
        MeasurementResult result2 = measurePerformance(test2);
        
        System.out.printf("Тест 2 (5x5):\n");
        System.out.printf("Угорський: %.6f мс, вартість: %d\n", result2.time, result2.cost);
        System.out.printf("Brute-force: N/A\n\n");
        
        MeasurementResult lib1 = measureLibrary(test1);
        MeasurementResult lib2 = measureLibrary(test2);
        
        System.out.printf("Порівняння з бібліотекою:\n");
        System.out.printf("Тест 1: Угорський %.6f мс, Бібліотека %.6f мс, Прискорення %.2fx\n", 
            result1.time, lib1.time, lib1.time / result1.time);
        System.out.printf("Тест 2: Угорський %.6f мс, Бібліотека %.6f мс, Прискорення %.2fx\n", 
            result2.time, lib2.time, lib2.time / result2.time);
    }
    
    private static MeasurementResult measurePerformance(int[][] costMatrix) {
        int n = costMatrix.length;
        int[][] originalMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(costMatrix[i], 0, originalMatrix[i], 0, n);
        }
        
        int iterations = Math.min(10, 100 / n);
        long totalTime = 0;
        int cost = 0;
        
        try {
            for (int i = 0; i < iterations; i++) {
                int[][] matrixCopy = new int[n][n];
                for (int j = 0; j < n; j++) {
                    System.arraycopy(originalMatrix[j], 0, matrixCopy[j], 0, n);
                }
                
                HungarianAlgorithm solver = new HungarianAlgorithm(matrixCopy);
                long startTime = System.nanoTime();
                int[] assignment = solver.solve();
                long endTime = System.nanoTime();
                
                totalTime += (endTime - startTime);
                if (i == 0) {
                    cost = solver.getTotalCost(originalMatrix);
                }
            }
        } catch (RuntimeException e) {
            return new MeasurementResult(0, 0);
        }
        
        return new MeasurementResult(totalTime / (iterations * 1_000_000.0), cost);
    }
    
    private static MeasurementResult measureBruteForce(int[][] costMatrix) {
        int n = costMatrix.length;
        if (n > 10) {
            return new MeasurementResult(0, 0);
        }
        
        int iterations = 10;
        long totalTime = 0;
        int cost = 0;
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            BruteForceSolver.Result result = BruteForceSolver.bruteForceSolve(costMatrix);
            long endTime = System.nanoTime();
            
            totalTime += (endTime - startTime);
            if (i == 0) {
                cost = result.totalCost;
            }
        }
        
        return new MeasurementResult(totalTime / (iterations * 1_000_000.0), cost);
    }
    
    private static MeasurementResult measureLibrary(int[][] costMatrix) {
        int n = costMatrix.length;
        int iterations = 10;
        long totalTime = 0;
        int cost = 0;
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            LibraryComparison.Result result = LibraryComparison.solveWithLibrary(costMatrix);
            long endTime = System.nanoTime();
            
            totalTime += (endTime - startTime);
            if (i == 0) {
                cost = result.totalCost;
            }
        }
        
        return new MeasurementResult(totalTime / (iterations * 1_000_000.0), cost);
    }
    
    private static class MeasurementResult {
        final double time;
        final int cost;
        
        MeasurementResult(double time, int cost) {
            this.time = time;
            this.cost = cost;
        }
    }
}
