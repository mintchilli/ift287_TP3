package JardinCollectif.DataAcces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;

public class PlanteAccess {

	Connexion conn;

	public PlanteAccess(Connexion cx) {
		conn = cx;
	}
	
	public boolean ajouterPlante(String nomPlante, int tempsDeCulture) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("insert into Plante(nomPlante, tempsCulture) values(?,?)");
			s.setString(1, nomPlante);
			s.setInt(2, tempsDeCulture);

			s.execute();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean retirerPlante(String nomPlante) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("select count(*) from plantelot where idplante = ?");
			s.setInt(1, getPlanteId(nomPlante));
			s.execute();
			
			ResultSet rs = s.getResultSet();
			if (rs.next() && rs.getInt("count") > 0)
				return false;
			
			PreparedStatement s2 = conn.getConnection()
					.prepareStatement("delete from Plante where nomPlante = ?");
			s2.setString(1, nomPlante);
			s2.execute();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean planterPlante(int idLot, int idPlante, Date datePlantation, int nbExemplaires, Date dateDeRecoltePrevu) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("insert into PlanteLot(idLot, idPlante, datePlantation, nbExemplaires, dateDeRecoltePrevu) values(?,?,?,?,?)");
			s.setInt(1, idLot);
			s.setInt(2, idPlante);
			s.setDate(3, datePlantation);
			s.setInt(4, nbExemplaires);
			s.setDate(5, dateDeRecoltePrevu);

			s.execute();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean recolterPlante(int idPlante, int idLot) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("select * from PlanteLot where idLot = ? and idPlante = ?");
			s.setInt(1, idLot);
			s.setInt(2, idPlante);

			s.execute();
			
			ResultSet rs = s.getResultSet();
			if (!rs.next())
				return false;
			
			PreparedStatement s2 = conn.getConnection()
					.prepareStatement("delete from plantelot where idLot = ? and idPlante = ?");
			s2.setInt(1, idLot);
			s2.setInt(2, idPlante);
			s2.execute();
			
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int getPlanteId(String nomPlante) {
		try {
			PreparedStatement s = conn.getConnection().prepareStatement("SELECT idPlante FROM plante WHERE nomplante = ?");

			s.setString(1, nomPlante);

			s.execute();
			ResultSet rs = s.getResultSet();
			if(rs.next())
				return rs.getInt("idplante");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public String getPlanteNom(int idPlante) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT nomPlante FROM plante WHERE idPlante = ?");
			s.setInt(1, idPlante);
			s.execute();
			ResultSet rs = s.getResultSet();
			if(rs.next())
				return rs.getString("nomPlante");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getPlanteNbrTotal(int idPlante) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT SUM(nbExemplaires) FROM plantelot WHERE idPlante = ?");
			s.setInt(1, idPlante);
			s.execute();
			ResultSet rs = s.getResultSet();
			if(rs.next())
				return rs.getInt("sum");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getTempsCulture(String nomPlante) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT tempsCulture FROM plante WHERE nomplante = ?");

			s.setString(1, nomPlante);
			s.execute();
			
			ResultSet rs = s.getResultSet();
			if(rs.next())
				return rs.getInt("tempsCulture");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public Date getDatePlantation(int idLot, int idPlante) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT datePlantation FROM plantelot WHERE idLot = ? and idPlante = ?");

			s.setInt(1, idLot);
			s.setInt(2, idPlante);
			s.execute();
			
			ResultSet rs = s.getResultSet();
			if(rs.next())
				return rs.getDate("datePlantation");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Date getDateDeRecoltePrevu(int idLot, int idPlante) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT dateDeRecoltePrevu FROM plantelot WHERE idLot = ? and idPlante = ?");

			s.setInt(1, idLot);
			s.setInt(2, idPlante);
			s.execute();
			
			ResultSet rs = s.getResultSet();
			if(rs.next())
				return rs.getDate("dateDeRecoltePrevu");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<String> getPlantesList() {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT DISTINCT idplante FROM plantelot");
			s.execute();
			ResultSet rs = s.getResultSet();
			ArrayList<String> ret = new ArrayList<String>();
			
			while (rs.next()) {
				
				int idPlante = rs.getInt("idplante");
				
				String data = "Plante : ";
				data += getPlanteNom(idPlante);
				data += ", Nombre d'exemplaires : ";
				data += Integer.toString(getPlanteNbrTotal(idPlante));
				
				ret.add(data);
			}

			return ret;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getPlantesPourLot(int idLot) {
		try {
			PreparedStatement s = conn.getConnection()
					.prepareStatement("SELECT idplante, datePlantation, dateDeRecoltePrevu FROM plantelot WHERE idLot = ?");
			s.setInt(1, idLot);
			s.execute();
			ResultSet rs = s.getResultSet();
			
			ArrayList<String> ret = new ArrayList<String>();
			
			while (rs.next()) {
				int idPlante = rs.getInt("idplante");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				
				String data = "Plante : ";
				data += getPlanteNom(idPlante);
				data += ", Date de plantation : ";
				data += df.format(rs.getDate("datePlantation"));
				data += ", Date de recolte prevu : ";
				data += df.format(rs.getDate("dateDeRecoltePrevu"));
				
				ret.add(data);
			}

			return ret;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
