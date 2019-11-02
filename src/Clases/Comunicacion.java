/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.Vector;

/**
 *
 * @author alan
 */
public class Comunicacion {

    String tipo;
    Vector<String> datos;

    public Comunicacion(String tipo, Vector<String> vector) {
        this.tipo = tipo;
        this.datos = vector;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Vector<String> getVector() {
        return datos;
    }

    public void setVector(Vector<String> vector) {
        this.datos = vector;
    }
}
