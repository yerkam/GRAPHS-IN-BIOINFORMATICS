import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        printPerformanceTest();

        System.out.println("Please enter a threshold value: ");
        String input = System.console().readLine();
        while(input.isEmpty() || !input.matches("\\d+")) { // Geçersiz threshold kontrolü
            System.out.println("Invalid input. Please enter a valid integer threshold value: ");
            input = System.console().readLine();
        }
        int threshold = Integer.parseInt(input);
        System.out.println();

        DirectedGraph graph = null;
        List<String[]> proteinInfoList = null;
        int option = -1;        while(option != 7) {
            System.out.println("1 - Load graph from files");
            System.out.println("2 - Search for protein by protein ID");
            System.out.println("3 - Check if two proteins have an interaction");
            System.out.println("4 - Find the most confident path between two proteins");
            System.out.println("5 - Calculate the basic graph metrics (VertexCount, EdgeCount, Average Degree, Diameter, Reciprocity)");
            System.out.println("6 - Breadth-First and Depth-First Traverse by specifying the origin protein");
            System.out.println("7 - Exit");
            System.out.println("Please select an option: ");
            input = System.console().readLine();
            while(input.isEmpty() || !input.matches("\\d+")) { // Geçersiz seçenek kontrolü
                System.out.println("Invalid input. Please enter a valid integer option value: ");
                input = System.console().readLine();
            }
            option = Integer.parseInt(input);
            if(graph == null && option != 1 && option != 7 && option < 8 && 0 < option) { // Graph yüklenmemişse ve seçenek 1 değilse
                System.out.println("Please load the graph first by selecting option 1.");
                
            }else{ // Graph yüklendiyse veya seçenek 1 ise
                switch(option) {
                    case 1:
                        Reader reader = new Reader("9606.protein.links.v12.0.txt", "9606.protein.info.v12.0.txt");
                        graph = reader.loadGraph(threshold);
                        proteinInfoList = reader.loadProteinInfo();
                        System.out.println("Graph loaded.");
                        break;
                    case 2:
                        System.out.println("Enter protein ID to search: ");
                        String proteinId = System.console().readLine();
                        boolean found = false;
                        for(String[] info : proteinInfoList) {
                            if(info[0].equals(proteinId)) {
                                found = true;
                                System.out.println("Protein ID: " + info[0]);
                                Thread.sleep(500);
                                System.out.println("Protein Name: " + info[1]);
                                Thread.sleep(500);
                                System.out.println("Protein Size: " + info[2]);
                                Thread.sleep(500);
                                System.out.println("Protein Annotation: " + info[3]);
                                Thread.sleep(500);

                                break;
                            }
                        }
                        if(!found)
                            System.out.println("Protein with ID " + proteinId + " not found.");

                        break;
                    case 3:
                        System.out.println("Enter source protein ID: ");
                        String sourceId = System.console().readLine();
                        System.out.println("Enter target protein ID: ");
                        String targetId = System.console().readLine();
                        Vertex sourceVertex = null;
                        Vertex targetVertex = null;
                        for (Vertex v : graph.vertices()) {
                            if (v.getName().equals(sourceId)) {
                                sourceVertex = v;
                            }
                            if (v.getName().equals(targetId)) {
                                targetVertex = v;
                            }
                        }
                        if (sourceVertex != null && targetVertex != null && sourceVertex.hasEdge(targetId)) {
                            System.out.println("There is an interaction between " + sourceId + " and " + targetId + ".");
                        } else {
                            System.out.println("No interaction found between " + sourceId + " and " + targetId + ".");
                        }
                        break;
                    case 4:
                        System.out.println("Feature not implemented yet.");
                        break;
                    case 5:
                        System.out.println("Feature not implemented yet.");
                        break;
                    case 6:
                        System.out.println("Please specify the origin protein ID for traversal: ");
                        input = System.console().readLine();
                        if(!graph.containsVertex(input)) {
                            System.out.println("Protein ID not found in the graph.");
                            break;
                        }
                        String inputBFSorDFS = null;
                        System.out.println("Please choose traversal method (BFS/DFS): ");
                        inputBFSorDFS = System.console().readLine();
                        inputBFSorDFS = inputBFSorDFS.toLowerCase();
                        while(!inputBFSorDFS.equals("bfs") && !inputBFSorDFS.equals("dfs")) {
                            System.out.println("Invalid input. Please enter BFS or DFS: ");
                            inputBFSorDFS = System.console().readLine();
                            inputBFSorDFS = inputBFSorDFS.toLowerCase();
                        }
                        if(inputBFSorDFS.equals("bfs")) {
                            Queue<String> bfsResult = graph.getBreadthFirstTraversal(input);
                            System.out.println("Breadth-First Traversal Order: " + bfsResult);
                        } else {
                            Queue<String> dfsResult = graph.getDepthFirstTraversal(input);
                            System.out.println("Depth-First Traversal Order: " + dfsResult);
                        }
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please enter a number between 1 and 7.");
                }
            }
            Thread.sleep(1000); // 1 saniye bekle
            System.out.println("-----------------------------------------------------------------------------------------------------");
        }
    }// end main

    public static void printPerformanceTest() throws IOException {
        int threshold = 500;
        DirectedGraph graph = null;
        Reader reader = new Reader("9606.protein.links.v12.0.txt", "9606.protein.info.v12.0.txt");
        System.out.println("Threshold , Vertex Count , Edge Count , Load Time (ms)");
        
        long startTime = System.nanoTime();
        graph = reader.loadGraph(threshold);
        long endTime = System.nanoTime();
        long loadTime = (endTime - startTime) / 1000000; 
        System.out.println(threshold + " , " + graph.size() + " , " + graph.getEdgeCount() + " , " + loadTime);
        graph.clear();

        threshold = 700;
        startTime = System.nanoTime();
        graph = reader.loadGraph(threshold);
        endTime = System.nanoTime();
        loadTime = (endTime - startTime) / 1000000; 
        System.out.println(threshold + " , " + graph.size() + " , " + graph.getEdgeCount() + " , " + loadTime);
        graph.clear();

        threshold = 900;
        startTime = System.nanoTime();
        graph = reader.loadGraph(threshold);
        endTime = System.nanoTime();
        loadTime = (endTime - startTime) / 1000000; 
        System.out.println(threshold + " , " + graph.size() + " , " + graph.getEdgeCount() + " , " + loadTime);
        graph.clear();
        
        threshold = 950;
        startTime = System.nanoTime();
        graph = reader.loadGraph(threshold);
        endTime = System.nanoTime();
        loadTime = (endTime - startTime) / 1000000; 
        System.out.println(threshold + " , " + graph.size() + " , " + graph.getEdgeCount() + " , " + loadTime);
        graph.clear();

        threshold = 980;
        startTime = System.nanoTime();
        graph = reader.loadGraph(threshold);
        endTime = System.nanoTime();
        loadTime = (endTime - startTime) / 1000000; 
        System.out.println(threshold + " , " + graph.size() + " , " + graph.getEdgeCount() + " , " + loadTime);
        graph.clear();

        threshold = 999;
        startTime = System.nanoTime();
        graph = reader.loadGraph(threshold);
        endTime = System.nanoTime();
        loadTime = (endTime - startTime) / 1000000; 
        System.out.println(threshold + " , " + graph.size() + " , " + graph.getEdgeCount() + " , " + loadTime);
        graph.clear();
    }
    
}
