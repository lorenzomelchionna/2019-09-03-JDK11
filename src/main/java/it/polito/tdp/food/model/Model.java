package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	
	private SimpleWeightedGraph<String,DefaultWeightedEdge> grafo;
	
	private List<String> typePortions;
	
	private List<String> percorsoMigliore;
	private int pesoMigliore;
	
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
	
	public SimpleWeightedGraph<String,DefaultWeightedEdge> getGrafo(){
		
		return this.grafo;
		
	}
	
	public String grafoCreato() {
		
		return "#Vertici: "+grafo.vertexSet().size()+"\n#Archi: "+grafo.edgeSet().size()+"\n";
		
	}
	
	public String getConnessi(String type) {
		
		String result = "";
		
		for(String s : Graphs.neighborListOf(grafo, type)) {
			result += s+"\n";
		}
		
		return result;
		
	}
	
	public void findPercorso(String partenza, int lunghezza){
		
		List<String> parziale = new ArrayList<>();
		
		percorsoMigliore = null;
		pesoMigliore = 0;
		
		parziale.add(partenza);
		
		cerca(parziale,0,lunghezza);
		
	}
	
	public List<String> getPercorso() {
		
		return this.percorsoMigliore;
		
	}
	
	public int pesoTotale() {
		
		return this.pesoMigliore;
		
	}

	private void cerca(List<String> parziale, int l, int lunghezza) {
		
		if(l == lunghezza) {
			
			int pesoTot = 0;
			
			for(int i = 0; i < parziale.size()-1; i++) {
				pesoTot += grafo.getEdgeWeight(grafo.getEdge(parziale.get(i), parziale.get(i+1)));
			}
			
			if(pesoTot > this.pesoMigliore) {
				this.percorsoMigliore = new ArrayList<>(parziale);
				this.pesoMigliore =  pesoTot;
			}
			return;
			
		}
		
		for(String s : Graphs.neighborListOf(grafo, parziale.get(l))) {
			
			if(!parziale.contains(s)) {
			
				parziale.add(s);
			
				cerca(parziale, l+1, lunghezza);
			
				parziale.remove(parziale.size()-1);
			
			}
			
		}
		
	}
	
}
