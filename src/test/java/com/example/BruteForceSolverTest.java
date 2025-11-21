package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BruteForceSolverTest {

    @Test
    public void testBruteForce2x2() {
        int[][] costMatrix = {
            {1, 2},
            {2, 1}
        };
        
        BruteForceSolver.Result result = 
            BruteForceSolver.bruteForceSolve(costMatrix);
        
        assertNotNull(result.assignment, "Призначення не має бути null");
        assertEquals(2, result.assignment.length, 
            "Розмір призначення має бути 2");
        assertEquals(2, result.totalCost, 
            "Мінімальна вартість має бути 2");
        
        boolean[] used = new boolean[2];
        for (int i = 0; i < 2; i++) {
            assertFalse(used[result.assignment[i]]);
            used[result.assignment[i]] = true;
        }
    }

    @Test
    public void testBruteForce3x3() {
        int[][] costMatrix = {
            {9, 2, 7},
            {6, 4, 3},
            {5, 8, 1}
        };
        
        BruteForceSolver.Result result = 
            BruteForceSolver.bruteForceSolve(costMatrix);
        
        assertNotNull(result.assignment);
        assertEquals(3, result.assignment.length);
        assertEquals(9, result.totalCost, 
            "Мінімальна вартість має бути 9");
    }

    @Test
    public void testBruteForceThrowsExceptionForLargeMatrix() {
        int[][] costMatrix = new int[11][11];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                costMatrix[i][j] = i + j;
            }
        }
        
        assertThrows(IllegalArgumentException.class, () -> {
            BruteForceSolver.bruteForceSolve(costMatrix);
        }, "Має викидатися виняток для n > 10");
    }

    @Test
    public void testBruteForceAllZeros() {
        int[][] costMatrix = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        BruteForceSolver.Result result = 
            BruteForceSolver.bruteForceSolve(costMatrix);
        
        assertEquals(0, result.totalCost, 
            "Вартість має бути 0");
    }
}

