package Clases;

public class Mensaje {
    private long remitente;
    private long destinatario;
    private String mensaje;

    public Mensaje(long remitente, long destinatario, String mensaje) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
    }

    public Mensaje() {
    }

    public long getRemitente() {
        return remitente;
    }

    public void setRemitente(long remitente) {
        this.remitente = remitente;
    }

    public long getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(long destinatario) {
        this.destinatario = destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
