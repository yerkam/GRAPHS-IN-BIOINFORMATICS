Protein Interaction Network Analyzer
Project Overview
The Protein Interaction Network Analyzer is a Java-based command-line tool designed to model, visualize (text-based), and analyze Protein-Protein Interaction (PPI) networks. By parsing large biological datasets, the application constructs a directed graph where proteins serve as vertices and their interactions as weighted edges. The tool allows researchers to filter interactions based on confidence thresholds and calculate complex topological metrics.

Features
1. Graph Construction & Filtering
Threshold-Based Loading: Filters raw interaction data based on a user-defined confidence score (weight), ensuring only significant interactions are analyzed.

Directed Graph Model: efficient representation of protein relationships using adjacency lists.

2. Network Analysis & Metrics
Basic Metrics: Calculates Vertex Count, Edge Count, and Average Degree.

Network Diameter: Computes the "longest shortest path" in the network to determine its breadth.

Reciprocity: Measures the likelihood of mutual interactions between proteins (bi-directional edges).

3. Pathfinding & Traversal
Most Confident Path: Implements a specialized algorithm to find a path between two proteins that prioritizes higher accumulated interaction weights (confidence) over simple distance.

Graph Traversal: Supports both Breadth-First Search (BFS) and Depth-First Search (DFS) starting from a specific origin protein.

4. Search & Data Retrieval
Protein Lookup: Retrieves detailed information (ID, Name, Size, Annotation) for specific proteins.

Interaction Check: Verifies if a direct edge exists between two specific proteins.

5. Performance Testing
Automated Benchmarking: Includes a built-in performance test suite that measures load times and graph sizes across varying threshold levels (500, 700, 900, 950, 980, 999).

Project Structure
The project consists of the following Java classes:

Main.java: The entry point of the application. Handles the CLI menu, user inputs, and performance testing orchestration.

DirectedGraph.java: The core data structure managing vertices and edges. Contains logic for pathfinding, metrics calculation (diameter, reciprocity), and traversals.

Vertex.java: Represents a node (protein) in the graph. Stores state information such as visited status, cost, and parent pointers for algorithms.

Edge.java: Represents a weighted connection between two vertices.

Reader.java: Handles File I/O operations. Parses the raw text files for protein links and information.

Getting Started
Prerequisites
Java Development Kit (JDK) 8 or higher.

Source data files (STRING DB format).

Installation & Compilation
Ensure all .java files are in the same directory.

Compile the source code:

Bash

javac *.java
Usage
Run the application:

Bash

java Main
Performance Test: Upon launch, you will be asked if you want to run the performance test. Enter y to run benchmarks or n to proceed to the main tool.

Threshold: Enter a minimum weight threshold (e.g., 700) to filter weak interactions.

Main Menu: Use the numbered menu (1-7) to navigate through features.

Input File Requirements
The application requires two specific text files in the root directory:

Link File (9606.protein.links.v12.0.txt): Space-separated file containing SourceID TargetID Weight.

Info File (9606.protein.info.v12.0.txt): Tab-separated file containing ProteinID Name Size Annotation.

Note: File names are hardcoded in Main.java and Reader.java. Ensure your files match these names or update the code accordingly.

Algorithm Details
Diameter Calculation: Iterates through vertices using BFS to find maximum shortest paths. Optimized to handle unweighted distance for topological size.

Cheapest/Confident Path: A variation of shortest-path algorithms that maximizes total edge weight to find the biologically most probable interaction chain.
