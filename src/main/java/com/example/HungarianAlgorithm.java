package com.example;

public class HungarianAlgorithm {
    private int[][] costMatrix;
    private int n;
    private int[] assignment;
    
    public HungarianAlgorithm(int[][] costMatrix) {
        this.n = costMatrix.length;
        this.costMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(costMatrix[i], 0, this.costMatrix[i], 0, n);
        }
        this.assignment = new int[n];
    }
    
    public int[] solve() {
        subtractRowMinima();
        
        subtractColumnMinima();
        
        int maxIterations = n * n * 2;
        int iteration = 0;
        
        while (iteration < maxIterations) {
            iteration++;
            
            boolean[][] graph = createZeroGraph();
            
            int[] matching = findMaximumMatching(graph);
            
            if (isCompleteMatching(matching)) {
                convertMatchingToAssignment(matching);
                break;
            }
            
            boolean matrixChanged = transformMatrix(graph, matching);
            
            if (!matrixChanged) {
                if (isCompleteMatching(matching)) {
                    convertMatchingToAssignment(matching);
                    break;
                }
                int minValue = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (costMatrix[i][j] > 0 && costMatrix[i][j] < minValue) {
                            minValue = costMatrix[i][j];
                        }
                    }
                }
                if (minValue != Integer.MAX_VALUE && minValue > 0) {
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            if (costMatrix[i][j] > 0) {
                                costMatrix[i][j] -= minValue;
                            }
                        }
                    }
                } else {
                    subtractRowMinima();
                    subtractColumnMinima();
                }
                continue;
            }
        }
        
        if (iteration >= maxIterations) {
            boolean[][] graph = createZeroGraph();
            int[] matching = findMaximumMatching(graph);
            if (isCompleteMatching(matching)) {
                convertMatchingToAssignment(matching);
            } else {
                for (int attempt = 0; attempt < 10; attempt++) {
                    subtractRowMinima();
                    subtractColumnMinima();
                    graph = createZeroGraph();
                    matching = findMaximumMatching(graph);
                    if (isCompleteMatching(matching)) {
                        convertMatchingToAssignment(matching);
                        return assignment;
                    }
                }
                throw new RuntimeException("Алгоритм не зійшовся за " + maxIterations + " ітерацій");
            }
        }
        
        return assignment;
    }
    
    private void subtractRowMinima() {
        for (int i = 0; i < n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (costMatrix[i][j] < min) {
                    min = costMatrix[i][j];
                }
            }
            if (min != Integer.MAX_VALUE) {
                for (int j = 0; j < n; j++) {
                    costMatrix[i][j] -= min;
                }
            }
        }
    }
    
    private void subtractColumnMinima() {
        for (int j = 0; j < n; j++) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                if (costMatrix[i][j] < min) {
                    min = costMatrix[i][j];
                }
            }
            if (min != Integer.MAX_VALUE) {
                for (int i = 0; i < n; i++) {
                    costMatrix[i][j] -= min;
                }
            }
        }
    }
    
    private boolean[][] createZeroGraph() {
        boolean[][] graph = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = (costMatrix[i][j] == 0);
            }
        }
        return graph;
    }
    
    private int[] findMaximumMatching(boolean[][] graph) {
        int[] matching = new int[n];
        for (int i = 0; i < n; i++) {
            matching[i] = -1;
        }
        
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            visited = new boolean[n];
            dfs(i, graph, matching, visited);
        }
        
        return matching;
    }
    
    private boolean dfs(int worker, boolean[][] graph, int[] matching, boolean[] visited) {
        for (int job = 0; job < n; job++) {
            if (graph[worker][job] && !visited[job]) {
                visited[job] = true;
                if (matching[job] == -1 || dfs(matching[job], graph, matching, visited)) {
                    matching[job] = worker;
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isCompleteMatching(int[] matching) {
        int count = 0;
        for (int j = 0; j < n; j++) {
            if (matching[j] != -1) {
                count++;
            }
        }
        return count == n;
    }
    
    private void convertMatchingToAssignment(int[] matching) {
        for (int i = 0; i < n; i++) {
            assignment[i] = -1;
        }
        
        for (int j = 0; j < n; j++) {
            if (matching[j] != -1) {
                int worker = matching[j];
                if (worker >= 0 && worker < n) {
                    if (assignment[worker] != -1) {
                        throw new RuntimeException("Робітник " + worker + " має подвійне призначення");
                    }
                    assignment[worker] = j;
                }
            }
        }
        
        for (int i = 0; i < n; i++) {
            if (assignment[i] == -1) {
                throw new RuntimeException("Робітник " + i + " не має призначення");
            }
        }
    }
    
    private boolean transformMatrix(boolean[][] graph, int[] matching) {
        boolean[] rowCovered = new boolean[n];
        boolean[] colCovered = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            boolean hasAssignment = false;
            for (int j = 0; j < n; j++) {
                if (matching[j] == i) {
                    hasAssignment = true;
                    break;
                }
            }
            if (!hasAssignment) {
                rowCovered[i] = true;
            }
        }
        
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < n; i++) {
                if (rowCovered[i]) {
                    for (int j = 0; j < n; j++) {
                        if (graph[i][j] && !colCovered[j]) {
                            colCovered[j] = true;
                            changed = true;
                        }
                    }
                }
            }
            for (int j = 0; j < n; j++) {
                if (colCovered[j] && matching[j] != -1) {
                    if (!rowCovered[matching[j]]) {
                        rowCovered[matching[j]] = true;
                        changed = true;
                    }
                }
            }
        }
        
        int minUncovered = Integer.MAX_VALUE;
        boolean hasUncovered = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!rowCovered[i] && !colCovered[j]) {
                    hasUncovered = true;
                    if (costMatrix[i][j] < minUncovered) {
                        minUncovered = costMatrix[i][j];
                    }
                }
            }
        }
        
        if (!hasUncovered) {
            minUncovered = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (costMatrix[i][j] < minUncovered) {
                        minUncovered = costMatrix[i][j];
                    }
                }
            }
            if (minUncovered == Integer.MAX_VALUE || minUncovered == 0) {
                return false;
            }
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    costMatrix[i][j] -= minUncovered;
                    if (costMatrix[i][j] < 0) {
                        costMatrix[i][j] = 0;
                    }
                }
            }
            return true;
        }
        
        if (minUncovered == 0 || minUncovered == Integer.MAX_VALUE) {
            return false;
        }
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!rowCovered[i] && !colCovered[j]) {
                    costMatrix[i][j] -= minUncovered;
                    if (costMatrix[i][j] < 0) {
                        costMatrix[i][j] = 0;
                    }
                } else if (rowCovered[i] && colCovered[j]) {
                    costMatrix[i][j] += minUncovered;
                }
            }
        }
        
        return true;
    }
    
    public int getTotalCost(int[][] originalCostMatrix) {
        int totalCost = 0;
        for (int i = 0; i < n; i++) {
            if (assignment[i] < 0 || assignment[i] >= n) {
                throw new IllegalStateException("Невірне призначення для робітника " + i + ": " + assignment[i]);
            }
            totalCost += originalCostMatrix[i][assignment[i]];
        }
        return totalCost;
    }
    
    public int[] getAssignment() {
        return assignment.clone();
    }
}
