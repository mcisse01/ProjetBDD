
package fr.insa.gestionproduction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author moussa
 */
public class PrecedenceOperations {

    private Connection conn;

    public PrecedenceOperations(Connection conn) {
        this.conn = conn;
    }

    public void ajouterPrecedenceOperation() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'opération avant : ");
            int opAvant = scanner.nextInt();
            System.out.print("Entrez l'opération après : ");
            int opApres = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                  "INSERT INTO precedence (opavant, opapres) VALUES (?, ?)")) {
                pst.setInt(1, opAvant);
                pst.setInt(2, opApres);
                pst.executeUpdate();
                System.out.println("Précédence d'opération ajoutée avec succès !");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la précédence d'opération : " + ex.getMessage());
        }
    }

    public void supprimerPrecedenceOperation() {
        try {
             Scanner scanner = new Scanner(System.in);
             System.out.print("Entrez l'opération avant à supprimer : ");
             int opAv = scanner.nextInt();
             System.out.print("Entrez l'opération après à supprimer : ");
             int opAp = scanner.nextInt();
             scanner.nextLine();
             
            try (java.sql.PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM precedence WHERE opavant = ? AND opapres = ?")) {
                pst.setInt(1, opAv);
                pst.setInt(2, opAp);
                int nbreligne = pst.executeUpdate();

                if (nbreligne > 0) {
                    System.out.println("Préférence d'opération supprimée avec succès !");
                } else {
                    System.out.println("Aucune préférence d'opération trouvée avec les opérations données.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression de la préférence d'opération : " + ex.getMessage());
        }
    }

    public void afficherPrecedenceOperations() {
        try {
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT opavant, opapres FROM precedence")) {
                ResultSet rs = pst.executeQuery();

                System.out.println("Préférences d'opérations :");
                while (rs.next()) {
                    int opAvant = rs.getInt("opavant");
                    int opApres = rs.getInt("opapres");
                    System.out.println("Opération avant : " + opAvant + ", Opération après : " + opApres);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des préférences d'opération : " + ex.getMessage());
        }
    }

    public void menuGestionPrecedenceOperations() {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("\nGestion des Préférences d'Opérations");
            System.out.println("====================================");
            System.out.println("1. Ajouter une préférence d'opération");
            System.out.println("2. Supprimer une préférence d'opération");
            System.out.println("3. Afficher les préférences d'opérations");
            System.out.println("0. Retour au menu principal");
            System.out.println("");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterPrecedenceOperation();
                    break;
                case 2:
                    supprimerPrecedenceOperation();
                    break;
                case 3:
                    afficherPrecedenceOperations();
                    break;
                case 0:
                    System.out.println("Retour au menu principal");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        } while (choix != 0);
    }
 
}

