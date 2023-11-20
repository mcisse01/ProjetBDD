
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
public class Machine {
    
    
    //bonjour
    private Connection conn;

    public Machine(Connection conn) {
        this.conn = conn;
    }
    
    public void ajouterMachine() {
        try {
            Scanner scanner = new Scanner(System.in); 
            System.out.print("Entrez la référence de la machine : ");
            String ref = scanner.nextLine();
            System.out.print("Entrez la description de la machine : ");
            String des = scanner.nextLine(); 
            System.out.print("Entrez la puissance de la machine : ");
            float puissance = scanner.nextFloat();
            
            try (PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO machine (ref, des, puissance) VALUES (?, ?, ?)")) {
                pst.setString(1, ref);
                pst.setString(2, des);
                pst.setFloat(3, puissance);
                pst.executeUpdate();
                System.out.println("Machine ajoutée avec succès !");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la machine : " + ex.getMessage());
        }
    }

    public void modifierMachine() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez modifier : ");
            int Idmachine = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Entrez la nouvelle référence de la machine : ");
            String ref = scanner.nextLine();
            System.out.print("Entrez la nouvelle description de la machine : ");
            String des = scanner.nextLine();
            System.out.print("Entrez la nouvelle puissance de la machine : ");
            float puissance = scanner.nextFloat();

            try (PreparedStatement pst = conn.prepareStatement(
                  "UPDATE machine SET ref = ?, des = ?, puissance = ? WHERE id = ?")) {
                pst.setString(1, ref);
                pst.setString(2, des);
                pst.setFloat(3, puissance);
                pst.setInt(4, Idmachine);
                int nbredelignemodifie = pst.executeUpdate(); 
                
                if (nbredelignemodifie > 0) {
                    System.out.println("Machine modifiée avec succès !");
                } else {
                    System.out.println("Aucune machine trouvée avec l'ID donné.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification de la machine : " + ex.getMessage());
        }
    }
    
    public void supprimerMachine() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez modifier : ");
            int Idmachine = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM machine WHERE id = ?")) {
                pst.setInt(1, Idmachine); 
                int nbredelignesuprime = pst.executeUpdate(); 
                
                if (nbredelignesuprime > 0) {
                    System.out.println("Machine supprimée avec succès !");
                } else {
                    System.out.println("Aucune machine trouvée avec cet ID.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression de la machine : " + ex.getMessage());
        }
    }
    
    public void rechercherMachine() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez chercher : ");
            int Idmachine = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT id, ref, des, puissance FROM machine WHERE id = ?")) {
                pst.setInt(1, Idmachine);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    float puissance = rs.getFloat("puissance");
                    System.out.println("ID: " + id + ", Référence: " + ref + ", Description: " + des + ", Puissance: " + puissance);
                } else {
                    System.out.println("Aucune machine trouvée avec cet ID.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la recherche de la machine : " + ex.getMessage());
        }
    }

    public void listeDesMachines() {
        try {
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                 "SELECT id, ref, des, puissance FROM machine")) {
                
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    float puissance = rs.getFloat("puissance");

                    System.out.println("ID: " + id + ", Référence: " + ref + ", Description: " + des + ", Puissance: " + puissance);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des machines : " + ex.getMessage()); 
        }
    }

    public void menuGestionMachines() {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("\nGestion des Machines");
            System.out.println("==============");
            System.out.println("1. Ajouter une machine");
            System.out.println("2. Modifier une machine");
            System.out.println("3. Supprimer une machine");
            System.out.println("4. Afficher la machine à rechercher");
            System.out.println("5. Afficher la liste des machines");
            System.out.println("0. Retour au menu principal");
            System.out.println("");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterMachine();
                    break;
                case 2:
                    modifierMachine();
                    break;
                case 3:
                    supprimerMachine();
                    break;
                case 4:
                    rechercherMachine();
                    break;
                case 5:
                    listeDesMachines();
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
