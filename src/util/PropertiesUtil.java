/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.MyLogger;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class PropertiesUtil {

    private static final Logger logger = Logger.getLogger(MyLogger.class.getName());
    private static Properties propertiesFile;
    private static OutputStream output;

    private final static String PROPERTIES_PATH = "resources/configuracion.properties";

    public static void init() {
        try {
            propertiesFile = new Properties();
            output = new FileOutputStream(PROPERTIES_PATH);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean existe() {

        return new File(PROPERTIES_PATH).exists();
    }

    public static void crearProperties() throws IOException {

        File properties = new File(PROPERTIES_PATH);

        File resources = new File("resources");
        //Comprueba si existe la carpeta "resources"
        if (!resources.exists()) {
            resources.mkdir();
        }

        if (!existe()) {
            properties.createNewFile();
        }
    }

    /**
     * Añade las configuraciones predeterminadas de la aplicación.
     *
     * @throws java.io.IOException
     */
    public static void añadirPropiedades() throws IOException {

        if (existe() && propertiesFile != null) {
            propertiesFile.setProperty("app.firstStart", "false");
            propertiesFile.setProperty("database.URL", OperacionesBDD.URL);
            propertiesFile.setProperty("database.USER", OperacionesBDD.USER);
            propertiesFile.setProperty("database.PASSWORD", OperacionesBDD.PASSWORD);

            propertiesFile.store(output, PROPERTIES_PATH);
        } else {
            logger.severe("Error al añadir las propiedades predeterminadas");
        }
    }

    public static Properties getProperties() {

        if (existe()) {

            Properties p = new Properties();
            InputStream ip = null;
            try {
                ip = new FileInputStream(PROPERTIES_PATH);
                p.load(ip);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PropertiesUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PropertiesUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

            return p;
        } else {
            return null;
        }
    }
}
