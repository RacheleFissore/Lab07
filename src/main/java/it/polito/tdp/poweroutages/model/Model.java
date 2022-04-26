package it.polito.tdp.poweroutages.model;

import java.util.*;
import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO poDAO;
	List<Poweroutages> poweroutages;
	List<Poweroutages> poweroutagesBest;
	int maxPersoneCoinvolte = 0;
	int numPCoinvolte = 0;
	
	public Model() {
		poDAO = new PowerOutageDAO();
		poweroutages = new LinkedList<Poweroutages>();
		poweroutagesBest = new LinkedList<Poweroutages>();
	}
	
	public List<Nerc> getNercList() {
		return poDAO.getNercList();
	}

	public List<Poweroutages> trova_eventi(String nercValue, List<Poweroutages> parziale,int numMaxAnni, int numMaxOre) {
		poweroutages = poDAO.getEventiBlackout(nercValue); // Prendo il risultato della query
		trovaEventiRicorsiva(parziale, numMaxAnni, numMaxOre, 0);		
		return poweroutagesBest;
	}
	
	public void trovaEventiRicorsiva(List<Poweroutages> parziale,int numMaxAnni, int numMaxOre, int livello) {
		// Il livello è il numero di ore di disservizio degli eventi che sono già stati inseriti nella lista parziale
		
		if(livello > numMaxOre){
			// Devo controllare che, se il livello > numMaxOre, allora non va più bene e devo tornare alla soluzione precedente
			return;
		}
		
		// Il caso in cui invece livello <= numMaxOre è una possibile soluzione, quindi non metto nessuna else perchè lui deve solo controllare
		// se il livello > numMaxOre allora sicuramente la soluzione non va bene e torno al livello precedente grazie alla return;
		
		// Ogni volta devo controllare se il numero di persone della mia soluzione parziale è superiore al numero di persone massimo della
		// soluzione migliore, se è migliore allora sostituisco al mio valore massimo di persone il valore migliore che ho appena trovato e 
		// sostituisco dentro la lista di poweroutages migliori la mia lista parziale. Ad ogni livello della ricorsione lui passa dentro 
		// questo controllo se non finisce nell'if del livello > numMaxore
		
		if(numPCoinvolte > maxPersoneCoinvolte) {
			maxPersoneCoinvolte = numPCoinvolte;
			poweroutagesBest = new LinkedList<Poweroutages>(parziale);
		}
		
		// Siamo sicuramente nel caso in cui livello <= numMaxOre che è il caso in cui ottengo la soluzione migliore
		for(Poweroutages p : poweroutages) {
			if(p.getOreDisservizio() < numMaxOre && !parziale.contains(p) && controllo(numMaxAnni, parziale, p)) {
				parziale.add(p);
				livello += p.getOreDisservizio(); // Aggiungo le ore di disservizio di quel determinato evento
				numPCoinvolte += p.getPersoneCoinvolte();				
				trovaEventiRicorsiva(parziale, numMaxAnni, numMaxOre, livello);
				parziale.remove(p);
				// Siccome il livello è una somma, quando tolgo un elemento dalla lista parziale devo togliere anche le ore relative al suo
				// disservizio dal livello e devo togliere le persone che sono state oggette di questo disservizio dal numero di persone coinvolte
				livello -= p.getOreDisservizio();
				numPCoinvolte -= p.getPersoneCoinvolte();
			}
		}
			
	}
	
	public boolean controllo(int numMaxAnni, List<Poweroutages> parziale, Poweroutages p) {
		// Devo controllare che il primo evento inserito e l'ultimo evento inserito abbiano una differenza di anni non superiore a numMaxAnni
		if(parziale.size() > 0 && (parziale.get(0).getAnno() - parziale.get(parziale.size()-1).getAnno()) > numMaxAnni) {
			return false;
		}
		
		return true;
	}
}

