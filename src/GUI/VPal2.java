
package GUI;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class VPal2 extends javax.swing.JFrame {
    

   
    public VPal2(String title) {
        super(title);
        initComponents();
        this.setSize(400,150);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        
        //Izquierda
        JLabel j1 = new JLabel();
        j1.setText("       ");
        ImageIcon imagen = new ImageIcon("res\\img\\botones\\botones_resize\\interrogacion_resize.png");
        j1.setIcon(imagen);
        
        
        //Centro
        //Panel Principal
        JPanel p1 = new JPanel(); 
        p1.setLayout(new GridLayout(2,1));
        p1.setOpaque(false);
        p1.setBackground(Color.GREEN);
 
        //Parte de arriba
        JPanel p2 = new JPanel(); 
        p2.setLayout(new BorderLayout());
        
        JLabel margen = new JLabel();
        margen.setText("       ");
        
        JLabel margen_izq = new JLabel();
        margen_izq.setText("             ");
        
//        JLabel j2 = new JLabel();
        JTextArea j2 = new JTextArea();
        j2.setText("Tu pantalla no esta configurada en una resolucion \n"
                + "1920x1080 o con la escala al 100%, es posible que\n "
                + "algunos componentes no se muestren correctamente");
        j2.setOpaque(false);
        j2.setBackground(Color.MAGENTA);
        
     
        p2.add(margen, BorderLayout.NORTH);
        p2.add(margen_izq, BorderLayout.WEST);
        p2.add(j2, BorderLayout.SOUTH);
        
        
        //Parte de abajo
        JPanel p3 = new JPanel(); 
        p3.setLayout(new BorderLayout());
        
        JLabel margen_abajo = new JLabel();
//        margen_abajo.setText("    ");
        
        JPanel p4 = new JPanel(); 
//        p4.setLayout(new BoxLayout(p4,BoxLayout.Y_AXIS));
        p4.setLayout(new GridBagLayout());
        
        JButton j3 = new JButton();
        j3.setText("OK");
        j3.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                VPal2.this.dispose();
                
            }

            
        });
        
        p4.add(j3);
        
        
        p3.add(margen_abajo, BorderLayout.WEST);
        p3.add(p4, BorderLayout.CENTER);
        
        
        
        p1.add(p2);
        p1.add(p3);
        
        // Añadir el JLabel al centro del JFrame
        this.add(j1, BorderLayout.WEST);
        this.add(p1, BorderLayout.CENTER);
    }



    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
