/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Pojos.Usuario;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author alan
 */
public class EnviarSocket {

    String respuestaJSONString;
    Gson gson;
    Vector<String> vector;
    Comunicacion enviar;
    BufferedWriter bw;
    BufferedReader br;
    Socket socketCredenciales;
    Respuesta leerRespuesta;

    /**
     *
     * @param tipo el tipo de accion que hara el server
     * @param datos los datos que vas a mandar
     */
    public EnviarSocket(String tipo, Vector datos) {
        gson = new Gson();

        vector = datos;
        enviar = new Comunicacion(tipo, vector);

    }

    /**
     *
     * @return envia los datos al server y obtiene una respuesta
     */
    public Respuesta enviar() {

        try {

            socketCredenciales = new Socket(new Usuario().obtenerObjeto().getIpServer(), 1234);//Obtiene la ip del archivo local
            bw = new BufferedWriter(new OutputStreamWriter(socketCredenciales.getOutputStream()));
            br = new BufferedReader(new InputStreamReader(socketCredenciales.getInputStream()));
            bw.write(gson.toJson(enviar) + "\n");

            bw.flush();
            respuestaJSONString = br.readLine();
            socketCredenciales.close();
            br.close();
            bw.close();
            //Objeto de respuesta para guardar lo obtenido del server
            gson = new Gson();

            leerRespuesta = gson.fromJson(respuestaJSONString, Respuesta.class); //Se iguala el json string al objeto respuesta
        } catch (Exception ex) {
            System.out.println(ex);
            return new Respuesta();
        }
        return leerRespuesta;
    }

}
