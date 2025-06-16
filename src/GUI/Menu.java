
package GUI;


import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Menu extends javax.swing.JFrame {
    private final String RUTA_VIDEO_INTRO = "res\\media\\gif\\VIDEO_INTRO.gif";
    private final String AUDIO_TORMENTA_INTRO = "res\\media\\audio\\rain_and_thunder.wav";
    private final String RUTA_VIDEO_MENU = "res\\media\\gif\\video_menu.gif";
    private final String AUDIO_TORMENTA = "res\\media\\audio\\storm.wav";
    private final String AUDIO_DISPARO = "res\\media\\audio\\shoot.wav";
    private Clip audio_tormenta_intro;
    private Clip audio_tormenta;
    private Clip audio_disparo;
    private boolean video_introduccion;
    
    public Menu(String title, Boolean video_introduccion) {
        super(title);
        ImageIcon iconoApp = new ImageIcon("res\\img\\logo\\logo_3.jpg");
        this.setIconImage(iconoApp.getImage());
        this.setUndecorated(true);
        this.setResizable(false);
        initComponents();
        this.video_introduccion = video_introduccion;
        ponerPantallaCompleta();
        this.setLayout(new GridLayout());
        comprobarResoluciones();
//        addComponents(video_introduccion);
        
        
       

    }

    //Esta funcion comprueba la resolucion del monitor del usuario y dependiendo de si cumple nuestros requisitos, le mostraremos una
    //ventana informativa o no
    private void comprobarResoluciones() {
        if (this.getWidth() != 1920 && this.getHeight() != 1080 && video_introduccion) {
            
            //Si no cumple los requisitos, ocultamos todo, mostramos la ventana durante 7 segunodos, cerramos la ventana despues de 
            //la espera y volvemos a colocar los componentes
            this.setVisible(false);
            this.botonJugar.setVisible(false);
            this.botonSalir.setVisible(false);
            this.pTitulo.setVisible(false);
            
            VPal2 v2 = new VPal2("ADVERTENCIA");
            v2.setVisible(true);
            
            Timer timer = new Timer(7000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    v2.dispose();
                    Menu.this.setVisible(true);
                    Menu.this.botonJugar.setVisible(true);
                    Menu.this.botonSalir.setVisible(true);
                    Menu.this.pTitulo.setVisible(true);
                    addComponents(video_introduccion);
                    
                }
            });
            timer.setRepeats(false); //Hacemos que solo se repita una vez
            timer.start();
        }else{
            addComponents(video_introduccion);
        }
    }
    
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pTitulo = new javax.swing.JPanel();
        txtTitulo = new javax.swing.JLabel();
        botonJugar = new javax.swing.JPanel();
        txtJugar = new javax.swing.JLabel();
        botonSalir = new javax.swing.JPanel();
        txtSalir = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pTitulo.setBackground(new java.awt.Color(255, 255, 204));
        pTitulo.setLayout(new java.awt.GridLayout(1, 0));

        txtTitulo.setIcon(new javax.swing.ImageIcon("res\\img\\titulo\\titulo_1.2.png"));
        pTitulo.add(txtTitulo);

        botonJugar.setBackground(new java.awt.Color(153, 255, 153));
        botonJugar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonJugarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonJugarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonJugarMouseExited(evt);
            }
        });
        botonJugar.setLayout(new java.awt.GridLayout(1, 0));

        txtJugar.setIcon(new javax.swing.ImageIcon("res\\img\\botones\\botones_resize\\JUGAR1.png"));
        botonJugar.add(txtJugar);

        botonSalir.setBackground(new java.awt.Color(255, 153, 51));
        botonSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonSalirMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botonSalirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botonSalirMouseExited(evt);
            }
        });
        botonSalir.setLayout(new java.awt.GridLayout(1, 0));

        txtSalir.setIcon(new javax.swing.ImageIcon("res\\img\\botones\\botones_resize\\SALIR1.png"));
        botonSalir.add(txtSalir);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(201, 201, 201)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(botonSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonJugar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(174, Short.MAX_VALUE)
                .addComponent(pTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 962, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(botonJugar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(botonSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addComponent(pTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonSalirMouseEntered
        txtSalir.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/SALIR2.png"));
    }//GEN-LAST:event_botonSalirMouseEntered

    private void botonSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonSalirMouseExited
        txtSalir.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/SALIR1.png"));
    }//GEN-LAST:event_botonSalirMouseExited

    private void botonSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonSalirMouseClicked
        añadirAudio(audio_disparo,AUDIO_DISPARO,-15.0f,false);
        this.dispose();
    }//GEN-LAST:event_botonSalirMouseClicked

    private void botonJugarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonJugarMouseEntered
        txtJugar.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/JUGAR2.png"));
    }//GEN-LAST:event_botonJugarMouseEntered

    private void botonJugarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonJugarMouseExited
        txtJugar.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/JUGAR1.png"));
    }//GEN-LAST:event_botonJugarMouseExited

    private void botonJugarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonJugarMouseClicked
        // AQUI HAY QUE PONER LA VENTANA DE RUBEN
        añadirAudio(audio_disparo,AUDIO_DISPARO,-15.0f,false);
        audio_tormenta.stop();
        vistaPartida v = new vistaPartida();
        this.dispose();
    }//GEN-LAST:event_botonJugarMouseClicked

    private void ponerPantallaCompleta() {

        //Estaria bien poner una excepcion por si no se puede poner en pantalla completa
        
        //Creamos un controlador de graficos del dispositivo para que nos diga sus propiedades
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        //Pedimos al controlador que muestre la ventana en pantalla completa (this muestra que es esta pantalla)
        gd.setFullScreenWindow(this);
 
    }

    private void addComponents(Boolean video_introduccion) {

        int ancho_panel = 1000;
        int ancho = Menu.this.getWidth();
        int alto = Menu.this.getHeight();
        int x = (ancho - ancho_panel) / 2;
        int y = 0;

        //Este va a ser el panel que esta justo encima del contentPane, osea que sera el que contenga todo
        JPanel panel_fondo = new JPanel();
        panel_fondo.setBackground(Color.WHITE);
        panel_fondo.setLayout(null);
//        Menu.this.add(panel_fondo);

        
        //VIDEO_INTRO
        if(video_introduccion){
            try {
                ponerVideoIntro(panel_fondo);
            } catch (Exception ex) {
                System.out.println("Error inesperado");;
            }
            
        }
        

        this.add(pTitulo);
        this.add(botonJugar);
        this.add(botonSalir);



        
        //Este es el panel donde ira el boton de jugar y el logo del juego, esta centrado en la ventana sin importar la resolucion
        JPanel panel_centro = new JPanel();
        panel_centro.setLayout(null);
        panel_centro.setBounds(x, y, ancho_panel, alto);
        panel_centro.setBackground(Color.BLUE);
        panel_centro.setOpaque(false);  /////
        panel_fondo.add(panel_centro);

//      Añadimos el logo del titulo del juego
        pTitulo.setSize(962, 480);
        pTitulo.setBounds((panel_centro.getWidth()-pTitulo.getWidth())/2, 0, 962, 480);
        pTitulo.setOpaque(false);
        panel_centro.add(pTitulo);

//            pTitulo.setSize(900, 380);
//            pTitulo.setBounds((panel_centro.getWidth()-pTitulo.getWidth())/2, 0, 900, 380);
//            pTitulo.setOpaque(false);
//            panel_centro.add(pTitulo);

        //Añadimos el boton de jugar 
        botonJugar.setSize(341,60);
        botonJugar.setBounds((panel_centro.getWidth()-botonJugar.getWidth())/2, pTitulo.getHeight()+150, 341, 60);
        botonJugar.setOpaque(false);
        panel_centro.add(botonJugar);

        //Añadimos el boton de salir del juego
        botonSalir.setSize(341,60);
        botonSalir.setBounds((panel_centro.getWidth()-botonSalir.getWidth())/2, botonJugar.getY()+150, 341, 60);   
        botonSalir.setOpaque(false);
        panel_centro.add(botonSalir);


        //Este JLabel añade el video de fondo y el audio de la tormenta
        JLabel fondo = new JLabel();
        ImageIcon fondo_menu_gif = new ImageIcon(RUTA_VIDEO_MENU);
        fondo.setIcon(fondo_menu_gif);
        fondo.setBounds(0, 0, fondo_menu_gif.getIconWidth(), fondo_menu_gif.getIconHeight());
        panel_fondo.add(fondo);
        audio_tormenta = añadirAudio(audio_tormenta,AUDIO_TORMENTA,-15.0f, true);

        //Añadirmo el panel que contiene todo y hacemos una recarga
        Menu.this.add(panel_fondo);
        this.repaint();
        this.revalidate();


    }

    private Clip añadirAudio(Clip audio_select, String ruta_audio, Float volumen, Boolean repetir) {
        try {
            //Cargamos el archivo de audio
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(ruta_audio));

            //Obetenmos un Clip de audio
            audio_select = AudioSystem.getClip();

            //Abrimos el archivo de audio y lo cargamos en el Clip que hemos hemos arriba
            audio_select.open(audioInputStream);
            
             // Obtener el control de volumen del Clip
            FloatControl control_volumen = (FloatControl) audio_select.getControl(FloatControl.Type.MASTER_GAIN);

            // Reducir el volumen en 10 decibelios (puedes ajustar este valor según tus necesidades)
            control_volumen.setValue(volumen); // -10.0f reducirá el volumen en 10 decibelios
            
            if(!repetir){
                audio_select.loop(0);
            }else{
                //Reproducir el audio infinitamente
                audio_select.loop(Clip.LOOP_CONTINUOUSLY); 
            }
            

            // Opcionalmente, puedes hacer que el audio se repita en un bucle
            // clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar y reproducir el audio: " + ex.getMessage(), "ERROR", 0);
        }
        
        return audio_select;
        
    }

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel botonJugar;
    private javax.swing.JPanel botonSalir;
    private javax.swing.JPanel pTitulo;
    private javax.swing.JLabel txtJugar;
    private javax.swing.JLabel txtSalir;
    private javax.swing.JLabel txtTitulo;
    // End of variables declaration//GEN-END:variables

    private void ponerVideoIntro(JPanel panel_fondo) throws Exception {
        
        this.remove(botonJugar);
        this.remove(botonSalir);
        this.remove(pTitulo);
 

        try {
            generarVideo(panel_fondo);
        } catch (InterruptedException ex) {
            System.out.println("Error inesperado al generar el video");
        }
        
        
        
    }

    private void cambiarTamañoImagen(JLabel fondo) {
        //Esta funcion busca cambiar el tamaño del video de la intro tomando como parametro, dependiendo de la resolucion que tenga el usuario
        //Teniamos problemas con esto ya que si el programa se ejecutaba en pantallas con una resolucion inferior a 
        //1920x1080 ya que se cortaba la imagen, para solucionarlo, hemos tenido que crear una instancia extra llamada icon
        //en la que escalamos la imagen dependiendo de la resolucion que posea el usuario
        ImageIcon fondo_menu_gif = new ImageIcon(RUTA_VIDEO_INTRO);
        Icon icon = new ImageIcon(
                fondo_menu_gif.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT)
        );
        
        fondo.setBounds(0, 0, this.getWidth(), this.getHeight());
        fondo.setIcon(icon);
    }

    private void generarVideo(JPanel panel_fondo) throws InterruptedException {
        
        //Este JLabel añade el video de fondo y el audio de la tormenta
        JLabel fondo = new JLabel();
        
        cambiarTamañoImagen(fondo);

        panel_fondo.add(fondo);
        audio_tormenta_intro = añadirAudio(audio_tormenta_intro,AUDIO_TORMENTA_INTRO,-10.0f, false);
        
        Menu.this.add(panel_fondo);
        this.repaint();
        this.revalidate();
        
        Timer timer = new Timer(25000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audio_tormenta_intro.stop();
                panel_fondo.remove(fondo);
                Menu.this.repaint();
                Menu.this.revalidate();
            }
        });
        timer.setRepeats(false);
        timer.start();

    }

    
}
