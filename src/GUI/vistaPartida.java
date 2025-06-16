package GUI;

import data.Animaciones;
import data.Barco;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class vistaPartida extends javax.swing.JFrame implements MouseListener {

    private final String AUDIO_JUEGO = "res\\media\\audio\\AUDIO_JUEGO.wav";
    private Clip clip;
    private final JPanel PanelPrincipal = new JPanel();
    private JButton[][] tableroJugador;
    private JButton[][] tableroCPU;
    private final short[][] estadoTableroCPU = new short[TAMANIO][TAMANIO];
    private final short[][] estadoTableroCPU2 = new short[TAMANIO][TAMANIO];  //Este tablero ficiticio lo usaremos para marcar rachas de bajas
    private final short[][] estadoTableroPlayer = new short[TAMANIO][TAMANIO];
    private static final short TAMANIO = 10;
    private static final short TAMANIO_BORDE = 1;
    private JPanel panelTableroCPU;
    private JPanel panelTableroJugador;
    private JPanel panelRachaBajas;
    private JPanel panelSalir;
    private JButton botonSalir;
    private JLabel labelImpactos;
    private JLabel contador;
    private final ImageIcon[] rachas_color = new ImageIcon[4];
    private final ImageIcon[] rachas_black = new ImageIcon[4];
    private JPanel panelRotar;
    private JButton botonRotar;
    private short rotacion = 0;       //Variable que nos indica la rotacion de los barcos (0 = horizontal ; 1 == vertical)
    private short barco_actual = 0;   //Variable importante que nos indica el barco que queremos colocar
    private short contadorValidarGanadorPlayer = 0;
    private short contadorValidarGanadorCPU = 0;
    private final boolean barcosColocados = false;
    Color color_anterior;             //Variable que guarda el color anterior del componente
    private Boolean ganador = false;
    private final short delay = 1500;     //Variable para el delay que tarda el bot en disparar 
    private final JButton[] tableroRachaBajas = new JButton[4];
    private int aciertosSeguidosPlayer = 0;
    private boolean rachaActiva = false;
    private boolean botonPresionado = false;    //Variable que nos controla si se ha activado una animacion dentro del hilo que hemos creado
    private short animacionSeleccionada = 0; //1->UAV   2->PREDATOR   3->PRADATOR FALLIDO   4->VTOL     5->NUCLEAR
    private boolean hiloAbierto = true;
    private final boolean WALLHACK = false;

    public vistaPartida() {
        super("Ocean of Fire");
        ImageIcon iconoApp = new ImageIcon("res\\img\\logo\\logo_3.jpg");
        this.setIconImage(iconoApp.getImage());
        this.setUndecorated(true);// Elimina la aparencia predeterminada 

        this.setResizable(false);// No deja al user cambiar el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra ventana 
        this.setLayout(null); // Posiciones 
        ponerPantallaCompleta();

        clip = añadirAudio(clip, AUDIO_JUEGO, -15.0f, true);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        PanelPrincipal.setLayout(null);
        PanelPrincipal.setBounds(0, 0, this.getWidth(), this.getHeight());
        getContentPane().add(PanelPrincipal);

        crearTableros();
        crearPanelRachaBajas();
        botonRotarPosicion();
        botonSalir();
        miinitComponents();

        this.repaint();     //Hay que añadir ambos o no se ve bien
        this.revalidate();

        comenzarHilo();

        colocarBarcosCPU();
        colocarBarcosPlayer();
//        RachaUAV(tableroRachaBajas, estadoTableroCPU,  tableroCPU);
//        RachaPREDATOR(tableroRachaBajas, estadoTableroCPU,  tableroCPU);
//        RachaCaza_combate(tableroRachaBajas, estadoTableroCPU,  tableroCPU);
//        RachaNuclear(tableroRachaBajas, estadoTableroCPU,  tableroCPU);

    }

    private void colocarBarcosPlayer() {

        colocarMouseListenerTableroPlayer2_0();
    }

    private void colocarMouseListenerTableroPlayer2_0() {

        List<JButton> botonesConMouseListener = new ArrayList<>();

        //Creamos 5 barcos que son los maximos permitidos
        Barco[] BarcosPlayer = new Barco[5];
        BarcosPlayer[0] = new Barco(3);
        BarcosPlayer[1] = new Barco(3);
        BarcosPlayer[2] = new Barco(4);
        BarcosPlayer[3] = new Barco(4);
        BarcosPlayer[4] = new Barco(5);

        //Recorremos todo el tablero del jugador y añadimos un addMouseListener para poder hacer clic y colocar los barcos
        for (int i = 0; i < tableroJugador.length; i++) {
            for (int j = 0; j < tableroJugador[i].length; j++) {
                JButton boton = tableroJugador[i][j];
                int posicionI = i;
                int posicionJ = j;
                botonesConMouseListener.add(boton);
                boton.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {

                        //Comprobamos que aun se puedan colocar barcos
                        if (barco_actual < 5) {
                            int hueco_libre = 0;    //Variable que nos va a indicar el hueco entre barcos
                            int posicion;           //Variable para cambiar la posicion de los elemenos segun su rotacion

                            //Si la rotacion es 0 (horizontal), la posicion que cambia es posicionJ que es la "x" en un eje de cordenadas
                            if (rotacion == 0) {
                                posicion = posicionJ;
                            } else {
                                posicion = posicionI;
                            }

                            //Comprobamos que no se exceda de los bordes del tablero tanto verticales como horizontales
                            if ((TAMANIO) - BarcosPlayer[barco_actual].getTam() >= posicion) {
                                //Bucle que va recorrer las casillas que ocupe el barco para ver si hay algun barco alli o no
                                //tambien se controla la rotacion. "barco_actual.getTam() == tamaño que ocupa el barco, revisar clase barco"
                                for (int j = 0; j < BarcosPlayer[barco_actual].getTam(); j++) {
                                    if (rotacion == 0) {
                                        if (estadoTableroPlayer[posicionI][posicionJ + j] != 1) {
                                            hueco_libre++;
                                        }
                                    } else {
                                        if (estadoTableroPlayer[posicionI + j][posicionJ] != 1) {
                                            hueco_libre++;
                                        }
                                    }
                                }

                                //Si el hueco libre es igual al tamaño del barco que se quiere colocar, recorremos con el bucle esas
                                //posiciones que ocupe, y dependiendo de la rotacion, pintamos de verde la posicion donde iria el barco
                                if (hueco_libre == BarcosPlayer[barco_actual].getTam()) {
                                    for (int j = 0; j < BarcosPlayer[barco_actual].getTam(); j++) {
                                        //Pintamos las siguientes posiciones en verde para ver que se puede poner ahi el barco
                                        if (rotacion == 0) {
                                            color_anterior = tableroJugador[posicionI][posicionJ + j].getBackground();
                                            tableroJugador[posicionI][posicionJ + j].setContentAreaFilled(true);
                                            tableroJugador[posicionI][posicionJ + j].setBackground(Color.GREEN);
                                        } else {
                                            color_anterior = tableroJugador[posicionI + j][posicionJ].getBackground();
                                            tableroJugador[posicionI + j][posicionJ].setContentAreaFilled(true);
                                            tableroJugador[posicionI + j][posicionJ].setBackground(Color.GREEN);
                                        }
                                    }

                                    //Si no hay hueco para colocar el barco, recorremos la posicion donde deberia ir ese barco
                                    //y la pintamos de rojo esta vez
                                } else {
                                    color_anterior = tableroJugador[posicionI][posicionJ].getBackground();
                                    for (int j = 0; j < BarcosPlayer[barco_actual].getTam(); j++) {
                                        // Pinta las siguientes posiciones en rojo si están dentro del tablero
                                        if (rotacion == 0) {
                                            if (posicionJ + j < TAMANIO) {
                                                tableroJugador[posicionI][posicionJ + j].setContentAreaFilled(true);
                                                tableroJugador[posicionI][posicionJ + j].setBackground(Color.RED);
                                            }
                                        } else {
                                            if (posicionI + j < TAMANIO) {
                                                tableroJugador[posicionI + j][posicionJ].setContentAreaFilled(true);
                                                tableroJugador[posicionI + j][posicionJ].setBackground(Color.RED);
                                            }
                                        }
                                    }
                                }
                                //Si se exceden los limites del tablero tambien hay que controlarlo, en este caso, recorremos    
                                //lo que ocupe el barco siempre y cuando sea menor al tamaño del tablero para que no exceda el
                                //rojo los bordes del tablero y controlamos la rotacion para saber si es horizontal o vertical
                            } else {
                                for (int j = 0; j < BarcosPlayer[barco_actual].getTam(); j++) {
                                    // Pinta las siguientes posiciones en rojo si están dentro del tablero
                                    if (rotacion == 0) {
                                        //Con esto controlamos que no exceda del limite del tablero
                                        if (posicionJ + j < TAMANIO) {
                                            tableroJugador[posicionI][posicionJ + j].setContentAreaFilled(true);
                                            tableroJugador[posicionI][posicionJ + j].setBackground(Color.RED);
                                        }
                                    } else {
                                        if (posicionI + j < TAMANIO) {
                                            tableroJugador[posicionI + j][posicionJ].setContentAreaFilled(true);
                                            tableroJugador[posicionI + j][posicionJ].setBackground(Color.RED);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        //Este bucle recorre todo el tablero "ficiticio", si hay un uno es que hay un barco y lo pinta de magenta
                        for (int k = 0; k < tableroJugador.length; k++) {
                            for (int l = 0; l < tableroJugador.length; l++) {
                                if (estadoTableroPlayer[k][l] == 1) {
                                    tableroJugador[k][l].setBackground(Color.MAGENTA);
                                } else {
                                    tableroJugador[k][l].setContentAreaFilled(false);
                                }
                            }
                        }

                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        //Preguntamos si el contador de barcos colocados es menor de 5 para dejarle seguir colocando barcos o no
                        if (barco_actual < 5) {
                            if (rotacion == 0) {
                                //Comprobamos que el usuario no se exceda del tamaño permitido por la izquierda
                                if ((TAMANIO) - BarcosPlayer[barco_actual].getTam() >= posicionJ) {
                                    //Recorremos dentro del tablero que ficticio que no se ve si la posicion donde se coloca el barco ya esta elegida
                                    int hueco_libre = 0;
                                    for (int j = 0; j < BarcosPlayer[barco_actual].getTam(); j++) {
                                        if (estadoTableroPlayer[posicionI][posicionJ + j] != 1) {
                                            hueco_libre++;
                                        }
                                    }

                                    if (hueco_libre == BarcosPlayer[barco_actual].getTam()) {
                                        for (int j = 0; j < BarcosPlayer[barco_actual].getTam(); j++) {
                                            //Pintamos dentro del tablero ficiticio para poder comprobar colisiones
                                            estadoTableroPlayer[posicionI][posicionJ + j] = 1;
                                            tableroJugador[posicionI][posicionJ + j].setContentAreaFilled(true);
                                            tableroJugador[posicionI][posicionJ + j].setBackground(Color.MAGENTA);
                                        }
                                        color_anterior = tableroJugador[posicionI][posicionJ].getBackground();
                                        barco_actual++;
                                        if (barco_actual == 5) {
                                            PanelPrincipal.remove(panelRotar);
                                            PanelPrincipal.repaint();
                                        }
                                    }
                                }
                            } else {
                                //Comprobamos que el usuario no se exceda del tamaño permitido por la izquierda
                                if ((TAMANIO) - BarcosPlayer[barco_actual].getTam() >= posicionI) {
                                    //Recorremos dentro del tablero que ficticio que no se ve si la posicion donde se coloca el barco ya esta elegida
                                    int hueco_libre = 0;
                                    for (int j = 0; j < BarcosPlayer[barco_actual].getTam(); j++) {
                                        if (estadoTableroPlayer[posicionI + j][posicionJ] != 1) {
                                            hueco_libre++;
                                        }
                                    }

                                    if (hueco_libre == BarcosPlayer[barco_actual].getTam()) {
                                        for (int j = 0; j < BarcosPlayer[barco_actual].getTam(); j++) {
                                            //Pintamos dentro del tablero ficiticio para poder comprobar colisiones
                                            estadoTableroPlayer[posicionI + j][posicionJ] = 1;
                                            tableroJugador[posicionI + j][posicionJ].setContentAreaFilled(true);
                                            tableroJugador[posicionI + j][posicionJ].setBackground(Color.MAGENTA);
                                        }
                                        color_anterior = tableroJugador[posicionI][posicionJ].getBackground();
                                        barco_actual++;
                                        if (barco_actual == 5) {
                                            PanelPrincipal.remove(panelRotar);
                                            PanelPrincipal.repaint();
                                        }
                                    }
                                }
                            }

                        }

                        if (barco_actual >= 5) {
                            borrarListenerBotones();
                            gestionarTurnos();
                        }

                    }

                    private void borrarListenerBotones() {
                        for (JButton boton : botonesConMouseListener) {
                            for (MouseListener listener : boton.getMouseListeners()) {
                                boton.removeMouseListener(listener);
                            }
                        }
                    }

                });

            }
        }
    }

    private void colocarBarcosCPU() {
        //Este metodo busca colocar los barcos de la CPU de forma aleatoria e independiente
        //Creamos 5 barcos que son los maximos permitidos
        Barco[] BarcosCPU = new Barco[5];
        BarcosCPU[0] = new Barco(3);
        BarcosCPU[1] = new Barco(3);
        BarcosCPU[2] = new Barco(4);
        BarcosCPU[3] = new Barco(4);
        BarcosCPU[4] = new Barco(5);

        //Bucle que recorrera todos los barcos de la CPU
        for (int i = 0; i < BarcosCPU.length; i++) {
            //Esta variable cambiara dependiendo de la rotacion del barco (horizontal o vertical)
            Random rotacion_random = new Random();
            int rotacion = rotacion_random.nextInt(2);

            //Creamos dos random para la posicion I,J dentro del tablero
            Random posicionI_random = new Random();
            Random posicionJ_random = new Random();

            //Rotacion 0 = Horizontal <--> Rotacion 1 = Vertical
//            rotacion = 0;
            if (rotacion == 0) {
                Boolean comprobacion = false;

                do {

                    int posicionI = posicionI_random.nextInt(TAMANIO);
                    int posicionJ = posicionJ_random.nextInt(TAMANIO);
                    //Variable que comprueba si hay hueco para colocar el barco
                    int hueco_libre = 0;

                    //Comprobamos que el barco no se exceda de los limites del tablero (en este caso, los de arriba)
                    if ((TAMANIO) - BarcosCPU[i].getTam() >= posicionI) {
                        //Recorremos dentro del tablero que ficticio que no se ve si la posicion donde se coloca el barco ya esta elegida
                        for (int j = 0; j < BarcosCPU[i].getTam(); j++) {
                            if (estadoTableroCPU[posicionJ][posicionI + j] != 1) {
                                hueco_libre++;
                            }
                        }
                        //Pregunta que comprueba si hay hueco para colocar el barco
                        if (hueco_libre == BarcosCPU[i].getTam()) {
                            //Recorremos las posiciones que mide el barco para pintar dentro de un tablero simbolico donde estan todos los datos
                            for (int j = 0; j < BarcosCPU[i].getTam(); j++) {
                                estadoTableroCPU[posicionJ][posicionI + j] = 1;
                                if (WALLHACK) {
                                    tableroCPU[posicionJ][posicionI + j].setContentAreaFilled(true);
                                    tableroCPU[posicionJ][posicionI + j].setBackground(Color.MAGENTA);
                                }

                            }
                            this.repaint();     //Hay que añadir ambos o no se ve bien
                            this.revalidate();
                            comprobacion = true;
                        }
                    }

                } while (!comprobacion);

            } else {
                Boolean comprobacion = false;

                do {
                    int posicionI = posicionI_random.nextInt(TAMANIO);
                    int posicionJ = posicionJ_random.nextInt(TAMANIO);
                    //Variable que comprueba si hay hueco para colocar el barco
                    int hueco_libre = 0;

                    //Comprobamos que el barco no se exceda de los limites del tablero (en este caso, los de arriba)
                    if ((TAMANIO) - BarcosCPU[i].getTam() >= posicionJ) {
                        //Recorremos dentro del tablero que ficticio que no se ve si la posicion donde se coloca el barco ya esta elegida
                        for (int j = 0; j < BarcosCPU[i].getTam(); j++) {
                            if (estadoTableroCPU[posicionJ + j][posicionI] != 1) {
                                hueco_libre++;
                            }
                        }
                        //Pregunta que comprueba si hay hueco para colocar el barco
                        if (hueco_libre == BarcosCPU[i].getTam()) {
                            //Recorremos las posiciones que mide el barco para pintar dentro de un tablero simbolico donde estan todos los datos
                            for (int j = 0; j < BarcosCPU[i].getTam(); j++) {
                                estadoTableroCPU[posicionJ + j][posicionI] = 1;
                                if (WALLHACK) {
                                    tableroCPU[posicionJ + j][posicionI].setContentAreaFilled(true);
                                    tableroCPU[posicionJ + j][posicionI].setBackground(Color.MAGENTA);
                                }

                            }
                            this.repaint();     //Hay que añadir ambos o no se ve bien
                            this.revalidate();
                            comprobacion = true;
                        }
                    }

                } while (!comprobacion);

            }
        }
        //Aqui inicializamos todo el segundo tablero ficiticio de la CPU que mas adelante usaremos con las rachas de bajas
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                estadoTableroCPU2[i][j] = 0;
            }
        }
    }

    private void crearTableros() {
        // Creacion de tableros
        tableroCPU = new JButton[TAMANIO][TAMANIO];// Creo el tablero de botones  
        tableroJugador = new JButton[TAMANIO][TAMANIO];// Creo el tablero de botones
        inicializarTablero(tableroCPU); // Creo e inicializo el boton con color de fondo y borde.
        inicializarTablero(tableroJugador);//Creo e inicializo el boton con color de fondo y borde.

        // Añado los tableros al panel principal.
        panelTableroCPU = new JPanel(new GridLayout(TAMANIO, TAMANIO, TAMANIO_BORDE, TAMANIO_BORDE)); // Creo el contenedor o el panel donde se va a almacenar los botones
        // le creo con GridLayout personalizado,
        // dandole el tamaño de 10x10 y su tamaño de borde
        // para que las dimensiones sean las mismas que las del tablero
        panelTableroJugador = new JPanel(new GridLayout(TAMANIO, TAMANIO, TAMANIO_BORDE, TAMANIO_BORDE));
        panelTableroCPU.setBounds(70, 200, 650, 650);// Le doy posicion al contenedor
        panelTableroJugador.setBounds(775, 200, 650, 650);
        panelTableroJugador.setOpaque(false);
        panelTableroCPU.setOpaque(false);

        for (int i = 0; i < tableroJugador.length; i++) {
            for (int j = 0; j < tableroJugador[i].length; j++) {

                tableroCPU[i][j].addMouseListener(this); // Agregar listener al botón del tableroCPU
                panelTableroCPU.add(tableroCPU[i][j]); // Asignar a cada contenedor el botón
                tableroJugador[i][j].addMouseListener(this); // Agregar listener al botón del tableroJugador
                panelTableroJugador.add(tableroJugador[i][j]);
            }
        }

        PanelPrincipal.add(panelTableroCPU); // Añado el panel o contenedor final
        PanelPrincipal.add(panelTableroJugador);

    }

    private void crearPanelRachaBajas() {
//        JButton[] tableroRachaBajas = new JButton[4]; // Creo los botones del tablero de racha de bajas
        inicializarRachaBajas(tableroRachaBajas);
        panelRachaBajas = new JPanel(new GridLayout(4, 1)); // Creo el contenedor o el panel y le doy un Layout de 1 columna y 4 filas
        panelRachaBajas.setBounds(1475, 200, 400, 650);// Posiciono el panel 

        for (int i = 0; i < tableroRachaBajas.length; i++) {
            tableroRachaBajas[i].addMouseListener(this);
            panelRachaBajas.add(tableroRachaBajas[i]);// Asigno los cuatros 
        }

        PanelPrincipal.add(panelRachaBajas);

    }

    private void inicializarTablero(JButton[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                tablero[i][j] = new JButton(); // Creo el JButton del tablero
//                    tablero[i][j].setBackground(Color.BLUE);// Le doy color de fondo
                tablero[i][j].setContentAreaFilled(false);
                tablero[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));// Le doy color de borde   
            }
        }
    }

    private void inicializarRachaBajas(JButton[] panelRachaBajas) {

        rachas_color[0] = new ImageIcon("res\\img\\Rachas de bajas\\resize\\UAV_resize.jpg");
        rachas_color[1] = new ImageIcon("res\\img\\Rachas de bajas\\resize\\PREDATOR_resize.jpg");
        rachas_color[2] = new ImageIcon("res\\img\\Rachas de bajas\\resize\\VTOL_resize.jpg");
        rachas_color[3] = new ImageIcon("res\\img\\Rachas de bajas\\resize\\Nuclear_resize.jpg");

        rachas_black[0] = new ImageIcon("res\\img\\Rachas de bajas\\resize\\UAV_resize_black.png");
        rachas_black[1] = new ImageIcon("res\\img\\Rachas de bajas\\resize\\PREDATOR_resize_black.png");
        rachas_black[2] = new ImageIcon("res\\img\\Rachas de bajas\\resize\\VTOL_resize_black.png");
        rachas_black[3] = new ImageIcon("res\\img\\Rachas de bajas\\resize\\Nuclear_resize_black.png");

        for (int i = 0; i < panelRachaBajas.length; i++) {
            panelRachaBajas[i] = new JButton();// Creo el JButton del panel de rachas
            panelRachaBajas[i].setBackground(Color.BLUE);// Le doy color de fondo
            panelRachaBajas[i].setIcon(rachas_black[i]);
//             panelRachaBajas[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));// Le doy color de borde      
        }
    }

    private void ponerPantallaCompleta() {
        //Aqui hay 2 metodos para hacerlo
        //1. 
        //Creamos un controlador de graficos del dispositivo para que nos diga sus propiedades
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        //Pedimos al controlador que muestre la ventana en pantalla completa (this muestra que esta pantalla)
        gd.setFullScreenWindow(this);

        //Aplicamos un doble buffer para evitar parpadeos y lag, es fundamental estas dos lineas
        this.createBufferStrategy(2);
        BufferStrategy bufferStrategy = this.getBufferStrategy();

        //2.
        //Mediante la clase abstracta Toolkit podemos sacar la resolucion de nuestra pantalla y de esa forma 
        //poner la pantalla en modo pantalla completa
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        setSize(screenSize.width, screenSize.height);
//        this.setVisible(true);
    }

    private void botonRotarPosicion() {
        panelRotar = new JPanel(new GridLayout());
        botonRotar = new javax.swing.JButton();// Creo un nuevo boton
        panelRotar.setOpaque(false);    //Volvemos transaprarente el panel
        botonRotar.setContentAreaFilled(false);     //Quitamos la opcaciodad del boton
        botonRotar.setBorderPainted(false);     //Quitamos el borde del boton
        botonRotar.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/ROTAR1.png"));
//        botonRotar.setBackground(Color.PINK);// Le doy color
        panelRotar.setBounds(937, 900, 341, 64);// Le doy posicion
        panelRotar.add(botonRotar);
        botonRotar.addMouseListener(this);
        PanelPrincipal.add(panelRotar);// Lo añado al contenedor
    }

    private void botonSalir() {
        panelSalir = new JPanel();
        panelSalir.setBounds(1510, 900, 341, 64);
        panelSalir.setOpaque(false);
        botonSalir = new javax.swing.JButton();
        botonSalir.setContentAreaFilled(false);     //Quitamos la opcaciodad del boton
        botonSalir.setBorderPainted(false);     //Quitamos el borde del boton
        panelSalir.setLayout(new java.awt.GridLayout());
        botonSalir.setFocusable(false);     //Le quitamos el foco para que no se vea el borde

        //Colocar el marcador de impactos
        contador = new JLabel();
        labelImpactos = new javax.swing.JLabel();
//        labelImpactos.setOpaque(true);
        labelImpactos.setBackground(Color.BLUE);
        contador.setHorizontalAlignment(SwingConstants.CENTER);
        labelImpactos.setHorizontalAlignment(SwingConstants.CENTER);
        contador.setFont(new java.awt.Font("Unispace", 1, 30));
        labelImpactos.setFont(new java.awt.Font("Unispace", 1, 70));
        contador.setForeground(Color.WHITE);
        labelImpactos.setForeground(Color.WHITE);
        contador.setText("Contador de impactos");
        labelImpactos.setText("0");
        contador.setBounds(1470, 40, 400, 80);
        labelImpactos.setBounds(1630, 100, 100, 80);
        PanelPrincipal.add(contador);
        PanelPrincipal.add(labelImpactos);

        botonSalir.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/SALIR1.png"));
        panelSalir.add(botonSalir);
        botonSalir.addMouseListener(this);
        PanelPrincipal.add(panelSalir);

    }

    private void gestionarTurnos() {

//        do {
        disparoPlayer();
//        }
//        while (contadorValidarGanadorPlayer < 19 && contadorValidarGanadorCPU < 19 
//               && contadorValidarGanadorPlayer >= 0 &&contadorValidarGanadorCPU >= 0);
//        while(ganador == false);

    }

    private void comprobarGanador() {
        //En este metodo validamos si ha habido un ganador o no y quien ha sido
        if (contadorValidarGanadorPlayer == 19 || contadorValidarGanadorCPU == 19) {
            if (contadorValidarGanadorPlayer == 19) {
                System.out.println("GANASTE");
                clip.close();
                scoreboard v2 = new scoreboard(1, contadorValidarGanadorPlayer);
                vistaPartida.this.dispose();
                hiloAbierto = false;
                ganador = true;

            } else {
                if (contadorValidarGanadorCPU == 19) {
                    System.out.println("PERDISTE");
                    clip.close();
                    scoreboard v2 = new scoreboard(2, contadorValidarGanadorPlayer);
                    vistaPartida.this.dispose();
                    hiloAbierto = false;
                    ganador = true;
                }
            }

        }

    }

    private void disparoCPU() {
        //Creamos un temporizador para que el bot se espere el "delay" que queramos antes de disparar
//        Timer timer = new Timer(delay, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
        Boolean comprobacion = false;
        do {
            Random rand = new Random();
            int fila = rand.nextInt(TAMANIO);
            int columna = rand.nextInt(TAMANIO);
            int posicion = estadoTableroPlayer[fila][columna];

            if (posicion == 2 || posicion == 3) {
                //Aplicamos recursividad por si el bot ha disparado en un sitio en el que ya lo habia hecho
                //Además,ponemos comprobacion a true para que cuando vuelva de la recursividad, pueda salir del Do While

                //Version final: Hemos eliminado la recursividad para evitar problemas :(
//                disparoCPU();
                comprobacion = false;
            } else {
                //Comprobamos si ha acertado o no y pintamos el boton dependiendo de lo que haya hecho
                if (posicion == 1) {
                    comprobacion = true;
                    estadoTableroPlayer[fila][columna] = 3;
                    tableroJugador[fila][columna].setContentAreaFilled(true);
                    tableroJugador[fila][columna].setBackground(Color.RED);
                    contadorValidarGanadorCPU++;
                    //                    System.out.println("Contador CPU: "+contadorValidarGanadorCPU);
                } else {
                    comprobacion = true;
                    estadoTableroPlayer[fila][columna] = 2;
                    tableroJugador[fila][columna].setContentAreaFilled(true);
                    tableroJugador[fila][columna].setBackground(Color.BLUE);
                }
            }
        } while (!comprobacion);

        comprobacion = false;
        if (contadorValidarGanadorCPU == 19) {
            comprobarGanador();
            return;
        }

//        });
//        timer.setRepeats(false);        //Ponme que el timer solo se repita una vez
//        timer.start();                      //Empezamos el timer
    }

    private void disparoPlayer() {
        //Creamos una lista de botones donde guardaremos los eventos de cada boton para que asi mas tarde, podamos eliminarlos
        //y asi impedir que el user siga haciendo clic
        List<JButton> botonesConMouseListener = new ArrayList<>();
        for (int i = 0; i < tableroCPU.length; i++) {
            for (int j = 0; j < tableroCPU[i].length; j++) {
                //Comprobamos si en esos botones ya habia un disparo efectuado con anterioridad para no colocar ese evento de nuevo
                if (estadoTableroCPU2[i][j] == 1 || estadoTableroCPU2[i][j] == 2 || estadoTableroCPU2[i][j] == 3) {
                    tableroCPU[i][j].removeMouseListener(this);
                } else {
                    JButton boton = tableroCPU[i][j];
                    int columna = j;
                    int fila = i;
                    botonesConMouseListener.add(boton);     //Guardamos el boton en la lista
                    boton.addMouseListener(new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {

                            if (estadoTableroCPU[fila][columna] == 1) {
                                tableroCPU[fila][columna].setOpaque(true);
                                tableroCPU[fila][columna].setContentAreaFilled(true);
                                tableroCPU[fila][columna].setBackground(Color.RED);
                                tableroCPU[fila][columna].removeMouseListener(this);
                                estadoTableroCPU2[fila][columna] = 1;
                                contadorValidarGanadorPlayer++;
                                aciertosSeguidosPlayer++;

                            } else {
                                tableroCPU[fila][columna].setContentAreaFilled(true);
                                tableroCPU[fila][columna].setBackground(Color.BLUE);
                                tableroCPU[fila][columna].removeMouseListener(this);
                                estadoTableroCPU2[fila][columna] = 2;
                                if (!rachaActiva) {
                                    aciertosSeguidosPlayer = 0;
                                }
                                rachaActiva = false;
                            }

                            comprobarRachas();
                            comprobarGanador();
                            if (ganador == true) {
                                borrarListenerBotones();
                                return;
                            } else {
                                disparoCPU();

                            }

                        }

                        //Borramos todos los mouseListener independientemente de cada boton
                        private void borrarListenerBotones() {
                            for (JButton boton : botonesConMouseListener) {
                                for (MouseListener listener : boton.getMouseListeners()) {
                                    boton.removeMouseListener(listener);
                                }
                            }
                        }

                    });
                }

            }
        }
    }

    private void comprobarRachas() {
//        System.out.println("" + aciertosSeguidosPlayer);
        labelImpactos.setText("" + aciertosSeguidosPlayer);
//          labelImpactos.setText("" + contadorValidarGanadorPlayer);

        labelImpactos.repaint();
        if (aciertosSeguidosPlayer >= 9) {
            tableroRachaBajas[3].setIcon(rachas_color[3]);
            RachaNuclear(tableroRachaBajas, estadoTableroCPU, tableroCPU);
        } else {
            if (aciertosSeguidosPlayer >= 7) {
                tableroRachaBajas[2].setIcon(rachas_color[2]);
                RachaCaza_combate(tableroRachaBajas, estadoTableroCPU, tableroCPU);
            } else {
                if (aciertosSeguidosPlayer >= 5) {
                    tableroRachaBajas[1].setIcon(rachas_color[1]);
                    RachaPREDATOR(tableroRachaBajas, estadoTableroCPU, tableroCPU);
                } else {
                    if (aciertosSeguidosPlayer >= 3) {
                        tableroRachaBajas[0].setIcon(rachas_color[0]);
                        RachaUAV(tableroRachaBajas, estadoTableroCPU, tableroCPU);
                    } else {
                        for (int i = 0; i < tableroRachaBajas.length; i++) {
                            tableroRachaBajas[i].setIcon(rachas_black[i]);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Obtener el botón sobre el que se encuentra el ratón
        JButton boton = (JButton) e.getSource();
        // Cambiar el color del botón cuando el ratón entra

//        if (boton.getParent() == panelTableroCPU) {
//            boton.setBackground(Color.RED); // Cambiar a color verde si el botón está en el tablero CPU
//        } 
//        if(boton.getParent() == panelTableroJugador) {
//            boton.setBackground(Color.YELLOW); // Cambiar a color azul si el botón no está en el tablero CPU
//        }
        if (boton.getParent() == panelRachaBajas) {
            boton.setBackground(Color.CYAN); // Cambiar a color azul si el botón no está en el tablero CPU
        }
        if (boton.getParent() == panelRotar) {
            botonRotar.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/ROTAR2.png"));
        }
        if (boton.getParent() == panelSalir) {
            botonSalir.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/SALIR2.png"));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Obtener el botón sobre el que estaba el ratón
        JButton boton = (JButton) e.getSource();
        // Cambiar el color del botón cuando el ratón sale

//        if (boton.getParent() == panelTableroCPU) {
//            boton.setBackground(Color.BLUE); // Cambiar a color verde si el botón está en el tablero CPU
//        } 
//        if(boton.getParent() == panelTableroJugador) {
//            boton.setBackground(Color.BLUE); // Cambiar a color azul si el botón no está en el tablero CPU
//        }
        if (boton.getParent() == panelRachaBajas) {
            boton.setBackground(Color.BLUE); // Cambiar a color azul si el botón no está en el tablero CPU
        }
        if (boton.getParent() == panelRotar) {
            botonRotar.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/ROTAR1.png"));
        }
        if (boton.getParent() == panelSalir) {
            botonSalir.setIcon(new javax.swing.ImageIcon("res/img/botones/botones_resize/SALIR1.png"));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        JButton boton = (JButton) e.getSource();
        if (boton.getParent() == panelRotar) {
            if (rotacion == 0) {
                rotacion = 1;
            } else {
                rotacion = 0;
            }
        }

        if (boton.getParent() == panelSalir) {
            clip.close();
            scoreboard v1 = new scoreboard();
            vistaPartida.this.dispose();
            hiloAbierto = false;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelFondo.setIcon(new javax.swing.ImageIcon("res/media/gif/barco.jpg"));
        labelFondo.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelFondo)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelFondo)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labelFondo;
    // End of variables declaration//GEN-END:variables

    private void RachaUAV(JButton[] tableroRachaBajas, short[][] estadoTableroCPU1, JButton[][] tableroCPU) {
        tableroRachaBajas[0].addActionListener(new ActionListener() { // le agregamos al primer boton un evento de escucha para cuando se haga clik en el 
            int rachaUAV = 3; // creamos una variable racha puntos para saber ocupara, en este caso sera de 3x3 

            @Override
            public void actionPerformed(ActionEvent e) { // dentro del evento de boton creamos otro para los eventos del tablero  

                if (aciertosSeguidosPlayer >= 3) {
                    List<JButton> botonesConMouseListener = new ArrayList<>();

                    for (int i = 0; i < tableroCPU.length; i++) { // y con este bucle recorremos todo el array del tablero de la CPU
                        for (int j = 0; j < tableroCPU.length; j++) {
                            JButton boton = tableroCPU[i][j];
                            botonesConMouseListener.add(boton);
                            tableroCPU[i][j].addMouseListener(new MouseAdapter() { // y aqui le agregamos a todos los botones de tablero de la CPU los eventos uno a uno 

                                @Override
                                public void mouseEntered(MouseEvent e) {
                                    JButton botonClicado = (JButton) e.getSource(); // creamos un boton nuevo para en este guardar la informacion del boton que se hace clik o entra el raton 
                                    int fila = -1, columna = -1; // luego creamos fila-colunma y en esta ponemos valores menores a 0 ya que en esta se guardara la pocicion exacta de boton 

                                    // Buscar la posición del botón en tableroCPU[][]
                                    for (int i = 0; i < tableroCPU.length; i++) {
                                        for (int j = 0; j < tableroCPU[i].length; j++) {
                                            if (tableroCPU[i][j] == botonClicado) { // aqui comparamos la iformacion de todos los botones de tablero la inforrmacion del boton que recibe el evento 
                                                fila = i;      //luego guardamos aqui su posicion 
                                                columna = j;   //luego guardamos aqui su posicion 

                                                break; // y en cuanto lo encontramos paramos el bucle 
                                            }

                                        }

                                    }

                                    // Pintar los botones a partir de las coordenadas del botón en el que se hizo clic
                                    for (int k = fila; k < fila + rachaUAV && k < tableroCPU.length; k++) {  //como ya tenemos la posicion del boton empezamos el bucles desde esa posicion fila columna hasta 
                                        for (int l = columna; l < columna + rachaUAV && l < tableroCPU[k].length; l++) { // y luego decimos que tienes que llegar hasta fila,columa mas la variable de puntos y que no se  
                                            // pasar del largo del tablero 
                                            tableroCPU[k][l].setOpaque(true);  //luego hacmos bisible los botones 
                                            tableroCPU[k][l].setBackground(Color.ORANGE); // los pintamos de rojos
                                            tableroCPU[k][l].repaint();
                                            tableroCPU[k][l].revalidate();

                                        }
                                    }
                                }//llave del mouse entered  

                                public void mouseExited(MouseEvent e) {
                                    for (int k = 0; k < tableroCPU.length; k++) {
                                        for (int l = 0; l < tableroCPU.length; l++) {
                                            if (estadoTableroCPU2[k][l] == 1) {
                                                tableroCPU[k][l].setOpaque(true);
                                                tableroCPU[k][l].setBackground(Color.RED);
                                                tableroCPU[k][l].repaint();
                                                tableroCPU[k][l].revalidate();
                                            } else {
                                                if (estadoTableroCPU2[k][l] == 2) {
                                                    tableroCPU[k][l].setOpaque(true);
                                                    tableroCPU[k][l].setBackground(Color.BLUE);
                                                    tableroCPU[k][l].repaint();
                                                    tableroCPU[k][l].revalidate();
                                                } else {
                                                    if (estadoTableroCPU2[k][l] == 3) { // aqui valido si hay  un uno o no cuando sale el raron para que mantenga el color magenta al salir 
                                                        tableroCPU[k][l].setOpaque(true);
                                                        tableroCPU[k][l].setBackground(Color.red);
                                                        tableroCPU[k][l].repaint();
                                                        tableroCPU[k][l].revalidate();

                                                    } else {

                                                        tableroCPU[k][l].setOpaque(false); // y si no los vuelvo hago que se queden ocutos 
                                                        tableroCPU[k][l].repaint();
                                                        tableroCPU[k][l].revalidate();

                                                    }
                                                }
                                            }

                                        }
                                    }

                                }

                                public void mouseClicked(MouseEvent e) { // esto aqui es lo mismo lo unico que cambia es el for 
                                    animacionSeleccionada = 1;
                                    aciertosSeguidosPlayer = 0;
                                    botonPresionado = true;
                                    panelRachaBajas.setVisible(false);
                                    panelTableroCPU.setVisible(false);
                                    panelTableroJugador.setVisible(false);
                                    panelRotar.setVisible(false);
                                    panelSalir.setVisible(false);
                                    contador.setVisible(false);
                                    labelImpactos.setVisible(false);
                                    clip.stop();

                                    Timer timer = new Timer(14000, new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            panelRachaBajas.setVisible(true);
                                            panelTableroCPU.setVisible(true);
                                            panelTableroJugador.setVisible(true);
                                            panelRotar.setVisible(true);
                                            panelSalir.setVisible(true);
                                            contador.setVisible(true);
                                            labelImpactos.setVisible(true);
                                            labelFondo.setIcon(new javax.swing.ImageIcon("res/media/gif/barco.gif"));
                                            clip.start();
                                            clip.loop(Clip.LOOP_CONTINUOUSLY);
                                        }
                                    });
                                    timer.setRepeats(false);
                                    timer.start();

                                    JButton botonClicado = (JButton) e.getSource(); // Obtener el botón en el que se hizo clic
                                    int fila = -1, columna = -1;

                                    // Buscar la posición del botón en tableroCPU[][]
                                    for (int i = 0; i < tableroCPU.length; i++) {
                                        for (int j = 0; j < tableroCPU[i].length; j++) {
                                            if (tableroCPU[i][j] == botonClicado) {
                                                fila = i;
                                                columna = j;
                                                break;
                                            }

                                        }

                                    }
                                    // Pintar los botones a partir de las coordenadas del botón en el que se hizo clic
                                    for (int k = fila; k < fila + rachaUAV && k < tableroCPU.length; k++) {
                                        for (int l = columna; l < columna + rachaUAV && l < tableroCPU[k].length; l++) {

                                            if (estadoTableroCPU2[k][l] == 2) {
                                                tableroCPU[k][l].setOpaque(true);
                                                tableroCPU[k][l].setBackground(Color.BLUE);
                                                tableroCPU[k][l].repaint();
                                                tableroCPU[k][l].revalidate();
                                            } else {
                                                if (estadoTableroCPU1[k][l] == 1) { // aqui hago lo mismo que en el ejercicio anterio solo que esta vez si encuentra algo lo pinta de azul 
                                                    if (k == fila && l == columna) {
                                                        tableroCPU[k][l].setBackground(Color.RED);
                                                    } else {
                                                        tableroCPU[k][l].setBackground(Color.MAGENTA);
                                                        tableroCPU[k][l].repaint();
                                                        tableroCPU[k][l].revalidate();
                                                    }

                                                } else {
                                                    tableroCPU[k][l].setOpaque(false);
                                                    tableroCPU[k][l].repaint();
                                                    tableroCPU[k][l].revalidate();
                                                }
                                            }
                                        }
                                    }
                                    borrarBotones();
                                    disparoPlayer();
                                }////fin mouseclik

                                private void borrarBotones() {
                                    for (JButton boton : botonesConMouseListener) {
                                        for (MouseListener listener : boton.getMouseListeners()) {
                                            boton.removeMouseListener(listener);
                                        }
                                    }
                                }

                            }); //fin de los evento de los botones 

                        }
                    }

                }

            } //llave   

        });// llave del fin del evento de racha de puntos 

    }//llaves del metodo     

    private void RachaPREDATOR(JButton[] tableroRachaBajas, short[][] estadoTableroCPU, JButton[][] tableroCPU) {
        tableroRachaBajas[1].addActionListener(new ActionListener() {
            int rachaPREDATOR = 2;
            boolean impacto = false;    //variable que nos va a decir si a habido algun impacto para para sacar una animacion u otra
            int delay = 0;

            @Override
            public void actionPerformed(ActionEvent e) {

                List<JButton> botonesConMouseListener = new ArrayList<>();
                for (int i = 0; i < tableroCPU.length; i++) {
                    for (int j = 0; j < tableroCPU.length; j++) {
                        botonesConMouseListener.add(tableroCPU[i][j]);

                        tableroCPU[i][j].addMouseListener(new MouseAdapter() {

                            @Override
                            public void mouseEntered(MouseEvent e) {
                                JButton botonClicado = (JButton) e.getSource(); // Obtener el botón en el que se hizo clic
                                int fila = -1, columna = -1;

                                // Buscar la posición del botón en tableroCPU[][]
                                for (int i = 0; i < tableroCPU.length; i++) {
                                    for (int j = 0; j < tableroCPU[i].length; j++) {
                                        if (tableroCPU[i][j] == botonClicado) {
                                            fila = i;
                                            columna = j;
                                            break;
                                        }

                                    }

                                }

                                // Verificar si se encontró la posición del botón
                                // Pintar los botones a partir de las coordenadas del botón en el que se hizo clic
                                for (int k = fila; k < fila + rachaPREDATOR && k < tableroCPU.length; k++) {
                                    for (int l = columna; l < columna + rachaPREDATOR && l < tableroCPU[k].length; l++) {
                                        tableroCPU[k][l].setOpaque(true);
                                        tableroCPU[k][l].setBackground(Color.ORANGE);
                                        tableroCPU[k][l].repaint();
                                        tableroCPU[k][l].revalidate();

                                    }
                                }

                            }

                            // es excatamente lo mismo que el metodo anterior 
                            public void mouseExited(MouseEvent e) {
                                for (int k = 0; k < tableroCPU.length; k++) {
                                    for (int l = 0; l < tableroCPU.length; l++) {
                                        if (estadoTableroCPU2[k][l] == 1) {
                                            tableroCPU[k][l].setOpaque(true);
                                            tableroCPU[k][l].setBackground(Color.RED);
                                            tableroCPU[k][l].repaint();
                                            tableroCPU[k][l].revalidate();
                                        } else {
                                            if (estadoTableroCPU2[k][l] == 2) {
                                                tableroCPU[k][l].setOpaque(true);
                                                tableroCPU[k][l].setBackground(Color.BLUE);
                                                tableroCPU[k][l].repaint();
                                                tableroCPU[k][l].revalidate();
                                            } else {
                                                if (estadoTableroCPU2[k][l] == 3) { // aqui valido si hay  un uno o no cuando sale el raron para que mantenga el color magenta al salir 
                                                    tableroCPU[k][l].setOpaque(true);
                                                    tableroCPU[k][l].setBackground(Color.red);
                                                    tableroCPU[k][l].repaint();
                                                    tableroCPU[k][l].revalidate();

                                                } else {
                                                    tableroCPU[k][l].setOpaque(false); // y si no los vuelvo hago que se queden ocutos 
                                                    tableroCPU[k][l].repaint();
                                                    tableroCPU[k][l].revalidate();

                                                }
                                            }
                                        }

                                    }
                                }

                            }

                            public void mouseClicked(MouseEvent e) {
                                JButton botonClicado = (JButton) e.getSource(); // Obtener el botón en el que se hizo clic
                                int fila = -1, columna = -1;

                                // Buscar la posición del botón en tableroCPU[][]
                                for (int i = 0; i < tableroCPU.length; i++) {
                                    for (int j = 0; j < tableroCPU[i].length; j++) {
                                        if (tableroCPU[i][j] == botonClicado) {
                                            fila = i;
                                            columna = j;
                                            break;
                                        }
                                    }
                                }

                                // Pintar los botones a partir de las coordenadas del botón en el que se hizo clic
                                for (int k = fila; k < fila + rachaPREDATOR && k < tableroCPU.length; k++) {
                                    for (int l = columna; l < columna + rachaPREDATOR && l < tableroCPU[k].length; l++) {
                                        if (estadoTableroCPU[k][l] == 1) {
                                            if (k != fila || l != columna) {
                                                contadorValidarGanadorPlayer++;
                                            }
                                            estadoTableroCPU2[k][l] = 3;
                                            tableroCPU[k][l].setBackground(Color.RED);
                                            tableroCPU[k][l].repaint();
                                            tableroCPU[k][l].revalidate();
                                            impacto = true;
                                        } else {
                                            estadoTableroCPU2[k][l] = 2;
                                            tableroCPU[k][l].setBackground(Color.BLUE);
                                            tableroCPU[k][l].repaint();
                                            tableroCPU[k][l].revalidate();
                                        }
                                    }
                                }
                                //Preguntamos si ha habido impacto para cargar una animacion u otra
                                if (impacto == true) {
                                    animacionSeleccionada = 2;
                                    delay = 21000;
                                } else {
                                    animacionSeleccionada = 3;
                                    delay = 14000;
                                }
                                impacto = false;    //Reiniciamos la variable por si vuelve a usar el predator
                                aciertosSeguidosPlayer = 0;
                                botonPresionado = true;
                                panelRachaBajas.setVisible(false);
                                panelTableroCPU.setVisible(false);
                                panelTableroJugador.setVisible(false);
                                panelRotar.setVisible(false);
                                panelSalir.setVisible(false);
                                contador.setVisible(false);
                                labelImpactos.setVisible(false);
                                clip.stop();

                                Timer timer = new Timer(delay, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        panelRachaBajas.setVisible(true);
                                        panelTableroCPU.setVisible(true);
                                        panelTableroJugador.setVisible(true);
                                        panelRotar.setVisible(true);
                                        panelSalir.setVisible(true);
                                        contador.setVisible(true);
                                        labelImpactos.setVisible(true);
                                        labelFondo.setIcon(new javax.swing.ImageIcon("res/media/gif/barco.gif"));
                                        clip.start();
                                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();

                                borrarBotones();
                                disparoPlayer();

                            }////fin mouseclik

                            private void borrarBotones() {
                                for (JButton boton : botonesConMouseListener) {
                                    for (MouseListener listener : boton.getMouseListeners()) {
                                        boton.removeMouseListener(listener);
                                    }
                                }
                            }

                        }); // fin de los eventos del tablero 

                    }
                }

            }

        }); // fin del evento del evento de boton de la racha 

    }//llaves del metodo

    private void RachaCaza_combate(JButton[] tableroRachaBajas, short[][] estadoTableroCPU, JButton[][] tableroCPU) {
        tableroRachaBajas[2].addActionListener(new ActionListener() {
            int rachacaza_combate = 2;

            @Override
            public void actionPerformed(ActionEvent e) {
                List<JButton> botonesConMouseListener = new ArrayList<>();
                for (int i = 0; i < tableroCPU.length; i++) {
                    for (int j = 0; j < tableroCPU.length; j++) {
                        botonesConMouseListener.add(tableroCPU[i][j]);
                        tableroCPU[i][j].addMouseListener(new MouseAdapter() {

                            @Override
                            public void mouseEntered(MouseEvent e) {
                                JButton botonClicado = (JButton) e.getSource(); // Obtener el botón en el que se hizo clic
                                int fila = -1, columna = -1;

                                // Buscar la posición del botón en tableroCPU[][]
                                for (int i = 0; i < tableroCPU.length; i++) {
                                    for (int j = 0; j < tableroCPU[i].length; j++) {
                                        if (tableroCPU[i][j] == botonClicado) {
                                            fila = i;
                                            columna = j;
                                            break;
                                        }
                                    }
                                }

                                // Pintar los botones a partir de las coordenadas del botón en el que se hizo clic
                                for (int k = fila; k < fila + rachacaza_combate && k < tableroCPU.length; k++) {
                                    for (int l = 0; l < tableroCPU[k].length; l++) { // la diferencia es que esta vez el segundo for ira hasta el largo del tablero para ocupar la fila completa y como 
                                        tableroCPU[k][l].setOpaque(true);     // la variable rachacaza_combate = 2 solo ocupara 2 filas el resto es igual 
                                        tableroCPU[k][l].setBackground(Color.ORANGE);
                                        tableroCPU[k][l].repaint();
                                        tableroCPU[k][l].revalidate();

                                    }
                                }

                            }

                            // es excatamente lo mismo que el metodo anterior 
                            public void mouseExited(MouseEvent e) {
                                for (int k = 0; k < tableroCPU.length; k++) {
                                    for (int l = 0; l < tableroCPU.length; l++) {
                                        if (estadoTableroCPU2[k][l] == 1) {
                                            tableroCPU[k][l].setOpaque(true);
                                            tableroCPU[k][l].setBackground(Color.RED);
                                            tableroCPU[k][l].repaint();
                                            tableroCPU[k][l].revalidate();
                                        } else {
                                            if (estadoTableroCPU2[k][l] == 2) {
                                                tableroCPU[k][l].setOpaque(true);
                                                tableroCPU[k][l].setBackground(Color.BLUE);
                                                tableroCPU[k][l].repaint();
                                                tableroCPU[k][l].revalidate();
                                            } else {
                                                if (estadoTableroCPU2[k][l] == 3) { // aqui valido si hay  un uno o no cuando sale el raron para que mantenga el color magenta al salir 
                                                    tableroCPU[k][l].setOpaque(true);
                                                    tableroCPU[k][l].setBackground(Color.red);
                                                    tableroCPU[k][l].repaint();
                                                    tableroCPU[k][l].revalidate();

                                                } else {
                                                    tableroCPU[k][l].setOpaque(false); // y si no los vuelvo hago que se queden ocutos 
                                                    tableroCPU[k][l].repaint();
                                                    tableroCPU[k][l].revalidate();

                                                }
                                            }
                                        }
                                    }
                                }

                            }

                            // aqui lo mismo solo cambio el for que pinta 
                            public void mouseClicked(MouseEvent e) {
                                animacionSeleccionada = 4;
                                aciertosSeguidosPlayer = 0;
                                botonPresionado = true;
                                panelRachaBajas.setVisible(false);
                                panelTableroCPU.setVisible(false);
                                panelTableroJugador.setVisible(false);
                                panelRotar.setVisible(false);
                                panelSalir.setVisible(false);
                                contador.setVisible(false);
                                labelImpactos.setVisible(false);
                                clip.stop();

                                Timer timer = new Timer(15000, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        panelRachaBajas.setVisible(true);
                                        panelTableroCPU.setVisible(true);
                                        panelTableroJugador.setVisible(true);
                                        panelRotar.setVisible(true);
                                        panelSalir.setVisible(true);
                                        contador.setVisible(true);
                                        labelImpactos.setVisible(true);
                                        labelFondo.setIcon(new javax.swing.ImageIcon("res/media/gif/barco.gif"));
                                        clip.start();
                                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                                    }
                                });
                                timer.setRepeats(false);
                                timer.start();

                                JButton botonClicado = (JButton) e.getSource(); // Obtener el botón en el que se hizo clic
                                int fila = -1, columna = -1;

                                // Buscar la posición del botón en tableroCPU[][]
                                for (int i = 0; i < tableroCPU.length; i++) {
                                    for (int j = 0; j < tableroCPU[i].length; j++) {
                                        if (tableroCPU[i][j] == botonClicado) {
                                            fila = i;
                                            columna = j;
                                            break;
                                        }

                                    }

                                }
                                // Pintar los botones a partir de las coordenadas del botón en el que se hizo clic
                                for (int k = fila; k < fila + rachacaza_combate && k < tableroCPU.length; k++) { // aqui al hacer clik pilla la pocicion en la cual lo ha hecho
                                    for (int l = 0; l < tableroCPU[k].length; l++) { // y luego empieza en la columna 0 para que pinte desde el inicio hasta hasta el largo de tablero y como la k no pasa de 2
                                        if (estadoTableroCPU[k][l] == 1) {

                                            estadoTableroCPU2[k][l] = 3;
                                            tableroCPU[k][l].setBackground(Color.RED);
                                            tableroCPU[k][l].repaint();
                                            tableroCPU[k][l].revalidate();
                                            contadorValidarGanadorPlayer++;
                                        } else {
                                            estadoTableroCPU2[k][l] = 2;
                                            tableroCPU[k][l].setBackground(Color.BLUE);
                                            tableroCPU[k][l].repaint();
                                            tableroCPU[k][l].revalidate();
                                        }

                                    }
                                }
                                borrarBotones();
                                disparoPlayer();
                            } ////fin mouseclik

                            private void borrarBotones() {
                                for (JButton boton : botonesConMouseListener) {
                                    for (MouseListener listener : boton.getMouseListeners()) {
                                        boton.removeMouseListener(listener);
                                    }
                                }
                            }

                        }); // fin evento de los botones 

                    }
                }
            }

        }); // fin del evento de boton de rachas 

    } // fin del metodo 

    private void RachaNuclear(JButton[] tableroRachaBajas, short[][] estadoTableroCPU, JButton[][] tableroCPU) {
        tableroRachaBajas[3].addActionListener(new ActionListener() {
            int rachaUAV = 3;

            boolean eventoActivo = false;

            @Override
            public void actionPerformed(ActionEvent e) { // este era aun mas facil porque solo tenia que esperar a que se haga clip sobre el boton y en cuanto lo haga 
                for (int k = 0; k < tableroCPU.length; k++) { // con el bucle punto todo el tablero de rojo 
                    for (int l = 0; l < tableroCPU.length; l++) {
                        tableroCPU[k][l].setOpaque(true);
                        tableroCPU[k][l].setBackground(Color.ORANGE);
                        tableroCPU[k][l].repaint();
                        tableroCPU[k][l].revalidate();
                        contadorValidarGanadorPlayer = 19;
                    }
                }

                animacionSeleccionada = 5;
                aciertosSeguidosPlayer = 0;
                botonPresionado = true;
                panelRachaBajas.setVisible(false);
                panelTableroCPU.setVisible(false);
                panelTableroJugador.setVisible(false);
                panelRotar.setVisible(false);
                panelSalir.setVisible(false);
                contador.setVisible(false);
                labelImpactos.setVisible(false);
                clip.stop();
                Timer timer = new Timer(13000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        clip.close();
                        scoreboard v2 = new scoreboard(1, contadorValidarGanadorPlayer);
                        hiloAbierto = false;
                        vistaPartida.this.dispose();
                    }
                });
                timer.setRepeats(false);
                timer.start();

            }////fin mouseclik

        }); // fin del evento del teclado 

    } // fin del metodo 

    private void comenzarHilo() {
        new Thread(() -> {
            while (hiloAbierto) {
//                System.out.println("prueba");
                // Comprueba el estado de la variable para decidir si se debe ejecutar la animación
                if (botonPresionado) {

                    switch (animacionSeleccionada) {
                        case 1 -> {
                            Animaciones a1 = new Animaciones(1);
                            a1.generarVideo(labelFondo, vistaPartida.this.getWidth(), vistaPartida.this.getHeight());
                        }
                        case 2 -> {
                            Animaciones a1 = new Animaciones(2);
                            a1.generarVideo(labelFondo, vistaPartida.this.getWidth(), vistaPartida.this.getHeight());
                        }
                        case 3 -> {
                            Animaciones a1 = new Animaciones(3);
                            a1.generarVideo(labelFondo, vistaPartida.this.getWidth(), vistaPartida.this.getHeight());
                        }
                        case 4 -> {
                            Animaciones a1 = new Animaciones(4);
                            a1.generarVideo(labelFondo, vistaPartida.this.getWidth(), vistaPartida.this.getHeight());
                        }
                        case 5 -> {
                            Animaciones a1 = new Animaciones(5);
                            a1.generarVideo(labelFondo, vistaPartida.this.getWidth(), vistaPartida.this.getHeight());
                        }
                    }

                    // Restablece el estado de la variable después de ejecutar la animación
                    botonPresionado = false;
                }

                // Duerme el hilo por un corto período de tiempo para evitar el uso excesivo de CPU
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void miinitComponents() {

        labelFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelFondo.setIcon(new javax.swing.ImageIcon("res/media/gif/barco.gif"));
        labelFondo.setText("jLabel1");
        PanelPrincipal.add(labelFondo);
        labelFondo.setBounds(0, 0, vistaPartida.this.getWidth(), vistaPartida.this.getHeight());

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

            if (!repetir) {
                audio_select.loop(0);
            } else {
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
