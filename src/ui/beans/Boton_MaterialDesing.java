/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.beans;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.Serializable;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class Boton_MaterialDesing extends JLabel implements Serializable, MouseListener {

    private File imagenPrincipal, imagenSombreada;
    private ImageIcon iconoPrincipal, iconoSombreado;
    private Color colorSombreado;
    private boolean mostrarSombreado;

    public boolean isMostrarSombreado() {
        return mostrarSombreado;
    }

    public void setMostrarSombreado(boolean mostrarSombreado) {
        this.mostrarSombreado = mostrarSombreado;
    }

    public Color getColorSombreado() {
        return colorSombreado;
    }

    public void setColorSombreado(Color colorSombreado) {
        this.colorSombreado = colorSombreado;
    }

    public File getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(File imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
        iconoPrincipal = new ImageIcon(imagenPrincipal.getPath());
        this.setIcon(iconoPrincipal);
    }

    public File getImagenSombreada() {
        return imagenSombreada;
    }

    public void setImagenSombreada(File imagenSombreada) {
        this.imagenSombreada = imagenSombreada;
        this.iconoSombreado = new ImageIcon(imagenSombreada.getPath());
    }

    public Boton_MaterialDesing() {
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent me) {

    }

    @Override
    public void mousePressed(MouseEvent me) {

        if (mostrarSombreado) {
            if (this.colorSombreado != null) {
                this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, getColorSombreado(), getColorSombreado().darker()));
            } else {
                this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.gray, Color.gray));
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent me) {

        if (mostrarSombreado) {
            this.setBorder(BorderFactory.createEmptyBorder());
        }

    }

    @Override
    public void mouseEntered(MouseEvent me) {

        if (this.imagenPrincipal != null && this.imagenSombreada != null) {
            this.setIcon(iconoSombreado);
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if (this.imagenPrincipal != null && this.imagenSombreada != null) {
            this.setIcon(iconoPrincipal);
        }
    }

}
