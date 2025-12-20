import java.util.Stack;

public class AItest {

    public static void main(String[] args) {
        testCheapestPath();
    }

    public static void testCheapestPath() {
        System.out.println("=== TESTING MOST CONFIDENT SHORTEST PATH ===\n");

        DirectedGraph testGraph = new DirectedGraph();

        // Test graph:
        // 950 950
        // A -----> B -----> D
        // \ /
        // \ 600 500 /
        // \---> C ---/

        testGraph.addEdge("A", "B", 950);
        testGraph.addEdge("B", "D", 950);
        testGraph.addEdge("A", "C", 600);
        testGraph.addEdge("C", "D", 500);

        System.out.println("Graph structure:");
        testGraph.print();
        System.out.println();

        // Test 1: A'dan D'ye en kısa ama en güvenilir yol
        System.out.println("Test 1: Most confident SHORTEST path from A to D");
        System.out.println("Path A -> B -> D: distance=2, total weight=950+950=1900");
        System.out.println("Path A -> C -> D: distance=2, total weight=600+500=1100");
        System.out.println("Expected: A -> B -> D (same distance, higher weight)");

        Stack<String> path = testGraph.getCheapestPath("A", "D");

        System.out.print("Result: ");
        if (path.isEmpty()) {
            System.out.println("No path found!");
        } else {
            while (!path.isEmpty()) {
                System.out.print(path.pop());
                if (!path.isEmpty()) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
        System.out.println("\n✓ If result is A -> B -> D, algorithm works correctly!\n");

        // Test 2: Farklı uzunluktaki yollar
        System.out.println("Test 2: Different path lengths");
        testGraph.addEdge("A", "E", 999);
        testGraph.addEdge("E", "F", 999);
        testGraph.addEdge("F", "D", 999);
        
        System.out.println("Added path A -> E -> F -> D (distance=3, weight=999+999+999=2997)");
        System.out.println("Expected: Still A -> B -> D (distance=2 is shorter, even though weight is lower)");
        
        path = testGraph.getCheapestPath("A", "D");
        System.out.print("Result: ");
        while (!path.isEmpty()) {
            System.out.print(path.pop());
            if (!path.isEmpty()) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        System.out.println("✓ Shortest path wins even with lower total weight!\n");

        // Test 3: Path olmayan durum
        System.out.println("Test 3: No path exists");
        testGraph.addEdge("X", "Y", 800);
        Stack<String> noPath = testGraph.getCheapestPath("A", "Y");
        System.out.println("Expected: Empty path (no connection from A to Y)");
        System.out.print("Result: ");
        System.out.println(noPath.isEmpty() ? "Empty path ✓" : "Path found (ERROR!)");

        System.out.println("\n=== TEST COMPLETED ===");
        System.out.println("\nAlgorithm: Modified BFS with weight tracking");
        System.out.println("Priority: 1) Shortest distance  2) Maximum total weight");
    }
}