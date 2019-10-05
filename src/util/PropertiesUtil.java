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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Set;
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
    private static InputStream ip;

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
            propertiesFile.setProperty("app.firstTime", "true");
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
            ip = null;
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

    public static void añadirBDD(String URL, String USER, String PASSWORD, String firstTime) throws IOException {

        if (existe() && propertiesFile != null) {

            propertiesFile.setProperty("app.firstTime", firstTime);
            propertiesFile.setProperty("database.URL", URL);
            propertiesFile.setProperty("database.USER", USER);
            propertiesFile.setProperty("database.PASSWORD", PASSWORD);
            propertiesFile.store(output, PROPERTIES_PATH);
        } else {
            logger.severe("Error los datos de la base de datos al fichero de configuracion");
        }
    }

    /*
    public static void setFirstTime(boolean bool) throws IOException{

        if(bool){
            propertiesFile.setProperty("app.firstTime", "true");
        }else{
            propertiesFile.setProperty("app.firstTime", "false");
        }
        propertiesFile.store(output, PROPERTIES_PATH);
    }
     */
}
