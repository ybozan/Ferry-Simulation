# Ferry Simulation 🚢🚗🚌🚛

This project simulates a ferry transportation system using multithreading in Java. Vehicles of different types (cars, minibuses, trucks) are transported between two shores, respecting capacity constraints and random arrival logic.

## 🧠 Overview

The simulation models a real-world ferry operation:

- Vehicles are randomly generated and assigned to either the **LEFT** or **RIGHT** shore.
- Each vehicle passes through a toll booth and waits in a queue.
- The ferry has a **fixed capacity** of 20 units:
  - Car = 1 unit
  - Minibus = 2 units
  - Truck = 3 units
- The ferry loads vehicles from its current shore, crosses to the other shore, unloads, and repeats.
- The simulation continues until all vehicles return to their original shore.

## 🔧 Technologies and Concepts

- **Java Multithreading**
- **ExecutorService (Thread Pool)**
- **BlockingQueue** for synchronized vehicle queues
- **Enums** for vehicle type and shore side
- **Randomization** for vehicle type and starting side
- **Thread-safe design** to avoid race conditions

## 📁 Project Structure

FerrySimulation.java # Main class containing all logic
report.pdf # Project report with detailed explanation
README.md # You're reading it!

## ▶️ How to Run

1. Make sure you have Java installed (JDK 11+ recommended).
  
2. Compile the Java file:

   ```bash
   javac FerrySimulation.java
   ```
   
3.Run the program:
  
  ```bash
    java FerrySimulation
  ```

## 📄 Report
For a detailed explanation of the simulation structure, components, and sample output, see report.pdf.

## 🧑‍💻 Author

**Yunus Bozan**  
Computer Engineering Student  
Manisa Celal Bayar University

## 📌 License

This project is open source and free to use under the MIT License.
