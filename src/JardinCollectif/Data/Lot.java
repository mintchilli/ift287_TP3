package JardinCollectif.Data;

import javax.persistence.*;

@Entity
public class Lot {

	@Id @GeneratedValue
	private Integer idLot;
	private String nomLot;
	private Integer noMaxMembre;

	public Lot(String nomLot, Integer noMaxMembre) {
		super();
		this.nomLot = nomLot;
		this.noMaxMembre = noMaxMembre;
	}

	public Integer getIdLot() {
		return idLot;
	}

	public void setIdLot(Integer idLot) {
		this.idLot = idLot;
	}

	public String getNomLot() {
		return nomLot;
	}

	public void setNomLot(String nomLot) {
		this.nomLot = nomLot;
	}

	public Integer getNoMaxMembre() {
		return noMaxMembre;
	}

	public void setNoMaxMembre(Integer noMaxMembre) {
		this.noMaxMembre = noMaxMembre;
	}

}
