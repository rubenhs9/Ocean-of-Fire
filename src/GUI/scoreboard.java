
package GUI;


import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Kelvin
 */
public final class scoreboard extends javax.swing.JFrame {    
    
    //Quien gano es la variable que nos diga quien ha ganado: 1--> Player   2->CPU
    private final String AUDIO_DERROTA = "res\\media\\audio\\AUDIO_DERROTA.wav";
    private final String AUDIO_VICTORIA = "res\\media\\audio\\AUDIO_VICTORIA.wav";
    private final String AUDIO_EMPATE = "res\\media\\audio\\AUDIO_EMPATE.wav";
    private Clip clip;
    
    public scoreboard(int quienGano,int resultados)  {
        super("Ocean of Fire");
        ImageIcon iconoApp = new ImageIcon("res\\img\\logo\\logo_3.jpg");
        this.setIconImage(iconoApp.getImage());
        this.setUndecorated(true);
        this.setResizable(false);
        initComponents();
        this.setLayout(new GridLayout());
        ponerPantallaCompleta();
        validarganador(quienGano,resultados);   // este es el metodo mas importarte ya que este metodoes el que te muestra una vista u otra 
//        BotonX();// este es simplemente el boton de la X que esta arriba a la derecha 
        botonvolverjugar(); // y este es el boton de volver a jugar 
    }

    public scoreboard() {
        ImageIcon iconoApp = new ImageIcon("res\\img\\logo\\logo_3.jpg");
        this.setIconImage(iconoApp.getImage());
        this.setUndecorated(true);
        this.setResizable(false);
        initComponents();
        this.setLayout(new GridLayout());
        ponerPantallaCompleta();
//        BotonX();// este es simplemente el boton de la X que esta arriba a la derecha 
        botonvolverjugar(); // y este es el boton de volver a jugar 
        validarganador();
       
   }


    public void ponerPantallaCompleta() {

        //Estaria bien poner una excepcion por si no se puede poner en pantalla completa
        
        //Creamos un controlador de graficos del dispositivo para que nos diga sus propiedades
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        //Pedimos al controlador que muestre la ventana en pantalla completa (this muestra que esta pantalla)
        gd.setFullScreenWindow(this);
 
    }
        
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        P_fondo = new javax.swing.JPanel();
        imgfondo = new javax.swing.JLabel();
        P_logo = new javax.swing.JPanel();
        txt_puntos = new javax.swing.JLabel();
        img_logo = new javax.swing.JLabel();
        B_volverjugar = new javax.swing.JPanel();
        txtvolverjugar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        P_fondo.setLayout(null);

        imgfondo.setBackground(new java.awt.Color(255, 255, 255));
        P_fondo.add(imgfondo);
        imgfondo.setBounds(90, 50, 220, 190);

        P_logo.setBackground(new java.awt.Color(255, 255, 255));
        P_logo.setOpaque(false);
        P_logo.setLayout(null);

        txt_puntos.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        txt_puntos.setForeground(new java.awt.Color(255, 255, 255));
        P_logo.add(txt_puntos);
        txt_puntos.setBounds(190, 400, 190, 40);

        img_logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        P_logo.add(img_logo);
        img_logo.setBounds(-50, -10, 470, 430);

        P_fondo.add(P_logo);
        P_logo.setBounds(170, 300, 470, 430);

