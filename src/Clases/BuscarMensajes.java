package Clases;

import com.google.gson.Gson;
import gui.VistaChat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BuscarMensajes extends Thread {

    private VistaChat vistaDelChat;
    private ServerSocket serverSocket;
    private Respuesta answerObject;

    public BuscarMensajes(VistaChat vista) {
        vistaDelChat = vista;
        try {
            serverSocket = new ServerSocket(1234);
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() {
        Socket c;
        String answer;
        Comunicacion datosRecibidos;

        while (true) {
            try {
                c = serverSocket.accept();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                answer = bufferedReader.readLine();
                Gson gsonMsg = new Gson();
                datosRecibidos = gsonMsg.fromJson(answer, Comunicacion.class);
                try {
                    System.out.println("Se envio un success");
                    answerObject.setSuccess(vistaDelChat.enEsperaDeMsgs(datosRecibidos));
                    bufferedWriter.write(gsonMsg.toJson(answerObject));
                } catch (Exception e) {
                    System.out.println(e);
                }


                bufferedWriter.newLine();
                bufferedWriter.flush();
                bufferedWriter.close();
                bufferedReader.close();
            } catch (Exception ex) {
                //System.out.println("Fallo el socket de respuesta Incompetente ! >:v");
            }
        }
    }
}
