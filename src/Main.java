import Clases.BuscarMensajes;
import gui.VistaChat;

public class Main {
    public static void main(String []args){

        VistaChat vc = new VistaChat();
        vc.empezarAPP();


        Thread hilo = new Thread();
        hilo = new BuscarMensajes(vc);
    }
}
