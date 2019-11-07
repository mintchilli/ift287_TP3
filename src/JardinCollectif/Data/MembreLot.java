package JardinCollectif.Data;

import javax.persistence.*;

@Entity
public class MembreLot {

	private Integer idMembre;
	private Integer idLot;
	private Boolean validationAdmin;

	public MembreLot(Integer noMembre, Integer idLot) {
		super();
		this.idMembre = noMembre;
		this.idLot = idLot;
		this.validationAdmin = false;
	}

	public Integer getIdMembre() {
		return idMembre;
	}

	public void setIdMembre(Integer idMembre) {
		this.idMembre = idMembre;
	}

	public Integer getIdLot() {
		return idLot;
	}

	public void setNomLot(Integer idLot) {
		this.idLot = idLot;
	}

	public Boolean getValidationAdmin() {
		return validationAdmin;
	}

	public void setValidationAdmin(Boolean validationAdmin) {
		this.validationAdmin = validationAdmin;
	}

}
