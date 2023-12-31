package fr.insa.gestionproduction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author moussa
 */
public class TypeOperation {
    
    private int id;
    private String ref;
    private String des;

    public TypeOperation(int id, String des) {
        this.id = id;
        this.des = des;
    }

    public TypeOperation( String des) {
        this(-1, des);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return des;
    }
    
    public void setDescription(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "TypeOperation{" +
                "id=" + id +
                ", description='" + des  +
                '}';
    }

    public static TypeOperation ajouterTypeOperation( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez la description du TypeOperation : ");
            String des = scanner.nextLine();
            
            TypeOperation nouvelleTypeOperation = new TypeOperation(des);
            
            try (PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO typeOperation (des) VALUES (?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, nouvelleTypeOperation.des);
                pst.executeUpdate();
                try (ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    nouvelleTypeOperation.setId(rid.getInt(1));
                }
                System.out.println("du TypeOperation ajoutée avec succès !");
            } catch (SQLException ex) {
                System.out.println("Erreur lors de l'ajout du TypeOperation : " + ex.getMessage());
            }
            
        return nouvelleTypeOperation;   
    }
    
    public static TypeOperation modifierTypeOperation( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID du TypeOperation que vous souhaitez modifier : ");
            int IdtypeOperation = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Entrez la nouvelle description du TypeOperation : ");
            String des = scanner.nextLine();

            try (PreparedStatement pst = conn.prepareStatement(
                  "UPDATE typeOperation SET des = ? WHERE id = ?")) {
                pst.setString(1, des);
                pst.setInt(2, IdtypeOperation);
                
            } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification du TypeOperation : " + ex.getMessage());
            }
        TypeOperation machineModifiee = new TypeOperation(IdtypeOperation, des);
        return machineModifiee ;
    }
    
    public static TypeOperation supprimerTypeOperation( Connection conn)throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez modifier : ");
            int Idmachine = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM typeOperation WHERE id = ?")) {
                pst.setInt(1, Idmachine); 
                
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la suppression du TypeOperation : " + ex.getMessage());
            }
        TypeOperation machineSupprimee = new TypeOperation(Idmachine, null);
        return machineSupprimee ;
    }

    public static List listeDesTypeOperations(Connection conn) throws SQLException {
        List<TypeOperation> typeOperations = new ArrayList<>();
            
        try (PreparedStatement pst = conn.prepareStatement(
             "SELECT id, des, puissance FROM typeOperation")) {
            try (ResultSet rs = pst.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String des = rs.getString("des");
                    typeOperations.add(new TypeOperation(id, des));
                }
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la récupération des typeOperations : " + ex.getMessage()); 
            }
        }
        return typeOperations;
    }

    public static void menuGestionTypeOperations(Connection conn) throws SQLException {
        int choix = 0;
        do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("\nGestion des TypeOperation");
                System.out.println("==============");
                System.out.println("1. Ajouter un Produit");
                System.out.println("2. Modifier un Produit");
                System.out.println("3. Supprimer un Produit");
                System.out.println("4. Afficher la liste des Produit");
                System.out.println("0. Retour au menu principal");
                System.out.println("");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        ajouterTypeOperation(conn);
                        break;
                    case 2:
                        modifierTypeOperation(conn);
                        break;
                    case 3:
                        supprimerTypeOperation(conn);
                        break;
                    case 4:
                        listeDesTypeOperations(conn);
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
