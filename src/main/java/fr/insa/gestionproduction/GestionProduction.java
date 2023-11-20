
package fr.insa.gestionproduction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
 
/**
 *
 * @author moussa
 */
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
        return connectGeneralMySQL("92.222.25.165", 3306,
                "m3_mcisse01", "m3_mcisse01", "2c72a1d3");
    }
    
    // Méthode principale du programme
    public static void main(String[] args) {
        try (Connection con = connectSurServeurM3()) {
            System.out.println("Connecté à la base de données."); 
            GestionProduction gestion = new GestionProduction (con); 
            gestion.menuPrincipal(); 
        } catch (SQLException ex) {
            throw new Error("Connexion impossible", ex); 
        }
    }

    // Méthode pour créer la table 'machine' dans la base de données
    public void creerSchema() {
    try (Statement st = conn.createStatement()) {
        
        // Table machine
        st.executeUpdate( "Create table if not exists machine ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "ref VARCHAR(255) NOT NULL,"
                + "des TEXT NOT NULL,"
                + "puissance FLOAT NOT NULL)"
        ); // Exécute la requête pour créer la table
        
        // Table typeoperation
        st.executeUpdate( "Create table if not exists typeoperation ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "des TEXT NOT NULL)"
        );
        
        // Table realise
        st.executeUpdate( "Create table if not exists realise ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "idmachine INT,"
                + "idtypeoperation INT,"
                + "duree INT,"
                + "UNIQUE KEY fk_machine_typeoperation (idmachine, idtypeoperation),"
                + "FOREIGN KEY (idmachine) REFERENCES machine(id),"
                + "FOREIGN KEY (idtypeoperation) REFERENCES typeoperation(id))"
        );
        
        // Table produit
        st.executeUpdate( "Create table if not exists produit ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "ref VARCHAR(255) NOT NULL,"
                + "des TEXT NOT NULL)"
        );
        
        // Table operation
        st.executeUpdate( "Create table if not exists operation ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "idtypeoperation INT,"
                + "idproduit INT,"
                + "UNIQUE KEY fk_produit_typeoperation (idproduit, idtypeoperation),"
                + "FOREIGN KEY (idtypeoperation) REFERENCES typeoperation(id),"
                + "FOREIGN KEY (idproduit) REFERENCES produit(id))"
        );

        // Table precedence
        st.executeUpdate( "Create table if not exists precedence ("
                + "opavant INT,"
                + "opapres INT,"
                + "UNIQUE KEY fk_operation (opavant, opapres),"
                + "FOREIGN KEY (opavant) REFERENCES operation(id),"
                + "FOREIGN KEY (opapres) REFERENCES operation(id))"
        );

        System.out.println("Schéma créé avec succès !");
    } catch (SQLException ex) {
        System.out.println("Erreur lors de la création du schéma : " + ex.getMessage());
    }
    }
    
    // Méthode pour supprimer le schéma complet de la base de données
    public void supprimerSchema() {
        try (Statement st = conn.createStatement()) {
            
             st.executeUpdate( "DROP TABLE IF EXISTS precedence");

             st.executeUpdate("DROP TABLE IF EXISTS operation");

             st.executeUpdate("DROP TABLE IF EXISTS produit");

             st.executeUpdate("DROP TABLE IF EXISTS realise");

             st.executeUpdate("DROP TABLE IF EXISTS typeoperation");

             st.executeUpdate("DROP TABLE IF EXISTS machine");

            System.out.println("Schéma supprimé avec succès !");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression du schéma : " + ex.getMessage());
        }
    }
    
    // Méthode pour afficher le menu principal du programme et traiter les choix de l'utilisateur
    public void menuPrincipal() {
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
                    creerSchema();
                    break;
                case 2:
                    supprimerSchema();
                    break;
                case 3:
                    Machine gestion1 = new Machine(conn);
                    gestion1.menuGestionMachines();
                    break;
                case 4:
                    TypeOperation gestion2 = new TypeOperation(conn);
                    gestion2.menuGestionTypeOperation();
                    break;
                case 5:
                    Realise gestion3 = new Realise(conn);
                    gestion3.menuGestionAssociations();
                    break;
                case 6:
                    Produit gestion4 = new Produit(conn);
                    gestion4.menuGestionProduit();
                    break;
                case 7:
                    Operation gestion5 = new Operation(conn);
                    gestion5.menuGestionOperation();
                    break;
                case 8:
                    PrecedenceOperations gestion6 = new PrecedenceOperations(conn);
                    gestion6.menuGestionPrecedenceOperations();
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