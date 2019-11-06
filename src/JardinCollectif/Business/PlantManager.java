package JardinCollectif.Business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import JardinCollectif.DataAcces.Connexion;
import JardinCollectif.DataAcces.LotAccess;
import JardinCollectif.DataAcces.PlanteAccess;

public class PlantManager {
	
	Connexion conn;

	public PlantManager(Connexion cx) {
		conn = cx;
	}

	public ArrayList<String> getPlantesParLot(String nomLot) {
		LotAccess la = new LotAccess(conn);
		PlanteAccess pa = new PlanteAccess(conn);
		int idLot = la.getLotid(nomLot);
		
		return pa.getPlantesPourLot(idLot);
	}
}
