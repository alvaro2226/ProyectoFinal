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
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.text.JTextComponent;
/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class Util {
    
    /**
     * Comprueba si el email introducido es válido
     * @param email
     * @return Devuelve "true" si el email es válido
     */
    public static boolean validarEmail(String email) {
      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
      return email.matches(regex);
   }
    
    /**
     * Comprueba si las contraseñas coinciden
     * @param contraseña1
     * @param contraseña2 
     * @return  Devuelve "true" si las contraseñas coinciden
     */
    public static boolean comprobarContraseñas(JPasswordField contraseña1, JPasswordField contraseña2){
        
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
     * @param componentes Un list con los componentes de texto
     * @return Devuelve "true" si los campos están vacíos.
     */
    public static boolean comprobarCamposVacíos(List <JTextComponent> componentes){
        
        boolean camposVacios = false;
        
        for (int i=0 ; i < componentes.size() ; i++){
            
            if (componentes.get(i).getText().equals("")){
                camposVacios = true;
            }
        }
        
        return camposVacios;
    }
    
    
    /**
     * Comprueba que el texto de los textComponent que se pasan por parámetros
     * no estén vacíos.
     * @param componente El componentes de texto
     * @return Devuelve "true" si el campo está vacío.
     */
    public static boolean comprobarCamposVacíos(JTextComponent componente){
        
        boolean camposVacios = false;

            if (componente.getText().equals("")){
                camposVacios = true;
            }
        
        
        return camposVacios;
    }
}
