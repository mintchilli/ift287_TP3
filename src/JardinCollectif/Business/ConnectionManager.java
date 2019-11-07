package JardinCollectif.Business;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.StringTokenizer;

import JardinCollectif.IFT287Exception;
import JardinCollectif.JardinCollectif;
import JardinCollectif.DataAcces.Connexion;
import JardinCollectif.DataAcces.LotAccess;
import JardinCollectif.DataAcces.MembreAccess;
import JardinCollectif.DataAcces.PlanteAccess;

public class ConnectionManager {
	private static Connexion cx;
	private JardinCollectif jc = new JardinCollectif();
	private MembreAccess ma;
	private LotAccess la;
	private PlanteAccess pa;

	public void getParams(String serveur, String bd, String user, String pass) throws IFT287Exception, SQLException {
		cx = new Connexion(serveur, bd, user, pass);

	}

	/**
	 * Decodage et traitement d'une transaction
	 * 
	 * @throws SQLException
	 */
	public void executeTransaction(String transaction) throws SQLException {

		boolean isCommiteableTransaction = false;

		try {
			System.out.print(transaction);
			// Decoupage de la transaction en mots
			StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
			if (tokenizer.hasMoreTokens()) {
				String command = tokenizer.nextToken();

				if (command.equals("inscrireMembre")) {
					// Lecture des parametres
					String prenom = readString(tokenizer);
					String nom = readString(tokenizer);
					String motDePasse = readString(tokenizer);
					int noMembre = readInt(tokenizer);

					if (ma == null)
						ma = new MembreAccess(cx);

					ma.inscrireMembre(prenom, nom, motDePasse, noMembre);
					isCommiteableTransaction = true;

				} else if (command.equals("supprimerMembre")) {
					int noMembre = readInt(tokenizer);

					MembreManager mm = new MembreManager(cx);

					if (ma == null)
						ma = new MembreAccess(cx);

					if (!mm.isOnlyLotMember(noMembre)) {
						ma.supprimerMembre(noMembre);
						isCommiteableTransaction = true;
					}

					else {
						jc.AfficherErreur("erreur, le membre est seul sur un lot");
					}

				} else if (command.equals("promouvoirAdministrateur")) {
					int noMembre = readInt(tokenizer);

					if (ma == null)
						ma = new MembreAccess(cx);

					ma.makeAdmin(noMembre);
					isCommiteableTransaction = true;
				} else if (command.equals("ajouterLot")) {
					String nomLot = readString(tokenizer);
					int noMaxMembre = readInt(tokenizer);

					if (la == null)
						la = new LotAccess(cx);

					la.ajouterLot(nomLot, noMaxMembre);
					isCommiteableTransaction = true;
				} else if (command.equals("supprimerLot")) {
					String nomLot = readString(tokenizer);

					if (la == null)
						la = new LotAccess(cx);
					LotManager lm = new LotManager(cx);
					if (lm.hasPlants(nomLot))
						jc.AfficherErreur("erreur, il y a encore desd plantes non r�colt� dans ce lot");
					else {
						la.supprimerLot(nomLot);
						isCommiteableTransaction = true;
					}

				} else if (command.equals("rejoindreLot")) {
					String nomLot = readString(tokenizer);
					int noMembre = readInt(tokenizer);

					if (la == null)
						la = new LotAccess(cx);

					la.rejoindreLot(la.getLotid(nomLot), noMembre);
					isCommiteableTransaction = true;
				} else if (command.equals("accepterDemande")) {
					String nomLot = readString(tokenizer);
					int noMembre = readInt(tokenizer);

					LotManager lm = new LotManager(cx);

					if (la == null)
						la = new LotAccess(cx);

					if (lm.hasSpace(nomLot)) {
						la.accepterDemande(la.getLotid(nomLot), noMembre);
						isCommiteableTransaction = true;
					} else {
						jc.AfficherErreur("erreur, plus d'espace dans le lot");
					}

				} else if (command.equals("refuserDemande")) {
					String nomLot = readString(tokenizer);
					int noMembre = readInt(tokenizer);

					if (la == null)
						la = new LotAccess(cx);

					la.refuserDemande(la.getLotid(nomLot), noMembre);
					isCommiteableTransaction = true;
				} else if (command.equals("ajouterPlante")) {
					String nomPlante = readString(tokenizer);
					int tempsDeCulture = readInt(tokenizer);

					if (pa == null)
						pa = new PlanteAccess(cx);

					pa.ajouterPlante(nomPlante, tempsDeCulture);
					isCommiteableTransaction = true;
					
				} else if (command.equals("retirerPlante")) {
					String nomPlante = readString(tokenizer);

					if (pa == null)
						pa = new PlanteAccess(cx);

					pa.retirerPlante(nomPlante);
					isCommiteableTransaction = true;
					
				} else if (command.equals("planterPlante")) {
					String nomPlante = readString(tokenizer);
					String nomLot = readString(tokenizer);
					int noMembre = readInt(tokenizer);
					int nbExemplaires = readInt(tokenizer);
					Date datePlantation = readDate(tokenizer);

					if (pa == null)
						pa = new PlanteAccess(cx);

					if (ma == null)
						ma = new MembreAccess(cx);

					if (la == null)
						la = new LotAccess(cx);

					int idLot = la.getLotid(nomLot);
					int idPlante = pa.getPlanteId(nomPlante);
					int tempsCulture = pa.getTempsCulture(nomPlante);

					Calendar calendar = Calendar.getInstance();
					calendar.setTime(datePlantation);
					calendar.add(Calendar.DATE, tempsCulture);
					Date dateDeRecoltePrevu = new Date(calendar.getTimeInMillis());

					if (nbExemplaires > 0 && idLot != -1 && idPlante != -1
							&& la.getMembrePourLot(idLot).contains(noMembre))
					{
						pa.planterPlante(idLot, idPlante, datePlantation, nbExemplaires, dateDeRecoltePrevu);
						isCommiteableTransaction = true;
					}
				} else if (command.equals("recolterPlante")) {
					String nomPlante = readString(tokenizer);
					String nomLot = readString(tokenizer);
					int noMembre = readInt(tokenizer);

					if (pa == null)
						pa = new PlanteAccess(cx);

					if (la == null)
						la = new LotAccess(cx);

					int idPlante = pa.getPlanteId(nomPlante);
					int idLot = la.getLotid(nomLot);

					Date dateDeRevoltePrevu = pa.getDateDeRecoltePrevu(idLot, idPlante);
					Date currentDate = new Date(new java.util.Date().getTime());

					boolean pretPourRecolte = dateDeRevoltePrevu.before(currentDate)
							|| dateDeRevoltePrevu.equals(currentDate);

					if (pretPourRecolte && idPlante != -1 && idLot != -1
							&& la.getMembrePourLot(la.getLotid(nomLot)).contains(noMembre))
					{
						pa.recolterPlante(idPlante, idLot);
						isCommiteableTransaction = true;
					}
				} else if (command.equals("afficherMembres")) {
					MembreManager mm = new MembreManager(cx);
					jc.afficherListeMembre(mm.getMembre());
				} else if (command.equals("afficherPlantes")) {
					if (pa == null)
						pa = new PlanteAccess(cx);

					PlantManager pm = new PlantManager(cx);
					jc.afficherPlantes(pa.getPlantesList());
				} else if (command.equals("afficherLots")) {
					LotManager lm = new LotManager(cx);
					jc.afficherLots(lm.getLotsAvecMembre());
				} else if (command.equals("afficherPlantesLot")) {
					String nomLot = readString(tokenizer);
					PlantManager pm = new PlantManager(cx);
					jc.afficherPlantesLots(nomLot, pm.getPlantesParLot(nomLot));
				} else {
					jc.AfficherErreur(" : Transaction non reconnue");
					return;
				}
			}

			if (isCommiteableTransaction)
				cx.commit();
		} catch (Exception e) {
			jc.AfficherErreur(e.toString());
			// Ce rollback est ici seulement pour vous aider et éviter des problèmes lors
			// de la correction
			// automatique. En théorie, il ne sert à rien et ne devrait pas apparaître
			// ici dans un programme
			// fini et fonctionnel sans bogues.
			cx.rollback();
		}

	}

	/** Lecture d'une chaine de caracteres de la transaction entree a l'ecran */
	static String readString(StringTokenizer tokenizer) throws Exception {
		if (tokenizer.hasMoreElements())
			return tokenizer.nextToken();
		else
			throw new Exception("Autre parametre attendu");
	}

	/**
	 * Lecture d'un int java de la transaction entree a l'ecran
	 */
	static int readInt(StringTokenizer tokenizer) throws Exception {
		if (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken();
			try {
				return Integer.valueOf(token).intValue();
			} catch (NumberFormatException e) {
				throw new Exception("Nombre attendu a la place de \"" + token + "\"");
			}
		} else
			throw new Exception("Autre parametre attendu");
	}

	static Date readDate(StringTokenizer tokenizer) throws Exception {
		if (tokenizer.hasMoreElements()) {
			String token = tokenizer.nextToken();
			try {
				return Date.valueOf(token);
			} catch (IllegalArgumentException e) {
				throw new Exception("Date dans un format invalide - \"" + token + "\"");
			}
		} else
			throw new Exception("Autre parametre attendu");
	}

	public void fermer() throws SQLException {
		if (cx != null) {
			cx.fermer();
		}

	}

}
