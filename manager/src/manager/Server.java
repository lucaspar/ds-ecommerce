/* Running:
 * $ rmiregistry &
 * $ java Server &
 * $ java Client
 */

package manager;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

class Server {

    public static void main(String[] args) {

        System.setSecurityManager(new SecurityManager());

        try {
            System.out.println("Creating...");
            LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost/Manager", new Manager());
            System.out.println("Server created object");
        }
        catch (Exception e) {
            System.out.println("Could not create object.");
            e.printStackTrace();
        }
    }

}
