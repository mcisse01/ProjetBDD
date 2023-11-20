
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
public class Produit {
    
    private Connection conn;

    public Produit(Connection conn) {
        this.conn = conn;
    }
    
    public void ajouterProduit() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez la référence du produit : ");
            String ref = scanner.nextLine(); 
            System.out.print("Entrez la description du produit : ");
            String des = scanner.nextLine(); 

            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO produit (ref, des) VALUES (?, ?)")) {
                pst.setString(1, ref); 
                pst.setString(2, des);
                pst.executeUpdate();
                System.out.println("Produit ajoutée avec succès !"); 
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout du produit : " + ex.getMessage());
        }
    }
    
    public void modifierProduit() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID du produit que vous souhaitez modifier : ");
            int Idproduit = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Entrez la nouvelle référence du produit : ");
            String ref = scanner.nextLine();
            System.out.print("Entrez la nouvelle description du produit : ");
            String des = scanner.nextLine();

            try (PreparedStatement pst = conn.prepareStatement(
                    "UPDATE produit SET ref = ?, des = ? WHERE id = ?")) {
                pst.setString(1, ref);
                pst.setString(2, des);
                pst.setInt(4, Idproduit);
                int nbreligne = pst.executeUpdate();

                if (nbreligne > 0) {
                    System.out.println("Produit modifiée avec succès !");
                } else {
                    System.out.println("Aucune produit trouvée avec l'ID donné.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification du produit : " + ex.getMessage());
        }
    }
    
    public void supprimerProduit() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID du produit que vous souhaitez supprimer : ");
            int Idproduit = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM produit WHERE id = ?")) {
                pst.setInt(1, Idproduit); 
                int nbreligne = pst.executeUpdate(); 
                if (nbreligne > 0) {
                    System.out.println("Produit supprimée avec succès !");
                } else {
                    System.out.println("Aucune produit trouvée avec cet ID.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression du produit : " + ex.getMessage());
        }
    }
    
    public void rechercherProduit() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID du produit que vous souhaitez supprimer : ");
            int Idproduit = scanner.nextInt();
            scanner.nextLine();
            try (java.sql.PreparedStatement pst = conn.prepareStatement(
                    "SELECT id, ref, des FROM produit WHERE id = ?")) {
                pst.setInt(1, Idproduit);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    System.out.println("ID: " + id + ", Référence: " + ref + ", Description: " + des);
                } else {
                    System.out.println("Aucune produit trouvée avec cet ID.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la recherche du produit : " + ex.getMessage());
        }
    }

    public void listeDesProduits() {
        try {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT id, ref, des FROM produit")) {
                while (rs.next()) {
                    int id = rs.getInt("id"); 
                    String ref = rs.getString("ref"); 
                    String des = rs.getString("des"); 
                    
                    System.out.println("ID: " + id + ", Référence: " + ref + ", Description: " + des);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des machines : " + ex.getMessage()); // Affiche une erreur en cas d'échec
        }
    }

    public void menuGestionProduit() {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("\nGestion des Produits");
            System.out.println("==============");
            System.out.println("1. Ajouter un produit");
            System.out.println("2. Modifier un produit");
            System.out.println("3. Supprimer un produit");
            System.out.println("4. Afficher le produit à rechercher");
            System.out.println("5. Afficher la liste des produits");
            System.out.println("0. Retour au menu principal");
            System.out.println("");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); 

            switch (choix) {
                case 1:
                    ajouterProduit();
                    break;
                case 2:
                    modifierProduit();
                    break;
                case 3:
                    supprimerProduit();
                    break;
                case 4:
                    rechercherProduit();
                    break;
                case 5:
                    listeDesProduits();
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
