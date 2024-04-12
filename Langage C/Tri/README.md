# Sorting Algorithms

There are several sorting algorithms. In this small study, I chose the 5 most famous.
It is important to note that the permutation of elements occurs through mathematical calculation and not through the allocation of an auxiliary variable.

The permutation calculation:

```
    A = A + B;
    B = A - B;
    A = A - B;
```

Exemple:

```
    A = 13, B = 7

    A = A + B => A = 20;
    B = A - B => B = 13;
    A = A - B => A = 7;

    Final result:
    A = 7, B = 13
```

## Test machine configuration

Motherboard - MSI B550-A PRO
CPU - AMD Ryzen 5600G (3,9Ghz)
RAM - 32Go (2x16Go) DDR4 3200MT

## Performance test method

The speed of the algorithms is measured within the code, that is, the construction of the arrays or other technical details are not measured, only the speed of the algorithm. To do so, it use the clock() function and then divide by the CLOCKS_PER_SEC constant.

## Bubble sort

Bubble sort works on the repeatedly swapping of adjacent elements until they are not in the intended order.
This algorithm is quite simple and not very performant.

Performance test

    Worst Case

| Number of Elements |  Time (S)  |
|--------------------|------------|
|       100          |  0.000025  |
|     1,000          |  0.002114  |
|    10,000          |  0.203513  |
|   100,000          |  21.611405 |
|   500,000          | 539.067030 |
| 1,000,000          |2129.223877 |

    Best Case

| Number of Elements |  Time (S)  |
|--------------------|------------|
|       100          |  0.000008  |
|     1,000          |  0.000563  |
|    10,000          |  0.056235  |
|   100,000          |  5.570250  |
|   500,000          | 138.762758 |
|  1,000,000         | 2129.223877|

## Insertion Sort

Insertion sort works similarly as we sort cards in our hand in a card game.
We assume that the first card is already sorted then, we select an unsorted card. If the unsorted card is greater than the card in hand, it is placed on the right otherwise, to the left. In the same way, other unsorted cards are taken and put in their right place.

    Worst Case

| Number of Elements |  Time (S)  |
|--------------------|------------|
|       100          |  0.000014  |
|     1,000          |  0.001112  |
|    10,000          |  0.094597  |
|   100,000          |  9.584576  |
|   500,000          | 254.510650 |
| 1,000,000          |1017.206157 |

    Best Case

| Number of Elements |  Time (S)  |
|--------------------|------------|
|       100          |  0.000001  |
|     1,000          |  0.000003  |
|    10,000          |  0.000023  |
|   100,000          |  0.000218  |
|   500,000          |  0.001171  |
|  1,000,000         |  0.002356  |

## Selection Sort

Selection sort works by repeatedly selecting the smallest element from the unsorted portion of the list and moving it to the sorted portion of the list. 

    Worst Case

| Number of Elements |  Time (S)  |
|--------------------|------------|
|       100          |  0.000011  |
|     1,000          |  0.000832  |
|    10,000          |  0.056473  |
|   100,000          |  5.421092  |
|   500,000          | 136.120536 |
| 1,000,000          | 544.751023 |

    Best Case

| Number of Elements |  Time (S)  |
|--------------------|------------|
|       100          |  0.000006  |
|     1,000          |  0.000588  |
|    10,000          |  0.048312  |
|   100,000          |  4.694348  |
|   500,000          | 117.513314 |
|  1,000,000         | 474.655823 |

## Merge Sort

Merge sort is one of the most efficient sorting algorithms. is a sorting algorithm that follows the divide-and-conquer approach. It works by recursively dividing the input array into smaller subarrays, then merges those subarrays into a sorted list.

    Worst Case

| Number of Elements |   Time (S)  |
|--------------------|-------------|
|       100          |  0.000006   |
|     1,000          |  0.000061   |
|    10,000          |  0.000735   |
|   100,000          |  0.008268   |
|   500,000          |  0.038671   |
| 1,000,000          |StackOverFlow|

    Best Case

| Number of Elements |   Time (S)  |
|--------------------|-------------|
|       100          |  0.000009   |
|     1,000          |  0.000060   |
|    10,000          |  0.000782   |
|   100,000          |  0.008702   |
|   500,000          |  0.041066   |
| 1,000,000          |StackOverFlow|

## Quick Sort

Quick sort is a sorting algorithm that follows the divide-and-conquer approach. It chooses one of the values as the 'pivot' element, and moves the other values so that lower values are on the left of the pivot element, and higher values are on the right of it.

    Worst Case

| Number of Elements |   Time (S)  |
|--------------------|-------------|
|       100          |  0.000008   |
|     1,000          |  0.000548   |
|    10,000          |  0.045356   |
|   100,000          |  4.265892   |
|   500,000          |StackOverFlow|
| 1,000,000          |StackOverFlow|

    Best Case

| Number of Elements |   Time (S)  |
|--------------------|-------------|
|       100          |  0.000010   |
|     1,000          |  0.000494   |
|    10,000          |  0.041256   |
|   100,000          |  3.876956   |
|   500,000          |StackOverFlow|
| 1,000,000          |StackOverFlow|