        B_volverjugar.setOpaque(false);
        B_volverjugar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                B_volverjugarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                B_volverjugarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                B_volverjugarMouseExited(evt);
            }
        });
        B_volverjugar.setLayout(new java.awt.GridLayout(1, 0));

        txtvolverjugar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtvolverjugar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        B_volverjugar.add(txtvolverjugar);

        P_fondo.add(B_volverjugar);
        B_volverjugar.setBounds(410, 270, 150, 70);

        getContentPane().add(P_fondo);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void B_volverjugarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_B_volverjugarMouseClicked
             //aqui sera donde se llamara al metodo del jugar de la otra ventada        
            scoreboard.this.dispose();
            clip.stop();
            Menu m1 = new Menu("Ocean of Fire", false);
             
             
    }//GEN-LAST:event_B_volverjugarMouseClicked

    private void B_volverjugarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_B_volverjugarMouseEntered
       ImageIcon icono = new ImageIcon("res\\img\\botones\\botones_resize\\VOLVER_A_JUGAR_2.PNG");
       txtvolverjugar.setIcon(icono);
       B_volverjugar.repaint();
       
    }//GEN-LAST:event_B_volverjugarMouseEntered

    private void B_volverjugarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_B_volverjugarMouseExited
      ImageIcon icono = new ImageIcon("res\\img\\botones\\botones_resize\\VOLVER_A_JUGAR.PNG");
      txtvolverjugar.setIcon(icono);
      B_volverjugar.repaint();
      
    }//GEN-LAST:event_B_volverjugarMouseExited

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel B_volverjugar;
    private javax.swing.JPanel P_fondo;
    private javax.swing.JPanel P_logo;
    private javax.swing.JLabel img_logo;
    private javax.swing.JLabel imgfondo;
    private javax.swing.JLabel txt_puntos;
    private javax.swing.JLabel txtvolverjugar;
    // End of variables declaration//GEN-END:variables


    private void validarganador(int quienGano, int resultados) {
        String puntos = Integer.toString(resultados); // aqui me creo una varibale de tipo de String co los puntos del jugandor para pintarlo  
        ImageIcon imagenfondo;                                       // ya que en la imagen despues de puntos hay un JLabel encima para pintarlo 
        ImageIcon imagenlogo;
        //Icon icon;
        if(quienGano == 1 ){ // esta es simplememente la condicion que uso para pintar una u otra esto variara en funcion de lo que reciba del jugador 
            imagenfondo = new ImageIcon("res\\img\\pantalla_final\\gif_victoria3.GIF");
            imagenlogo = new ImageIcon("res\\img\\pantalla_final\\logo_victoria1 - copia.PNG");
            clip = añadirAudio(clip,AUDIO_VICTORIA,-10.0f, true);
            imgfondo.setIcon(imagenfondo); //imgfondo es el JLABEL que tiene la imagen de fondo asi que como tienen un null layout pongo el resto de elemento encima de el 
            img_logo.setIcon(imagenlogo); // como la imagenfondo(imagen del fondo ) y la imagen del logo  
            txt_puntos.setText(puntos); // luego con el String de antes escribos los puntos del usuario dependiendo de lo que escriba 
            txt_puntos.setBounds(215,405,50,50);        
        
        }else{
            if(quienGano == 2){ // luego aqui es mismo solo que arriba con las imagenes de victoria y aqui con las imagenes y el logo de derrota 

                imagenfondo = new ImageIcon("res\\img\\pantalla_final\\gif_derrota4.GIF");
                imagenlogo = new ImageIcon("res\\img\\pantalla_final\\logo_derrota.PNG");
                clip = añadirAudio(clip,AUDIO_DERROTA,-20.0f, true);
                imgfondo.setIcon(imagenfondo);
                img_logo.setIcon(imagenlogo);
                txt_puntos.setText(puntos);
                txt_puntos.setBounds(225,440,50,50);
        
            }
        }
      imagenfondo();  
      logo_victoria();
      
      
    }
    

        // este es el mismo metodo de antes solo que la vez anterior lo sobre cargue para que pinte si el jugador gano a perdio y aqui simplememto pinto el empate y no tienes que pasale nada 
        private void validarganador() {
            ImageIcon imagenfondo;
            ImageIcon imagenlogo;
            imagenfondo = new ImageIcon("res\\img\\pantalla_final\\empate.JPEG");
            imagenlogo = new ImageIcon("res\\img\\pantalla_final\\logo_empate3.PNG");
            clip = añadirAudio(clip,AUDIO_EMPATE,-10.0f, true);


            imgfondo.setIcon(imagenfondo); //imgfondo es el JLABEL que tiene la imagen de fondo asi que como tienen un null layout pongo el resto de elemento encima de el 
            img_logo.setIcon(imagenlogo);


            //aqui como cambio la pocicion del logo la modifique aqui mismo asi no toco lo que tenia antes 
            P_logo.setLocation(this.getWidth()-1350,150); // P_logo es el panel que tiene el logo de la parte de la derecha y por eso le pongo una pocicion de 0,0
            System.out.println("");
            P_logo.setSize(900,800); // luego le doy un tamaño para luego poner la imagen 
            P_logo.setBackground(Color.red);
          //  P_logo.setOpaque(true);
            img_logo.setSize(P_logo.getWidth(),P_logo.getHeight()); // luego el JLabel que tiene la imagen le doy el el tamaño del panel par que lo ocupe por completo 
             imgfondo.add(P_logo); // y por ultimos tambien agrego este panel con sus cosas dentro del JLabel que tiene la imagen de fondo.


            imagenfondo();  
         
        }

        
    
    
    
     // todos los paneles,botones y JLabel esta creando desde el source lo unico que hice con estos metodos fue darles tamaño y una posicion 
     // dentro del Jlabel del img fondo 

    private void imagenfondo() //img fondo es un JLABEL 
    { 
        imgfondo.setLocation(0,0);// este metodo independientemente de la iamgen que reciba la pone en la posicion 0,0 ed decir arriba a la izquierda 
        imgfondo.setSize(this.getWidth(),this.getHeight()); // luego hago que el JLabel tome el alto u ancho de toda la pantalla  
    }

    private void botonvolverjugar()  
    {  
        B_volverjugar.setLocation(this.getWidth()-1130,this.getHeight()-140); // aqui lo que hago es dividir el ancho de la pantalla entre 2 para que quede mas o en el medio y de alto agarre el alto de la pantalla y le reste 70 porque era mas o menos lo que tenia el boton de alto                                                                  // luego 
        B_volverjugar.setSize(326,90);
        B_volverjugar.setBackground(Color.red);
//        B_volverjugar.setOpaque(true);
        ImageIcon icono = new ImageIcon("res\\img\\botones\\botones_resize\\VOLVER_A_JUGAR.PNG");
        txtvolverjugar.setIcon(icono);
        imgfondo.add(B_volverjugar); // y luego igual el boton lo pongo encima de la imagen de fondo 
    }

    private void logo_victoria()
    {
        
        P_logo.setLocation(60, 100); // P_logo es el panel que tiene el logo de la parte de la derecha y por eso le pongo una pocicion de 0,0
        P_logo.setSize(500,550); // luego le doy un tamaño para luego poner la imagen 
        img_logo.setSize(P_logo.getWidth(),P_logo.getHeight()); // luego el JLabel que tiene la imagen le doy el el tamaño del panel par que lo ocupe por completo 
        imgfondo.add(P_logo); // y por ultimos tambien agrego este panel con sus cosas dentro del JLabel que tiene la imagen de fondo.

        
              
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
   
        
}


