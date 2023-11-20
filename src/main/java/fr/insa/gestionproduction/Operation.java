
package fr.insa.gestionproduction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.ResultSet ;

/**
 *
 * @author moussa
 */
public class Operation {

    private Connection conn;

    public Operation(Connection conn) {
        this.conn = conn;
    }

    public void ajouterOperation() {
        try {
            Scanner scanner = new Scanner(System.in); 
            System.out.print("Entrez l'idTypeOperation : ");
            int idTypeOperation = scanner.nextInt();  
            System.out.print("Entrez l'idProduit : ");
            int idProduit = scanner.nextInt(); 
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO operations (idtypeoperation, idproduit) VALUES (?, ?)")) {
                pst.setInt(1, idTypeOperation);
                pst.setInt(2, idProduit);
                pst.executeUpdate();
                System.out.println("Opération ajoutée avec succès !");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de l'opération : " + ex.getMessage());
        }
    }

    public void modifierOperation() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de l'opération que vous souhaitez modifier : ");
            int idO = scanner.nextInt();
            System.out.print("Entrez le nouvel ID du type d'opération : ");
            int idT = scanner.nextInt();
            System.out.print("Entrez le nouvel ID du produit : ");
            int idP = scanner.nextInt();
            scanner.nextLine();
            
            String updateQuery = "UPDATE operations SET idtypeoperation = ?, idproduit = ? WHERE id = ?";
            try (java.sql.PreparedStatement pst = conn.prepareStatement(updateQuery)) {
                pst.setInt(1, idT);
                pst.setInt(2, idP);
                pst.setInt(3, idO);
                int nbredelignemodifie = pst.executeUpdate();

                if (nbredelignemodifie > 0) {
                    System.out.println("Opération modifiée avec succès !");
                } else {
                    System.out.println("Aucune opération trouvée avec l'ID donné.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification de l'opération : " + ex.getMessage());
        }
    }

    public void supprimerOperation() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de l'opération à supprimer : ");
            int idoperation = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM operations WHERE id = ?")) {
                pst.setInt(1, idoperation);
                int nbredelignemodifie = pst.executeUpdate();

                if (nbredelignemodifie > 0) {
                    System.out.println("Opération supprimée avec succès !");
                } else {
                    System.out.println("Aucune opération trouvée avec cet ID.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression de l'opération : " + ex.getMessage());
        }
    }

    public void afficherListeOperations() {
        try {
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT id, idtypeoperation, idproduit FROM operations");
                ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idTypeOperation = rs.getInt("idtypeoperation");
                    int idProduit = rs.getInt("idproduit");
                    System.out.println("ID: " + id + ", ID TypeOperation: " + idTypeOperation + ", ID Produit: " + idProduit);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des opérations : " + ex.getMessage());
        }
    }

    public void menuGestionOperation() {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("\nGestion des Opérations");
            System.out.println("======================");
            System.out.println("1. Ajouter une opération");
            System.out.println("2. Modifier une opération");
            System.out.println("3. Supprimer une opération");
            System.out.println("4. Afficher la liste des opérations");
            System.out.println("0. Retour au menu principal");
            System.out.println("");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterOperation();
                    break;
                case 2:
                    modifierOperation();
                    break;
                case 3:                 
                    supprimerOperation();
                    break;
                case 4:
                    afficherListeOperations();
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
