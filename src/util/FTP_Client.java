/*
 * Copyright (C) 2021 alvar
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import log.MyLogger;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author alvar
 */
public class FTP_Client {
    
    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());
    
    public static final String server = "programaloalvaro.es";
    private final int port = 21;
    private final String user = "u544052383.alvaromb";
    private final String password = "OrderTracker2021";
    //public static final String RUTA_IMAGENES = "fotos/productos/";
    private FTPClient ftp;
    
    public void iniciarConexion() throws IOException {
        
        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftp.connect(server, port);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        
        int respuesta = ftp.getReplyCode();
        
        if (!FTPReply.isPositiveCompletion(respuesta)) {
            ftp.disconnect();
            
            System.out.println("Error al conectar al servidor FTP \n "
                    + "URL: " + server + ":" + port + "\n "
                    + "Usuario: " + user + "\n "
                    + "Contraseña: " + password);
            
            logger.severe("Error al conectar al servidor FTP \n "
                    + "URL: " + server + ":" + port + "\n "
                    + "Usuario: " + user + "\n "
                    + "Contraseña: " + password);
            
            throw new IOException("Error al conectar al servidor FTP");
            
        }
        
        ftp.login(user, password);
    }
    
    public void cerrarConexion() throws IOException {
        ftp.disconnect();
    }
    
    public void descargarArchivo(String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        ftp.retrieveFile(source, out);
    }
    
    public void subirArchivo(File file, String path) throws IOException {
        
        if (file != null){
           ftp.storeFile(path, new FileInputStream(file)); 
        }
        
    }
    
    public void borrarArchivo(String ruta) throws IOException {
        
        if(ruta !=null){
            ftp.deleteFile(ruta);
        }
        
        
    }
}
