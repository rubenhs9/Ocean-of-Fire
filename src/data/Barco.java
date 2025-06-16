

package data;
    
import javax.swing.JOptionPane;


public class Barco {
    
    private static int AUTONUM = 0;
    private int numero;
    private int tam;

    public Barco(int tam) {
        comprobarTamaño(tam);
        this.numero = ++AUTONUM;
        
    }

    public int getAUTONUM() {
        return AUTONUM;
    }

    public int getTam() {
        return tam;
    }
    
    

    @Override
    public String toString() {
        return "Barco "+ numero + ", Tamaño=" + tam;
    }

    private void comprobarTamaño(int tam) {
        if (tam >= 3 && tam <= 5)
            this.tam = tam;
        else
            JOptionPane.showMessageDialog(null, "El tamaño del barco debe estar entre 3 y 5", "Tamaño del barco incorrecto", 0);
    }
    
    
    
}
