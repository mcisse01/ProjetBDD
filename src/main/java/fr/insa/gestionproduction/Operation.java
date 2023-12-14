
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
    
//    public static Operation modifierMachine( Connection conn) throws SQLException {
//        
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Entrez l'ID de la machine que vous souhaitez modifier : ");
//            int Idmachine = scanner.nextInt();
//            scanner.nextLine();
//            
//            System.out.print("Entrez la nouvelle référence de la machine : ");
//            String ref = scanner.nextLine();
//            System.out.print("Entrez la nouvelle description de la machine : ");
//            String des = scanner.nextLine();
//            System.out.print("Entrez la nouvelle puissance de la machine : ");
//            float puissance = scanner.nextFloat();
//
//            try (PreparedStatement pst = conn.prepareStatement(
//                  "UPDATE machine SET ref = ?, des = ?, puissance = ? WHERE id = ?")) {
//                pst.setString(1, ref);
//                pst.setString(2, des);
//                pst.setFloat(3, puissance);
//                pst.setInt(4, Idmachine);
//                
//            } catch (SQLException ex) {
//            System.out.println("Erreur lors de la modification de la machine : " + ex.getMessage());
//            }
//        Machine machineModifiee = new Machine(Idmachine, ref, des, puissance);
//        return machineModifiee ;
//    }
//    
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
    
//    public static Machine rechercherMachine( Connection conn)throws SQLException {
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Entrez l'ID de la machine que vous souhaitez chercher : ");
//            int Idmachine = scanner.nextInt();
//            scanner.nextLine();
//            System.out.print("Entrez la puissance minimale de la machine : ");
//            float puissanceMinimale = scanner.nextFloat();
//            
//            int id = 0;
//            String ref = null;
//            String des = null;
//            float puissance = 0.0f ;
//            
//            try (PreparedStatement pst = conn.prepareStatement(
//                    "SELECT id, ref, des, puissance FROM machine WHERE id = ? AND puissance > ?")) {
//                pst.setInt(1, Idmachine);
//                pst.setFloat(2, puissanceMinimale);
//                ResultSet rs = pst.executeQuery();
//                if (rs.next()) {
//                    id = rs.getInt("id");
//                    ref = rs.getString("ref");
//                    des = rs.getString("des");
//                    puissance = rs.getFloat("puissance");
//                }else{
//                    System.out.println("Aucune machine trouvé");
//                }
//            }catch (SQLException ex) {
//                System.out.println("Erreur lors de la recherche de la machine : " + ex.getMessage());
//            }
//        Machine machineTrouvee = new Machine(Idmachine, ref, des, puissance);
//        return machineTrouvee ;
//    }

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
                System.out.println("2. Modifier une machine");
                System.out.println("3. Supprimer une machine");
                System.out.println("4. Afficher la machine à rechercher");
                System.out.println("5. Afficher la liste des machines");
                System.out.println("0. Retour au menu principal");
                System.out.println("");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        ajouterOperation(conn);
                        break;
//                    case 2:
//                        modifierOperation(conn);
//                        break;
                    case 3:
                        supprimerOperation(conn);
                        break;
//                    case 4:
//                        rechercherMachine(conn);
//                        break;
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