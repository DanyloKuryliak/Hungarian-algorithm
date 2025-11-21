package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HungarianAlgorithmTest {

    @Test
    public void testSimple2x2() {
        int[][] costMatrix = {
            {1, 2},
            {2, 1}
        };
        
        HungarianAlgorithm solver = new HungarianAlgorithm(costMatrix);
        int[] assignment = solver.solve();
        int totalCost = solver.getTotalCost(costMatrix);
        
        boolean[] used = new boolean[2];
        for (int i = 0; i < 2; i++) {
            assertTrue(assignment[i] >= 0 && assignment[i] < 2, 
                "Призначення має бути в межах [0, 1]");
            assertFalse(used[assignment[i]], 
                "Кожна робота має бути призначена тільки один раз");
            used[assignment[i]] = true;
        }
        
        assertEquals(2, totalCost, "Мінімальна вартість має бути 2");
    }

    @Test
    public void test3x3Example() {
        int[][] costMatrix = {
            {9, 2, 7},
            {6, 4, 3},
            {5, 8, 1}
        };
        
        HungarianAlgorithm solver = new HungarianAlgorithm(costMatrix);
        int[] assignment = solver.solve();
        int totalCost = solver.getTotalCost(costMatrix);
        
        boolean[] used = new boolean[3];
        for (int i = 0; i < 3; i++) {
            assertTrue(assignment[i] >= 0 && assignment[i] < 3);
            assertFalse(used[assignment[i]]);
            used[assignment[i]] = true;
        }
        
        assertEquals(9, totalCost, "Мінімальна вартість має бути 9");
    }



    @Test
    public void testAllZeros() {
        int[][] costMatrix = {
            {0, 0},
            {0, 0}
        };
        
        HungarianAlgorithm solver = new HungarianAlgorithm(costMatrix);
        int[] assignment = solver.solve();
        int totalCost = solver.getTotalCost(costMatrix);
        
        assertEquals(0, totalCost, "Вартість має бути 0");
        
        boolean[] used = new boolean[2];
        for (int i = 0; i < 2; i++) {
            assertTrue(assignment[i] >= 0 && assignment[i] < 2);
            assertFalse(used[assignment[i]]);
            used[assignment[i]] = true;
        }
    }

    @Test
    public void testIdentityMatrix() {
        int[][] costMatrix = {
            {0, 1, 1},
            {1, 0, 1},
            {1, 1, 0}
        };
        
        HungarianAlgorithm solver = new HungarianAlgorithm(costMatrix);
        int[] assignment = solver.solve();
        int totalCost = solver.getTotalCost(costMatrix);
        
        assertEquals(0, totalCost, "Мінімальна вартість має бути 0");
        
        for (int i = 0; i < 3; i++) {
            assertEquals(i, assignment[i], 
                "Оптимальне призначення має бути діагональним");
        }
    }

    @Test
    public void testVerificationWithBruteForce() {
        int[][] costMatrix = {
            {9, 2, 7},
            {6, 4, 3},
            {5, 8, 1}
        };
        
        HungarianAlgorithm solver = new HungarianAlgorithm(costMatrix);
        int[] assignment = solver.solve();
        int hungarianCost = solver.getTotalCost(costMatrix);
        
        BruteForceSolver.Result bruteForceResult = 
            BruteForceSolver.bruteForceSolve(costMatrix);
        
        assertEquals(bruteForceResult.totalCost, hungarianCost, 
            "Результати угорського алгоритму та brute-force мають збігатися");
    }

}

