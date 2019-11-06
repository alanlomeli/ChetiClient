/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Clases.EnviarSocket;
import Clases.Respuesta;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author relg1
 */
public class OlvidoContra extends JDialog {
    
    private JButton botonVerificar, botonRegresar;
    private JLabel titulo, numeroTitulo, preguntaTitulo;
    private JTextField numeroTexto, preguntaTexto;
    private Login login;
    public OlvidoContra(Login login) {
        this.login=login;
        Inicio();

    }

    private void Inicio() {
        //this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Chetis");

        botonVerificar = new JButton("Verificar");
        botonRegresar = new JButton("Regresar");
        titulo = new JLabel("¿Olvidaste tu contraseña?");
        numeroTitulo = new JLabel("Número telefonico:");
        preguntaTitulo = new JLabel("¿Cuál es el nombre del lugar donde naciste?");
        numeroTexto = new JTextField();
        preguntaTexto = new JTextField();



        botonVerificar.addActionListener((ActionEvent e) -> {
            olvido();
        });

        botonRegresar.addActionListener((ActionEvent e) -> {
            this.setVisible(false);
            login.setVisible(true);
        });

        GroupLayout orden = new GroupLayout(this.getContentPane());

        orden.setAutoCreateGaps(true);
        orden.setAutoCreateContainerGaps(true);

        orden.setVerticalGroup(
                orden.createSequentialGroup()
                        .addComponent(titulo)
                        .addComponent(numeroTitulo)
                        .addComponent(numeroTexto)
                        .addComponent(preguntaTitulo)
                        .addComponent(preguntaTexto)
                        .addComponent(botonVerificar)
                        .addComponent(botonRegresar)
        );

        orden.setHorizontalGroup(
                orden.createParallelGroup()
                        .addComponent(titulo)
                        .addComponent(numeroTitulo)
                        .addComponent(numeroTexto)
                        .addComponent(preguntaTitulo)
                        .addComponent(preguntaTexto)
                        .addComponent(botonVerificar)
                        .addComponent(botonRegresar)
        );

        setLayout(orden);
        this.pack();
    }

    private void olvido() {
        Vector<String> vector = new Vector<>(2, 2);
        vector.addElement(numeroTexto.getText());
        vector.addElement(preguntaTexto.getText());

        EnviarSocket olvideContra = new EnviarSocket("recuperarCuenta", vector);
        Respuesta respuestaDatos = olvideContra.enviar();

        if (respuestaDatos.success()) {
            JOptionPane.showMessageDialog(null, "Tu contraseña es: " + respuestaDatos.getDatos());
            this.setVisible(false);
            login.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Ni Dios sabe tu contraseña");
        }
    }
}
