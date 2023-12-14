
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
public class PrecedenceOperations {
    
    private int id;
    private int opavant;
    private int opapres;

    public PrecedenceOperations(int id, int opavant, int opapres) {
        this.id = id;
        this.opavant = opavant;
        this.opapres = opapres;
    }

    public PrecedenceOperations(int opavant, int opapres) {
        this(-1, opavant, opapres );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpavant() {
        return opavant;
    }

    public void setOpavant(int opavant) {
        this.opavant = opavant;
    }

    public int getOpapres() {
        return opapres;
    }

    public void setOpapres(int opapres) {
        this.opapres = opapres;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", opavant='" + opavant + '\'' +
                ", opapres='" + opapres +
                '}';
    }

    public static PrecedenceOperations ajouterPrecedenceOperations( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in); 
            System.out.print("Entrez la référence de la machine : ");
            int opavant = scanner.nextInt();
            System.out.print("Entrez la description de la machine : ");
            int opapres = scanner.nextInt(); 
            
            PrecedenceOperations nouvelleOperation = new PrecedenceOperations(opavant, opapres);
            
            try (PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO precedence (opavant, opapres) VALUES (?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, nouvelleOperation.opavant);
                pst.setInt(2, nouvelleOperation.opapres);
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
       
    public static PrecedenceOperations supprimerPrecedenceOperations( Connection conn)throws SQLException {
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
        PrecedenceOperations machineSupprimee = new PrecedenceOperations(IdOperation, 0, 0);
        return machineSupprimee ;
    }
    

    public static List listeDesPrecedenceOperations(Connection conn) throws SQLException {
        List<PrecedenceOperations> operations = new ArrayList<>();
            
        try (PreparedStatement pst = conn.prepareStatement(
             "SELECT id, idTypeOperation, idProduit FROM precedence")) {
            try (ResultSet rs = pst.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idTypeOperation = rs.getInt("idTypeOperation");
                    int idProduit = rs.getInt("idProduit");
                    operations.add(new PrecedenceOperations(id, idTypeOperation, idProduit));
                }
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la récupération des operations : " + ex.getMessage()); 
            }
        }
        return operations;
    }

    public static void menuGestionPrecedenceOperations(Connection conn) throws SQLException {
        int choix = 0;
        do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("\nGestion des Machines");
                System.out.println("==============");
                System.out.println("1. Ajouter une machine");
                System.out.println("3. Supprimer une machine");
                System.out.println("5. Afficher la liste des machines");
                System.out.println("0. Retour au menu principal");
                System.out.println("");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        ajouterPrecedenceOperations(conn);
                        break;
                    case 3:
                        supprimerPrecedenceOperations(conn);
                        break;
                    case 5:
                        listeDesPrecedenceOperations(conn);
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