/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Pojos.Usuario;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author alan
 */
public class CambiarIP extends JDialog {

    private JButton btnAceptar;
    private JLabel labelInstrucciones;
    private JTextField textFieldIP;

    public CambiarIP() {
        textFieldIP = new JTextField();

        init();

    }

    public CambiarIP(String IP) {
        textFieldIP = new JTextField(IP);

        init();

    }

    private void init() {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        labelInstrucciones = new JLabel("Cambiar dirección IP: ");
        btnAceptar = new JButton("Aceptar");

        btnAceptar.addActionListener((ActionEvent e) -> {
            if (textFieldIP.getText().length() < 7 || textFieldIP.getText().length() > 15) {
                System.out.println(textFieldIP.getText().length());
                JOptionPane.showMessageDialog(null, "IP erronea");
            } else {
                Usuario guardarIP = new Usuario();
                if (guardarIP.obtenerObjeto() != null) {
                    guardarIP = guardarIP.obtenerObjeto();
                }
                guardarIP.setIpServer(textFieldIP.getText());
                guardarIP.escribirArchivo(guardarIP);
                dispose();

            }
        });

        GroupLayout orden = new GroupLayout(this.getContentPane());
        orden.setHorizontalGroup(
                orden.createParallelGroup()
                        .addComponent(labelInstrucciones, 200, 200, 200)
                        .addComponent(textFieldIP, 200, 200, 200)
                        .addGroup(
                                orden.createSequentialGroup().addComponent(btnAceptar, 200, 200, 200)
                        )
        );

        orden.setVerticalGroup(
                orden.createSequentialGroup()
                        .addComponent(labelInstrucciones)
                        .addComponent(textFieldIP)
                        .addGroup(
                                orden.createParallelGroup().addComponent(btnAceptar)
                        )
        );

        this.setLayout(orden);

        this.pack();
    }
}
