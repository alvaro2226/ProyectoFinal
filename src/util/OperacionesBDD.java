/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;
import log.MyLogger;

/**
 * Contiene datos del servidor de base de datos, asi como métodos para
 * conectar,desconectar y realizar transacciones.
 *
 * @author Álvaro Morcillo Barbero
 */
public class OperacionesBDD {

    public static String URL = "jdbc:mysql://localhost:3306/mydb?zeroDateTimeBehavior=convertToNull";
    public static String USER = "root";
    public static String PASSWORD = "root";
    private Connection conexion;
    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());
    private Properties properties;
    
    public OperacionesBDD(String URL, String USER, String PASSWORD) {
        this.URL = URL;
        this.USER = USER;
        this.PASSWORD = PASSWORD;

    }
    
    public OperacionesBDD(){
        
    }

    public void iniciarConexion() throws ClassNotFoundException, SQLException {

        properties = PropertiesUtil.getProperties();
        if (conexion == null) {
            Class.forName("com.mysql.jdbc.Driver");
            
            //Recibe los parametros del fichero de configuracion
            conexion = (Connection) DriverManager.getConnection(properties.getProperty("database.URL"), 
                    properties.getProperty("database.USER"), 
                    properties.getProperty("database.PASSWORD"));
            
            conexion.setAutoCommit(false);
            
            logger.info("Conexion con la base de datos establecida correctamente.");
        }else{
            logger.warning("Se ha intentado establecer una conexion con la base de"
                    + "datos cuando ya se estaba conectado");
        }

    }

    public void cerrarConexion() throws SQLException {

        if (conexion != null) {
            conexion.close();
            
            logger.info("Se ha finalizado la conexion con la base de datos correctamente");
        }else{
            logger.warning("La conexion con la base de datos ya estaba cerrada");
        }

    }

    /**
     * Inserta en la base de datos un nuevo registro con los datos de la empresa
     * que se introducen mediante parametros.
     *
     * @param nombre
     * @param formaJuridica
     * @param CIF
     * @param email
     * @param emailPaypal
     * @param calle
     * @param localidad
     * @param provincia
     * @param codigoPostal
     * @param pais
     */
    public void introducirEmpresa(String nombre, String formaJuridica, String CIF,
            String email, String emailPaypal, String calle, String localidad,
            String provincia, String codigoPostal, String pais) throws SQLException {

        boolean transaccionCompletada = false;
        
        //Comprueba que no exista otra empresa
        Statement st = conexion.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM datos_empresa");

        if (rs.next()) {
            //Ya existe una empresa, por tanto no se hace nada
            logger.info("Ya existe una empresa.");
        } else {
            //No existe ninguna empresa, por tanto se procede a introducir una
            logger.info("Se procede a introducir los datos de la nueva empresa");
            //Solo hay una empresa, por tanto la primary key es "1" para ambos registros
            String queryDireccion = "INSERT INTO direccion VALUES (1,?, ?, ?, ?, ?);";
            PreparedStatement psDireccion = conexion.clientPrepareStatement(queryDireccion);

            psDireccion.setString(1, calle);
            psDireccion.setString(2, localidad);
            psDireccion.setString(3, provincia);
            psDireccion.setString(4, codigoPostal);
            psDireccion.setString(5, pais);

            psDireccion.executeUpdate();

            //Si el email es válido ->
            if (Util.validarEmail(email) && Util.validarEmail(emailPaypal)) {

                //Solo hay una empresa, por tanto la primary key es "1" para ambos registros
                String queryEmpresa = "INSERT INTO datos_empresa VALUES (1, ?, ?, ?, 1, ?, ?);";
                PreparedStatement psEmpresa = conexion.clientPrepareStatement(queryEmpresa);

                psEmpresa.setString(1, nombre);
                psEmpresa.setString(2, formaJuridica);
                psEmpresa.setString(3, CIF);
                psEmpresa.setString(4, email);
                psEmpresa.setString(5, emailPaypal);

                psEmpresa.executeUpdate();
                conexion.commit();
                transaccionCompletada = true;
                
            } else {
                logger.info("Email introducido no válido");
                
            }

            if (transaccionCompletada) {

                logger.info("Transaccion completada. Se ha añadido una empresa"
                        + "a la base de datos");
            } else {
                logger.info("No se ha completado la transaccion. No se ha "
                        + "podido añadir la empresa a la base de datos");
            }

        }
    }

}
