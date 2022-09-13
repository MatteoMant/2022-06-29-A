package it.polito.tdp.itunes.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private ItunesDAO dao;
	private Graph<Album, DefaultWeightedEdge> grafo;
	private Map<Album, Integer> albumCanzoni;
	
	// Variabili per la ricorsione
	private List<Album> best;
	private int bilancioVerticePartenza;
	
	public Model() {
		dao = new ItunesDAO();
		albumCanzoni = new HashMap<>();
	}
	
	public void creaGrafo(int n) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, dao.getAllAlbumsWithCanzoni(n));
		
		for (Album a : this.grafo.vertexSet()) {
			int numCanzoni = dao.calcolaNumCanzoni(a);
			albumCanzoni.put(a, numCanzoni);
		}
		
		// Aggiunta degli archi
		for (Album a1 : this.grafo.vertexSet()) {
			for (Album a2 : this.grafo.vertexSet()) {
				if (!a1.equals(a2)) {
					if (albumCanzoni.get(a1) > albumCanzoni.get(a2)) {
						Graphs.addEdge(this.grafo, a2, a1, albumCanzoni.get(a1) - albumCanzoni.get(a2));
					} else if (albumCanzoni.get(a1) < albumCanzoni.get(a2)) {
						Graphs.addEdge(this.grafo, a1, a2, albumCanzoni.get(a2) - albumCanzoni.get(a1));
					} else if (albumCanzoni.get(a1) == albumCanzoni.get(a2)) {
						// non faccio niente
					}
				}
			}
		}
			
	}
	
	public List<AlbumAndBilancio> getAllSuccessori(Album a1){
		List<AlbumAndBilancio> result = new LinkedList<>();
		
		for (Album a : Graphs.successorListOf(this.grafo, a1)) {
			int bilancio = calcolaBilancio(a);
			result.add(new AlbumAndBilancio(a, bilancio));
		}
		
		if (result != null)
			Collections.sort(result);
		return result;
	}
	
	public int calcolaBilancio(Album a) {
		int bilancio = 0;
		
		int sommaEntranti = 0;
		for (DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(a)) {
			sommaEntranti += this.grafo.getEdgeWeight(edge);
		}
		
		int sommaUscenti = 0;
		for (DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(a)) {
			sommaUscenti += this.grafo.getEdgeWeight(edge);
		}
		
		bilancio = sommaEntranti - sommaUscenti;
		return bilancio;
	}
	
	public Album calcolaBilancioMassimo() {
		int massimo = Integer.MIN_VALUE;
		Album albumMassimo = null;
		for (Album a : this.grafo.vertexSet()) {
			if (calcolaBilancio(a) > massimo) {
				massimo = calcolaBilancio(a);
				albumMassimo = a;
			}
		}
		return albumMassimo;
	}
	
	public List<Album> calcolaPercorso(Album a1, Album a2, int soglia){
		this.best = new LinkedList<>();
		List<Album> parziale = new LinkedList<>();
		parziale.add(a1);
		
		this.bilancioVerticePartenza = calcolaBilancio(a1);
		
		cerca(parziale, 1, a2, soglia);
		
		return best;
	}
	
	private void cerca(List<Album> parziale, int livello, Album a2, int soglia) {
		Album ultimo = parziale.get(parziale.size()-1);
		if (ultimo.equals(a2)) {
			if (calcolaNumVerticiConBilancioMaggioreVerticePartenza(parziale) 
					>= calcolaNumVerticiConBilancioMaggioreVerticePartenza(best)) {
				best = new LinkedList<>(parziale);
				return;
			} else {
				// non faccio niente
				return;
			}
		}
		
		for (DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(ultimo)) {
			if (this.grafo.getEdgeWeight(e) >= soglia) {
				Album prossimo = Graphs.getOppositeVertex(this.grafo, e, ultimo);
				if (!parziale.contains(prossimo)) {
					parziale.add(prossimo);
					cerca(parziale, livello+1, a2, soglia);
					parziale.remove(parziale.size()-1);
				}
			}
		}
	}
	
	public int calcolaNumVerticiConBilancioMaggioreVerticePartenza(List<Album> albums) {
		int count = 0;
		for (Album a : albums) {
			if (calcolaBilancio(a) > this.bilancioVerticePartenza) {
				count++;
			}
		}
		return count;
	}

	public List<Album> getAllAlbums(){
		List<Album> albums = new LinkedList<>(this.grafo.vertexSet());
		Collections.sort(albums);
		return albums;
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
}
