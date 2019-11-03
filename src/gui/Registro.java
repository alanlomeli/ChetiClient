/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Clases.EnviarSocket;
import Clases.Respuesta;
import Pojos.Usuario;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author alan
 */
public class Registro extends JFrame {

    private JLabel LRegistrate, LApellido, LNombre, LNumTel, LContrasena, LPregSeg, LLugNac;
    JTextField LugNac, NumTel, Nombre, Apellido;
    JPasswordField Contrasena;
    private JPanel Registro;
    JButton BRegistrar, BCancelar;

    public Registro() {
        this.setResizable(false);
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        //Label
        LRegistrate = new JLabel("REGISTRATE");
        LNombre = new JLabel("Nombre:");
        LNumTel = new JLabel("Número telefónico:");
        LContrasena = new JLabel("Contraseña:");
        LPregSeg = new JLabel("Pregunta de seguridad:");
        LApellido = new JLabel("Apellido: ");
        LLugNac = new JLabel("¿Cuál es el nombre del lugar donde naciste?");
        //TextField
        Nombre = new JTextField();//"Ingrese su nombre"
        NumTel = new JTextField();//"Ingrese su número telefónico"
        Contrasena = new JPasswordField();//"Ingrese la contraseña"
        LugNac = new JTextField();//"Ingrese su respuesta"
        Apellido = new JTextField();
        //Button
        BRegistrar = new JButton("Registrar");
        BCancelar = new JButton("Cancelar");
        BRegistrar.addActionListener((ActionEvent e) -> {
            btnRegistrar();
        });
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(LRegistrate)
                        .addComponent(LNombre)
                        .addComponent(Nombre)
                        .addComponent(LApellido)
                        .addComponent(Apellido)
                        .addComponent(LNumTel)
                        .addComponent(NumTel)
                        .addComponent(LContrasena)
                        .addComponent(Contrasena)
                        .addComponent(LPregSeg)
                        .addComponent(LLugNac)
                        .addComponent(LugNac)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(BRegistrar)
                                .addComponent(BCancelar))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(LRegistrate)
                        .addComponent(LNombre)
                        .addComponent(Nombre)
                        .addComponent(LApellido)
                        .addComponent(Apellido)
                        .addComponent(LNumTel)
                        .addComponent(NumTel)
                        .addComponent(LContrasena)
                        .addComponent(Contrasena)
                        .addComponent(LPregSeg)
                        .addComponent(LLugNac)
                        .addComponent(LugNac)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(BRegistrar)
                                .addComponent(BCancelar))
        );

        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //this.setSize(1280,720);
    }

    private void btnRegistrar() {
        Vector<String> vector = new Vector<>(5, 5);
//Celular, Nombre, Apellido, Passwd, Respuesta
        vector.add(NumTel.getText());
        vector.add(Nombre.getText());
        vector.add(Apellido.getText());
        vector.add(String.valueOf(Contrasena.getPassword()));
        vector.add(LugNac.getText());

        EnviarSocket enviarDato = new EnviarSocket("registro", vector);

        Respuesta respuestaDatos = enviarDato.enviar();

        if (respuestaDatos.success()) {        //Si el registro fue correcta guardamos los datos
            VistaChat abrirchat = new VistaChat(); //Abre el chat
            abrirchat.setVisible(true);
            this.setVisible(false); //Hace el login invisible
            JOptionPane.showMessageDialog(null, "Se Registró correctamente");
            Usuario usuario = new Usuario();
            usuario = usuario.obtenerObjeto();

            usuario.setCelular(Long.parseLong(respuestaDatos.getDatos().get(0)));
            usuario.setNombre(respuestaDatos.getDatos().get(1));
            usuario.setApellido(respuestaDatos.getDatos().get(2));
            usuario.setNotpasswd(String.valueOf(Contrasena.getPassword()));
            usuario.escribirArchivo(usuario); //Escribimos el archivo


        } else {

            JOptionPane.showMessageDialog(null, "Se Registró Incorrectamente");

        }
    }
}
