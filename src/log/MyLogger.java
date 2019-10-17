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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.logging.*;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class MyLogger {

    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());
    private static FileHandler fh = null;
    private static File logdir = new File("resources" + File.separator + "logs");
    private static File logFile;

    public static void init() {

        //Comprueba si existe la carpeta "resources"
        if (!logdir.exists()) {
            logdir.mkdirs();
        }
        
        logFile = new File(logdir.getPath() + File.separator + "log_" + LocalDate.now() + ".txt");

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.getLogger(MyLogger.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        try {
            fh = new FileHandler(logFile.getPath(), false);
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

    /**
     * Borra el archivo .lck que se genera con el log. Borra los .lck de días
     * anteriores. Este método se debe ejecutar cada vez que se crea el log
     */
    private static void borrarLck() {

        FilenameFilter filtro = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".lck");
            }
        };

        File[] archivos = logdir.listFiles(filtro);
        File aux = null;

        aux = new File(logdir.getPath() + File.separator + "log_" + LocalDate.now() + ".txt.lck");

        for (File file : archivos) {

            //Si el nombre no es igual al lck de hoy, lo borra
            if (!file.getName().equals(aux.getName())) {
                file.delete();
            }

        }
    }

}
