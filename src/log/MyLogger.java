/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.*;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class MyLogger {

    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());
    private static FileHandler fh = null;

    public static void init() {
        try {
            File resources = new File("resources");
            //Comprueba si existe la carpeta "resources"
            if(!resources.exists()){
                resources.mkdir();
            }
            fh = new FileHandler("resources/log.log", false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        Logger l = Logger.getLogger("");
        fh.setFormatter(new SimpleFormatter());
        l.addHandler(fh);
        l.setLevel(Level.CONFIG);
        
        logger.info("Track your Order iniciado...");
    }

    /**
     * Esta funcion nos permite convertir el stackTrace en un String, necesario
     * para poder imprimirlos al log debido a cambios en como Java los maneja
     * internamente
     *
     * @param e Excepcion de la que queremos el StackTrace
     * @return StackTrace de la excepcion en forma de String
     */
    static String getStackTrace(Exception e) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        e.printStackTrace(pWriter);
        return sWriter.toString();
    }

}
