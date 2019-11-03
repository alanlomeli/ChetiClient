/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pojos;

import com.google.gson.Gson;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

/**
 *
 * @author alan
 */
public class Usuario {

    long celular;
    String nombre;
    String apellido;
    String notpasswd;
    String ipServer;
    boolean darkMode;

    public Usuario(long celular, String nombre, String apellido, String notpasswd, String ipServer, boolean darkMode) {
        this.celular = celular;
        this.nombre = nombre;
        this.apellido = apellido;
        this.notpasswd = notpasswd;
        this.darkMode = darkMode;
        this.ipServer = ipServer;
    }

    public Usuario(boolean darkMode, String ip) {
        this.celular = 0;
        this.nombre = "";
        this.apellido = "";
        this.notpasswd = "";
        this.ipServer = ip;
        this.darkMode = darkMode;
    }

    public Usuario() {
        this.celular = 0;
        this.nombre = "";
        this.apellido = "";
        this.notpasswd = "";
        this.ipServer = "";
        this.darkMode = false;
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

    public String getNotpasswd() {
        return notpasswd;
    }

    public void setNotpasswd(String notpasswd) {
        this.notpasswd = notpasswd;
    }

    public String getIpServer() {
        return ipServer;
    }

    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    
    public boolean existeArchivo() {

        try {
            FileInputStream archivoConfig = new FileInputStream("UserConfig/configuracion");
            DataInputStream reader = new DataInputStream(archivoConfig);
            reader.close();

        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    public void escribirArchivo(Usuario usuario) {

        try {

            String k = new Gson().toJson(usuario); //Convertimos mi objeto usuario a json y lo guardamos en un string
            FileOutputStream archivoConfig = new FileOutputStream("UserConfig/configuracion");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(archivoConfig));
            outStream.writeUTF(k);
            outStream.close();

        } catch (Exception ex) {
            System.out.println("Exception al intentar escribir archivo de config"+ex);
        }
    }

    public String obtenerArchivo() {
        String lectura;
        try {
            FileInputStream archivoConfig = new FileInputStream("UserConfig/configuracion");
            DataInputStream reader = new DataInputStream(archivoConfig);
            lectura = reader.readUTF();
            reader.close();

        } catch (Exception ex) {
            return "";
        }
        return lectura;

    }

    public Usuario obtenerObjeto() {
        try {
            FileInputStream archivoConfig = new FileInputStream("UserConfig/configuracion");
            DataInputStream reader = new DataInputStream(archivoConfig);
            reader.close();
        } catch (Exception ex) {
            return null;
        }
        Usuario usuario = new Usuario();
        Gson gson = new Gson();
        usuario = gson.fromJson(usuario.obtenerArchivo(), Usuario.class);

        return usuario;

    }
    
    public boolean borrarArchivo(){
        try{
         File file = new File("UserConfig/configuracion");
        return Files.deleteIfExists(file.toPath()); //surround it in try catch block
        }catch(Exception e){
            return false;
        }
    }
}
