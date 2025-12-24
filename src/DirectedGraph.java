import java.util.*;

public class DirectedGraph {
	private HashMap<String, Vertex> vertices;
	private int edgeCount;

	public DirectedGraph() {
		this.vertices = new HashMap<>();
	}

	public void addEdge(String source, String destination, int weight) { // Yönlü bir edge ekle

		Vertex source_v = vertices.get(source);
		Vertex destination_v = vertices.get(destination);

		if (source_v != null && destination_v != null && source_v.hasEdge(destination)) {
			System.out.println("This edge has already added!");
		}
		else
		{
			if (vertices.get(source) == null) {
				source_v = new Vertex(source);
				vertices.put(source, source_v);
			}

			if (vertices.get(destination) == null) {
				destination_v = new Vertex(destination);
				vertices.put(destination, destination_v);
			}

			Edge edge = new Edge(source_v, destination_v, weight);
			source_v.addEdge(edge);
		} 
		edgeCount++;
	}

	public void print() { // Grafın tüm vertexlerini ve komşularını yazdır

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

	public Iterable<Vertex> vertices() { // Grafın tüm vertexlerini döndür
		return vertices.values();
	}

	public int size() { // Grafın vertex sayısını döndür
		return vertices.size();
	}

	private void resetVertices() { // Tüm vertexleri ziyaret edilmemiş olarak işaretle
		for (Vertex v : vertices.values()) {
			v.unvisit();
			v.setCost(0);
			v.setParent(null);
		}
	}

	public void clear() { // Grafı temizle
		vertices.clear();
		edgeCount = 0;
	}

	public Queue<String> getBreadthFirstTraversal(String origin) // BFS ile gezinti
	{
		resetVertices();
		Queue<String> traversalOrder = new LinkedList<>();
		Queue<Vertex> vertexQueue = new LinkedList<>();

		Vertex originVertex = vertices.get(origin);
		originVertex.visit();

		traversalOrder.add(origin);
		vertexQueue.add(originVertex);

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
				}
			}
		}

		return traversalOrder;
	}

	public Queue<String> getDepthFirstTraversal(String origin) // DFS ile gezinti
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
	}

	public boolean containsVertex(String vertexName) { // Vertex'in graf içinde olup olmadığını kontrol et
		return vertices.containsKey(vertexName);
	}


	public Stack<String> getCheapestPath(String origin, String end) {
		resetVertices();
		
		Vertex originVertex = vertices.get(origin);
		Vertex endVertex = vertices.get(end);
		
		if (originVertex == null || endVertex == null) {
			return new Stack<>();
		}
		
		// BFS ile en kısa yolu bul, ama aynı uzunluktaki yollar arasından
		// en yüksek toplam ağırlığa sahip olanı seç
		
		// Her vertex için: [distance, totalWeight, predecessor]
		HashMap<Vertex, PathInfo> pathInfoMap = new HashMap<>();
		Queue<Vertex> queue = new LinkedList<>();
		
		// Origin için başlangıç değerleri
		pathInfoMap.put(originVertex, new PathInfo(0, 0, null));
		queue.add(originVertex);
		originVertex.visit();
		
		while (!queue.isEmpty()) {
			Vertex current = queue.remove();
			PathInfo currentInfo = pathInfoMap.get(current);
			
			Iterator<Vertex> neighbors = current.getNeighborIterator();
			while (neighbors.hasNext()) {
				Vertex neighbor = neighbors.next();
				int edgeWeight = getActualEdgeWeight(current, neighbor);
				
				int newDistance = currentInfo.distance + 1;
				int newTotalWeight = currentInfo.totalWeight + edgeWeight;
				
				PathInfo neighborInfo = pathInfoMap.get(neighbor);
				
				if (neighborInfo == null) {
					// İlk kez ulaşıyoruz
					pathInfoMap.put(neighbor, new PathInfo(newDistance, newTotalWeight, current));
					if (!neighbor.isVisited()) {
						neighbor.visit();
						queue.add(neighbor);
					}
				} else {
					// Daha önce ulaşılmış
					// Eğer daha kısa yol bulduysak veya aynı mesafede ama daha yüksek ağırlıkla ulaşıyorsak güncelle
					if (newDistance < neighborInfo.distance) {
						// Daha kısa yol bulduk
						neighborInfo.distance = newDistance;
						neighborInfo.totalWeight = newTotalWeight;
						neighborInfo.predecessor = current;
						queue.add(neighbor);
					} else if (newDistance == neighborInfo.distance && newTotalWeight > neighborInfo.totalWeight) {
						// Aynı uzunlukta ama daha yüksek ağırlıklı yol
						neighborInfo.totalWeight = newTotalWeight;
						neighborInfo.predecessor = current;
					}
				}
			}
		}
		
		// Yolu geri inşa et
		Stack<String> path = new Stack<>();
		PathInfo endInfo = pathInfoMap.get(endVertex);
		
		if (endInfo != null && endInfo.predecessor != null) {
			Vertex current = endVertex;
			while (current != null) {
				path.push(current.getName());
				PathInfo info = pathInfoMap.get(current);
				current = info.predecessor;
			}
		} else if (originVertex.equals(endVertex)) {
			path.push(originVertex.getName());
		}
		
		return path;
	}

	// Edge'in gerçek ağırlığını döndür (1000-weight değil!)
	private int getActualEdgeWeight(Vertex source, Vertex destination) {
		for (Edge edge : source.getEdges()) {
			if (edge.getDestination().equals(destination)) {
				return edge.getWeight();
			}
		}
		return 0;
	}

	// PathInfo sınıfı - her vertex için yol bilgisini tutar
	private class PathInfo {
		int distance;        // Kaç edge uzaklıkta
		int totalWeight;     // Toplam edge ağırlığı
		Vertex predecessor;  // Önceki vertex
		
		public PathInfo(int distance, int totalWeight, Vertex predecessor) {
			this.distance = distance;
			this.totalWeight = totalWeight;
			this.predecessor = predecessor;
		}
	}

	// ============ GRAPH METRICS ============

	// 1. Edge Count
	public int getEdgeCount() {
		return edgeCount;
	}

	// 2. Average Degree (out-degree için)
	public double getAverageDegree() {
		if (vertices.isEmpty()) {
			return 0.0;
		}
		return (double) getEdgeCount() / vertices.size();
	}

	// 3. Diameter - Grafın çapı (en uzun en kısa yol)
	public int getDiameter() {
		int diameter = 0;
		
		// Her vertex için BFS yaparak en uzun en kısa yolu bul
		for (Vertex source : vertices.values()) {
			resetVertices();
			int maxDistance = getMaxDistanceFrom(source);
			if (maxDistance > diameter && maxDistance != Integer.MAX_VALUE) {
				diameter = maxDistance;
			}
		}
		
		return diameter;
	}

	// Bir vertex'ten diğer tüm vertex'lere en uzak mesafeyi bulur
	private int getMaxDistanceFrom(Vertex source) {
		Queue<Vertex> queue = new LinkedList<>();
		source.visit();
		source.setCost(0);
		queue.add(source);
		
		int maxDistance = 0;
		
		while (!queue.isEmpty()) {
			Vertex current = queue.remove();
			Iterator<Vertex> neighbors = current.getNeighborIterator();
			
			while (neighbors.hasNext()) {
				Vertex neighbor = neighbors.next();
				if (!neighbor.isVisited()) {
					neighbor.visit();
					neighbor.setCost(current.getCost() + 1);
					queue.add(neighbor);
					
					if (neighbor.getCost() > maxDistance) {
						maxDistance = (int) neighbor.getCost();
					}
				}
			}
		}
		
		return maxDistance;
	}

	// 4. Reciprocity - Karşılıklı bağlantı oranı
	public double getReciprocity() {
		int reciprocalEdges = 0;
		int totalEdges = 0;
		
		// Her edge için karşılığının olup olmadığını kontrol et
		for (Vertex v : vertices.values()) {
			for (Edge edge : v.getEdges()) {
				totalEdges++;
				Vertex destination = edge.getDestination();
				
				// Destination'dan source'a geri bir edge var mı kontrol et
				if (destination.hasEdge(v.getName())) {
					reciprocalEdges++;
				}
			}
		}
		
		if (totalEdges == 0) {
			return 0.0;
		}
		
		// Her reciprocal çift iki kez sayıldığı için toplam reciprocal edges / total edges
		return (double) reciprocalEdges / totalEdges;
	}
}