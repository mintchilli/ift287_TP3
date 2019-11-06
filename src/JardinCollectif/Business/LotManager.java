package JardinCollectif.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import JardinCollectif.DataAcces.Connexion;
import JardinCollectif.DataAcces.LotAccess;
import JardinCollectif.DataAcces.MembreAccess;

public class LotManager {

	Connexion conn;
	
	public LotManager(Connexion cx) {
		conn = cx;
	}

	public boolean hasSpace(String nomLot) {
		
		MembreAccess ma = new MembreAccess(conn);
		LotAccess la = new LotAccess(conn);
		//get membremax
		
		int max = la.getMembreMax(nomLot);
		
		//get membreCount
		int  count = ma.getMembreCount(la.getLotid(nomLot));
		
		if(count++ > max)
			return false;
		else
			return true;
	}
	
	public boolean hasPlants(String nomLot) {
		
		LotAccess la = new LotAccess(conn);
		
		int count = la.getPlantsForLot(nomLot);
		
		
		if(count!= 0)
			return true;
		else
			return false;
	}

	public Map<String, ArrayList<String>> getLotsAvecMembre() {
		Map<String, ArrayList<String>> ret = new HashMap<String, ArrayList<String>>();
		
		LotAccess la = new LotAccess(conn);
		ArrayList<String> lots = la.getLots();
		MembreAccess ma = new MembreAccess(conn);
		
		for (String lot : lots) {
			
			int lotId = la.getLotid(lot.split(",")[0]);

			
			ArrayList<Integer> idMembre = la.getMembrePourLot(lotId);
			ArrayList<String> membres = new ArrayList<String>();
			
			for (Integer noMembre : idMembre) {
				String membre = ma.getMembre(noMembre);
				membres.add(membre);
			}
			ret.put(lot, membres);
			
		}
		
		return ret;
	}

}
