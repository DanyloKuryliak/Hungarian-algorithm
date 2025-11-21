# Задача призначень мінімальної вартості

Реалізація угорського алгоритму (Kuhn-Munkres) для розв'язання задачі призначень мінімальної вартості.

## Структура проекту

### Основні класи:
- `HungarianAlgorithm.java` - реалізація угорського алгоритму
- `BruteForceSolver.java` - brute-force метод для верифікації
- `LibraryComparison.java` - порівняння з Apache Commons Math

### Демонстрація:
- `Example.java` - приклади використання
- `PerformanceMeasurement.java` - порівняння продуктивності

### Тести:
- `HungarianAlgorithmTest.java` - тести угорського алгоритму
- `BruteForceSolverTest.java` - тести brute-force методу

## Запуск

### Приклади використання:
```bash
mvn exec:java -Dexec.mainClass="com.example.Example"
```

### Порівняння продуктивності:
```bash
mvn exec:java -Dexec.mainClass="com.example.PerformanceMeasurement"
```

### Тести:
```bash
mvn test
```