/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

