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

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.MyLogger;

/**
 * Contiene datos del servidor de base de datos, asi como métodos para
 * conectar,desconectar y realizar transacciones. Se tiene que instanciar un
 * objeto de esta clase, iniciar la conexion mediante "iniciarConexion()" y
 * cerrarla desde "cerrarConexion()". Una vez abierta la conexion, podemos
 * utilizar los metodos de esta clase para interactuar con la bdd
 *
 * @author Álvaro Morcillo Barbero
 */
public class OperacionesBDD {

    public static String URL = "jdbc:mysql://localhost:3306/mydb";
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
        } else {
            logger.warning("Se ha intentado establecer una conexion con la base de"
                    + "datos cuando ya se estaba conectado");
        }

    }

    /**
     * Metodo estático que comprueba se realiza una conexion exitosa a la base
     * de datos segun los parámetros introducidos.
     *
     * @param URL
     * @param USER
     * @param PASSWORD
     * @return true si la conexion se realiza correctamente
     */
    public static boolean comprobarConexion(String URL, String USER, String PASSWORD) {

        boolean conexionEstablecida = false;
        logger.info("Comprobando conexión:"
                + "\n \t URL:" + URL
                + "\n \t USER:" + USER
                + "\n \t PASSWORD:" + PASSWORD);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexionPrueba;

            conexionPrueba = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);

            conexionEstablecida = true;
            conexionPrueba.close();
            logger.info("Conexion existosa");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            
            conexionEstablecida = false;
        }

        if(!conexionEstablecida){
            logger.info("Conexion fallida");
        }
        return conexionEstablecida;
    }

    public void cerrarConexion() throws SQLException {

        if (conexion != null) {
            conexion.close();

            logger.info("Se ha finalizado la conexion con la base de datos correctamente");
        } else {
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
