

package data;

import GUI.Menu;
import GUI.vistaPartida;
import java.awt.Image;
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


public class Animaciones {
    //UAV
    private final String RUTA_VIDEO_UAV = "res\\media\\gif\\VIDEO_UAV.gif";
    private final String AUDIO_UAV = "res\\media\\audio\\AUDIO_UAV.wav";
    private Clip clip_uav;
   
    //PREDATOR
    private final String RUTA_VIDEO_PREDATOR = "res\\media\\gif\\VIDEO_PREDATOR.gif";
    private final String AUDIO_PREDATOR = "res\\media\\audio\\AUDIO_PREDATOR.wav";
    private Clip clip_predator;
    
    //PREDATOR FALLIDO
    private final String RUTA_VIDEO_PREDATOR_FALLADO = "res\\media\\gif\\VIDEO_PREDATOR_FALLIDO.gif";
    private final String AUDIO_PREDATOR_FALLADO = "res\\media\\audio\\AUDIO_PREDATOR_PREDATOR.wav";
    private Clip clip_predator_fallado;
    
    //VTOL
    private final String RUTA_VIDEO_VTOL = "res\\media\\gif\\VIDEO_VTOL.gif";
    private final String AUDIO_VTOL = "res\\media\\audio\\AUDIO_VTOL.wav";
    private Clip clip_vtol;
    
    //NUCLEAR
    private final String RUTA_VIDEO_NUCLEAR = "res\\media\\gif\\VIDEO_NUCLEAR.gif";
    private final String AUDIO_NUCLEAR = "res\\media\\audio\\AUDIO_NUCLEAR.wav";
    private Clip clip_nuclear;
    
    private int animacionSeleccionada;

    //Constructor
    public Animaciones(int num_animacion) {
        this.animacionSeleccionada = num_animacion;
    }
    
    public void generarVideo(JLabel fondo, int ancho, int alto) {
        //Este JLabel añade el video de fondo y el audio de la tormenta
        
        if (animacionSeleccionada == 1) {
//            System.out.println("patata");
            cambiarTamañoImagen(fondo , RUTA_VIDEO_UAV,ancho, alto);
            clip_uav = añadirAudio(clip_uav,AUDIO_UAV,-10.0f, false);
        }else{
            if (animacionSeleccionada == 2) {
                cambiarTamañoImagen(fondo , RUTA_VIDEO_PREDATOR,ancho, alto);
                clip_predator = añadirAudio(clip_predator,AUDIO_PREDATOR,-10.0f, false);
            }else{
                if (animacionSeleccionada == 3) {
                    cambiarTamañoImagen(fondo , RUTA_VIDEO_PREDATOR_FALLADO,ancho, alto);
                    clip_predator = añadirAudio(clip_predator,AUDIO_PREDATOR_FALLADO,-10.0f, false);
                }else{
                    if (animacionSeleccionada == 4) {
                        cambiarTamañoImagen(fondo , RUTA_VIDEO_VTOL,ancho, alto);
                        clip_predator = añadirAudio(clip_predator,AUDIO_VTOL,-10.0f, false);
                    }else{
                        if (animacionSeleccionada == 5) {
                            cambiarTamañoImagen(fondo , RUTA_VIDEO_NUCLEAR,ancho, alto);
                            clip_predator = añadirAudio(clip_predator,AUDIO_NUCLEAR,5.0f, false);
                        }
                    }
                }
            }
        }
        

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
    
    
    private void cambiarTamañoImagen(JLabel fondo, String Ruta, int ancho, int alto) {
        //Esta funcion busca cambiar el tamaño del video de la intro tomando como parametro, dependiendo de la resolucion que tenga el usuario
        //Teniamos problemas con esto ya que si el programa se ejecutaba en pantallas con una resolucion inferior a 
        //1920x1080 ya que se cortaba la imagen, para solucionarlo, hemos tenido que crear una instancia extra llamada icon
        //en la que escalamos la imagen dependiendo de la resolucion que posea el usuario
        ImageIcon fondo_menu_gif = new ImageIcon(Ruta);
        Icon icon = new ImageIcon(
                fondo_menu_gif.getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT)
        );
        
        fondo.setBounds(0, 0,ancho, alto);
        fondo.setIcon(icon);
    }
    
}
