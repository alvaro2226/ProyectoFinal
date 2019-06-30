/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author �lvaro Morcillo Barbero
 */
public class Util {
    
    /**
     * Comprueba si el email introducido es v�lido
     * @param email
     * @return Devuelve "true" si el email es v�lido
     */
    public static boolean validarEmail(String email) {
      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
      return email.matches(regex);
   }
}
