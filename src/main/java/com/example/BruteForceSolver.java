package com.example;

import java.util.ArrayList;
import java.util.List;

public class BruteForceSolver {
    
    public static Result bruteForceSolve(int[][] costMatrix) {
        int n = costMatrix.length;
        
        if (n > 10) {
            throw new IllegalArgumentException("Brute force можна використовувати тільки для n <= 10");
        }
        
        int[] bestAssignment = null;
        int minCost = Integer.MAX_VALUE;
        
        List<int[]> permutations = generatePermutations(n);
        
        for (int[] perm : permutations) {
            int cost = calculateCost(costMatrix, perm);
            if (cost < minCost) {
                minCost = cost;
                bestAssignment = perm.clone();
            }
        }
        
        return new Result(bestAssignment, minCost);
    }
    
    private static List<int[]> generatePermutations(int n) {
        List<int[]> result = new ArrayList<>();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        generatePermutationsHelper(arr, 0, result);
        return result;
    }
    
    private static void generatePermutationsHelper(int[] arr, int index, List<int[]> result) {
        if (index == arr.length) {
            result.add(arr.clone());
            return;
        }
        
        for (int i = index; i < arr.length; i++) {
            swap(arr, index, i);
            generatePermutationsHelper(arr, index + 1, result);
            swap(arr, index, i);
        }
    }
    
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private static int calculateCost(int[][] costMatrix, int[] assignment) {
        int cost = 0;
        for (int i = 0; i < assignment.length; i++) {
            cost += costMatrix[i][assignment[i]];
        }
        return cost;
    }
    
    public static class Result {
        public final int[] assignment;
        public final int totalCost;
        
        public Result(int[] assignment, int totalCost) {
            this.assignment = assignment;
            this.totalCost = totalCost;
        }
    }
}

