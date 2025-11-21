package com.example;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.List;

public class LibraryComparison {
    
    public static Result solveWithLibrary(int[][] costMatrix) {
        int n = costMatrix.length;
        
        double[] coefficients = new double[n * n];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                coefficients[idx++] = costMatrix[i][j];
            }
        }
        
        LinearObjectiveFunction objective = new LinearObjectiveFunction(coefficients, 0);
        
        List<LinearConstraint> constraints = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            double[] coeffs = new double[n * n];
            for (int j = 0; j < n; j++) {
                coeffs[i * n + j] = 1.0;
            }
            constraints.add(new LinearConstraint(coeffs, Relationship.EQ, 1.0));
        }
        
        for (int j = 0; j < n; j++) {
            double[] coeffs = new double[n * n];
            for (int i = 0; i < n; i++) {
                coeffs[i * n + j] = 1.0;
            }
            constraints.add(new LinearConstraint(coeffs, Relationship.EQ, 1.0));
        }
        
        for (int i = 0; i < n * n; i++) {
            double[] coeffs = new double[n * n];
            coeffs[i] = 1.0;
            constraints.add(new LinearConstraint(coeffs, Relationship.GEQ, 0.0));
        }
        
        LinearConstraintSet constraintSet = new LinearConstraintSet(constraints);
        SimplexSolver solver = new SimplexSolver();
        
        try {
            org.apache.commons.math3.optim.PointValuePair solution = 
                solver.optimize(objective, constraintSet, GoalType.MINIMIZE);
            
            double[] point = solution.getPoint();
            double optimalValue = solution.getValue();
            
            int[] assignment = new int[n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (Math.abs(point[i * n + j] - 1.0) < 0.001) {
                        assignment[i] = j;
                        break;
                    }
                }
            }
            
            return new Result(assignment, (int) Math.round(optimalValue));
        } catch (Exception e) {
            throw new RuntimeException("Помилка при розв'язанні через бібліотеку: " + e.getMessage(), e);
        }
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

