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

    private Login loginVista;
    private ConfiguracionUsuario configUsr;
    private JPanel panelChat, panelMessage, panelConectados, panelDesconectados; //División de paneles principales
    private JToggleButton compitas, usuarios, grupos;
    private JButton btnConfiguracion;
    private long numeroChatActivo; //Tiene el celular de la persona con la que se esta enviando un mensaje
    private ButtonGroup buttonGroup;
    private JLabel indicadorPanelConectados;
    private JLabel indicadorPanelDesconectados;
    private JLabel labelNombreChat; //Indica en el panel de chats con quien estas chateando
    private JTextField etxtMsg;
    private JButton btnSendMsg;
    private JPanel panelMensajes; //Contiene los mensajes

    private ListaUsuarios listaUsuarios;  //Esta lista tiene todos los usuarios, amigos y no amigos, tambien tiene si estan online

    public VistaChat() {
        iniciarComponentes(); //En este void inicia la magia o.0
        this.setSize(new Dimension(1138, 720));
        setLocationRelativeTo(null); //Al correr, la ventana se centra en medio de la pantalla
       // this.setResizable(false); //Pa que nuestra ventana no se vaya a cambiar de tamaño
    }

    private void iniciarComponentes() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("CHETI CHAT");
        loginVista = new Login(this);
        panelChat = new JPanel();
        panelChat.setBackground(Color.cyan);
        panelMensajes = new JPanel();
        labelNombreChat = new JLabel();
        panelChat.add(labelNombreChat);
        panelChat.add(panelMensajes);
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
        panelMessage = new JPanel();
        panelMessage.setBackground(Color.green);
        panelConectados = new JPanel();
        panelConectados.setBackground(Color.yellow);
        panelDesconectados = new JPanel();
        panelDesconectados.setBackground(Color.pink);

        indicadorPanelConectados = new JLabel("Compitas conectados");
        indicadorPanelDesconectados = new JLabel("Compitas desconectados");

        compitas = new JToggleButton();
        usuarios = new JToggleButton();
        grupos = new JToggleButton();
        buttonGroup = new ButtonGroup();

        compitas.setText("Compitas");
        usuarios.setText("Usuarios");
        grupos.setText("Grupos");


        buttonGroup.add(compitas);
        buttonGroup.add(usuarios);
        buttonGroup.add(grupos); //Al añadir los botones a un grupo, cuando se seleccione uno, el otro se deseleccionará.
        compitas.setSelected(true); //Por defecto compitas se selecciona

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
                        .addComponent(indicadorPanelConectados, 314, 314, 314)
                        .addComponent(panelConectados, 314, 314, 314)
                        .addComponent(indicadorPanelDesconectados, 314, 314, 314)

                        .addComponent(panelDesconectados, 314, 314, 314))
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
                        .addComponent(indicadorPanelConectados)
                        .addComponent(panelConectados, 400, 400, 400)
                        .addComponent(indicadorPanelDesconectados)
                        .addComponent(panelDesconectados, 320, 320, 320)
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
                Usuario configGuardada = new Usuario();

                configGuardada = configGuardada.obtenerObjeto();
                if (existe && configGuardada.obtenerObjeto().getCelular() != 0) {
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
            JOptionPane.showMessageDialog(null, "Error iniciando sesion automaticamente.");
        }

    }

    /**
     * Esta funciona obtiene toda la lista de usuarios del server,
     * esta tendrá si el usuario es tu amigo, o no, también dira si esta conectado actualmente.
     */
    public void obtenerListaUsuarios() {
        Gson gson = new Gson();
        Usuario usuario = new Usuario();
        usuario = usuario.obtenerObjeto();
        Vector<String> datos = new Vector<>();
        datos.add(usuario.getCelular() + "");
        EnviarSocket obtenerlista = new EnviarSocket("verUsuarios", datos);
        Respuesta respuesta = obtenerlista.enviar();

        if (respuesta.success()) {
            listaUsuarios = gson.fromJson(respuesta.getDatos().get(0), ListaUsuarios.class);
            listaUsuarios.getPersonas().remove(usuario.getCelular());        //ELiminarse a sí mismo de la lista xddd

            acomodarPanelesUsuarios();
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
                panelConectados.setBackground(Color.GRAY);
                panelDesconectados.setBackground(Color.GRAY);

            } else {
                panelChat.setBackground(Color.cyan);
                panelMessage.setBackground(Color.green);
                panelConectados.setBackground(Color.yellow);
                panelDesconectados.setBackground(Color.pink);
            }


        });
    }

    private void iniciarPanelSuperiorDer() {

        //Listener panel superior derecho
        //Este boton busca a los amigos que tienes para mostrarlos en la vista superior derecha.
        compitas.addActionListener((ActionEvent e) -> {
            indicadorPanelConectados.setText("Compitas conectados");
            indicadorPanelDesconectados.setText("Compitas desconectados");
            obtenerListaUsuarios();
        });


        usuarios.addActionListener((ActionEvent e) -> {
            //  System.out.println("Usuraios");
            indicadorPanelConectados.setText("Usuarios conectados");
            indicadorPanelDesconectados.setText("Usuarios desconectados");
            obtenerListaUsuarios();


        });

        grupos.addActionListener((ActionEvent e) -> {
            // System.out.println("Grupos");
            indicadorPanelConectados.setText("Grupos disponibles");
            indicadorPanelDesconectados.setText("Crear un grupo");
            obtenerListaUsuarios();


        });
    }

    /**
     * @param datos los datos de la persona seleccionada
     */
    private void personaSeleccionada(String datos) {

        String[] parts = datos.split(",");

        this.numeroChatActivo = Long.parseLong(parts[2]);

        labelNombreChat.setText("Chateando con: " + (!parts[1].equals("") ? parts[1] : parts[2]));
        labelNombreChat.setAlignmentX(CENTER_ALIGNMENT);
        System.out.println("Chateando con: " + (!parts[1].equals("") ? parts[1] : parts[2]));
        panelChat.validate();
    }

    /**
     * En esta funcion, todos los paneles obtendran los respectivos usuarios correspondientes, ya sea compita, usuario
     * conectado y no conectado.
     */
    private void acomodarPanelesUsuarios() {

        panelConectados.removeAll();
        panelDesconectados.removeAll();

        panelDesconectados.setLayout(new FlowLayout());
        JButton[] botonCompitas = new JButton[listaUsuarios.getCompitas().size()];
        JButton[] botonOnline = new JButton[listaUsuarios.obtenerListaCompleta().size()];


        if (compitas.isSelected()) {         //Aqui se acomodan los compitas

            int i = 0;
            for (long key : listaUsuarios.getCompitas().keySet()) {
                if (!listaUsuarios.getCompitas().get(key).getApodo().equals("")) {

                    botonCompitas[i] = new JButton(listaUsuarios.getCompitas().get(key).getApodo());
                } else {
                    botonCompitas[i] = new JButton(listaUsuarios.getCompitas().get(key).getCelular() + "");
                }
                botonCompitas[i].setActionCommand(listaUsuarios.getCompitas().get(key).getNombre() + "," +
                        listaUsuarios.getCompitas().get(key).getApodo() + "," +
                        listaUsuarios.getCompitas().get(key).getCelular()
                );

                botonCompitas[i].addActionListener((ActionEvent eventoBoton) -> {
                    personaSeleccionada(eventoBoton.getActionCommand());
                });
                if (listaUsuarios.getCompitas().get(key).isOnline()) {  //Conectados al panel online
                    panelConectados.add(botonCompitas[i]);
                } else {                                                    //Desconectados al panel offline
                    panelDesconectados.add(botonCompitas[i]);

                }

            }
        } else if (usuarios.isSelected()) {

            int i = 0;
            for (long key : listaUsuarios.obtenerListaCompleta().keySet()) {
                if (!listaUsuarios.obtenerListaCompleta().get(key).getApodo().equals("")) {

                    botonOnline[i] = new JButton(listaUsuarios.obtenerListaCompleta().get(key).getApodo());
                } else {
                    botonOnline[i] = new JButton(listaUsuarios.obtenerListaCompleta().get(key).getNombre());
                }
                botonOnline[i].setActionCommand(listaUsuarios.obtenerListaCompleta().get(key).getNombre() + "," +
                        listaUsuarios.obtenerListaCompleta().get(key).getApodo() + "," +
                        listaUsuarios.obtenerListaCompleta().get(key).getCelular()
                );

                botonOnline[i].addActionListener((ActionEvent eventoBoton) -> {
                    personaSeleccionada(eventoBoton.getActionCommand());
                });
                if (listaUsuarios.obtenerListaCompleta().get(key).isOnline()) {  //Conectados al panel online
                    panelConectados.add(botonOnline[i]);
                } else {                                                    //Desconectados al panel offline
                    panelDesconectados.add(botonOnline[i]);

                }
                i++;
            }
        } else if (grupos.isSelected()) {
            JLabel nombreDelGrupo = new JLabel("Nombre del grupo.");
            JLabel miembrosAlGrupo = new JLabel("Agregue miembros (celular)");
            JTextField agregarNombre = new JTextField();
            JTextField agregarMiembros = new JTextField();
            JButton btnagregarMiembro = new JButton("✓");


            Vector<String> datos = new Vector<>();

            btnagregarMiembro.addActionListener((ActionEvent e) -> {
            if(agregarMiembros.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Escribe un numero primero");
            }else{
                if(listaUsuarios.obtenerListaCompleta().get(Long.parseLong(agregarMiembros.getText()))!=null){
                    datos.add(agregarMiembros.getText());
                    agregarMiembros.setText("");

                }else{
                    JOptionPane.showMessageDialog(null, "Usuario no encontrado");
                }

            }

            });


            JButton btnCrearGrupo = new JButton("Crear grupo");

            btnCrearGrupo.addActionListener((ActionEvent e) -> {
                Usuario personaActual= new Usuario();
                personaActual= personaActual.obtenerObjeto();
              if(datos.size()==1||agregarNombre.getText().equals("")){
                  JOptionPane.showMessageDialog(null, "Asegurese de agregar miembros y " +
                          "nombre del grupo");
              }else{
                  datos.insertElementAt(personaActual.getCelular()+","+agregarNombre.getText(),0);
                  EnviarSocket crearGrupo = new EnviarSocket("crearGrupo",datos);
                  Respuesta respuesta=crearGrupo.enviar();
                    datos.removeAllElements();
                  agregarNombre.setText("");
                  if(respuesta.success()){
                      JOptionPane.showMessageDialog(null, "Grupo creado correctamente");
                        acomodarPanelesUsuarios();
                  }
              }

            });

            GroupLayout crearGrupo = new GroupLayout(panelDesconectados);

            crearGrupo.setHorizontalGroup(
                    crearGrupo.createParallelGroup()
                            .addComponent(nombreDelGrupo).addComponent(agregarNombre).addComponent(miembrosAlGrupo)
                            .addGroup(
                                    crearGrupo.createSequentialGroup().addComponent((agregarMiembros)
                                    ).addComponent(btnagregarMiembro)
                            ).addComponent(btnCrearGrupo)
            );

            crearGrupo.setVerticalGroup(
                    crearGrupo.createSequentialGroup()
                            .addComponent(nombreDelGrupo).addComponent(agregarNombre,27,27,27).addComponent(miembrosAlGrupo)
                            .addGroup(
                                    crearGrupo.createParallelGroup().addComponent((agregarMiembros), 27, 27, 27
                                    ).addComponent(btnagregarMiembro)
                            ).addComponent(btnCrearGrupo)
            );

            panelDesconectados.setLayout(crearGrupo);


        }
        panelConectados.validate();
        panelDesconectados.validate();
        panelConectados.repaint();
        panelDesconectados.repaint();


    }

}
