package JardinCollectif.Data;

import javax.persistence.*;

@Entity
public class MembreLot {

	private Integer idMembre;
	private String nomLot;
	private Boolean validationAdmin;

	public MembreLot(Integer noMembre, String nomLot) {
		super();
		this.idMembre = noMembre;
		this.nomLot = nomLot;
		this.validationAdmin = false;
	}

	public Integer getIdMembre() {
		return idMembre;
	}

	public void setIdMembre(Integer idMembre) {
		this.idMembre = idMembre;
	}

	public String getNomLot() {
		return nomLot;
	}

	public void setNomLot(String nomLot) {
		this.nomLot = nomLot;
	}

	public Boolean getValidationAdmin() {
		return validationAdmin;
	}

	public void setValidationAdmin(Boolean validationAdmin) {
		this.validationAdmin = validationAdmin;
	}

}
