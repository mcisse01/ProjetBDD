
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
public class Realise {

    private Connection conn;

    public Realise(Connection conn) {
        this.conn = conn;
    }

    public void associerTypeOperationMachine() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de la machine : ");
            int idm = scanner.nextInt();
            System.out.print("Entrez l'ID du type d'opération : ");
            int Idt = scanner.nextInt();
            System.out.print("Entrez la durée de l'opération sur cette machine : ");
            int duree = scanner.nextInt();
            scanner.nextLine();

            try (PreparedStatement pst = conn.prepareStatement(
                    "INSERT INTO realise (idmachine, idtypeoperation, duree) VALUES (?, ?, ?)")) {
                pst.setInt(1, idm);
                pst.setInt(2, Idt);
                pst.setInt(3, duree);
                pst.executeUpdate();
                System.out.println("Association ajoutée avec succès !");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'association : " + ex.getMessage());
        }
    }

    public void modifierDureeOperationMachine() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de l'association à modifier : ");
            int idassociation = scanner.nextInt();
            System.out.print("Entrez la nouvelle durée de l'opération sur cette machine : ");
            int nouvelleDuree = scanner.nextInt();
            scanner.nextLine();

            try (java.sql.PreparedStatement pst = conn.prepareStatement(
                    "UPDATE realise SET duree = ? WHERE id = ?")) {
                pst.setInt(1, nouvelleDuree);
                pst.setInt(2, idassociation);
                int nbreligneaffecte = pst.executeUpdate();

                if (nbreligneaffecte > 0) {
                    System.out.println("Durée modifiée avec succès !");
                } else {
                    System.out.println("Aucune association trouvée avec l'ID donné.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification de la durée : " + ex.getMessage());
        }
    }

    public void dissocierTypeOperationMachine() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Entrez l'ID de l'association à dissocier : ");
            int Idassociation = scanner.nextInt();
            scanner.nextLine();

            try (PreparedStatement pst = conn.prepareStatement(
                    "DELETE FROM realise WHERE id = ?")) {
                pst.setInt(1, Idassociation);
                int nbreligneaffecte = pst.executeUpdate();

                if (nbreligneaffecte > 0) {
                    System.out.println("Association dissociee avec succès !");
                } else {
                    System.out.println("Aucune association trouvée avec l'ID donné.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la dissociation : " + ex.getMessage());
        }
    }

    public void menuGestionAssociations() {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("\nGestion des Associations Machines-TypeOperations");
            System.out.println("==============================================");
            System.out.println("1. Associer un type d'opération à une machine");
            System.out.println("2. Modifier la durée d'une opération sur une machine");
            System.out.println("3. Dissocier un type d'opération d'une machine");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();

            switch (choix) {
                case 1:
                    associerTypeOperationMachine();
                    break;
                case 2:
                    modifierDureeOperationMachine();
                    break;
                case 3:
                    dissocierTypeOperationMachine();
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

