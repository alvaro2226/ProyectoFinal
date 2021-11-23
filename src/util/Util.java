/*
 * Copyright (C) 2019 Álvaro Morcillo Barbero
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package util;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class Util {

    /**
     * Comprueba si el email introducido es válido
     *
     * @param email
     * @return Devuelve "true" si el email es válido
     */
    public static boolean validarEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public static void resultSetToTableModel(ResultSet rs, JTable table) throws SQLException {
        //Create new table model
        DefaultTableModel tableModel = new DefaultTableModel();

        //Retrieve meta data from ResultSet
        ResultSetMetaData metaData = rs.getMetaData();

        //Get number of columns from meta data
        int columnCount = metaData.getColumnCount();

        //Get all column names from meta data and add columns to table model
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            tableModel.addColumn(metaData.getColumnLabel(columnIndex));
        }

        //Create array of Objects with size of column count from meta data
        Object[] row = new Object[columnCount];

        //Scroll through result set
        while (rs.next()) {
            //Get object from column with specific index of result set to array of objects
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            //Now add row to table model with that array of objects as an argument
            tableModel.addRow(row);
        }

        //Now add that table model to your table and you are done :D
        table.setModel(tableModel);
    }

    /**
     * Comprueba si las contraseñas coinciden
     *
     * @param contraseña1
     * @param contraseña2
     * @return Devuelve "true" si las contraseñas coinciden
     */
    public static boolean comprobarContraseñas(JPasswordField contraseña1, JPasswordField contraseña2) {

        String contra1 = String.valueOf(contraseña1.getPassword());
        String contra2 = String.valueOf(contraseña2.getPassword());

        return contra1.equals(contra2);
    }

    /**
     * Este método recibe un label, el cual cambiará el color y el texto según
     * los parámetros introducidos. ATENCION hace visible al label
     *
     * @param label El componente de la consola
     * @param bool Si es "true" se muestra en verde. "false" en rojo
     * @param mensaje El mensaje que va a mostrar el componente
     */
    public static void mensajeConsola(JLabel label, String mensaje, boolean bool) {
        label.setText(mensaje);
        if (bool) {
            label.setForeground(Color.GREEN.darker());
        } else {
            label.setForeground(Color.RED);
        }

        if (!label.isVisible()) {
            label.setVisible(true);
        }
    }

    /**
     * Comprueba que el texto de los textComponent que se pasan por parámetros
     * no estén vacíos.
     *
     * @param componentes Un list con los componentes de texto
     * @return Devuelve "true" si los campos están vacíos.
     */
    public static boolean comprobarCamposVacíos(List<JTextComponent> componentes) {

        boolean camposVacios = false;

        for (int i = 0; i < componentes.size(); i++) {

            if (componentes.get(i).getText().equals("")) {
                camposVacios = true;
            }
        }

        return camposVacios;
    }

    /**
     * Comprueba que el texto de los textComponent que se pasan por parámetros
     * no estén vacíos.
     *
     * @param componente El componentes de texto
     * @return Devuelve "true" si el campo está vacío.
     */
    public static boolean comprobarCamposVacíos(JTextComponent componente) {

        boolean camposVacios = false;

        if (componente.getText().equals("")) {
            camposVacios = true;
        }

        return camposVacios;
    }

    public static Image getImagenIcono() {

        Toolkit kit = Toolkit.getDefaultToolkit();
        Image icono = kit.createImage("src/ui/images/icono.png");

        return icono;

    }

    //Pone en mayusculas la primera letra un string, despues de los espacios en blanco
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
