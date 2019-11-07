package JardinCollectif.DataAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import JardinCollectif.Data.Membre;
import JardinCollectif.Data.MembreLot;

public class MembreAccess {

	Connexion conn;

	public MembreAccess(Connexion cx) {
		conn = cx;
	}

	public boolean inscrireMembre(String prenom, String nom, String motDePasse, int noMembre) {
		try {
			conn.getConnection().getTransaction().begin();
			Membre m = new Membre(prenom, nom, motDePasse, noMembre);
			conn.getConnection().persist(m);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean supprimerMembre(int noMembre) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("DELETE FROM Membre m WHERE m.noMembre = :noMembre");
			query.setParameter("noMembre", noMembre).executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean makeAdmin(int noMembre) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("UPDATE Membre SET estAdmin = 1 WHERE noMembre = :noMembre");
			query.setParameter("noMembre", noMembre).executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public ArrayList<Integer> getMembreLots(int noMembre) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT idlot FROM membrelot WHERE nomembre = :noMembre");
			List<MembreLot> membrelots = query.setParameter("noMembre", noMembre).getResultList();

			for (MembreLot m : membrelots) {
				ret.add(m.getIdLot());
			}

			return ret;

		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
			return ret;
		}

	}

	public int getMembreCount(int idLot) {
		try {

			Query query = conn.getConnection().createQuery("SELECT count(*) FROM membrelot WHERE idlot = :idLot");
			return (int) query.setParameter("idLot", idLot).getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public ArrayList<String> getMembreList() {
		try {

			Query query = conn.getConnection().createQuery("SELECT nom, prenom, estadmin FROM membre;");
			List<Membre> membres = query.getResultList();

			ArrayList<String> ret = new ArrayList<String>();

			for (Membre membre : membres) {
				String data = "Pr�nom : ";
				data += membre.getPrenom();
				data += ", Nom : ";
				data += membre.getNom();
				data += ", Est un administrateur: ";
				if (membre.getIsAdmin())
					data += "oui";
				else
					data += "non";
				ret.add(data);
			}

			return ret;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getMembre(int noMembre) {
		try {

			Query query = conn.getConnection()
					.createQuery("SELECT nom, prenom, estadmin FROM membre WHERE nomembre = :noMembre");
			Membre membre = (Membre) query.setParameter("noMembre", noMembre).getSingleResult();

			String data = "";

			data = "Pr�nom : ";
			data += membre.getPrenom();
			data += ", Nom : ";
			data += membre.getNom();
			data += ", Est un administrateur: ";
			if (membre.getIsAdmin())
				data += "oui";
			else
				data += "non";

			return data;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
