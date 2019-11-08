package JardinCollectif.DataAcces;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import JardinCollectif.Data.Lot;
import JardinCollectif.Data.MembreLot;

public class LotAccess {

	Connexion conn;

	public LotAccess(Connexion cx) {
		conn = cx;
	}

	public boolean ajouterLot(String nomLot, int noMaxMembre) {
		try {
			if (!conn.getConnection().getTransaction().isActive())
				conn.getConnection().getTransaction().begin();
			Lot l = new Lot(nomLot, noMaxMembre);
			conn.getConnection().persist(l);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean rejoindreLot(int idLot, int noMembre) {
		try {
			if (!conn.getConnection().getTransaction().isActive())
				conn.getConnection().getTransaction().begin();
			MembreLot ml = new MembreLot(noMembre, idLot);
			conn.getConnection().persist(ml);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public int getLotid(String nomLot) {
		try {
			EntityManager newEm = conn.getEmf().createEntityManager();
			if (!newEm.getTransaction().isActive())
				newEm.getTransaction().begin();
			Query query = newEm.createQuery("SELECT idLot FROM Lot WHERE nomLot = :nomLot");

			Integer idLot = (Integer) query.setParameter("nomLot", nomLot).getSingleResult();

			if (idLot != null) {
				newEm.close();
				return idLot;
			}
				
			return -1;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public boolean accepterDemande(int idLot, int noMembre) {
		try {
			if (!conn.getConnection().getTransaction().isActive())
				conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery(
					"UPDATE MembreLot SET validationAdmin = 1 WHERE idMembre = :noMembre AND idLot = :idLot");
			query.setParameter("noMembre", noMembre);
			query.setParameter("idLot", idLot).executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean refuserDemande(int idLot, int noMembre) {
		try {
			if (!conn.getConnection().getTransaction().isActive())
				conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection()
					.createQuery("DELETE FROM MembreLot WHERE idMembre = :noMembre AND idLot = :idLot");
			query.setParameter("noMembre", noMembre);
			query.setParameter("idLot", idLot).executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public int getMembreMax(String nomLot) {
		try {

			Query query = conn.getConnection().createQuery("SELECT noMaxMembre FROM Lot WHERE nomLot = :nomLot");
			Integer max = (Integer) query.setParameter("nomLot", nomLot).getSingleResult();

			if (max != null) {
				return max;
			}
			return -1;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean supprimerLot(String nomLot) {
		try {
			if (!conn.getConnection().getTransaction().isActive())
				conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("DELETE FROM MembreLot WHERE idLot = :idLot");
			query.setParameter("idLot", getLotid(nomLot)).executeUpdate();

			Query query2 = conn.getConnection().createQuery("DELETE FROM Lot WHERE nomLot = :nomLot");
			query2.setParameter("nomLot", nomLot).executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public int getPlantsForLot(String nomLot) {
		try {

			Query query = conn.getConnection().createQuery("SELECT COUNT(*) FROM PlanteLot WHERE idLot = :idLot", Integer.class);
			return (int) query.setParameter("idLot", getLotid(nomLot)).getSingleResult();
			

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public ArrayList<String> getLots() {
		try {
			
			Query query = conn.getConnection().createQuery("SELECT l FROM Lot l");
			List<Lot> lots = query.getResultList();

			ArrayList<String> ret = new ArrayList<String>();
			
			for (Lot lot : lots) {
				String data = "";
				data += lot.getNomLot();
				data += ",";
				data += lot.getNoMaxMembre();
				ret.add(data);
			}


			return ret;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<Integer> getMembrePourLot(int lotId) {
		try {
			
			Query query = conn.getConnection().createQuery("SELECT c FROM MembreLot c WHERE idLot = :idLot and validationAdmin = 1");
			List<MembreLot> ml = query.setParameter("idLot", lotId).getResultList();

			ArrayList<Integer> ret = new ArrayList<Integer>();
			
			for (MembreLot membreLots : ml) {
				ret.add(membreLots.getIdMembre());
			}

			return ret;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
