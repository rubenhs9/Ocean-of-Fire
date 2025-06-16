
package data;

import GUI.Menu;
import GUI.VPal2;
import GUI.pantallaCargaInicial;
import GUI.vistaPartida;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


public class Main {

    
    public static void main(String[] args) {
        
        pantallaCargaInicial p1 = new pantallaCargaInicial();
        p1.setVisible(true);
        Timer timer = new Timer(7000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p1.dispose();
                Menu m1 = new Menu("Ocean of Fire", true);
//                 vistaPartida v1 = new vistaPartida();

           }
        });
        timer.setRepeats(false); //Hacemos que solo se repita una vez
        timer.start();
        
//       Menu m1 = new Menu("Ocean of Fire", false);
//        vistaPartida v1 = new vistaPartida();
            
            
    }
    
}

