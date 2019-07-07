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
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.MyLogger;
import ui.Frame_Bienvenida;
import util.OperacionesBDD;
import util.PropertiesUtil;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class Main {

    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());

    public static void main(String[] args) {

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

            //--------------------------
            //Inicia la app
            new Frame_Bienvenida().setVisible(true);
            
            logger.info("Se ha cerrado la aplicación.");
            

    }

}

