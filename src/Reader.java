import java.io.*;
import java.util.*;

public class Reader {
    
    private String linkFilepath;
    private String infoFilepath;
    
    public Reader(String linkFilepath, String infoFilepath) {
        this.linkFilepath = linkFilepath;
        this.infoFilepath = infoFilepath;
    }

    public DirectedGraph loadGraph(int threshold) throws IOException{
        DirectedGraph graph = new DirectedGraph();
        try (BufferedReader reader = new BufferedReader(new FileReader(linkFilepath))) {
            String line;
            reader.readLine(); // İlk satırı  atla (başlıklar)
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String source = parts[0];
                String target = parts[1];
                int weight = Integer.parseInt(parts[2]);
                if(weight >= threshold) {
                    graph.addEdge(source, target, weight);
                }
            }
        }
        return graph;
    }

    public List<String[]> loadProteinInfo() throws IOException {
        List<String[]> proteinInfoList = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(infoFilepath))) {
            String line;
            reader.readLine(); // İlk satırı atla (başlıklar)
            while((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                proteinInfoList.add(parts);
            }
        }
        return proteinInfoList;
    } 
}// end reader