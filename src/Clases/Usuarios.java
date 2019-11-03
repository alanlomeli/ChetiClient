package Clases;


//Esta clase tiene todos los usuarios existentes y por existir
public class Usuarios {

    private long celular;
    private String nombre;
    private String apellido;
    private String apodo; //Solo se usare si es compita
    private String ip;
    private boolean online;


    public Usuarios() {
    }

    public Usuarios(long celular, String nombre, String apellido, boolean online) {
        this.celular = celular;
        this.nombre = nombre;
        this.apellido = apellido;
        this.online = online;
        this.ip="";
    }
    public Usuarios(long celular, String nombre, String apellido, boolean online, String ip) {
        this.celular = celular;
        this.nombre = nombre;
        this.apellido = apellido;
        this.online = online;
        this.ip=ip;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public long getCelular() {
        return celular;
    }

    public void setCelular(long celular) {
        this.celular = celular;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
