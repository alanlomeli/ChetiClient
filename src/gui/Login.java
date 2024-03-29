package gui;

import Clases.Comunicacion;
import Clases.EnviarSocket;
import Clases.Respuesta;
import Pojos.Usuario;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.Insets;
import java.awt.font.TextAttribute;
import java.net.Socket;
import java.util.Map;
import javax.swing.*;
//import javax.swing.JPanel;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * @author juanp
 */
public class Login extends JDialog {

    private Socket socketCredenciales;
    private JButton btnRegistrar, btnIngresar, txtForgot;
    private JLabel txtInicia, txtNum, txtPass;
    private JTextField etxtNum;
    private JPasswordField etxtPass;
    private int contadorErrores;
    private VistaChat vistachat;
    private Registro vistaRegistro;
    //private JTextView txtIniciar;

    public Login(VistaChat vistaChat) {
        this.vistachat = vistaChat;
        this.vistaRegistro = new Registro(vistaChat, this);
        start();
    }

    private void start() {
        contadorErrores = 0;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegistrar = new JButton("Registrar");
        btnIngresar = new JButton("Ingresar");
        txtForgot = new JButton("Olvidé mi contraseña");
        btnIngresar.addActionListener((ActionEvent e) -> {
            enviarCredenciales();

        });

        btnRegistrar.addActionListener((ActionEvent e) -> {
            vistaRegistro.setVisible(true);
            this.dispose();
        });

        txtForgot.addActionListener((ActionEvent e) ->{
            OlvidoContra olvido=new OlvidoContra(this);
            olvido.setVisible(true);
            this.setVisible(false);
        });
        txtInicia = new JLabel("<html><h2>Inicia sesión</h2></html>");
        txtPass = new JLabel("Contraseña");
        txtNum = new JLabel("Número Telefónico");
        //txtForgot = new JLabel("<html><font color='blue'><u>Olvidé mi contraseña</u></font></html>");
        etxtNum = new JTextField();
        etxtPass = new JPasswordField();
        /*
        JPanel panel = new JPanel();
        Container cont = getContentPane();
         */
        GroupLayout Login = new GroupLayout(this.getContentPane());
        //this.setSize(500, 500);
        Login.setAutoCreateGaps(true);
        Login.setAutoCreateContainerGaps(true);

        //txtforgot subrayado
        Font font = txtForgot.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);

        txtForgot.setFont(font.deriveFont(attributes));

        //btnIngresar.setMargin(new Insets(20,20,20,20));
        Login.setHorizontalGroup(
                Login.createParallelGroup()
                        .addComponent(txtInicia,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNum,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(etxtNum,
                                200,
                                200,
                                200)
                        .addComponent(txtPass,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(etxtPass,
                                200,
                                200,
                                200)
                        .addGroup(
                                Login.createSequentialGroup()
                                        .addComponent(btnRegistrar,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnIngresar,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                        )
                        .addComponent(txtForgot,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)
        );

        Login.setVerticalGroup(
                Login.createSequentialGroup()
                        .addComponent(txtInicia,
                                50,
                                50,
                                50)
                        .addComponent(txtNum,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(etxtNum,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPass,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(etxtPass,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addGroup(
                                Login.createParallelGroup()
                                        .addComponent(btnRegistrar,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnIngresar,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                        )
                        .addComponent(txtForgot,
                                50,
                                50,
                                50)
        );

        setLayout(Login);
        this.pack();

    }

    private void enviarCredenciales() {

        Vector<String> vector = new Vector<>(2, 2);

        vector.addElement(etxtNum.getText());
        vector.addElement(String.valueOf(etxtPass.getPassword()));

        EnviarSocket enviarDato = new EnviarSocket("login", vector);
        Respuesta respuestaDatos = enviarDato.enviar();

        if (respuestaDatos.success()) {        //Si la contra fue correcta guardamos los datos


            this.setVisible(false); //Hace el login invisible
            JOptionPane.showMessageDialog(null, "Sesion iniciada");
            Usuario usuario = new Usuario();
            usuario = usuario.obtenerObjeto();
            usuario.setCelular(Long.parseLong(respuestaDatos.getDatos().get(0)));
            usuario.setNombre(respuestaDatos.getDatos().get(1));
            usuario.setApellido(respuestaDatos.getDatos().get(2));
            usuario.setNotpasswd(String.valueOf(etxtPass.getPassword()));
            usuario.escribirArchivo(usuario); //Escribimos el archivo
            vistachat.obtenerListaUsuarios();
            vistachat.setVisible(true);

        } else {

            contadorErrores++;
            if (contadorErrores == 3) {

                vistaRegistro.setVisible(true);
                this.setVisible(false); //Hace el login invisible
            }
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta");

        }

    }
}
