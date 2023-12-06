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
public class MachineFdB {

    private int id;
    private String ref;
    private String description;
    private int puissance;
    private int couthoraire;

    public MachineFdB(int id, String ref, String description, int puissance, int couthoraire) {
        this.id = id;
        this.ref = ref;
        this.description = description;
        this.puissance = puissance;
        this.couthoraire = couthoraire;
    }

    public MachineFdB(String ref, String description, int puissance, int couthoraire) {
        this.id = -1;
        this.ref = ref;
        this.description = description;
        this.puissance = puissance;
        this.couthoraire = couthoraire;
    }

    public static MachineFdB demande() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez la référence de la machine : ");
        String ref = scanner.nextLine();
        System.out.print("Entrez la description de la machine : ");
        String des = scanner.nextLine();
        System.out.print("Entrez la puissance de la machine : ");
        int puissance = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Entrez le cout horraire de la machine : ");
        int couthoraire = scanner.nextInt();
        scanner.nextLine();

        return new MachineFdB(ref, des, puissance, couthoraire);

    }

    public void save(Connection conn) {
        try (PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO machine (ref, des, puissance) VALUES (?, ?, ?)")) {
            pst.setString(1, ref);
            pst.setString(2, this.description);
            pst.setFloat(3, puissance);
            pst.executeUpdate();
            System.out.println("Machine ajoutée avec succès !");

        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout de la machine : " + ex.getMessage());
        }

    }

    public List<MachineFdB> listeDesMachines(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT id, ref, des, puissance FROM machine")) {
            List<MachineFdB> res = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String ref = rs.getString("ref");
                String des = rs.getString("des");
                float puissance = rs.getFloat("puissance");
                MachineFdB cur = new MachineFdB(id, ref, description, (int) puissance, couthoraire);
                res.add(cur);
            }
            return res;
        } 
    }

    public void menuGestionMachines() {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
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
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterMachine();
                    break;
                case 2:
                    modifierMachine();
                    break;
                case 3:
                    supprimerMachine();
                    break;
                case 4:
                    rechercherMachine();
                    break;
                case 5:
                    listeDesMachines();
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
