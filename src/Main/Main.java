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
package Main;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.MyLogger;
import ui.bienvenida.Frame_Login;
import ui.bienvenida.Frame_Bienvenida;
import ui.principal.Frame_Principal;
import util.OperacionesBDD;
import util.PropertiesUtil;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class Main {

    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());
    private static Properties properties = null;

    public static void main(String[] args) {

        new Frame_Principal().setVisible(true);
        //--------------------------
        //Inicia el logger
        MyLogger.init();

        //--------------------------
        //Comprubea que el archivo de configuracion existe, si no existe lo crea
        //y lo carga con la configuracion predeterminada
        try {
            if (!PropertiesUtil.existe()) {
                PropertiesUtil.init();
                PropertiesUtil.crearProperties();
                PropertiesUtil.añadirPropiedades();
            }

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        properties = PropertiesUtil.getProperties();;
        //Comprueba si es la primera vez que se inicia la aplicación
        if (properties.getProperty("app.firstTime").equals("true")) {
            //Es la primera vez, por tanto se tiene que mostrar el frame
            // de bienvenida
            //
            //OperacionesBDD.crearBDD();
            //OperacionesBDD.añadirEmpresa("nombre", "fm", "cif", "email", "paypal", "calle", "localidad", "prov", "cp", "pais", "tele");
            //OperacionesBDD.añadirAdmin("askmk", "asdsddddddd");
            new Frame_Bienvenida().setVisible(true);
        } else {
            //Se tiene que mostrar el frame de iniciar sesión
            //new Frame_Login().setVisible(true);
        }

    }

}
