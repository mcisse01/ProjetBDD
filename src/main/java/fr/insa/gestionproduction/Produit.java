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
public class Produit {
    
    private int id;
    private String ref;
    private String des;

    public Produit(int id, String ref, String des) {
        this.id = id;
        this.ref = ref;
        this.des = des;
    }

    public Produit(String ref, String des) {
        this(-1, ref, des);
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

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", reference='" + ref + '\'' +
                ", description='" + des  +
                '}';
    }

    public static Produit ajouterProduit( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in); 
            System.out.print("Entrez la référence du Produit : ");
            String ref = scanner.nextLine();
            System.out.print("Entrez la description du Produit : ");
            String des = scanner.nextLine();
            
            Produit nouvelleProduit = new Produit(ref, des);
            
            try (PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO typeOperation (ref, des) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, nouvelleProduit.ref);
                pst.setString(2, nouvelleProduit.des);
                pst.executeUpdate();
                try (ResultSet rid = pst.getGeneratedKeys()) {
                    rid.next();
                    nouvelleProduit.setId(rid.getInt(1));
                }
                System.out.println("du Produit ajoutée avec succès !");
            } catch (SQLException ex) {
                System.out.println("Erreur lors de l'ajout du Produit : " + ex.getMessage());
            }
            
        return nouvelleProduit;   
    }
    
    public static Produit modifierProduit( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID du Produit que vous souhaitez modifier : ");
            int IdProduit = scanner.nextInt();
            scanner.nextLine();
            
            System.out.print("Entrez la nouvelle référence du Produit : ");
            String ref = scanner.nextLine();
            System.out.print("Entrez la nouvelle description du Produit : ");
            String des = scanner.nextLine();

            try (PreparedStatement pst = conn.prepareStatement(
                  "UPDATE produit SET ref = ?, des = ? WHERE id = ?")) {
                pst.setString(1, ref);
                pst.setString(2, des);
                pst.setInt(4, IdProduit);
                
            } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification du Produit : " + ex.getMessage());
            }
        Produit machineModifiee = new Produit(IdProduit, ref, des);
        return machineModifiee ;
    }
    
    public static Produit supprimerProduit( Connection conn)throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID du Produit que vous souhaitez modifier : ");
            int IdProduit = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM produit WHERE id = ?")) {
                pst.setInt(1, IdProduit); 
                
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la suppression du Produit : " + ex.getMessage());
            }
        Produit machineSupprimee = new Produit(IdProduit, null, null);
        return machineSupprimee ;
    }
    
    public static Produit rechercherProduit( Connection conn)throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez chercher : ");
            int IdTypeOperation = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Entrez l'ID du Produit : ");
            String desTypeOperation = scanner.nextLine();
            
            int id = 0;
            String ref = null;
            String des = null;
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "SELECT id, ref, des FROM produit WHERE id = ? AND des =?")) {
                pst.setInt(1, IdTypeOperation);
                pst.setString(2, desTypeOperation);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("id");
                    ref = rs.getString("ref");
                    des = rs.getString("des");
                }else{
                    System.out.println("Aucune TypeOperation trouvé");
                }
            }catch (SQLException ex) {
                System.out.println("Erreur lors de la recherche du TypeOperation : " + ex.getMessage());
            }
        Produit machineTrouvee = new Produit(IdTypeOperation, ref, des);
        return machineTrouvee ;
    }

    public static List listeDesProduits(Connection conn) throws SQLException {
        List<Produit> produits = new ArrayList<>();
            
        try (PreparedStatement pst = conn.prepareStatement(
             "SELECT id, ref, des FROM produit")) {
            try (ResultSet rs = pst.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    produits.add(new Produit(id, ref, des));
                }
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la récupération des typeOperations : " + ex.getMessage()); 
            }
        }
        return produits;
    }

    public static void menuGestionProduits(Connection conn) throws SQLException {
        int choix = 0;
        do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("\nGestion des TypeOperation");
                System.out.println("==============");
                System.out.println("1. Ajouter un Produit");
                System.out.println("2. Modifier un Produit");
                System.out.println("3. Supprimer un Produit");
                System.out.println("4. Afficher le Produit à rechercher");
                System.out.println("5. Afficher la liste des Produit");
                System.out.println("0. Retour au menu principal");
                System.out.println("");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        ajouterProduit(conn);
                        break;
                    case 2:
                        modifierProduit(conn);
                        break;
                    case 3:
                        supprimerProduit(conn);
                        break;
                    case 4:
                        rechercherProduit(conn);
                        break;
                    case 5:
                        listeDesProduits(conn);
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
