# Sudoku solver

In this repository there are basically three algorithms and some hybrid implementations. They are:

* Backtracking
* Graph Coloring
* Dancing Links

## Test machine configuration

Motherboard - MSI B550-A PRO

CPU - AMD Ryzen 5600G (3,9Ghz)

RAM - 32Go (2x16Go) DDR4 3200MT

## Performance test method

The code execution speed is measured only when executing the method that searches for the sudoku answer through the System.currentTimeMillis function.
There are around 30 boards of each size and the average number of runs is taken into account

## Backtracking

Is a simple algorithm that checks all possibilities for a given table. However, before choosing a possibility, it checks whether it is possible to insert that number in the desired position. If possible, it makes this choice, and in the future, if the choice proves to be wrong, the algorithm can choose the next number until it manages to find a solution or goes through all the probabilities and therefore proves that the sudoku is unsolvable.

Performance test

    Average

| Sudoku Size |  Time (S)  |
|-------------|------------|
|     4X4     |  0.000025  |
|     9X9     |  0.002114  |
|    16x16    |  0.203513  |
|    25x25    |  21.611405 |