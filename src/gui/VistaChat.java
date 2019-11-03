/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Clases.Comunicacion;
import Clases.EnviarSocket;
import Clases.ListaUsuarios;
import Clases.Respuesta;
import Pojos.Usuario;
import com.google.gson.Gson;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.*;

public class VistaChat extends JFrame {

    private Login loginVista = new Login();
    private ConfiguracionUsuario configUsr;

    private JPanel panelChat, panelMessage, panelSuperiorDer, panelInferiorDer; //División de paneles principales
    private JButton compitas, usuarios, grupos;
    private JButton btnConfiguracion;
    private JLabel amigosConectados;
    private JList listaAmigosConectados;
    private long numeroChatActivo; //Tiene el celular de la persona con la que se esta enviando un mensaje

    private JTextField etxtMsg;
    private JButton btnSendMsg;

    private ListaUsuarios listaUsuarios;  //Esta lista tiene todos los usuarios, amigos y no amigos, tambien tiene si estan online

    public VistaChat() {
        iniciarComponentes(); //En este void inicia la magia o.0
        this.setSize(new Dimension(1138, 720));
        setLocationRelativeTo(null); //Al correr, la ventana se centra en medio de la pantalla
        this.setResizable(false); //Pa que nuestra ventana no se vaya a cambiar de tamaño
    }

    private void iniciarComponentes() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("CHETI CHAT");
        panelChat = new JPanel();
        panelChat.setBackground(Color.cyan);
        panelMessage = new JPanel();
        panelMessage.setBackground(Color.green);
        panelSuperiorDer = new JPanel();
        panelSuperiorDer.setBackground(Color.yellow);
        panelInferiorDer = new JPanel();
        panelInferiorDer.setBackground(Color.pink);

        iniciarPanelMensajes();
        iniciarPanelSuperiorDer();

