/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author pablo
 */
import Clases.EnviarSocket;
import Clases.Respuesta;
import Pojos.Usuario;
import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VistaChat extends JFrame {

    public Login loginVista = new Login();
    public ConfiguracionUsuario configUsr;

    private JPanel panelChat, panelMessage, panelSuperiorDer, panelInferiorDer; //División de paneles principales
    private JButton compitas, usuarios, grupos;
    private JButton enviar, btnConfiguracion;
    private JTextField inputMessage;
    private JLabel amigosConectados;
    private JList listaAmigosConectados;
    //PANEL ENVIAR MSG****************************************
    private JTextField etxtMsg;
    private JButton btnSendMsg;

    //DefaultListModel modeloLista;
    public VistaChat() {
        inicia(); //En este void inicia la magia o.0
        this.setSize(new Dimension(1080, 720));
        setLocationRelativeTo(null); //Al correr, la ventana se centra en medio de la pantalla
        this.setResizable(false); //Pa que nuestra ventana no se vaya a cambiar de tamaño
    }

    private void inicia() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("CETI CHAT");

        GroupLayout orden = new GroupLayout(this.getContentPane());
        orden.setAutoCreateContainerGaps(true);
        orden.setAutoCreateGaps(true);

        panelChat = new JPanel();
        panelChat.setBackground(Color.cyan);
        panelMessage = new JPanel();
        panelMessage.setBackground(Color.green);
        panelSuperiorDer = new JPanel();
        panelSuperiorDer.setBackground(Color.yellow);
        panelInferiorDer = new JPanel();
        panelInferiorDer.setBackground(Color.pink);
        compitas = new JButton();
        usuarios = new JButton();
        grupos = new JButton();
        compitas.setText("Compitas");
        usuarios.setText("Usuarios");
        grupos.setText("Grupos");

        /*
        PANEL DE ENVIAR MSG**************************BY: NITO**************************
        etxtMsg = new JTextField("Escriba el mensaje:");
        btnSendMsg = new JButton("Enviar");
        btnSendMsg.addActionListener((ActionEvent e)->{
            enviarMensaje();
        });
        GroupLayout Msg = new GroupLayout(this.getContentPane());
        Msg.setAutoCreateGaps(true);
        Msg.setAutoCreateContainerGaps(true);
        
        Msg.setHorizontalGroup(
                Msg.createSequentialGroup()
                
                .addComponent(etxtMsg,500,500,500)
                .addComponent(btnSendMsg)
        );
        
        Msg.setVerticalGroup(
        
                Msg.createSequentialGroup()
                
                .addGroup(
                        Msg.createParallelGroup()
                                
                        .addComponent(etxtMsg)
                        .addComponent(btnSendMsg)
                )
        );
        setLayout(Msg);
        this.pack();
        
         */
        orden.setHorizontalGroup(orden.createSequentialGroup()
                .addGroup(orden.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panelChat, 800, 800, 800)
                        .addComponent(panelMessage, 800, 800, 800))
                .addGroup(orden.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(orden.createSequentialGroup()
                                .addComponent(compitas)
                                .addComponent(usuarios)
                                .addComponent(grupos))
                        .addComponent(panelSuperiorDer, 280, 280, 280)
                        .addComponent(panelInferiorDer, 280, 280, 280))
        );

        orden.setVerticalGroup(orden.createParallelGroup()
                .addGroup(orden.createSequentialGroup()
                        .addComponent(panelChat, 580, 580, 580)
                        .addComponent(panelMessage, 140, 140, 140)
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

        inputMessage = new JTextField();
        panelMessage.add(inputMessage);
        enviar = new JButton("Enviar");
        btnConfiguracion = new JButton("Confi");
        panelMessage.add(enviar); //Pienso en que este también sea un toggle para que cuando no este seleccionado ningún chat, tampoco se pueda enviar ningún mensaje.
        //No entendi :u
        panelMessage.add(btnConfiguracion);

        btnConfiguracion.addActionListener((ActionEvent e) -> {
            //Aqui se cierra la ventana de Chat al cerrar sesion
            configUsr = new ConfiguracionUsuario();
            configUsr.setModal(true);
            configUsr.setVisible(true);
            
            //Boton de cerrar sesion fue presionado.
            if (configUsr.cerrarSesion) {

                System.out.println("Hay que cerrar sesion");
                this.setVisible(false);
                 CambiarIP cambiarIP = new CambiarIP();
                cambiarIP.setModal(true);
                cambiarIP.setVisible(true);
                loginVista.setVisible(true);
               
            }
            
            //Boton de darkMode fue presionado
            if(configUsr.darkMode == true){
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

        
        //Este boton busca a los amigos que tienes para mostrarlos en la vista superior derecha.
        compitas.addActionListener((ActionEvent e) -> {
            //Verificar los usuarios amigos de la BD
            panelSuperiorDer.removeAll();
            
            Vector<String> vector = new Vector(2, 2);
            System.out.println(new Usuario().obtenerObjeto().getCelular());
            vector.add(new Usuario().obtenerObjeto().getCelular()+"");
            EnviarSocket enviarDato = new EnviarSocket("compitasConectados", vector);
            Respuesta respuestaDatos = enviarDato.enviar();

            System.out.println("Compis");
            JButton boton[] = new JButton[respuestaDatos.getDatos().size()];
            
            for (int i = 0; i < boton.length; i++) {
                String[] parts = respuestaDatos.getDatos().elementAt(i).split(",");
                if(!parts[1].equals("")){
                    boton[i] = new JButton(parts[1]);
                } else {
                    boton[i] = new JButton(parts[0]);
                }
                
                panelSuperiorDer.add(boton[i]);
            }
            panelSuperiorDer.validate();
            panelSuperiorDer.repaint();
        });

        usuarios.addActionListener((ActionEvent e) -> {
            System.out.println("Usuraios");
        });

        grupos.addActionListener((ActionEvent e) -> {
            System.out.println("Grupos");



        });

        /* amigosConectados = new JLabel();
        amigosConectados.setText("Amigos Conectados");
        panelSuperiorDer.add(amigosConectados);
        String [] meses={"Juan Pablo", "Pablo Juan","Juan Pablo", "Pablo Juan"};
        listaAmigosConectados = new JList(meses);
        panelSuperiorDer.add(listaAmigosConectados);
        //modeloLista=new DefaultListModel();
        //listaAmigosConectados.setModel(modeloLista);
         */
        this.pack();
    }

    public void verificacion() {
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
        //Primero checamos si hay un usuario guardado checando si el archivo de configuracion existe

    }

    /**
     * Crea el archivo de configuracion si nadie habia abierto la app antes
     *
     * @return retorna true si ya habia un archivo guardado
     */
    public static boolean archivoDeConfiguracionExiste() {
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
    public void iniciarSesion() {

        String respuestaJSONString;
        Usuario usuarioGuardado = new Usuario();
        Gson gson = new Gson();
        Socket socketCredenciales;
        usuarioGuardado = usuarioGuardado.obtenerObjeto();
        Vector<String> vector = new Vector(2, 2);

        vector.addElement(usuarioGuardado.getCelular() + "");
        vector.addElement(usuarioGuardado.getNotpasswd());

        EnviarSocket enviarDato = new EnviarSocket("login", vector);
        Respuesta respuestaDatos = enviarDato.enviar();

        if (respuestaDatos.success()) {        //Si la contra fue correcta guardamos los datos

            this.setVisible(true);

            //JOptionPane.showMessageDialog(null, "Sesion iniciada");
            Usuario usuario = new Usuario();
            usuario = usuario.obtenerObjeto();
            usuario.setCelular(Integer.parseInt(respuestaDatos.getDatos().get(0)));
            usuario.setNombre(respuestaDatos.getDatos().get(1));
            usuario.setApellido(respuestaDatos.getDatos().get(2));
            usuario.escribirArchivo(usuario); //Escribimos el archivo

        } else {

            JOptionPane.showMessageDialog(null, "Parece que tu ultima contraseña cambio.");

        }

    }

    /*ACTION LISTENER BTN ENVIAR MSJ*******************BY:NITO***************************************
    
    private void enviarMensaje() {
        
        Gson gson = new Gson();
        Vector<String> vectorSendMsg = new Vector(3,3);
        
        vectorSendMsg.addElement(new Usuario().getCelular);//quien envia el msj
        vecotrSendMsg.addElement();//AQUI VA A QUIEN LO ENVÍA YA SEA GRUPO O USUARIO
        vectorSendMsg.addElement(etxtMsg.getText());//el mensaje
        
        //AQUI IRÍAN UNA CONDICIÓN PARA SABER SI LO ENVÍA A UN GRUPO O UN USUARIO
        
        
        //SI ES MENSAJE LO ENVIA A UN USUARIO:
        Comunicacion enviarMsg = new Comunicacion("MsgUsu",vectorSendMsg);
        
        //SI EL MENSAJE LO ENVIA A UN GRUPO
        
        Comunicacion enviarMsg = new Comunicacion("MsgGrupo", vectorSendMsg);
        
        try{
           
            
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
     */
}
