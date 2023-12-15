
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
    
    private int opavant;
    private int opapres;

    public PrecedenceOperations(int opavant, int opapres) {
        this.opavant = opavant;
        this.opapres = opapres;
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
        return "Precedence{" +
                ", opavant='" + opavant + '\'' +
                ", opapres='" + opapres +
                '}';
    }
    
    public static PrecedenceOperations ajouterPrecedenceOperations(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez l'opération avant : ");
        int opAvant = scanner.nextInt();
        System.out.print("Entrez l'opération après : ");
        int opApres = scanner.nextInt();

        try (PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO precedence (opavant, opapres) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, opAvant);
            pst.setInt(2, opApres);

            int nbreafeecete = pst.executeUpdate();

            if (nbreafeecete > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        System.out.println("Précédence ajoutée avec succès");
                        return new PrecedenceOperations( opAvant, opApres);
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la précédence : " + ex.getMessage());
        }
        return null;
    }

    public static PrecedenceOperations supprimerPrecedenceOperations(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez l'opération avant : ");
        int opAvant = scanner.nextInt();
        System.out.print("Entrez l'opération après : ");
        int opApres = scanner.nextInt();

        try (PreparedStatement pst = conn.prepareStatement(
                "DELETE FROM precedence WHERE opavant = ? AND opapres = ?")) {
            pst.setInt(1, opAvant);
            pst.setInt(2, opApres);

            int nbreAffectees = pst.executeUpdate();
            if (nbreAffectees > 0) {
                System.out.println("Précédence supprimée avec succès !");
                return new PrecedenceOperations(opAvant, opApres); 
            } else {
                System.out.println("Aucune précédence correspondante n'a été trouvée.");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression de la précédence : " + ex.getMessage());
        }
        return null;
    }

    public static List listeDesPrecedenceOperations(Connection conn) throws SQLException {
        List<PrecedenceOperations> operations = new ArrayList<>();
            
        try (PreparedStatement pst = conn.prepareStatement(
             "SELECT opavant, opapres FROM precedence")) {
            try (ResultSet rs = pst.executeQuery()){
                while (rs.next()) {
                    int opavant = rs.getInt("opavant");
                    int opapres = rs.getInt("opapres");
                    operations.add(new PrecedenceOperations(opavant, opapres));
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