        GroupLayout orden = new GroupLayout(this.getContentPane());
        orden.setAutoCreateContainerGaps(true);
        orden.setAutoCreateGaps(true);
        orden.setHorizontalGroup(orden.createSequentialGroup()
                .addGroup(orden.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panelChat, 800, 800, 800)
                        .addComponent(panelMessage, 800, 800, 800))
                .addGroup(orden.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(orden.createSequentialGroup()
                                .addComponent(compitas, 100, 100, 100)
                                .addComponent(usuarios, 100, 100, 100)
                                .addComponent(grupos, 100, 100, 100))
                        .addComponent(panelSuperiorDer, 314, 314, 314)
                        .addComponent(panelInferiorDer, 314, 314, 314))
        );

        orden.setVerticalGroup(orden.createParallelGroup()
                .addGroup(orden.createSequentialGroup()
                        .addComponent(panelChat, 580, 580, 580)
                        .addComponent(panelMessage, 80, 80, 80)
                )
                .addGroup(orden.createSequentialGroup()
                        .addGroup(orden.createParallelGroup()
                                .addComponent(compitas)
                                .addComponent(usuarios)
                                .addComponent(grupos))
                        .addComponent(panelSuperiorDer, 400, 400, 400)
                        .addComponent(panelInferiorDer, 320, 320, 320)
                )
        );
        setLayout(orden);
        this.pack();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                decirOffline();
            }
        });

    }


    /**
     * Pide la IP del server, inicia la App. Se llama desde el main.
     */
    public void empezarAPP() {
        boolean existe = archivoDeConfiguracionExiste();

        CambiarIP cambiarIP;
        if (existe) {
            Usuario configGuardada = new Usuario();

            configGuardada = configGuardada.obtenerObjeto();
            cambiarIP = new CambiarIP(configGuardada.getIpServer());
        } else {
            cambiarIP = new CambiarIP();

        }
        cambiarIP.setVisible(true);
        cambiarIP.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (existe) {
                    //Iniciar sesion automaticamente
                    iniciarSesion();


                } else {
                    loginVista.setVisible(true);

                }
            }
        });

    }

    /**
     * Crea el archivo de configuracion si nadie habia abierto la app antes
     *
     * @return retorna true si ya habia un archivo guardado
     */
    private static boolean archivoDeConfiguracionExiste() {
        boolean existe = true;
        Usuario usuario = new Usuario(true, "192.168.1.83");

        existe = usuario.existeArchivo();
        if (!existe) {   //Si no existia archivo lo crea
            usuario.escribirArchivo(usuario);
        }
        return existe;

    }

    /**
     * Inicia sesion usando las credenciales locales
     */
    private void iniciarSesion() {
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado = usuarioGuardado.obtenerObjeto();
        Vector<String> vector = new Vector<>(2, 2);

        vector.addElement(usuarioGuardado.getCelular() + "");
        vector.addElement(usuarioGuardado.getNotpasswd());

        EnviarSocket enviarDato = new EnviarSocket("login", vector);
        Respuesta respuestaDatos = enviarDato.enviar();

        if (respuestaDatos.success()) {        //Si la contra fue correcta guardamos los datos

            this.setVisible(true);

            //JOptionPane.showMessageDialog(null, "Sesion iniciada");
            Usuario usuario = new Usuario();
            usuario = usuario.obtenerObjeto();
            usuario.setCelular(Long.parseLong(respuestaDatos.getDatos().get(0)));
            usuario.setNombre(respuestaDatos.getDatos().get(1));
            usuario.setApellido(respuestaDatos.getDatos().get(2));
            usuario.escribirArchivo(usuario); //Escribimos el archivo
            obtenerListaUsuarios();

        } else {

            JOptionPane.showMessageDialog(null, "Parece que tu ultima contraseña cambio.");

        }

    }

    /**
     * Esta funciona obtiene toda la lista de usuarios del server,
     * esta tendrá si el usuario es tu amigo, o no, también dira si esta conectado actualmente.
     */
    private void obtenerListaUsuarios() {
        Gson gson = new Gson();
        Usuario usuario = new Usuario();
        usuario = usuario.obtenerObjeto();
        Vector<String> datos = new Vector<>();
        datos.add(usuario.getCelular() + "");
        EnviarSocket obtenerlista = new EnviarSocket("verUsuarios", datos);
        Respuesta respuesta = obtenerlista.enviar();

        if (respuesta.success()) {
            System.out.println(respuesta.getDatos().get(0));
            listaUsuarios = gson.fromJson(respuesta.getDatos().get(0), ListaUsuarios.class);
            System.out.println(gson.toJson(listaUsuarios));
        }
    }

    /**
     * Esta funcion le dice al server que el cliente irá offline
     */
    private void decirOffline() {
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado = usuarioGuardado.obtenerObjeto();
        Vector<String> datos = new Vector<>();
        datos.add(usuarioGuardado.getCelular() + "");

        EnviarSocket irOffline = new EnviarSocket("offline", datos);
        irOffline.enviar();
    }

    private void enviarMensaje() {

        Gson gson = new Gson();
        Vector<String> vectorSendMsg = new Vector<>(3, 3);

        vectorSendMsg.addElement(new Usuario().getCelular() + "");//quien envia el msj

        //vecotrSendMsg.addElement();//AQUI VA A QUIEN LO ENVÍA YA SEA GRUPO O USUARIO
        vectorSendMsg.addElement(etxtMsg.getText());//el mensaje

        //AQUI IRÍAN UNA CONDICIÓN PARA SABER SI LO ENVÍA A UN GRUPO O UN USUARIO


        //SI ES MENSAJE LO ENVIA A UN USUARIO:
        Comunicacion enviarMsg = new Comunicacion("MsgUsu", vectorSendMsg);

        //SI EL MENSAJE LO ENVIA A UN GRUPO

        Comunicacion enviarMsgGrpo = new Comunicacion("MsgGrupo", vectorSendMsg);

        try {
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void iniciarPanelMensajes() {

        etxtMsg = new JTextField("Escriba el mensaje:");
        btnSendMsg = new JButton("Enviar");
        btnConfiguracion = new JButton("Config");

        btnSendMsg.addActionListener((ActionEvent e) -> {
            enviarMensaje();
        });

        GroupLayout Msg = new GroupLayout(panelMessage);
        Msg.setAutoCreateGaps(true);
        Msg.setAutoCreateContainerGaps(true);

        Msg.setHorizontalGroup(
                Msg.createSequentialGroup()
                        .addComponent(etxtMsg, 620, 620, 620)
                        .addComponent(btnSendMsg)
                        .addComponent(btnConfiguracion)
        );

        Msg.setVerticalGroup(
                Msg.createSequentialGroup()
                        .addGroup(
                                Msg.createParallelGroup()
                                        .addComponent(etxtMsg, 50, 50, 50)
                                        .addComponent(btnSendMsg, 50, 50, 50)
                                        .addComponent(btnConfiguracion, 50, 50, 50)

                        )
        );

        panelMessage.setLayout(Msg);

        //Listener panel mensajes
        btnConfiguracion.addActionListener((ActionEvent e) -> {
            //Aqui se cierra la ventana de Chat al cerrar sesion
            configUsr = new ConfiguracionUsuario();
            configUsr.setModal(true);
            configUsr.setVisible(true);

            //Boton de cerrar sesion fue presionado.
            if (configUsr.cerrarSesion) {
                decirOffline();
                Usuario usr = new Usuario();
                usr.borrarArchivo();
                this.setVisible(false);
                CambiarIP cambiarIP = new CambiarIP();
                cambiarIP.setModal(true);
                cambiarIP.setVisible(true);
                loginVista.setVisible(true);

            }

            //Boton de darkMode fue presionado
            if (configUsr.darkMode) {
                panelChat.setBackground(Color.GRAY);
                panelMessage.setBackground(Color.GRAY);
                panelSuperiorDer.setBackground(Color.GRAY);
                panelInferiorDer.setBackground(Color.GRAY);

            } else {
                panelChat.setBackground(Color.cyan);
                panelMessage.setBackground(Color.green);
                panelSuperiorDer.setBackground(Color.yellow);
                panelInferiorDer.setBackground(Color.pink);
            }


        });
    }

    private void iniciarPanelSuperiorDer() {
        compitas = new JButton();
        usuarios = new JButton();
        grupos = new JButton();
        compitas.setText("Compitas");
        usuarios.setText("Usuarios");
        grupos.setText("Grupos");

        //Listener panel superior derecho
        //Este boton busca a los amigos que tienes para mostrarlos en la vista superior derecha.
        compitas.addActionListener((ActionEvent e) -> {

            //Verificar los usuarios amigos de la BD
            panelSuperiorDer.removeAll();

            Vector<String> vector = new Vector<>(2, 2);
            vector.add(new Usuario().obtenerObjeto().getCelular() + "");

            EnviarSocket enviarDato = new EnviarSocket("compitasConectados", vector); /*Esta ya no es la forma de
                                                                                           obtener a los amigos conectados,
                                                                                           ya estan en el objeto "listaUsuarios
                                                                                                    */
            Respuesta respuestaDatos = enviarDato.enviar();

            if (respuestaDatos.success()) {
                JButton[] boton = new JButton[respuestaDatos.getDatos().size()];

                for (int i = 0; i < boton.length; i++) {

                    String[] parts = respuestaDatos.getDatos().elementAt(i).split(",");
                    if (!parts[1].equals("")) {
                        boton[i] = new JButton(parts[1]);
                    } else {
                        boton[i] = new JButton(parts[0]);
                    }
                    boton[i].setActionCommand(respuestaDatos.getDatos().elementAt(i));

                    boton[i].addActionListener((ActionEvent eventoBoton) -> {
                        compitaSeleccionado(eventoBoton.getActionCommand());
                    });

                    panelSuperiorDer.add(boton[i]);
                }
                panelSuperiorDer.validate();
                panelSuperiorDer.repaint();
            }
        });


        usuarios.addActionListener((ActionEvent e) -> {
            //  System.out.println("Usuraios");
        });

        grupos.addActionListener((ActionEvent e) -> {
            // System.out.println("Grupos");


        });
    }

    /**
     * @param datos los datos del compita seleccionado
     */
    private void compitaSeleccionado(String datos) {

        String[] parts = datos.split(",");

        this.numeroChatActivo = Long.parseLong(parts[2]);

        JLabel nombreChat = new JLabel("Chateando con: " + (!parts[1].equals("") ? parts[1] : parts[2]));
        System.out.println("Chateando con: " + (!parts[1].equals("") ? parts[1] : parts[2]));
        nombreChat.setAlignmentX(CENTER_ALIGNMENT);
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS));
        panelChat.add(nombreChat);
        panelChat.validate();
    }
}
