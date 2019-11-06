package JardinCollectif.Data;

import javax.persistence.*;

@Entity
public class Membre {

	@Id
	private Integer noMembre;
	private String nom;
	private String prenom;
	private String motDePasse;
	private Boolean estAdmin;
	
	
	public Membre(String prenom, String nom, String motDePasse, Integer noMembre) {
		this.prenom = prenom;
		this.nom = nom;
		this.motDePasse = motDePasse;
		this.noMembre = noMembre;
		this.estAdmin = false;
	}
	
	public Integer getId() {
		return noMembre;
	}
	public void setId(Integer id) {
		this.noMembre = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getPassword() {
		return motDePasse;
	}
	public void setPassword(String password) {
		this.motDePasse = password;
	}
	public Boolean getIsAdmin() {
		return estAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.estAdmin = isAdmin;
	}


}
