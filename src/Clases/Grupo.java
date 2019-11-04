package Clases;

import java.util.Vector;

public class Grupo {
    int ID;
    String nombre;
    long creador;
    Vector<Long> miembros;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Grupo() {

    }

    public Grupo(long creador, Vector<Long> miembros) {
        this.creador = creador;
        this.miembros = miembros;
    }

    public long getCreador() {
        return creador;
    }

    public void setCreador(long creador) {
        this.creador = creador;
    }

    public Vector<Long> getMiembros() {
        return miembros;
    }

    public void setMiembros(Vector<Long> miembros) {
        this.miembros = miembros;
    }
}
