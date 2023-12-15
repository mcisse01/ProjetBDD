
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
public class Operation {
    
    private int id;
    private int idTypeOperation;
    private int idProduit;

    public Operation(int id, int idTypeOperation, int idProduit) {
        this.id = id;
        this.idTypeOperation = idTypeOperation;
        this.idProduit = idProduit;
    }

    public Operation(int idTypeOperation, int idProduit) {
        this(-1, idTypeOperation, idProduit );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTypeOperation() {
        return idTypeOperation;
    }

    public void setIdTypeOperation(int idTypeOperation) {
        this.idTypeOperation = idTypeOperation;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", idTypeOperation='" + idTypeOperation + '\'' +
                ", idProduit='" + idProduit +
                '}';
    }

    public static Operation ajouterOperation( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in); 
            System.out.print("Entrez la référence de la machine : ");
            int idTypeOperation = scanner.nextInt();
            System.out.print("Entrez la description de la machine : ");
            int idProduit = scanner.nextInt(); 
            
            Operation nouvelleOperation = new Operation(idTypeOperation, idProduit);
            
            try (PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO operation (idTypeOperation, idProduit) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, nouvelleOperation.idTypeOperation);
                pst.setInt(2, nouvelleOperation.idProduit);
                pst.executeUpdate();
                try (ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    nouvelleOperation.setId(rid.getInt(1));
                }
                System.out.println("Operation ajoutée avec succès !");
            } catch (SQLException ex) {
                System.out.println("Erreur lors de l'ajout de la machine : " + ex.getMessage());
            }
            
        return nouvelleOperation;   
    }
       
    public static Operation supprimerOperation( Connection conn)throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez modifier : ");
            int IdOperation = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM operation WHERE id = ?")) {
                pst.setInt(1, IdOperation); 
                pst.executeUpdate();
                
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la suppression de la machine : " + ex.getMessage());
            }
        Operation machineSupprimee = new Operation(IdOperation, 0, 0);
        return machineSupprimee ;
    }
    

    public static List listeDesOperations(Connection conn) throws SQLException {
        List<Operation> operations = new ArrayList<>();
            
        try (PreparedStatement pst = conn.prepareStatement(
             "SELECT id, idTypeOperation, idProduit FROM operation")) {
            try (ResultSet rs = pst.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idTypeOperation = rs.getInt("idTypeOperation");
                    int idProduit = rs.getInt("idProduit");
                    operations.add(new Operation(id, idTypeOperation, idProduit));
                }
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la récupération des operations : " + ex.getMessage()); 
            }
        }
        return operations;
    }

    public static void menuGestionOperations(Connection conn) throws SQLException {
        int choix = 0;
        do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("\nGestion des Machines");
                System.out.println("==============");
                System.out.println("1. Ajouter une machine");
                System.out.println("2. Supprimer une machine");
                System.out.println("3. Afficher la liste des machines");
                System.out.println("0. Retour au menu principal");
                System.out.println("");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        ajouterOperation(conn);
                        break;
                    case 2:
                        supprimerOperation(conn);
                        break;
                    case 3:
                        listeDesOperations(conn);
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