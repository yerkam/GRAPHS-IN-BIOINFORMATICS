import java.util.*;

public class DirectedGraph {
	private HashMap<String, Vertex> vertices;

	public DirectedGraph() {
		this.vertices = new HashMap<>();
	}

	public void addEdge(String source, String destination, int weight) {

		Vertex source_v = vertices.get(source);
		Vertex destination_v = vertices.get(destination);

		if (source_v != null && destination_v != null && source_v.hasEdge(destination)) {
			System.out.println("This edge has already added!");
		}
		else
		{
			if (vertices.get(source) == null) { // Kaynak düğüm yoksa oluştur
				source_v = new Vertex(source);
				vertices.put(source, source_v);
			}

			if (vertices.get(destination) == null) { // Hedef düğüm yoksa oluştur
				destination_v = new Vertex(destination);
				vertices.put(destination, destination_v);
			}

			Edge edge = new Edge(source_v, destination_v, weight);
			source_v.addEdge(edge);
		} 
	}

	public void print() {

		for (Vertex v : vertices.values()) {
			System.out.print(v.getName() + " -> ");
			
			Iterator<Vertex> neighbors = v.getNeighborIterator();
			while (neighbors.hasNext())
			{
				Vertex n = neighbors.next();
				System.out.print(n.getName() + " ");
			}
			System.out.println();
		}
	}

	public Iterable<Vertex> vertices() {
		return vertices.values();
	}

	public int size() {
		return vertices.size();
	}

	private void resetVertices() {
		for (Vertex v : vertices.values()) {
			v.unvisit();
			v.setCost(0);
			v.setParent(null);
		}
	}

	public Queue<String> getBreadthFirstTraversal(String origin)
	{
		resetVertices();
		Queue<String> traversalOrder = new LinkedList<>(); // Queue of vertex labels
		Queue<Vertex> vertexQueue = new LinkedList<>(); // Queue of Vertex objects

		Vertex originVertex = vertices.get(origin);
		originVertex.visit();

		traversalOrder.add(origin);    // Enqueue vertex label
		vertexQueue.add(originVertex); // Enqueue vertex

		while (!vertexQueue.isEmpty())
		{
			Vertex frontVertex = vertexQueue.remove();
			Iterator<Vertex> neighbors = frontVertex.getNeighborIterator();

			while (neighbors.hasNext())
			{
				Vertex nextNeighbor = neighbors.next();
				if (!nextNeighbor.isVisited())
				{
					nextNeighbor.visit();
					traversalOrder.add(nextNeighbor.getName());
					vertexQueue.add(nextNeighbor);
				} // end if
			} // end while
		} // end while

		return traversalOrder;
	} // end getBreadthFirstTraversal

	public Queue<String> getDepthFirstTraversal(String origin)
	{
		resetVertices();
		Queue<String> traversalOrder = new LinkedList<>();
		Stack<Vertex> vertexStack = new Stack<>();

		 Vertex originVertex = vertices.get(origin);
		if(originVertex == null) {
            return traversalOrder;
		}
		originVertex.visit();         
		traversalOrder.add(origin);         
		vertexStack.push(originVertex);          
		while (!vertexStack.isEmpty()){             
			Vertex top = vertexStack.peek();             
			Iterator<Vertex> neighbors = top.getNeighborIterator();             
			boolean pushed = false;              
			while (neighbors.hasNext()){                 
				Vertex nextNeighbor = neighbors.next();                 
				if (!nextNeighbor.isVisited()){                     
					nextNeighbor.visit();                     
					nextNeighbor.setParent(top);                     
					traversalOrder.add(nextNeighbor.getName());                     
					vertexStack.push(nextNeighbor);                     
					pushed = true;                     
					break;
				}             
			}              
			if (!pushed) {                 
				vertexStack.pop();             
			}         
		} 
		
		return traversalOrder;
	} // end getDepthFirstTraversal

	public boolean containsVertex(String vertexName) {
		return vertices.containsKey(vertexName);
	}
		
	public int getVertexCount() {
    	return vertices.size();
    }
	

public Stack<String> getCheapestPath(String origin, String end) {
    
	
	resetVertices();
    boolean done = false;
    
    // Priority queue to store entries with vertex and cost
    PriorityQueue<EntryPQ> priorityQueue = new PriorityQueue<>(
        (a, b) -> Double.compare(a.cost, b.cost)
    );
    
    Vertex originVertex = vertices.get(origin);
    Vertex endVertex = vertices.get(end);
    
    if (originVertex == null || endVertex == null) {
        return new Stack<>(); // Return empty stack if vertices don't exist
    }
    
    // Add origin vertex to priority queue with cost 0
    priorityQueue.add(new EntryPQ(originVertex, 0, null));
    
    while (!done && !priorityQueue.isEmpty()) {
        EntryPQ frontEntry = priorityQueue.remove();
        Vertex frontVertex = frontEntry.vertex;
        
        if (!frontVertex.isVisited()) {
            // Mark as visited
            frontVertex.visit();
            
            // Set the cost and predecessor
            frontVertex.setCost(frontEntry.cost);
            frontVertex.setParent(frontEntry.predecessor);
            
            // Check if we reached the destination
            if (frontVertex.getName().equals(end)) {
                done = true;
            } else {
                // Add neighbors to priority queue
                Iterator<Vertex> neighbors = frontVertex.getNeighborIterator();
                while (neighbors.hasNext()) {
                    Vertex nextNeighbor = neighbors.next();
                    
                    if (!nextNeighbor.isVisited()) {
                        // Get the edge weight
                        double weightOfEdgeToNeighbor = getEdgeWeight(frontVertex, nextNeighbor);
                        double nextCost = frontVertex.getCost() + weightOfEdgeToNeighbor;
                        
                        priorityQueue.add(new EntryPQ(nextNeighbor, nextCost, frontVertex));
                    }
                }
            }
        }
    }
    
    // Build path from end to origin using predecessors(bir başkasına işaret eden anlamında *enes* parrent anlamında kullnaıyoruz burda)
    Stack<String> path = new Stack<>();
    
    if (done) {
        Vertex current = endVertex;
        while (current != null) {
            path.push(current.getName());
            current = current.getParent();
        }
    }
    
    return path;
}

private double getEdgeWeight(Vertex source, Vertex destination) {
    for (Edge edge : source.getEdges()) {
        if (edge.getDestination().equals(destination)) {
            return 1000 - edge.getWeight(); //  most confidentı bulmak için chepast pathta 1000 den çıkarınca küçük değerler most confident olmuş oldu.  
        }
    }
    return Double.MAX_VALUE;
}

// Inner class for priority queue entries
private class EntryPQ {
    Vertex vertex;
    double cost;
    Vertex predecessor;
    
    public EntryPQ(Vertex vertex, double cost, Vertex predecessor) {
        this.vertex = vertex;
        this.cost = cost;
        this.predecessor = predecessor;
    }
}

public int getEdgeCount() {
    int totalEdges = 0;
    for (Vertex v : vertices.values()) {
        totalEdges += v.getEdges().size();
    }
    return totalEdges;
}

// 2. Average Degree (out-degree için)
public double getAverageDegree() {
    if (vertices.isEmpty()) {
        return 0.0;
    }
    return (double) getEdgeCount() / vertices.size();
}
}


