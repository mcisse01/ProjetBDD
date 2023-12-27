
package fr.insa.gestionproduction;

import static fr.insa.gestionproduction.Machine.menuGestionMachines;
import static fr.insa.gestionproduction.Operation.menuGestionOperations;
import static fr.insa.gestionproduction.PrecedenceOperations.menuGestionPrecedenceOperations;
import static fr.insa.gestionproduction.Produit.menuGestionProduits;
import static fr.insa.gestionproduction.Realise.menuGestionRealisations;
import static fr.insa.gestionproduction.TypeOperation.menuGestionTypeOperations;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
 

public class GestionProduction {
    
    private Connection conn;

    public GestionProduction(Connection conn) {
        this.conn = conn;
    }
    
    // Méthode pour se connecter à une base de données MySQL
    public static Connection connectGeneralMySQL(String host, int port, String database, String user, String pass)
        throws SQLException {
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port
                        + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }

    // Méthode pour se connecter au serveur M3
    public static Connection connectSurServeurM3() throws SQLException {
        return connectGeneralMySQL("92.222.25.165", 3306, "m3_mcisse01", "m3_mcisse01", "2c72a1d3");
    }
    
    // Méthode principale du programme
    public static void main(String[] args) {
        try (Connection con = connectSurServeurM3()) {
            System.out.println("Connecté à la base de données."); 
            menuPrincipal(con); 
        } catch (SQLException ex) {
            throw new Error("Connexion impossible", ex); 
        }
    }
    
    // Méthode pour créer les tables et les contraintes dans la base de données
    public static void creerSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {

            // Table machine
            st.executeUpdate("CREATE TABLE IF NOT EXISTS machine ("
                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                    + "ref VARCHAR(255), "
                    + "des TEXT, "
                    + "puissance FLOAT)"
            );

            // Table typeoperation
            st.executeUpdate("CREATE TABLE IF NOT EXISTS typeoperation ("
                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                    + "des TEXT)"
            );

            // Table realise
            st.executeUpdate("CREATE TABLE IF NOT EXISTS realise ("
                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                    + "idmachine INTEGER, "
                    + "idtypeoperation INTEGER)"
            );

            // Table produit
            st.executeUpdate("CREATE TABLE IF NOT EXISTS produit ("
                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                    + "ref VARCHAR(255), "
                    + "des TEXT)"
            );

            // Table operation
            st.executeUpdate("CREATE TABLE IF NOT EXISTS operation ("
                    + "id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                    + "idtypeoperation INTEGER, "
                    + "idproduit INTEGER)"
            );

            // Table precedence
            st.executeUpdate("CREATE TABLE IF NOT EXISTS precedence ("
                    + "opavant INTEGER,"
                    + "opapres INTEGER)"
            );

            // Ajout de contraintes étrangères aux tables realise, operation, precedence
            st.executeUpdate("ALTER TABLE realise "
                    + " ADD CONSTRAINT fk_realise_idmachine "
                    + " FOREIGN KEY (idmachine) REFERENCES machine(id)"
            );
            st.executeUpdate("ALTER TABLE realise "
                    + " ADD CONSTRAINT fk_realise_idtypeoperation "
                    + " FOREIGN KEY (idtypeoperation) REFERENCES typeoperation(id)"
            );

            st.executeUpdate("ALTER TABLE operation "
                    + " ADD CONSTRAINT fk_operation_idtypeoperation "
                    + " FOREIGN KEY (idtypeoperation) REFERENCES typeoperation(id)"
            );
            st.executeUpdate("ALTER TABLE operation "
                    + " ADD CONSTRAINT fk_operation_idproduit "
                    + " FOREIGN KEY (idproduit) REFERENCES produit(id)"
            );

            st.executeUpdate("ALTER TABLE precedence "
                    + " ADD CONSTRAINT fk_precedence_opavant "
                    + " FOREIGN KEY (opavant) REFERENCES operation(id)"
            );
            st.executeUpdate("ALTER TABLE precedence "
                    + " ADD CONSTRAINT fk_precedence_opapres "
                    + " FOREIGN KEY (opapres) REFERENCES operation(id)"
            );

        System.out.println("Schéma Crée avec succès !");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la création du schéma : " + ex.getMessage());
        }
    }

    // Méthode pour supprimer les tables et les contraintes de la base de données
    public static void supprimerSchema(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            try {
                st.executeUpdate("ALTER TABLE precedence "
                        + "DROP CONSTRAINT fk_precedence_opavant");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("ALTER TABLE precedence "
                        + "DROP CONSTRAINT fk_precedence_opapres");
            } catch (SQLException ex) {
            }

            try {
                st.executeUpdate("ALTER TABLE operation "
                        + "DROP CONSTRAINT fk_operation_idtypeoperation");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("ALTER TABLE operation "
                        + "DROP CONSTRAINT fk_operation_idproduit");
            } catch (SQLException ex) {
            }

            try {
                st.executeUpdate("ALTER TABLE realise "
                        + "DROP CONSTRAINT fk_realise_idmachine");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("ALTER TABLE realise "
                        + "DROP CONSTRAINT fk_realise_idtypeoperation");
            } catch (SQLException ex) {
            }

            try {
                st.executeUpdate("DROP TABLE precedence");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("DROP TABLE operation");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("DROP TABLE produit");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("DROP TABLE realise");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("DROP TABLE typeoperation");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("DROP TABLE machine");
            } catch (SQLException ex) {
            }

            System.out.println("Schéma supprimé avec succès !");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression du schéma : " + ex.getMessage());
        }
    }

    
    // Méthode pour afficher le menu principal du programme et traiter les choix de l'utilisateur
    public static void menuPrincipal(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int choix;
        do {
            System.out.println("\nMenu Principal");
            System.out.println("==============");
            System.out.println("Que souhaitez-vous faire ? ");
            System.out.println("1. Créer le schéma de la base de données");
            System.out.println("2. Supprimer le schéma de la base de données");
            System.out.println("3. Gérer les Machines");
            System.out.println("4. Gérer les TypeOperation");
            System.out.println("5. Gérer les Association Machine-TypeOperation");
            System.out.println("6. Gérer les Produits");
            System.out.println("7. Gérer les Operations");
            System.out.println("8. Gérer les Précédences des Operations");
            System.out.println("0. Quitter");
            System.out.println("");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); 

            switch (choix) {
                case 1:
                    creerSchema(conn);
                    break;
                case 2:
                    supprimerSchema(conn);
                    break;
                case 3:
                    menuGestionMachines(conn);
                    break;
                case 4:
                    menuGestionTypeOperations(conn);
                    break;
                case 5:
                    menuGestionRealisations(conn);
                    break;
                case 6:
                    menuGestionProduits(conn);
                    break;
                case 7:
                    menuGestionOperations(conn);
                    break;
                case 8:
                    menuGestionPrecedenceOperations(conn);
                    break;
                case 0:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
                    break;
            }
        } while (choix != 0); // Répète le menu tant que l'utilisateur ne choisit pas de quitter
    }
}
