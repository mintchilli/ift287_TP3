// Travail fait par :
//   NomEquipier1 - Matricule
//   NomEquipier2 - Matricule

package JardinCollectif;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import JardinCollectif.Business.ConnectionManager;
import JardinCollectif.DataAcces.Connexion;

import java.sql.*;

/**
 * Fichier de base pour le TP2 du cours IFT287
 *
 * <pre>
 * 
 * Vincent Ducharme
 * Universite de Sherbrooke
 * Version 1.0 - 7 juillet 2016
 * IFT287 - Exploitation de BD relationnelles et OO
 * 
 * Ce programme permet d'appeler des transactions d'un systeme
 * de gestion utilisant une base de donnees.
 *
 * Paramètres du programme
 * 0- site du serveur SQL ("local" ou "dinf")
 * 1- nom de la BD
 * 2- user id pour etablir une connexion avec le serveur SQL
 * 3- mot de passe pour le user id
 * 4- fichier de transaction [optionnel]
 *           si non spécifié, les transactions sont lues au
 *           clavier (System.in)
 *
 * Pré-condition
 *   - La base de donnees doit exister
 *
 * Post-condition
 *   - Le programme effectue les mises à jour associees à chaque
 *     transaction
 * </pre>
 */
public class JardinCollectif
{
    private static ConnectionManager cm;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        if (args.length < 4)
        {
            System.out.println("Usage: java JardinCollectif.JardinCollectif <serveur> <bd> <user> <password> [<fichier-transactions>]");
            return;
        }
        
        
        try
        {
            // Il est possible que vous ayez à déplacer la connexion ailleurs.
            // N'hésitez pas à le faire!
        	cm = new ConnectionManager();
        	cm.getParams(args[0], args[1], args[2], args[3]);
            BufferedReader reader = ouvrirFichier(args);
            String transaction = lireTransaction(reader);
            while (!finTransaction(transaction))
            {
            	cm.executeTransaction(transaction);
                transaction = lireTransaction(reader);
            }
        }
        finally
        {
            if (cm != null)
            	cm.fermer();
        }
    }



    
    // ****************************************************************
    // *   Les methodes suivantes n'ont pas besoin d'etre modifiees   *
    // ****************************************************************

    /**
     * Ouvre le fichier de transaction, ou lit à partir de System.in
     */
    public static BufferedReader ouvrirFichier(String[] args) throws FileNotFoundException
    {
        if (args.length < 5)
            // Lecture au clavier
            return new BufferedReader(new InputStreamReader(System.in));
        else
            // Lecture dans le fichier passe en parametre
            return new BufferedReader(new InputStreamReader(new FileInputStream(args[4])));
    }

    /**
     * Lecture d'une transaction
     */
    static String lireTransaction(BufferedReader reader) throws IOException
    {
        return reader.readLine();
    }

    /**
     * Verifie si la fin du traitement des transactions est atteinte.
     */
    static boolean finTransaction(String transaction)
    {
        // fin de fichier atteinte
        return (transaction == null || transaction.equals("quitter"));
    }
    
    public void AfficherErreur(String er) {
    	System.out.println(" " + er);
    }

	public void afficherListeMembre(ArrayList<String> listeMembre) {
		System.out.println();
		for (String str : listeMembre) {
			System.out.println(str);
		}
		
	}


	public void afficherLots(Map<String, ArrayList<String>> lotsAvecMembre) {

		for (String lots : lotsAvecMembre.keySet()) {
			System.out.println();
			System.out.println("Nom du lot: " + lots.split(",")[0] + " , Membres maximum possible: " + lots.split(",")[1]);
			System.out.println("----------Liste des membre pour le lot----------");
			ArrayList<String> membres = lotsAvecMembre.get(lots);
			for (String membre : membres) {
				System.out.println("\t" + membre);
			}
		}
	}

	public void afficherPlantes(ArrayList<String> plantesString) {
		System.out.println();
		for (String str : plantesString) {
			System.out.println(str);
		}
	}
	
	public void afficherPlantesLots(String nomLot, ArrayList<String> plantesDansLot) {
		
		System.out.println();
		System.out.println("Lot: " + nomLot);
		System.out.println("----------Liste des plantes dans le lot----------");
		
		for(String plante : plantesDansLot) {
			System.out.println("\t" + plante);
		}
	}

}
