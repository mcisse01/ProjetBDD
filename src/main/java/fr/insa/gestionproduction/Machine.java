
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
public class Machine {
    
    private int id;
    private String ref;
    private String des;
    private float puissance;

    public Machine(int id, String ref, String des, float puissance) {
        this.id = id;
        this.ref = ref;
        this.des = des;
        this.puissance = puissance;
    }

    public Machine(String ref, String des, float puissance) {
        this(-1, ref, des, puissance);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReference() {
        return ref;
    }

    public void setReference(String ref) {
        this.ref = ref;
    }

    public String getDescription() {
        return des;
    }
    
    public void setDescription(String des) {
        this.des = des;
    }

    public float getPuissance() {
        return puissance;
    }

    public void setPuissance(float puissance) {
        this.puissance = puissance;
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", reference='" + ref + '\'' +
                ", description='" + des + '\'' +
                ", puissance=" + puissance +
                '}';
    }

    public static Machine ajouterMachine( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in); 
            System.out.print("Entrez la référence de la machine : ");
            String ref = scanner.nextLine();
            System.out.print("Entrez la description de la machine : ");
            String des = scanner.nextLine(); 
            System.out.print("Entrez la puissance de la machine : ");
            float puissance = scanner.nextFloat();
            
            Machine nouvelleMachine = new Machine(ref, des, puissance);
            
            try (PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO machine (ref, des, puissance) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, nouvelleMachine.ref);
                pst.setString(2, nouvelleMachine.des);
                pst.setFloat(3, nouvelleMachine.puissance);
                pst.executeUpdate();
                try (ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    nouvelleMachine.setId(rid.getInt(1));
                }
                System.out.println("Machine ajoutée avec succès !");
            } catch (SQLException ex) {
                System.out.println("Erreur lors de l'ajout de la machine : " + ex.getMessage());
            }
            
        return nouvelleMachine;   
    }
    
    public static Machine modifierMachine( Connection conn) throws SQLException {
        
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
                
            } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification de la machine : " + ex.getMessage());
            }
        Machine machineModifiee = new Machine(Idmachine, ref, des, puissance);
        return machineModifiee ;
    }
    
    public static Machine supprimerMachine( Connection conn)throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez modifier : ");
            int Idmachine = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM machine WHERE id = ?")) {
                pst.setInt(1, Idmachine); 
                
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la suppression de la machine : " + ex.getMessage());
            }
        Machine machineSupprimee = new Machine(Idmachine, null, null, 0.0f);
        return machineSupprimee ;
    }
    
    public static Machine rechercherMachine( Connection conn)throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez chercher : ");
            int Idmachine = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Entrez la puissance minimale de la machine : ");
            float puissanceMinimale = scanner.nextFloat();
            
            int id = 0;
            String ref = null;
            String des = null;
            float puissance = 0.0f ;
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT id, ref, des, puissance FROM machine WHERE id = ? AND puissance > ?")) {
                pst.setInt(1, Idmachine);
                pst.setFloat(2, puissanceMinimale);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("id");
                    ref = rs.getString("ref");
                    des = rs.getString("des");
                    puissance = rs.getFloat("puissance");
                }else{
                    System.out.println("Aucune machine trouvé");
                }
            }catch (SQLException ex) {
                System.out.println("Erreur lors de la recherche de la machine : " + ex.getMessage());
            }
        Machine machineTrouvee = new Machine(Idmachine, ref, des, puissance);
        return machineTrouvee ;
    }

    public static List listeDesMachines(Connection conn) throws SQLException {
        List<Machine> machines = new ArrayList<>();
            
        try (PreparedStatement pst = conn.prepareStatement(
             "SELECT id, ref, des, puissance FROM machine")) {
            try (ResultSet rs = pst.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    float puissance = rs.getFloat("puissance");
                    machines.add(new Machine(id, ref, des, puissance));
                }
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la récupération des machines : " + ex.getMessage()); 
            }
        }
        return machines;
    }

    public static void menuGestionMachines(Connection conn) {
        int choix = 0;
        do {
            try (Scanner scanner = new Scanner(System.in)) {
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
                        ajouterMachine(conn);
                        break;
                    case 2:
                        modifierMachine(conn);
                        break;
                    case 3:
                        supprimerMachine(conn);
                        break;
                    case 4:
                        rechercherMachine(conn);
                        break;
                    case 5:
                        listeDesMachines(conn);
                        break;
                    case 0:
                        System.out.println("Retour au menu principal");
                        break;
                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erreur de saisie : " + e.getMessage());
            }
        } while (choix != 0);
    }
}
