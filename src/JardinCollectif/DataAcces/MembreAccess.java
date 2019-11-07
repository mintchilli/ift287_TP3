package JardinCollectif.DataAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import JardinCollectif.Data.Membre;

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
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT idlot FROM membrelot WHERE nomembre = ?");

			s.setInt(1, noMembre);

			s.execute();
			ResultSet rs = s.getResultSet();

			while (rs.next()) {
				ret.add(rs.getInt("idlot"));
			}

			return ret;

		} catch (SQLException e) {
			e.printStackTrace();
			return ret;
		}

	}

	// ------------------------------herre*---------------------
	public int getMembreCount(int idLot) {
		try {

			Query query = conn.getConnection().createQuery("SELECT count(*) FROM membrelot WHERE idlot = :idLot");
			query.setParameter("idLot", idLot).executeUpdate();

			return (int) query.setParameter("idLot", idLot).getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public ArrayList<String> getMembreList() {
		try {
			PreparedStatement s = conn.getConnection().prepareStatement("SELECT nom, prenom, estadmin FROM membre;");

			s.execute();
			ResultSet rs = s.getResultSet();

			ArrayList<String> ret = new ArrayList<String>();

			while (rs.next()) {
				String data = "Prénom : ";
				data += rs.getString("prenom");
				data += ", Nom : ";
				data += rs.getString("nom");
				data += ", Est un administrateur: ";
				if (rs.getBoolean("estadmin"))
					data += "oui";
				else
					data += "non";
				ret.add(data);
			}

			return ret;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getMembre(int noMembre) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT nom, prenom, estadmin FROM membre WHERE nomembre = ?");

			s.setInt(1, noMembre);
			s.execute();

			ResultSet rs = s.getResultSet();

			String data = "";

			while (rs.next()) {
				data = "Prénom : ";
				data += rs.getString("prenom");
				data += ", Nom : ";
				data += rs.getString("nom");
				data += ", Est un administrateur: ";
				if (rs.getBoolean("estadmin"))
					data += "oui";
				else
					data += "non";
			}

			return data;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
