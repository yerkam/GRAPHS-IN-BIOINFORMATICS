import java.util.Stack;

public class AItest {

    public static void main(String[] args) {
        testCheapestPath();
    }

    public static void testCheapestPath() {
        System.out.println("=== TESTING CHEAPEST PATH ===\n");

        DirectedGraph testGraph = new DirectedGraph();

        // Basit test graph'ı oluştur:
        // 950 950
        // A -----> B -----> D
        // \ /
        // \ 600 500 /
        // \---> C ---/

        testGraph.addEdge("A", "B", 950); // Yüksek confidence
        testGraph.addEdge("B", "D", 950); // Yüksek confidence
        testGraph.addEdge("A", "C", 600); // Orta confidence
        testGraph.addEdge("C", "D", 500); // Orta confidence

        System.out.println("Graph structure:");
        testGraph.print();
        System.out.println();

        // Test 1: A'dan D'ye en güvenilir yol
        System.out.println("Test 1: Most confident path from A to D");
        System.out.println("Expected: A -> B -> D (total confidence: 950+950=1900)");
        System.out.println("Alternative: A -> C -> D (total confidence: 600+500=1100)");

        Stack<String> path = testGraph.getCheapestPath("A", "D");

        System.out.print("Result: ");
        if (path.isEmpty()) {
            System.out.println("No path found!");
        } else {
            // Direkt path'ten yazdır, tempStack'e gerek yok!
            while (!path.isEmpty()) {
                System.out.print(path.pop());
                if (!path.isEmpty()) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
        System.out.println("\n✓ If result is A -> B -> D, algorithm works correctly!");
        System.out.println("  (Because 1000-950 + 1000-950 = 100 < 1000-600 + 1000-500 = 900)\n");

        // Test 2: Path olmayan durum
        System.out.println("Test 2: No path exists");
        testGraph.addEdge("E", "F", 800);
        Stack<String> noPath = testGraph.getCheapestPath("A", "F");
        System.out.println("Expected: Empty path (no connection from A to F)");
        System.out.print("Result: ");
        System.out.println(noPath.isEmpty() ? "Empty path ✓" : "Path found (ERROR!)");

        System.out.println("\n=== TEST COMPLETED ===\n");
    }

}
