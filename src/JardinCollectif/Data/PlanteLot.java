package JardinCollectif.Data;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class PlanteLot {
	
	private int idLot;
	private int idPlante;
	private Date datePlantation;
	private Date dateDeRecoltePrevu;
	private int nbExemplaires;
	
	public PlanteLot(int idLot, int idPlante, Date datePlantation, Date dateDeRecoltePrevu, int nbExemplaires) {
		super();
		this.idLot = idLot;
		this.idPlante = idPlante;
		this.datePlantation = datePlantation;
		this.dateDeRecoltePrevu = dateDeRecoltePrevu;
		this.nbExemplaires = nbExemplaires;
	}
	
	public int getIdLot() {
		return idLot;
	}
	public void setIdLot(int idLot) {
		this.idLot = idLot;
	}
	public int getIdPlante() {
		return idPlante;
	}
	public void setIdPlante(int idPlante) {
		this.idPlante = idPlante;
	}
	public Date getDatePlantation() {
		return datePlantation;
	}
	public void setDatePlantation(Date datePlantation) {
		this.datePlantation = datePlantation;
	}
	public Date getDateDeRecoltePrevu() {
		return dateDeRecoltePrevu;
	}
	public void setDateDeRecoltePrevu(Date dateDeRecoltePrevu) {
		this.dateDeRecoltePrevu = dateDeRecoltePrevu;
	}
	public int getNbExemplaires() {
		return nbExemplaires;
	}
	public void setNbExemplaires(int nbExemplaires) {
		this.nbExemplaires = nbExemplaires;
	}
}
