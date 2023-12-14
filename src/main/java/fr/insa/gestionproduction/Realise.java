
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
public class Realise {
    
    private int id;
    private int idmachine;
    private int idtypeoperation;
    private int duree;

    public Realise(int id, int idmachine, int idtypeoperation, int duree) {
        this.id = id;
        this.idmachine = idmachine;
        this.idtypeoperation = idtypeoperation;
        this.duree = duree ;
    }

    public Realise(int idmachine, int idtypeoperation, int duree) {
        this(-1, idmachine, idtypeoperation, duree );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getIdmachine() {
        return idmachine;
    }

    public void setIdmachine(int idmachine) {
        this.id = idmachine;
    }
    
    public int getIdtypeoperation() {
        return idtypeoperation;
    }

    public void setIdtypeoperation(int idtypeoperation) {
        this.id = idtypeoperation;
    }

    public int getDuree() {
        return duree;
    }


    public void setDuree(int duree) {
        this.duree = duree;
    }

    @Override
    public String toString() {
        return "Realise{" +
                "id=" + id +
                ", idmachine='" + idmachine + '\'' +
                ", idtypeoperation='" + idtypeoperation + '\'' +
                ", duree='" + duree +
                '}';
    }

    public static Realise ajouterRealise( Connection conn) throws SQLException {
        
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine : ");
            int idmachine = scanner.nextInt();
            System.out.print("Entrez l'ID du type d'opération : ");
            int idtypeoperation = scanner.nextInt();
            System.out.print("Entrez la durée de l'opération sur cette machine : ");
            int duree = scanner.nextInt();
            scanner.nextLine();
            
            Realise nouvelleOperation = new Realise(idmachine, idtypeoperation, duree);
            
            try (PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO realise (idmachine, idtypeoperation, duree) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, nouvelleOperation.idmachine);
                pst.setInt(2, nouvelleOperation.idtypeoperation);
                pst.setInt(3, nouvelleOperation.duree);
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
    
    public static Realise supprimerRealise( Connection conn)throws SQLException {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine que vous souhaitez modifier : ");
            int IdRealise = scanner.nextInt();
            scanner.nextLine();
            
            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM realise WHERE id = ?")) {
                pst.setInt(1, IdRealise); 
                pst.executeUpdate();
                
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la suppression de la machine : " + ex.getMessage());
            }
        Realise machineSupprimee = new Realise(IdRealise, 0, 0);
        return machineSupprimee ;
    }
    
    public static List listeDesRealisations(Connection conn) throws SQLException {
        List<Realise> realisations = new ArrayList<>();
            
        try (PreparedStatement pst = conn.prepareStatement(
             "SELECT id, idmachine, idtypeoperation FROM realise")) {
            try (ResultSet rs = pst.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idmachine = rs.getInt("idmachine");
                    int idtypeoperation = rs.getInt("idtypeoperation");
                    int duree = rs.getInt("duree");
                    realisations.add(new Realise(id, idmachine, idtypeoperation, duree));
                }
            } catch (SQLException ex) {
                System.out.println("Erreur lors de la récupération des realisations : " + ex.getMessage()); 
            }
        }
        return realisations;
    }

    public static void menuGestionRealisations(Connection conn) throws SQLException {
        int choix = 0;
        do {
                Scanner scanner = new Scanner(System.in);
                System.out.println("\nGestion des Realisations");
                System.out.println("==============");
                System.out.println("1. Ajouter une realisation");
                System.out.println("2. Supprimer une realisation");
                System.out.println("3. Afficher la liste des machines");
                System.out.println("0. Retour au menu principal");
                System.out.println("");
                System.out.print("Votre choix : ");
                choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        ajouterRealise(conn);
                        break;
                    case 2:
                        supprimerRealise(conn);
                        break;
                    case 3:
                        listeDesRealisations(conn);
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