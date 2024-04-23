# Backtracking

The backtracking algorithm is a classic recursive brute force algorithm that tests all possibilities, but instead of going to the end of each one it is able to evaluate whether an answer may not be satisfactory and therefore abandons that possibility as soon as it realizes that it is not. satisfactory.

In the case of sudoku, it starts by checking whether the square to be tested is empty, if not, it moves on to the next one until it finds one that is empty.
When it finds a empty one, it test all possible numbers for that board, in base 2 they would be from 1 to 4, in base 3 from 1 to 9 and so on.
But before choosing a number it checks if that number already exists in the row, column or zone, if so it abandons the possibility, if not it chooses the number and moves on to the next empty square, if this first combination does not reach a result it goes back and tests the next numbers until it finds a valid result.

## Pseudo Algotihm

```
checkRow(int row, int value)
    for all squares in the row
        if value in the row
            return false
    return true
```

```
checkColumn(int column, int value)
    for all squares in the column
        if value in the column
            return false
    return true
```

```
checkZone(int row, int column, int value)
    for all squares in the zone
        if value in the zone
            return false
    return true
```

```
isAvailable(int row, int column, int value)
    return checkRow && checkColumn && checkZone
```

```
backtracking(int row, int column)
    while row less then base*base
        while column less then base*base
            if board[row][column] equals empty
                for int value from 1 to base*base + 1 
                    if(isAvailable(row, column, value))
                        board[row][column] <- value
                        if(backtraking(row, column+1))
                            return true
                        else
                            board[row][column] <- empty
                return false
            column <- column + 1
        column <- 0
        row <- row + 1
    return true

```

## Algorithm Complexity

1. Best case

In the best case, where all squares are filled, the complexity is O(n²) where n is equal to base², as the algorithm will traverse the entire board. 

2. Worst case

In the worst case, the algorithm will traverse the entire recursion tree, knowing that it will enter the recursion only when the square does not have a number assigned, so we can deduce that it will enter the recursion n² - m times, where n² is the number of iterations and m is the number of filled squares, thus we arrive at the complexity n² + n^(n²-m).

