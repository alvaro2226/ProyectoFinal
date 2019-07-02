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
        
        logger.info("OrderTracker iniciado...");
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
