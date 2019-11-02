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
public class Respuesta {

    boolean success;
    Vector<String> datos;

    public Respuesta() {
        this.success = false;

    }

    public Vector<String> getDatos() {
        return datos;
    }

    public void setDatos(Vector<String> datos) {
        this.datos = datos;
    }

    public boolean success() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
