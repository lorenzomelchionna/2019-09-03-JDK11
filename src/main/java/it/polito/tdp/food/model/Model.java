package it.polito.tdp.food.model;

import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	
	private SimpleWeightedGraph<String,DefaultWeightedEdge> grafo;
	
	private List<String> typePortions;
	
	public Model() {
		
		dao = new FoodDao();
		
	}
	
	public void creaGrafo(double maxCalorie) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		typePortions = dao.getTypePortions(maxCalorie);
		
		Graphs.addAllVertices(grafo,typePortions);
		
		for(Adiacenza a : dao.getAdiacenze()) {
			
			if(typePortions.contains(a.getT1()) && typePortions.contains(a.getT2())) {
				Graphs.addEdgeWithVertices(grafo, a.getT1(), a.getT2(), a.getPeso());
			}
			
		}
		
	}
	
	public String grafoCreato() {
		
		return "#Vertici: "+grafo.vertexSet().size()+"\n#Archi: "+grafo.edgeSet().size()+"\n";
		
	}
	
}
