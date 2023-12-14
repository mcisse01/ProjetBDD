
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

    public static Operation ajouterPrecedenceOperations( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in); 
            System.out.print("Entrez la référence de la machine : ");
            int idTypeOperation = scanner.nextInt();
            System.out.print("Entrez la description de la machine : ");
            int idProduit = scanner.nextInt(); 
            
            PrecedenceOperations nouvelleOperation = new PrecedenceOperations(idTypeOperation, idProduit);
            
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
                System.out.println("3. Supprimer une machine");
                System.out.println("5. Afficher la liste des machines");
                System.out.println("0. Retour au menu principal");
                System.out.println("");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        ajouterOperation(conn);
                        break;
                    case 3:
                        supprimerOperation(conn);
                        break;
                    case 5:
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

