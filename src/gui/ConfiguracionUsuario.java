/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Clases.Comunicacion;
import Clases.EnviarSocket;
import Clases.Respuesta;
import Pojos.Usuario;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 *
 * @author alan
 */
public class ConfiguracionUsuario extends JDialog {

    private JButton btnCambiarNombre, btnVolver, btnCerrar, btnCambiarContra, btnCambiarIP, btnDarkMode;
    private JLabel labelTitulo, labelNombre, labelContra, labelCambiarIP;
    private JPanel lineaNegra;
    private JTextField textNombre, txtApellido;
    private Usuario configuracionGuardada;
    private JPasswordField textCambiarContra;

    public boolean cerrarSesion;
    public boolean darkMode;
    public boolean cambiarIP;

    public ConfiguracionUsuario() {
        configuracionGuardada = new Usuario();
        configuracionGuardada = configuracionGuardada.obtenerObjeto();

        init();
    }

    private void init() {
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        labelTitulo = new JLabel("Configuración de usuario");
        lineaNegra = new JPanel();
        labelNombre = new JLabel("Nombre de usuario:");
        btnDarkMode = new JButton("Dark");
        textNombre = new JTextField(configuracionGuardada.getNombre());
        txtApellido = new JTextField(configuracionGuardada.getApellido());

        btnCambiarNombre = new JButton("Editar");
        labelContra = new JLabel("Contraseña:");
        textCambiarContra = new JPasswordField("estoespurallenadera");

        btnCambiarContra = new JButton("Editar");

        labelCambiarIP = new JLabel("Cambiar IP:");
        btnCambiarIP = new JButton("Cambiar IP");

        btnVolver = new JButton("Volver");
        btnCerrar = new JButton("Cerrar sesión");

        cerrarSesion = false;

        btnCambiarNombre.addActionListener((ActionEvent e) -> { //Para cambiar el nombre
            Vector<String> nombre = new Vector();
            nombre.add(configuracionGuardada.getCelular() + "");
            nombre.add(textNombre.getText());
            nombre.add(txtApellido.getText());
            EnviarSocket cambiarNombre = new EnviarSocket("cambiarNombre", nombre);
            Respuesta respuesta = cambiarNombre.enviar();

            if (respuesta.success()) {
                JOptionPane.showMessageDialog(null, "Actualizadop");

            }

        });

        btnCambiarContra.addActionListener((ActionEvent e) -> {
            if (!String.valueOf(textCambiarContra.getPassword()).equals("estoespurallenadera")) {
                Vector<String> contra = new Vector();
                contra.add(String.valueOf(textCambiarContra.getPassword()));
                EnviarSocket cambiarNombre = new EnviarSocket("cambiarContra", contra);
                Respuesta respuesta = cambiarNombre.enviar();

                if (respuesta.success()) {
                    JOptionPane.showMessageDialog(null, "Contraseña cambiada.");
                    textCambiarContra.setText("estoespurallenadera");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Escribe contraseña primero.");

            }

        });
        btnVolver.addActionListener((ActionEvent e) -> {
            dispose();

        });

        btnCerrar.addActionListener((ActionEvent o) -> {
//                Borramos el archivo de datos del usuario.


            cerrarSesion = true;
            this.setVisible(false);
        });

        btnDarkMode.addActionListener((ActionEvent e) -> {

            if (darkMode) {
                darkMode = false;
            } else {
                darkMode = true;
            }

        });

        btnCambiarIP.addActionListener((ActionEvent e) -> {
            CambiarIP cambiarIP = new CambiarIP();
            cambiarIP.setModal(true);
            cambiarIP.setVisible(true);

        });
        GroupLayout orden = new GroupLayout(this.getContentPane());
        orden.setHorizontalGroup(
                orden.createParallelGroup()
                        .addComponent(labelTitulo, 250, 250, 250)
                        .addComponent(lineaNegra, 250, 250, 250)
                        .addGroup(
                                orden.createSequentialGroup().addComponent(labelNombre, 250, 250, 250).addComponent(btnDarkMode, 100, 100, 100)
                        ).addGroup(
                                orden.createSequentialGroup().addComponent(textNombre, 125, 125, 125).addComponent(txtApellido, 125, 125, 125).addComponent(btnCambiarNombre, 100, 100, 100)
                        ).addComponent(labelContra, 300, 300, 300).addGroup(
                        orden.createSequentialGroup().addComponent(textCambiarContra, 250, 250, 250).addComponent(btnCambiarContra, 100, 100, 100)
                ).addComponent(labelCambiarIP, 300, 300, 300).addComponent(btnCambiarIP, 250, 250, 250).addGroup(
                        orden.createSequentialGroup().addComponent(btnVolver, 100, 100, 100).addComponent(btnCerrar, 150, 150, 150)
                )
        );

        orden.setVerticalGroup(
                orden.createSequentialGroup()
                        .addComponent(labelTitulo)
                        .addComponent(lineaNegra)
                        .addGroup(
                                orden.createParallelGroup().addComponent(labelNombre).addComponent(btnDarkMode)
                        ).addGroup(
                                orden.createParallelGroup().addComponent(textNombre).addComponent(txtApellido).addComponent(btnCambiarNombre)
                        ).addComponent(labelContra).addGroup(
                        orden.createParallelGroup().addComponent(textCambiarContra).addComponent(btnCambiarContra)
                ).addComponent(labelCambiarIP).addComponent(btnCambiarIP).addGroup(
                        orden.createParallelGroup()
                                .addComponent(btnVolver)
                                .addComponent(btnCerrar)
                )
        );

        this.setLayout(orden);

        this.pack();
    }
}
