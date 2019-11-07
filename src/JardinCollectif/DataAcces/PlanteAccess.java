package JardinCollectif.DataAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import JardinCollectif.Data.Membre;
import JardinCollectif.Data.Plante;
import JardinCollectif.Data.PlanteLot;

import java.sql.Date;

public class PlanteAccess {

	Connexion conn;

	public PlanteAccess(Connexion cx) {
		conn = cx;
	}
	
	public boolean ajouterPlante(String nomPlante, int tempsDeCulture) {
		try {
			conn.getConnection().getTransaction().begin();
			Plante p = new Plante(nomPlante, tempsDeCulture);
			conn.getConnection().persist(p);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean retirerPlante(String nomPlante) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query1 = conn.getConnection().createQuery("SELECT count(*) FROM plante WHERE nomPlante = :nomPlante");

			int count = (int) query1.setParameter("nomPlante", nomPlante).getSingleResult();
			
			if (count > 0)
				return false;
			
			conn.getConnection().getTransaction().begin();
			Query query2 = conn.getConnection().createQuery("DELETE FROM Plante p WHERE p.nomPlante = :nomPlante");
			query2.setParameter("nomPlante", nomPlante).executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean planterPlante(int idLot, int idPlante, Date datePlantation, int nbExemplaires, Date dateDeRecoltePrevu) {
		try {
			conn.getConnection().getTransaction().begin();
			PlanteLot pl = new PlanteLot(idLot, idPlante, datePlantation, dateDeRecoltePrevu, nbExemplaires);
			conn.getConnection().persist(pl);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean recolterPlante(int idPlante, int idLot) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("select * from PlanteLot where idLot = :idLot and idPlante = :idPlante");
			PlanteLot pl = (PlanteLot) query
				.setParameter("idLot", idLot)
				.setParameter("idPlante", idPlante)
				.getSingleResult();
			
			conn.getConnection().getTransaction().begin();
			Query query2 = conn.getConnection().createQuery("delete from plantelot where idLot = :idLot and idPlante = :idPlante");
			query2
				.setParameter("idLot", idLot)
				.setParameter("idPlante", idPlante)
				.executeUpdate();
			
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int getPlanteId(String nomPlante) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT idPlante FROM plante WHERE nomPlante = :nomPlante");
			return (int) query
				.setParameter("nomPlante", nomPlante)
				.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public String getPlanteNom(int idPlante) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT nomPlante FROM plante WHERE idPlante = :idPlante");
			return (String) query
				.setParameter("idPlante", idPlante)
				.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getPlanteNbrTotal(int idPlante) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT SUM(nbExemplaires) FROM plantelot WHERE idPlante = :idPlante");
			return (int) query
				.setParameter("idPlante", idPlante)
				.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getTempsCulture(String nomPlante) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT tempsCulture FROM plante WHERE nomPlante = :nomPlante");
			return (int) query
				.setParameter("nomPlante", nomPlante)
				.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public Date getDatePlantation(int idLot, int idPlante) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT datePlantation FROM plantelot WHERE idLot = :idLot and idPlante = :idPlante");
			return (Date) query
				.setParameter("idLot", idLot)
				.setParameter("idPlante", idPlante)
				.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Date getDateDeRecoltePrevu(int idLot, int idPlante) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT dateDeRecoltePrevu FROM plantelot WHERE idLot = :idLot and idPlante = :idPlante");
			return (Date) query
				.setParameter("idLot", idLot)
				.setParameter("idPlante", idPlante)
				.getSingleResult();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> getPlantesList() {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT * FROM plante");
			List<Plante> pList = (List<Plante>) query.getResultList();
			
			ArrayList<String> ret = new ArrayList<String>();
			
			for (Plante plante : pList) {
				int idPlante = plante.getNoPlante();
				
				String data = "Plante : ";
				data += getPlanteNom(idPlante);
				data += ", Nombre d'exemplaires : ";
				data += Integer.toString(getPlanteNbrTotal(idPlante));
				
				ret.add(data);
			}

			return ret;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getPlantesPourLot(int idLot) {
		try {
			conn.getConnection().getTransaction().begin();
			Query query = conn.getConnection().createQuery("SELECT * FROM plantelot WHERE idLot = :idLot");
			List<PlanteLot> plList = (List<PlanteLot>) query
					.setParameter("idLot", idLot)
					.getResultList();
			
			ArrayList<String> ret = new ArrayList<String>();
			
			for (PlanteLot pl : plList) {
				int idPlante = pl.getIdPlante();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				
				String data = "Plante : ";
				data += getPlanteNom(idPlante);
				data += ", Date de plantation : ";
				data += df.format(pl.getDatePlantation());
				data += ", Date de recolte prevu : ";
				data += df.format(pl.getDateDeRecoltePrevu());
				
				ret.add(data);
			}

			return ret;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
