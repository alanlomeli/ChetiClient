package Clases;

import java.util.Vector;
//Hay uno de estos por chat y por archivo
public class Chat {
    private long dueño;
    private long compita;
    private Vector<Mensaje> mensajes;

    public Chat() {
    }

    public Chat(long dueño, long compita, Vector<Mensaje> mensajes) {
        this.dueño = dueño;
        this.compita = compita;
        this.mensajes = mensajes;
    }

    public long getDueño() {
        return dueño;
    }

    public void setDueño(long dueño) {
        this.dueño = dueño;
    }

    public long getCompita() {
        return compita;
    }

    public void setCompita(long compita) {
        this.compita = compita;
    }

    public Vector<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(Vector<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }
}
