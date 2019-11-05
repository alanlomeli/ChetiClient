package Clases;

import com.google.gson.Gson;
import gui.VistaChat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BuscarMensajes extends Thread{

    private VistaChat vistaDelChat;
    private ServerSocket serverSocket;
    private Respuesta answerObject;

    public BuscarMensajes(VistaChat vista){
        vistaDelChat=vista;
    }

    @Override
    public void run(){
        Socket socket;
        String answer;
        Comunicacion datosRecibidos;

        while (true){
            try {
                socket = serverSocket.accept();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                answer = bufferedReader.readLine();
                Gson gsonMsg = new Gson();
                datosRecibidos = gsonMsg.fromJson(answer,Comunicacion.class);
                answerObject.setSuccess(vistaDelChat.enEsperaDeMsgs(datosRecibidos));
                bufferedWriter.write(gsonMsg.toJson(answerObject));
                bufferedWriter.newLine();
                bufferedWriter.flush();
                bufferedWriter.close();
                bufferedReader.close();
            }catch (Exception ex){
                System.out.println("Fallo el socket Incompetente de respuesta! >:v");
            }
        }
    }
}
