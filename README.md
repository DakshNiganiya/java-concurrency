# Producer-Consumer in Java (Concurrency Study)

## Description
This project explores different implementations of the Producer-Consumer problem in Java, focusing on thread safety, synchronization, and performance.

## Versions

### v1 – Not Thread Safe
Initial implementation demonstrating race conditions and incorrect behavior.

### v2 – Over-Synchronized
Used synchronized everywhere, leading to performance issues.

### v3 – Manual Synchronization
Improved synchronization at critical sections.

### v4 – Correct Implementation
Used wait() and notify() to properly coordinate threads.

### v5 – ExecutorService
Used thread pools for improved thread management.

### v6 – Concurrent Collections
Used thread-safe collections to simplify synchronization.

## Notes
Developed as part of a university assignment.
