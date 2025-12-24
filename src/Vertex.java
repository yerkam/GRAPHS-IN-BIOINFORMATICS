import java.util.*;

public class Vertex {
	private String name; // Protein adı
	private ArrayList<Edge> edges; // Komşu kenarlar
	private Vertex parent; // Ebeveyn düğüm
	private boolean visited;  // Ziyaret durumu
	private double cost;  // Maliyet (Dijkstra için)

	public Vertex(String name) {
		this.name = name;
		edges = new ArrayList<Edge>();
		parent = null;
		visited = false;
	}

	public void addEdge(Edge e) {
		edges.add(e);
	}

	public ArrayList<Edge> getEdges() {
		return this.edges;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vertex getParent() {
		return parent;
	}

	public void setParent(Vertex parent) {
		this.parent = parent;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public void visit() {
		this.visited = true;
	}

	public void unvisit() {
		this.visited = false;
	}

	public boolean isVisited() {
		return this.visited;
	}

	public Vertex getUnvisitedNeighbor() { // Ziyaret edilmemiş komşuyu döndürür
		Vertex result = null;

		Iterator<Vertex> neighbors = getNeighborIterator();
		while (neighbors.hasNext() && (result == null))
		{
			Vertex nextNeighbor = neighbors.next();
			if (!nextNeighbor.isVisited())
				result = nextNeighbor;
		} // end while

			return result;
	}

	public boolean hasEdge(String neighbor) { // Belirtilen komşuya sahip mi
		boolean found = false;
		Iterator<Vertex> neighbors = getNeighborIterator();
		while (neighbors.hasNext())
		{
			Vertex nextNeighbor = neighbors.next();
			if (nextNeighbor.getName().equalsIgnoreCase(neighbor))
			{
				found = true;
				break;
			}
		} // end while

		return found;
	}

	public Iterator<Vertex> getNeighborIterator() // Komşu Iteratörü döndürür
	{
		return new NeighborIterator();
	} // end getNeighborIterator

	// İç sınıf: Komşu Iteratörü
	private class NeighborIterator implements Iterator<Vertex> 
	{
		int edgeIndex = 0;  
		private NeighborIterator()
		{
			edgeIndex = 0; 
		} // end default constructor

		public boolean hasNext()
		{
			return edgeIndex < edges.size();
		} // end hasNext

		public Vertex next()
		{
			Vertex nextNeighbor = null;

			if (hasNext())
			{
				nextNeighbor = edges.get(edgeIndex).getDestination();
				edgeIndex++;
			}
			else
				throw new NoSuchElementException();

			return nextNeighbor;
		} // end next

		public void remove()
		{
			throw new UnsupportedOperationException();
		} // end remove
	} // end NeighborIterator
}
