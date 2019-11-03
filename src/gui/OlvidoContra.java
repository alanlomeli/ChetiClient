/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author relg1
 */
public class OlvidoContra extends JDialog {
    
    private JButton botonVerificar, botonRegresar;
    private JLabel titulo, numeroTitulo, preguntaTitulo;
    private JTextField numeroTexto, preguntaTexto;

    public OlvidoContra() {
        Inicio();
    }

    private void Inicio() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Chetis");

        botonVerificar = new JButton("Verificar");
        botonRegresar = new JButton("Regresar");
        titulo = new JLabel("¿Olvidaste tu contraseña?");
        numeroTitulo = new JLabel("Número telefonico:");
        preguntaTitulo = new JLabel("¿Cuál es el nombre del lugar donde naciste?");
        numeroTexto = new JTextField("Número de teléfono");
        preguntaTexto = new JTextField("Escribe el lugar");

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
}
