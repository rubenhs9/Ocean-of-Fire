
package GUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class pantallaCargaInicial extends javax.swing.JFrame {

    
    public pantallaCargaInicial() {
        super("Ocean of Fire");
        ImageIcon iconoApp = new ImageIcon("res\\img\\logo\\logo_3.jpg");
        this.setIconImage(iconoApp.getImage());
        this.setUndecorated(true);
        initComponents();
        this.setSize(450,450);
        this.setLocationRelativeTo(null);

        // Crear un JPanel con BoxLayout como contenedor principal
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        contentPane.setOpaque(false);

        // Asegurarse de que 'fondo' también sea transparente
        pFondo.setOpaque(false);
        fondo.setIcon(new javax.swing.ImageIcon("res\\img\\logo\\logo_2_resize.png"));
        fondo.setOpaque(false);

        // Añadir el JPanel al JFrame
        this.setContentPane(contentPane);
        contentPane.add(pFondo);

        // Hacer que el JFrame sea "invisible"
        this.setBackground(new Color(0,0,0,0));

    

    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pFondo = new javax.swing.JPanel();
        fondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        pFondo.setLayout(new java.awt.GridLayout(1, 0));
        pFondo.add(fondo);

        getContentPane().add(pFondo);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel fondo;
    private javax.swing.JPanel pFondo;
    // End of variables declaration//GEN-END:variables
}
