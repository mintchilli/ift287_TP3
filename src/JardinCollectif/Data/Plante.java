package JardinCollectif.Data;

import javax.persistence.Id;
import javax.persistence.Entity;

@Entity
public class Plante {
	
	@Id
	private Integer noPlante;
	private String nomPlante;
	private int tempsCulture;
	
	public Plante(String nomPlante, int tempsCulture) {
		super();
		this.nomPlante = nomPlante;
		this.tempsCulture = tempsCulture;
	}
	
	public Integer getNoPlante() {
		return noPlante;
	}
	public void setNoPlante(Integer noPlante) {
		this.noPlante = noPlante;
	}
	public String getNomPlante() {
		return nomPlante;
	}
	public void setNomPlante(String nomPlante) {
		this.nomPlante = nomPlante;
	}
	public int getTempsCulture() {
		return tempsCulture;
	}
	public void setTempsCulture(int tempsCulture) {
		this.tempsCulture = tempsCulture;
	}
}
