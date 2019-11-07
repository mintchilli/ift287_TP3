package JardinCollectif.DataAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import JardinCollectif.Data.Lot;
import JardinCollectif.Data.Membre;
import JardinCollectif.Data.MembreLot;

public class LotAccess {

	Connexion conn;

	public LotAccess(Connexion cx) {
		conn = cx;
	}

	public boolean ajouterLot(String nomLot, int noMaxMembre) {
		try {
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
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT idLot FROM Lot WHERE nomLot = :nomLot");

			Lot lot = (Lot) query.setParameter("nomLot", nomLot).getSingleResult();

			if (lot != null)
				return lot.getIdLot();

			return -1;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public boolean accepterDemande(int idLot, int noMembre) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery(
					"UPDATE membrelot SET validationAdmin = 1 WHERE noMembre = :noMembre AND idLot = :idLot");
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

			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection()
					.createQuery("DELETE FROM membrelot WHERE noMembre = :noMembre AND idLot = :idLot");
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

			Query query = conn.getConnection().createQuery("SELECT nomaxmembre FROM lot WHERE nomlot = :nomLot");
			Lot lot = (Lot) query.setParameter("nomLot", nomLot).getSingleResult();

			if (lot != null) {
				return lot.getNoMaxMembre();
			}
			return -1;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public boolean supprimerLot(String nomLot) {
		try {

			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("DELETE FROM membrelot WHERE idlot = :idLot");
			query.setParameter("idLot", getLotid(nomLot)).executeUpdate();

			Query query2 = conn.getConnection().createQuery("DELETE FROM lot WHERE nomlot = :nomLot");
			query2.setParameter("nomLot", nomLot).executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public int getPlantsForLot(String nomLot) {
		try {

			Query query = conn.getConnection().createQuery("SELECT COUNT(*) FROM plantelot WHERE idlot = :idLot");
			return (int) query.setParameter("idLot", getLotid(nomLot)).getSingleResult();
			

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public ArrayList<String> getLots() {
		try {
			
			Query query = conn.getConnection().createQuery("SELECT nomlot, nomaxmembre FROM lot");
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
			
			Query query = conn.getConnection().createQuery("SELECT nomembre FROM membrelot WHERE idlot = :idLot and validationadmin = true");
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
