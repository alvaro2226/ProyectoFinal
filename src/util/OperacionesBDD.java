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
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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

    public static String URL = "jdbc:mysql://localhost:3306";
    public static String USER = "root";
    public static String PASSWORD = "root";
    private static Connection conexion;
    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());
    private static final Properties properties = PropertiesUtil.getProperties();

    // ========== CAMPOS NO VARIABLES DE LA BASE DE DATOS =============
    // TIPOS DE USUARIOS
    private static final int TIPO_ADMIN = 1;
    private static final int TIPO_EMPLEADO = 2;
    private static final int TIPO_CLIENTE = 3;
    // ========== ======================================= =============

    //Escribe en el fichero de configuracion los nuevos parametros
    public static void cambiarBDD(String URL, String USER, String PASSWORD) {

        try {
            PropertiesUtil.añadirBDD(URL, USER, PASSWORD, "true");
        } catch (IOException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void iniciarConexion() throws ClassNotFoundException, SQLException {

        Properties p = PropertiesUtil.getProperties();

        if (conexion == null || conexion.isClosed()) {
            Class.forName("com.mysql.jdbc.Driver");

            //Recibe los parametros del fichero de configuracion
            conexion = (Connection) DriverManager.getConnection(p.getProperty("database.URL"),
                    p.getProperty("database.USER"),
                    p.getProperty("database.PASSWORD"));

            conexion.setAutoCommit(false);

            logger.info("Se ha establecido una conexion con la base de datos.");
        } else {
            logger.warning("Se ha intentado establecer una conexion con la base de "
                    + "datos cuando ya se estaba conectado");
        }

    }

    private static void cerrarConexion() throws SQLException {

        if (conexion != null) {
            conexion.close();

            logger.info("Se ha finalizado la conexion con la base de datos correctamente");
        } else {
            logger.warning("La conexion con la base de datos ya estaba cerrada");
        }

    }

    /**
     * Devuelve true si el usuario y la contraseña coinciden en la base de datos
     *
     * @param user
     * @param password
     * @return
     */
    public static boolean iniciarSesion(String user, String password) throws ClassNotFoundException, SQLException {

        iniciarConexion();
        boolean inicioCorrecto = true;
        String query = "SELECT * FROM ordertracker.usuario WHERE usuario_contraseña=? AND usuario_nombreUsuario=?";
        logger.info("Intento de inicio de sesión: \n "
                + "Usuario: " + user + "\n" + " Contraseña: " + password);
        
            PreparedStatement pst = conexion.clientPrepareStatement(query);
            pst.setString(1, password);
            pst.setString(2, user);
            ResultSet rs = pst.executeQuery();
            
            if(!rs.first()){
                inicioCorrecto = false;
            }

        
        cerrarConexion();
        return inicioCorrecto;
    }

    /**
     * Comprueba se realiza una conexion exitosa a la base de datos segun los
     * parámetros introducidos.
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

            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
            conexionEstablecida = false;
        }

        if (!conexionEstablecida) {
            logger.info("Conexion fallida");
        }
        return conexionEstablecida;
    }

    /**
     * Inserta en la base de datos un nuevo registro con los datos de la empresa
     * que se introducen mediante parametros. Primero comprueba que no exista
     * otra empresa.
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
    public static void añadirEmpresa(String nombre, String formaJuridica, String CIF,
            String email, String emailPaypal, String calle, String localidad,
            String provincia, String codigoPostal, String pais, String telefono) {

        Statement st = null;
        ResultSet rs = null;
        try {

            iniciarConexion();

            //Comprueba que no exista otra empresa
            st = conexion.createStatement();
            rs = st.executeQuery("SELECT * FROM OrderTracker.datos_empresa");

            if (rs.next()) {
                //Ya existe una empresa, por tanto no se hace nada
                logger.info("Ya existe una empresa.");
            } else {
                //No existe ninguna empresa, por tanto se procede a introducir una
                logger.info("Se procede a introducir los datos de la nueva empresa");
                //Solo hay una empresa, por tanto la primary key es "1" para ambos registros
                String queryDireccion = "INSERT INTO OrderTracker.direccion VALUES (null,?, ?, ?, ?, ?);";
                PreparedStatement psDireccion = conexion.prepareStatement(queryDireccion);

                psDireccion.setString(1, calle);
                psDireccion.setString(2, localidad);
                psDireccion.setString(3, provincia);
                psDireccion.setString(4, codigoPostal);
                psDireccion.setString(5, pais);

                psDireccion.execute();

                conexion.commit();

                String queryEmpresa = "INSERT INTO OrderTracker.datos_empresa VALUES (null, ?, ?, ?,"
                        + " (SELECT direccion_id FROM OrderTracker.direccion WHERE direccion_calle = ? AND direccion_codigoPostal = ? )"
                        + ", ?, ?, ?);";
                PreparedStatement psEmpresa = conexion.clientPrepareStatement(queryEmpresa);

                psEmpresa.setString(1, nombre);
                psEmpresa.setString(2, formaJuridica);
                psEmpresa.setString(3, CIF);
                psEmpresa.setString(4, calle);
                psEmpresa.setString(5, codigoPostal);
                psEmpresa.setString(6, email);
                psEmpresa.setString(7, emailPaypal);
                psEmpresa.setString(8, telefono);
                psEmpresa.executeUpdate();

                conexion.commit();
                logger.info("Transaccion completada. Se ha añadido una empresa"
                        + "a la base de datos");

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            try {
                conexion.rollback();
                logger.info("No se ha completado la transaccion. No se ha "
                        + "podido añadir la empresa a la base de datos");
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                cerrarConexion();
                st.close();
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static void añadirAdmin(String user, String password) {

        PreparedStatement pst = null;
        try {
            /*
            + "  `usuario_id` INT AUTO_INCREMENT,\n"
            + "  `usuario_nombreUsuario` VARCHAR(15) NOT NULL,\n"
            + "  `usuario_email` VARCHAR(100) NULL,\n"
            + "  `usuario_contraseña` VARCHAR(25) NOT NULL,\n"
            + "  `usuario_nombre` VARCHAR(45) NULL,\n"
            + "  `usuario_Apellidos` VARCHAR(200) NULL,\n"
            + "  `usuario_telefono` VARCHAR(12) NULL,\n"
            + "  `usuario_fechaCreacion` TIMESTAMP NULL,\n"
            + "  `usuario_tipoUsuario_id` INT NULL,\n"
            + "  `usuario_direccion_id` INT NULL,\n"
            + "  `usuario_dni` VARCHAR(9) NULL,\n"
             */

            iniciarConexion();
            String query = "INSERT INTO `OrderTracker`.`USUARIO`"
                    + "(usuario_nombreUsuario,usuario_contraseña,usuario_tipoUsuario_id,usuario_fechaCreacion) "
                    + "VALUES(?,?,?,?);";
            pst = conexion.prepareStatement(query);

            pst.setString(1, user);
            pst.setString(2, password);
            pst.setInt(3, TIPO_ADMIN);

            Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
            pst.setTimestamp(4, ts);

            pst.executeUpdate();

            conexion.commit();

            logger.info("Admin \"" + user + " / " + password + "\" añadido");
        } catch (ClassNotFoundException | SQLException ex) {
            logger.warning("No se ha podido añadir el admin.");
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                cerrarConexion();
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * Crea el esquema de la bdd en el servidor. Añade todas las tablas. Tambien
     * inserta los tipos de usuario,los métodos de pago Y el estado de pedido.
     */
    public static void crearBDD() {

        Statement st = null;

        try {
            iniciarConexion();
            st = conexion.createStatement();

            //st.execute("DROP SCHEMA `OrderTracker` ;");
            st.execute("CREATE SCHEMA IF NOT EXISTS `OrderTracker` DEFAULT CHARACTER SET utf8 ;");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`TIPO_USUARIO` (\n"
                    + "  `tipoUsuario_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `tipoUsuario_tipo` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`tipoUsuario_id`))\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`DIRECCION` (\n"
                    + "  `direccion_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `direccion_calle` VARCHAR(45) NOT NULL,\n"
                    + "  `direccion_localidad` VARCHAR(45) NOT NULL,\n"
                    + "  `direccion_provincia` VARCHAR(45) NOT NULL,\n"
                    + "  `direccion_codigoPostal` VARCHAR(45) NOT NULL,\n"
                    + "  `direccion_pais` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`direccion_id`))\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`TIPO_USUARIO` (\n"
                    + "  `tipoUsuario_id` INT AUTO_INCREMENT,\n"
                    + "  `tipoUsuario_tipo` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`tipoUsuario_id`))\n"
                    + "ENGINE = InnoDB;");

            st.executeUpdate("INSERT INTO `OrderTracker`.`TIPO_USUARIO`"
                    + "VALUES (null,'ADMIN')");
            st.executeUpdate("INSERT INTO `OrderTracker`.`TIPO_USUARIO`"
                    + "VALUES (null,'EMPLEADO')");
            st.executeUpdate("INSERT INTO `OrderTracker`.`TIPO_USUARIO`"
                    + "VALUES (null,'CLIENTE')");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`USUARIO` (\n"
                    + "  `usuario_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `usuario_nombreUsuario` VARCHAR(15) NOT NULL UNIQUE,\n"
                    + "  `usuario_email` VARCHAR(100) NULL,\n"
                    + "  `usuario_contraseña` VARCHAR(25) NOT NULL,\n"
                    + "  `usuario_nombre` VARCHAR(45) NULL,\n"
                    + "  `usuario_Apellidos` VARCHAR(200) NULL,\n"
                    + "  `usuario_telefono` VARCHAR(12) NULL,\n"
                    + "  `usuario_fechaCreacion` TIMESTAMP NULL,\n"
                    + "  `usuario_tipoUsuario_id` INT NULL,\n"
                    + "  `usuario_direccion_id` INT NULL,\n"
                    + "  `usuario_dni` VARCHAR(9) NULL,\n"
                    + "  PRIMARY KEY (`usuario_id`),\n"
                    + "  INDEX `fk_usuario_TipoUsuario_idx` (`usuario_tipoUsuario_id` ASC) VISIBLE,\n"
                    + "  INDEX `fk_USUARIO_DIRECCION1_idx` (`usuario_direccion_id` ASC) VISIBLE,\n"
                    + "  CONSTRAINT `fk_usuario_TipoUsuario`\n"
                    + "    FOREIGN KEY (`usuario_tipoUsuario_id`)\n"
                    + "    REFERENCES `OrderTracker`.`TIPO_USUARIO` (`tipoUsuario_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_USUARIO_DIRECCION1`\n"
                    + "    FOREIGN KEY (`usuario_direccion_id`)\n"
                    + "    REFERENCES `OrderTracker`.`DIRECCION` (`direccion_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`PRODUCTO` (\n"
                    + "  `producto_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `producto_nombre` VARCHAR(45) NOT NULL,\n"
                    + "  `producto_descripcion` VARCHAR(255) NULL,\n"
                    + "  `producto_precio` FLOAT NOT NULL,\n"
                    + "  `producto_imagen` BLOB,\n"
                    + "  `producto_stock` INT NOT NULL,\n"
                    + "  PRIMARY KEY (`producto_id`))\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`ESTADO_PEDIDO` (\n"
                    + "  `estado_pedido_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `estado_pedido_estado` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`estado_pedido_id`))\n"
                    + "ENGINE = InnoDB;");

            st.executeUpdate("INSERT INTO `OrderTracker`.`ESTADO_PEDIDO`"
                    + "VALUES (null,'NO_PAGADO')");
            st.executeUpdate("INSERT INTO `OrderTracker`.`ESTADO_PEDIDO`"
                    + "VALUES (null,'PAGADO')");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`METODO_PAGO` (\n"
                    + "  `metodo_pago_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `metodo_pago_nombre` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`metodo_pago_id`))\n"
                    + "ENGINE = InnoDB;");

            st.executeUpdate("INSERT INTO `OrderTracker`.`METODO_PAGO`"
                    + "VALUES (null,'ONLINE')");
            st.executeUpdate("INSERT INTO `OrderTracker`.`METODO_PAGO`"
                    + "VALUES (null,'CONTRAREEMBOLSO')");
            st.executeUpdate("INSERT INTO `OrderTracker`.`METODO_PAGO`"
                    + "VALUES (null,'TIENDA')");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`PEDIDO` (\n"
                    + "  `pedido_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `pedido_fechaCreacion` TIMESTAMP NOT NULL,\n"
                    + "  `pedido_usuario_id` INT NOT NULL,\n"
                    + "  `pedido_costesEnvio` FLOAT NOT NULL,\n"
                    + "  `pedido_fechaEnvioEstimada` DATE NOT NULL,\n"
                    + "  `pedido_fechaEnvioRealizado` DATE NOT NULL,\n"
                    + "  `pedido_estadoPedido` INT NOT NULL,\n"
                    + "  `pedido_metodoPago` INT NOT NULL,\n"
                    + "  `pedido_pagado` BOOLEAN NOT NULL,\n"
                    + "  `pedido_empleadoAsignado` INT NOT NULL,\n"
                    + "  PRIMARY KEY (`pedido_id`),\n"
                    + "  INDEX `fk_pedido_usuario_idx` (`pedido_usuario_id` ASC) VISIBLE,\n"
                    + "  INDEX `fk_PEDIDO_ESTADO_PEDIDO1_idx` (`pedido_estadoPedido` ASC) VISIBLE,\n"
                    + "  INDEX `fk_PEDIDO_METODO_PAGO1_idx` (`pedido_metodoPago` ASC) VISIBLE,\n"
                    + "  INDEX `fk_pedido_empleadoAsignado_idx` (`pedido_empleadoAsignado` ASC) VISIBLE,\n"
                    + "  CONSTRAINT `fk_pedido_usuario`\n"
                    + "    FOREIGN KEY (`pedido_usuario_id`)\n"
                    + "    REFERENCES `OrderTracker`.`USUARIO` (`usuario_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_PEDIDO_ESTADO_PEDIDO1`\n"
                    + "    FOREIGN KEY (`pedido_estadoPedido`)\n"
                    + "    REFERENCES `OrderTracker`.`ESTADO_PEDIDO` (`estado_pedido_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_PEDIDO_METODO_PAGO1`\n"
                    + "    FOREIGN KEY (`pedido_metodoPago`)\n"
                    + "    REFERENCES `OrderTracker`.`METODO_PAGO` (`metodo_pago_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_PEDIDO_empleadoAsignado`\n"
                    + "    FOREIGN KEY (`pedido_empleadoAsignado`)\n"
                    + "    REFERENCES `OrderTracker`.`USUARIO` (`usuario_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`LINEA_PEDIDO` (\n"
                    + "  `linea_pedido_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `linea_pedido_producto_id` INT NOT NULL,\n"
                    + "  `linea_pedido_cantidad` INT NOT NULL,\n"
                    + "  `linea_pedido_pedido_id` INT NOT NULL,\n"
                    + "  `linea_pedido_total` FLOAT NOT NULL,\n"
                    + "  PRIMARY KEY (`linea_pedido_id`),\n"
                    + "  INDEX `fk_LINEA_PEDIDO_PRODUCTO1_idx` (`linea_pedido_producto_id` ASC) VISIBLE,\n"
                    + "  INDEX `fk_LINEA_PEDIDO_PEDIDO1_idx` (`linea_pedido_pedido_id` ASC) VISIBLE,\n"
                    + "  CONSTRAINT `fk_LINEA_PEDIDO_PRODUCTO1`\n"
                    + "    FOREIGN KEY (`linea_pedido_producto_id`)\n"
                    + "    REFERENCES `OrderTracker`.`PRODUCTO` (`producto_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_LINEA_PEDIDO_PEDIDO1`\n"
                    + "    FOREIGN KEY (`linea_pedido_pedido_id`)\n"
                    + "    REFERENCES `OrderTracker`.`PEDIDO` (`pedido_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + "ENGINE = InnoDB;");
            st.execute("CREATE TABLE IF NOT EXISTS `OrderTracker`.`DATOS_EMPRESA` (\n"
                    + "  `datos_empresa_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `datos_empresa_nombre` VARCHAR(25) NOT NULL,\n"
                    + "  `datos_empresa_formaJuridica` VARCHAR(5) NOT NULL,\n"
                    + "  `datos_empresa_cif` VARCHAR(15) NOT NULL,\n"
                    + "  `datos_empresa_direccion` INT NOT NULL,\n"
                    + "  `datos_empresa_email` VARCHAR(100) NOT NULL,\n"
                    + "  `datos_empresa_paypal` VARCHAR(100) NOT NULL,\n"
                    + "  `datos_empresa_telefono` VARCHAR(12) NULL,\n"
                    + "  PRIMARY KEY (`datos_empresa_id`),\n"
                    + "  INDEX `fk_DATOS_EMPRESA_DIRECCION1_idx` (`datos_empresa_direccion` ASC) VISIBLE,\n"
                    + "  CONSTRAINT `fk_DATOS_EMPRESA_DIRECCION1`\n"
                    + "    FOREIGN KEY (`datos_empresa_direccion`)\n"
                    + "    REFERENCES `OrderTracker`.`DIRECCION` (`direccion_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + "ENGINE = InnoDB;");

            conexion.commit();
            logger.info("Se ha creado la base de datos correctamente");

        } catch (SQLException | ClassNotFoundException ex) {
            logger.severe("No se ha podido crear la base de datos");

            try {

                if (conexion != null) {
                    conexion.rollback();
                }

            } catch (SQLException ex1) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                cerrarConexion();
                if (st != null) {
                    st.close();
                }

            } catch (SQLException ex) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
