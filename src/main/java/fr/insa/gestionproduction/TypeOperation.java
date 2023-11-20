
package fr.insa.gestionproduction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.ResultSet ;
/**
 *
 * @author moussa
 */
public class TypeOperation {
    
    // Attribut 
    private Connection conn;

    // Constructeur
    public TypeOperation(Connection conn) {
        this.conn = conn;
    }
    // Méthode pour ajouter un TypeOperation 
    public void ajouterTypeOperation() {
        try {
            Scanner scanner = new Scanner(System.in); 
            System.out.print("Entrez la description du TypeOperation : ");
            String des = scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO typeoperation (des) VALUES (?)")) {
                pst.setString(1, des); 
                pst.executeUpdate(); 
                System.out.println("TypeOperation ajoutée avec succès !"); 
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout du TypeOperation : " + ex.getMessage()); 
        }
    }
    
    public void modifierTypeOperation() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de TypeOperation que vous souhaitez modifier : ");
            int idTypeOperation = scanner.nextInt();
            scanner.nextLine(); 

            System.out.print("Entrez la nouvelle description du TypeOperation : ");
            String des = scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "UPDATE typeoperation SET des = ? WHERE id = ?")) {
                pst.setString(1, des);
                pst.setInt(2, idTypeOperation);
                int nbreligneaffecte = pst.executeUpdate();

                if (nbreligneaffecte > 0) {
                    System.out.println("TypeOperation modifiée avec succès !");
                } else {
                    System.out.println("Aucun TypeOperation trouvée avec l'ID donné.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification du TypeOperation : " + ex.getMessage());
        }
    
    }
    
    public void supprimerTypeOperation() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID du TypeOperation à supprimer : ");
            int IdTypeOperation = scanner.nextInt();
            scanner.nextLine(); 
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM typeoperation WHERE id = ?")) {
                pst.setInt(1, IdTypeOperation); 
                int nbreligneaffecte = pst.executeUpdate(); 
                
                if (nbreligneaffecte > 0) {
                    System.out.println("TypeOperation supprimée avec succès !");
                } else {
                    System.out.println("Aucun TypeOperation trouvée avec cet ID.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression du TypeOperation : " + ex.getMessage());
        }
    }
    
    public void rechercherTypeOperation() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine à supprimer : ");
            int IdTypeOperation = scanner.nextInt();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT id, des,  FROM typeoperation WHERE id = ?")) {
                pst.setInt(1, IdTypeOperation);
                java.sql.ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String des = rs.getString("des");
                    System.out.println("ID: " + id + ", Description: " + des);
                } else {
                    System.out.println("Aucun TypeOperation trouvée avec cet ID.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la recherche du TypeOperation: " + ex.getMessage());
        }
    }

    // Méthode pour afficher la liste des TypeOperation
    public void listeDesTypeOperation() {
        try {
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT id, des FROM typeoperation")) {
               
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String des = rs.getString("des");
                    System.out.println("ID: " + id + ", Description: " + des);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des machines : " + ex.getMessage()); 
        }
    }

    public void menuGestionTypeOperation() {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("\nGestion des TypeOperation");
            System.out.println("==============");
            System.out.println("1. Ajouter un TypeOperation");
            System.out.println("2. Modifier un TypeOperation");
            System.out.println("3. Supprimer un TypeOperation");
            System.out.println("4. Afficher le TypeOperation à rechercher");
            System.out.println("5. Afficher la liste des TypeOperation");
            System.out.println("0. Retour au menu principal");
            System.out.println("");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterTypeOperation();
                    break;
                case 2:
                    modifierTypeOperation();
                    break;
                case 3:
                    supprimerTypeOperation();
                    break;
                case 4:
                    rechercherTypeOperation();
                    break;
                case 5:
                    listeDesTypeOperation();
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

