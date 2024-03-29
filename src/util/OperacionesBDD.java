/*
 * Copyright (C) 2019 �lvaro Morcillo Barbero
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.MyLogger;

/**
 * Contiene datos del servidor de base de datos, asi como m�todos para
 * conectar,desconectar y realizar transacciones. Se tiene que instanciar un
 * objeto de esta clase, iniciar la conexion mediante "iniciarConexion()" y
 * cerrarla desde "cerrarConexion()". Una vez abierta la conexion, podemos
 * utilizar los metodos de esta clase para interactuar con la bdd
 *
 * @author �lvaro Morcillo Barbero
 */
public class OperacionesBDD {

    public static String URL = "";//"jdbc:mysql://153.92.220.151/u544052383_ordertracker";
    public static String USER = "";//"u544052383_alvaromb";
    public static String PASSWORD = "";//"OrderTracker2021";
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
            PropertiesUtil.a�adirBDD(URL, USER, PASSWORD, "true");
        } catch (IOException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void iniciarConexion() throws ClassNotFoundException, SQLException {

        Properties p = PropertiesUtil.getProperties();

        if (conexion == null || conexion.isClosed()) {
            Class.forName("com.mysql.jdbc.Driver");

            //Recibe los parametros del fichero de configuracion
            //System.out.println("Conectando a " + p.getProperty("database.URL"));
            //System.out.println("Usuario " + p.getProperty("database.USER"));
            //System.out.println("Contrase�a " + p.getProperty("database.PASSWORD"));

            conexion = (Connection) DriverManager.getConnection(p.getProperty("database.URL") + "?testOnBorrow=true&validationQuery='SELECT 1'&validationInterval=1000&autoReconnect=true" ,
                    p.getProperty("database.USER"),
                    p.getProperty("database.PASSWORD"));

            conexion.setAutoCommit(false);

            logger.info("Se ha establecido una conexion con la base de datos.");
        } else {
            logger.warning("Se ha intentado establecer una conexion con la base de "
                    + "datos cuando ya se estaba conectado");
        }

    }

    public static void cerrarConexion() throws SQLException {

        if (conexion != null) {
            conexion.close();

            logger.info("Se ha finalizado la conexion con la base de datos correctamente");
        } else {
            logger.warning("La conexion con la base de datos ya estaba cerrada");
        }

    }

    /**
     * Devuelve true si el usuario y la contrase�a coinciden en la base de datos
     *
     * @param user
     * @param password
     * @return
     */
    public static boolean iniciarSesion(String user, String password) throws ClassNotFoundException, SQLException {

        //iniciarConexion();
        boolean inicioCorrecto = true;
        String query = "SELECT * FROM usuario WHERE usuario_contrase�a=? AND usuario_nombreUsuario=?";
        logger.info("Intento de inicio de sesi�n: \n "
                + "Usuario: " + user + "\n" + " Contrase�a: " + password);

        PreparedStatement pst = conexion.clientPrepareStatement(query);
        pst.setString(1, password);
        pst.setString(2, user);
        ResultSet rs = pst.executeQuery();

        if (!rs.first()) {
            inicioCorrecto = false;
        }

        //cerrarConexion();
        return inicioCorrecto;
    }

    /**
     * Comprueba se realiza una conexion exitosa a la base de datos segun los
     * par�metros introducidos.
     *
     * @param URL
     * @param USER
     * @param PASSWORD
     * @return true si la conexion se realiza correctamente
     */
    public static boolean comprobarConexion(String URL, String USER, String PASSWORD) {

        boolean conexionEstablecida = false;
        logger.info("Comprobando conexi�n:"
                + "\n \t URL:" + URL
                + "\n \t USER:" + USER
                + "\n \t PASSWORD:" + PASSWORD);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexionPrueba;

            conexionPrueba = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);

            conexionEstablecida = true;
            conexionPrueba.close();
            logger.info("Conectado con �xito");
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

    public static String getNombreUsuarioPedido(int idPedidoSeleccionado) throws SQLException{
        String nombre = null;
        Statement st = conexion.createStatement();
        ResultSet rs;
        rs = st.executeQuery("SELECT concat(usuario_nombre,' ',usuario_apellidos) "
                + "FROM pedido, usuario WHERE pedido_usuario_id = usuario_id AND pedido_id = " + idPedidoSeleccionado);
        
        rs.next();
        nombre = rs.getString(1);
        
        return nombre;
    }
    public static String getRutaImagenProductoSeleccionado(int id) throws SQLException {

        String ruta = null;

        ResultSet rs = null;
        String query = "SELECT producto_imagen FROM producto WHERE producto_id=" + id + "";
        Statement st;

        st = conexion.createStatement();

        rs = st.executeQuery(query);

        rs.next();

        ruta = rs.getString(1);

        //System.out.println("rutaImagenProductoSeleccionado -> " + ruta);

        return ruta;
    }

    /**
     * Inserta en la base de datos un nuevo registro con los datos de la empresa
     * que se introducen mediante parametros.Primero comprueba que no exista
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
     * @param telefono
     */
    public static void a�adirEmpresa(String nombre, String formaJuridica, String CIF,
            String email, String emailPaypal, String calle, String localidad,
            String provincia, String codigoPostal, String pais, String telefono) {

        Statement st = null;
        ResultSet rs = null;
        try {

            //iniciarConexion();
            //Comprueba que no exista otra empresa
            st = conexion.createStatement();
            rs = st.executeQuery("SELECT * FROM datos_empresa");

            if (rs.next()) {
                //Ya existe una empresa, por tanto no se hace nada
                logger.info("Ya existe una empresa.");
            } else {
                //No existe ninguna empresa, por tanto se procede a introducir una
                logger.info("Se procede a introducir los datos de la nueva empresa");
                //Solo hay una empresa, por tanto la primary key es "1" para ambos registros
                String queryDireccion = "INSERT INTO direccion VALUES (null,?, ?, ?, ?, ?);";
                PreparedStatement psDireccion = conexion.prepareStatement(queryDireccion);

                psDireccion.setString(1, calle);
                psDireccion.setString(2, localidad);
                psDireccion.setString(3, provincia);
                psDireccion.setString(4, codigoPostal);
                psDireccion.setString(5, pais);

                psDireccion.execute();

                conexion.commit();

                String queryEmpresa = "INSERT INTO datos_empresa VALUES (null, ?, ?, ?,"
                        + " (SELECT direccion_id FROM direccion WHERE direccion_calle = ? AND direccion_codigoPostal = ? )"
                        + ", ?, ?);";
                PreparedStatement psEmpresa = conexion.clientPrepareStatement(queryEmpresa);

                psEmpresa.setString(1, nombre);
                psEmpresa.setString(2, formaJuridica);
                psEmpresa.setString(3, CIF);
                psEmpresa.setString(4, calle);
                psEmpresa.setString(5, codigoPostal);
                psEmpresa.setString(6, email);
                psEmpresa.setString(7, telefono);
                //psEmpresa.setString(8, paypal);
                psEmpresa.executeUpdate();

                conexion.commit();
                logger.info("Transaccion completada. Se ha a�adido una empresa"
                        + "a la base de datos");

            }
        } catch (SQLException ex) {
            try {
                conexion.rollback();
                logger.info("No se ha completado la transaccion. No se ha "
                        + "podido a�adir la empresa a la base de datos");
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex1) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    public static void cancelarPedido(int idPedido) throws SQLException {

        String query1 = "DELETE FROM linea_pedido WHERE linea_pedido_pedido_id = ?";
        String query2 = "DELETE FROM pedido WHERE pedido_id = ?";

        PreparedStatement pst1 = conexion.clientPrepareStatement(query1);
        PreparedStatement pst2 = conexion.clientPrepareStatement(query2);

        pst1.setInt(1, idPedido);
        pst2.setInt(1, idPedido);

        pst1.executeUpdate();
        pst2.executeUpdate();

        conexion.commit();

    }

    public static boolean comprobarContrase�a(String nombreUsuario, String contrase�a) {

        boolean existe = false;
        try {
            System.out.println("COMPROBANDO CONTRASE�A -->");
            System.out.println(nombreUsuario + " " + contrase�a);

            Statement st = conexion.createStatement();
            String query = "SELECT * FROM usuario WHERE usuario_nombreUsuario = '" + nombreUsuario + "'" + " AND usuario_contrase�a = '" + contrase�a + "'";

            ResultSet rs = st.executeQuery(query);

            if (rs.first()) {
                existe = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return existe;
    }

    /**
     *
     * @param nombreUsuario
     * @param contrase�a
     * @throws SQLException
     */
    public static void cambiarContrase�a(String nombreUsuario, String contrase�a) throws SQLException {

        PreparedStatement pst = null;
        String query = "UPDATE usuario SET usuario_contrase�a = ? WHERE usuario_nombreUsuario = ?";

        pst = conexion.prepareStatement(query);

        pst.setString(1, contrase�a);
        pst.setString(2, nombreUsuario);
        pst.executeUpdate();

        conexion.commit();
    }

    public static void a�adirAdmin(String user, String password) {

        PreparedStatement pst = null;
        try {
            /*
            + "  `usuario_id` INT AUTO_INCREMENT,\n"
            + "  `usuario_nombreUsuario` VARCHAR(15) NOT NULL,\n"
            + "  `usuario_email` VARCHAR(100) NULL,\n"
            + "  `usuario_contrase�a` VARCHAR(25) NOT NULL,\n"
            + "  `usuario_nombre` VARCHAR(45) NULL,\n"
            + "  `usuario_Apellidos` VARCHAR(200) NULL,\n"
            + "  `usuario_telefono` VARCHAR(12) NULL,\n"
            + "  `usuario_fechaCreacion` TIMESTAMP NULL,\n"
            + "  `usuario_tipoUsuario_id` INT NULL,\n"
            + "  `usuario_direccion_id` INT NULL,\n"
            + "  `usuario_dni` VARCHAR(9) NULL,\n"
             */

            //iniciarConexion();
            String query = "INSERT INTO usuario"
                    + "(usuario_nombreUsuario,usuario_contrase�a,usuario_tipoUsuario_id,usuario_fechaCreacion,usuario_direccion_id) "
                    + "VALUES(?,?,?,?,?);";
            pst = conexion.prepareStatement(query);

            pst.setString(1, user);
            pst.setString(2, password);
            pst.setInt(3, TIPO_ADMIN);

            Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
            pst.setTimestamp(4, ts);
            pst.setInt(5, 1);

            pst.executeUpdate();

            conexion.commit();

            logger.info("Admin \"" + user + " / " + password + "\" a�adido");
        } catch (SQLException ex) {
            logger.warning("No se ha podido a�adir el admin.");
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Crea el esquema de la bdd en el servidor. A�ade todas las tablas. Tambien
     * inserta los tipos de usuario,los m�todos de pago Y el estado de pedido.
     */
    public static void crearBDD() {

        Statement st = null;

        try {
            //iniciarConexion();
            st = conexion.createStatement();

            //st.execute("DROP SCHEMA `OrderTracker` ;");
            //st.execute("CREATE SCHEMA IF NOT EXISTS `OrderTracker` DEFAULT CHARACTER SET utf8 ;");
            st.execute("CREATE TABLE IF NOT EXISTS tipo_usuario (\n"
                    + "  `tipoUsuario_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `tipoUsuario_tipo` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`tipoUsuario_id`))\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS direccion (\n"
                    + "  `direccion_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `direccion_calle` VARCHAR(45) NOT NULL,\n"
                    + "  `direccion_localidad` VARCHAR(45) NOT NULL,\n"
                    + "  `direccion_provincia` VARCHAR(45) NOT NULL,\n"
                    + "  `direccion_codigoPostal` VARCHAR(45) NOT NULL,\n"
                    + "  `direccion_pais` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`direccion_id`))\n"
                    + "ENGINE = InnoDB;");

            st.executeUpdate("INSERT INTO tipo_usuario"
                    + " VALUES (null,'ADMIN')");
            st.executeUpdate("INSERT INTO tipo_usuario"
                    + " VALUES (null,'EMPLEADO')");
            st.executeUpdate("INSERT INTO tipo_usuario"
                    + " VALUES (null,'CLIENTE')");

            st.execute("CREATE TABLE IF NOT EXISTS usuario (\n"
                    + "  `usuario_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `usuario_nombreUsuario` VARCHAR(15) NOT NULL UNIQUE,\n"
                    + "  `usuario_email` VARCHAR(100) NULL,\n"
                    + "  `usuario_contrase�a` VARCHAR(25) NOT NULL,\n"
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
                    + "    REFERENCES tipo_usuario (`tipoUsuario_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_USUARIO_DIRECCION1`\n"
                    + "    FOREIGN KEY (`usuario_direccion_id`)\n"
                    + "    REFERENCES direccion (`direccion_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS producto (\n"
                    + "  `producto_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `producto_nombre` VARCHAR(45) NOT NULL,\n"
                    + "  `producto_descripcion` VARCHAR(20000) NULL,\n"
                    + "  `producto_precio` FLOAT NOT NULL,\n"
                    + "  `producto_imagen` VARCHAR(300),\n"
                    + "  `producto_stock` INT NOT NULL,\n"
                    + "  PRIMARY KEY (`producto_id`))\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS estado_pedido (\n"
                    + "  `estado_pedido_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `estado_pedido_estado` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`estado_pedido_id`))\n"
                    + "ENGINE = InnoDB;");

            st.executeUpdate("INSERT INTO estado_pedido"
                    + " VALUES (null,'DE_CAMINO')");
            st.executeUpdate("INSERT INTO estado_pedido"
                    + " VALUES (null,'PREPARADO')");
            st.executeUpdate("INSERT INTO estado_pedido"
                    + " VALUES (null,'FINALIZADO')");
            st.executeUpdate("INSERT INTO estado_pedido"
                    + " VALUES (null,'CANCELADO')");

            st.execute("CREATE TABLE IF NOT EXISTS metodo_pago (\n"
                    + "  `metodo_pago_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `metodo_pago_nombre` VARCHAR(45) NOT NULL,\n"
                    + "  PRIMARY KEY (`metodo_pago_id`))\n"
                    + "ENGINE = InnoDB;");

            st.executeUpdate("INSERT INTO metodo_pago"
                    + " VALUES (null,'ONLINE')");
            st.executeUpdate("INSERT INTO metodo_pago"
                    + " VALUES (null,'CONTRAREEMBOLSO')");
            st.executeUpdate("INSERT INTO metodo_pago"
                    + " VALUES (null,'TIENDA')");

            st.execute("CREATE TABLE IF NOT EXISTS pedido (\n"
                    + "  `pedido_id` INT NOT NULL AUTO_INCREMENT,\n"
                    + "  `pedido_fechaCreacion` TIMESTAMP NOT NULL,\n"
                    + "  `pedido_usuario_id` INT NOT NULL,\n"
                    + "  `pedido_costesEnvio` FLOAT NOT NULL,\n"
                    + "  `pedido_fechaEnvioEstimada` DATE NOT NULL,\n"
                    + "  `pedido_fechaEnvioRealizado` DATE ,\n"
                    + "  `pedido_estadoPedido` INT NOT NULL,\n"
                    + "  `pedido_metodoPago` INT NOT NULL,\n"
                    + "  `pedido_pagado` BOOLEAN NOT NULL,\n"
                    + "  `pedido_empleadoAsignado` INT,\n"
                    + "  PRIMARY KEY (`pedido_id`),\n"
                    + "  INDEX `fk_pedido_usuario_idx` (`pedido_usuario_id` ASC) VISIBLE,\n"
                    + "  INDEX `fk_PEDIDO_ESTADO_PEDIDO1_idx` (`pedido_estadoPedido` ASC) VISIBLE,\n"
                    + "  INDEX `fk_PEDIDO_METODO_PAGO1_idx` (`pedido_metodoPago` ASC) VISIBLE,\n"
                    + "  INDEX `fk_pedido_empleadoAsignado_idx` (`pedido_empleadoAsignado` ASC) VISIBLE,\n"
                    + "  CONSTRAINT `fk_pedido_usuario`\n"
                    + "    FOREIGN KEY (`pedido_usuario_id`)\n"
                    + "    REFERENCES usuario (`usuario_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_PEDIDO_ESTADO_PEDIDO1`\n"
                    + "    FOREIGN KEY (`pedido_estadoPedido`)\n"
                    + "    REFERENCES estado_pedido (`estado_pedido_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_PEDIDO_METODO_PAGO1`\n"
                    + "    FOREIGN KEY (`pedido_metodoPago`)\n"
                    + "    REFERENCES metodo_pago (`metodo_pago_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_PEDIDO_empleadoAsignado`\n"
                    + "    FOREIGN KEY (`pedido_empleadoAsignado`)\n"
                    + "    REFERENCES usuario (`usuario_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + "ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS linea_pedido (\n"
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
                    + "    REFERENCES producto (`producto_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION,\n"
                    + "  CONSTRAINT `fk_LINEA_PEDIDO_PEDIDO1`\n"
                    + "    FOREIGN KEY (`linea_pedido_pedido_id`)\n"
                    + "    REFERENCES pedido (`pedido_id`)\n"
                    + "    ON DELETE NO ACTION\n"
                    + "    ON UPDATE NO ACTION)\n"
                    + " ENGINE = InnoDB;");

            st.execute("CREATE TABLE IF NOT EXISTS datos_empresa (\n"
                    + "                     datos_empresa_id INT PRIMARY KEY AUTO_INCREMENT,\n"
                    + "                      datos_empresa_nombre VARCHAR(25) NOT NULL,\n"
                    + "                    datos_empresa_formaJuridica VARCHAR(5) NOT NULL,\n"
                    + "                    datos_empresa_cif VARCHAR(15) NOT NULL,\n"
                    + "                   datos_empresa_direccion INT NOT NULL,\n"
                    + "                   datos_empresa_email VARCHAR(100) NOT NULL,\n"
                    + "				datos_empresa_telefono VARCHAR(12) NULL)\n"
                    + "                     ENGINE = InnoDB;");

            conexion.commit();
            logger.info("Se ha creado la base de datos correctamente");

        } catch (SQLException ex) {
            logger.severe("No se ha podido crear la base de datos");

            try {

                if (conexion != null) {
                    conexion.rollback();
                }

            } catch (SQLException ex1) {
                Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param nombreUsuario
     * @return Devuelve un int que indica el rol del usuario (1 = admin, 2 =
     * empleado, 3 = cliente)
     */
    public static int getRolUsuario(String nombreUsuario) {
        int rol = 4;

        ResultSet rs = null;
        String query = "SELECT usuario_tipoUsuario_id FROM usuario WHERE usuario_nombreUsuario = '" + nombreUsuario + "'";
        Statement st;
        try {

            st = conexion.createStatement();
            //pst.setString(1, nombreUsuario);
            rs = st.executeQuery(query);

            rs.next();
            rol = rs.getInt("usuario_tipoUsuario_id");
            System.out.println("rol de: " + nombreUsuario + "  " + rol);
        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rol;
    }

    public static ResultSet getPedidos() {
        ResultSet pedidos = null;
        String query = "SELECT pedido_id AS ID ,\n" +
"                pedido_usuario_id AS ID_USUARIO,\n" +
"               pedido_costesEnvio AS COSTE_ENVIO,\n" +
"                pedido_fechaEnvioEstimada AS LLEGADA_EST,\n" +
"                pedido_fechaEnvioRealizado AS LLEGADA,\n" +
"                estado_pedido_estado AS ESTADO,\n" +
"                metodo_pago_nombre AS METODO_PAGO,\n" +
"                pedido_pagado AS PAGADO FROM pedido,estado_pedido,metodo_pago\n" +
"                                               WHERE pedido_estadoPedido = estado_pedido_id AND\n" +
"                                				  pedido_metodoPago = metodo_pago_id";
        Statement st;
        try {

            st = conexion.createStatement();
            pedidos = st.executeQuery(query);

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedidos;
    }

    public static ResultSet getProductos() {
        ResultSet productos = null;
        String query = "SELECT producto_id AS ID,producto_nombre AS NOMBRE,"
                + "producto_descripcion AS DESCRIPCION,"
                + "producto_precio AS PRECIO,"
                + "producto_stock AS STOCK FROM producto";
        Statement st;
        try {

            st = conexion.createStatement();
            productos = st.executeQuery(query);

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return productos;
    }

    public static ResultSet getUsuarios() {
        ResultSet pedidos = null;
        /*
        String query = "SELECT usuario_nombreUsuario AS USUARIO,\n"
                + "usuario_email AS EMAIL,\n"
                + "usuario_nombre + usuario_Apellidos AS NOMBRE,\n"
                + "usuario_telefono AS TELEFONO,\n"
                + "tipoUsuario_tipo AS ROL_USUARIO,\n"
                + "direccion_calle AS DIRECCION,\n"
                + "usuario_dni AS DNI\n"
                + " FROM ordertracker.usuario, ordertracker.tipo_usuario, ordertracker.direccion\n"
                + " WHERE usuario_tipoUsuario_id = tipoUsuario_id";
         */

        String query = "SELECT usuario_nombreUsuario AS USUARIO,\n"
                + "               usuario_email AS EMAIL,\n"
                + "               concat(usuario_nombre,\" \",usuario_Apellidos) AS NOMBRE,\n"
                + "                usuario_telefono AS TELEFONO,\n"
                + "                tipoUsuario_tipo AS ROL_USUARIO,\n"
                + "				concat(direccion_calle, \", \", direccion_localidad, \"(\", direccion_provincia,\")\")   AS DIRECCION,\n"
                + "                 usuario_dni AS DNI\n"
                + "                  FROM usuario, tipo_usuario, direccion\n"
                + "                 WHERE usuario_tipoUsuario_id = tipoUsuario_id AND"
                + "                 direccion_id = usuario_direccion_id";
        Statement st;
        try {

            st = conexion.createStatement();
            pedidos = st.executeQuery(query);

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedidos;
    }

    /**
     *
     * @param nombreUsuario el usuario que se va a eliminar. Se elimina tambien
     * su direcci�n
     */
    public static void eliminarUsuario(String nombreUsuario) {

        String query1 = "DELETE FROM direccion WHERE direccion_id = (SELECT usuario_direccion_id FROM usuario WHERE usuario_nombreUsuario = '" + nombreUsuario + "')";
        String query2 = "DELETE FROM usuario WHERE usuario_nombreUsuario = '" + nombreUsuario + "'";

        try {
            Statement statement = conexion.createStatement();

            //statement.executeUpdate(query1);
            statement.executeUpdate(query2);

            conexion.commit();

            System.out.println("Usuario " + nombreUsuario + " eliminado correctamente.");

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static ResultSet getLineas(int idPedido) {
        ResultSet pedidos = null;
        String query = "SELECT producto_nombre AS PRODUCTO,\n"
                + "linea_pedido_cantidad AS CANTIDAD,\n"
                + "linea_pedido_total AS TOTAL\n"
                + " FROM linea_pedido,producto, pedido\n"
                + " WHERE linea_pedido_pedido_id = pedido_id AND linea_pedido_producto_id = producto_id AND pedido_id=" + idPedido;

        try {
            Statement statement = conexion.createStatement();
            pedidos = statement.executeQuery(query);

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return pedidos;
    }

    /**
     *
     * @param nombre
     * @param desc
     * @param precio
     * @param imagen
     * @param stock
     */
    public static void a�adirProducto(String nombre, String desc, float precio, String rutaImagen, int stock) throws SQLException {

        PreparedStatement preparedStatement;
        String query = "INSERT INTO producto VALUES (null,?,?,?,?,?);";

        preparedStatement = conexion.clientPrepareStatement(query);
        preparedStatement.setString(1, nombre);
        preparedStatement.setString(2, desc);
        preparedStatement.setFloat(3, precio);
        preparedStatement.setString(4, rutaImagen);

        preparedStatement.setInt(5, stock);

        preparedStatement.executeUpdate();
        conexion.commit();

    }

    public static boolean comprobarUsuarioExiste(String nombreUsuario) {

        boolean existe = true;
        try {

            Statement st = null;
            String query = "SELECT * FROM usuario WHERE usuario_nombreUsuario = '" + nombreUsuario + "'";

            st = conexion.createStatement();
            existe = st.execute(query);

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return existe;
    }

    /**
     *
     * @param nombreUsuario
     * @param email
     * @param contrase�a
     * @param nombre
     * @param apellidos
     * @param Apellidos
     * @param telefono
     * @param DNI
     */
    public static void a�adirUsuario(String nombreUsuario, String email,
            String contrase�a, String nombre, String apellidos, String telefono,
            String DNI) throws SQLException {

        PreparedStatement pst = null;
        String query = "INSERT INTO usuario VALUES (null,?,?,?,?,?,?,?,?,1,?)";

        pst = conexion.prepareStatement(query);

        pst.setString(1, nombreUsuario);
        pst.setString(2, email);
        pst.setString(3, contrase�a);
        pst.setString(4, nombre);
        pst.setString(5, apellidos);
        pst.setString(6, telefono);

        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        pst.setTimestamp(7, timestamp);
        pst.setInt(8, TIPO_EMPLEADO);

        pst.setString(9, DNI);

        pst.executeUpdate();
        conexion.commit();

    }

    /**
     *
     * @param nombreUsuario
     * @param nombreUsuarioNuevo
     * @param email
     * @param nombre
     * @param apellidos
     * @param telefono
     * @param dni
     */
    public static void modificarUsuario(String nombreUsuario, String nombreUsuarioNuevo, String email, String nombre, String apellidos, String telefono, String dni) {

        PreparedStatement preparedStatement;
        String query = "UPDATE usuario SET"
                + "             usuario_nombreUsuario = ?,"
                + "		usuario_email = ?,"
                + "             usuario_nombre = ?,"
                + "             usuario_Apellidos = ?,"
                + "             usuario_telefono = ?,"
                + "             usuario_dni = ?"
                + "             WHERE usuario_nombreUsuario = ?";

        try {

            preparedStatement = conexion.clientPrepareStatement(query);

            preparedStatement.setString(1, nombreUsuarioNuevo);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, nombre);
            preparedStatement.setString(4, apellidos);
            preparedStatement.setString(5, telefono);
            preparedStatement.setString(6, dni);
            preparedStatement.setString(7, nombreUsuario);

            preparedStatement.executeUpdate();
            conexion.commit();

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param id
     * @param nombre
     * @param desc
     * @param precio
     * @param imagen
     * @param stock
     */
    public static void modificarProducto(int id, String nombre, String desc, float precio, String rutaImagen, int stock) {

        PreparedStatement preparedStatement;
        String query = "UPDATE producto SET"
                + " producto_nombre = ?,"
                + " producto_descripcion = ?,"
                + " producto_precio = ?,"
                + " producto_imagen = ?,"
                + " producto_stock = ? "
                + " WHERE producto_id = ?;";

        try {

            preparedStatement = conexion.clientPrepareStatement(query);

            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, desc);
            preparedStatement.setFloat(3, precio);
            preparedStatement.setString(4, rutaImagen);
            preparedStatement.setInt(5, stock);
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();
            conexion.commit();

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param id
     */
    public static void eliminarProducto(int id) {

        PreparedStatement preparedStatement;
        String query = "DELETE FROM producto WHERE producto_id = ?";

        try {

            preparedStatement = conexion.clientPrepareStatement(query);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();

            conexion.commit();

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBDD.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Lineas para insertar datos de prueba ********************************
     *
     * Inserta varios productos INSERT INTO ordertracker.producto VALUES
     * (null,"producto","producto",12.2,null,1);
     *
     * Inserta el pedido
     *
     * INSERT INTO `ordertracker`.`pedido` (`pedido_id`, `pedido_fechaCreacion`,
     * `pedido_usuario_id`, `pedido_costesEnvio`, `pedido_fechaEnvioEstimada`,
     * `pedido_fechaEnvioRealizado`, `pedido_estadoPedido`, `pedido_metodoPago`,
     * `pedido_pagado`, `pedido_empleadoAsignado`) VALUES (null,now(),1, 15,
     * 12/15/2018, 12/15/2018, 1, 1, 1, 1);
     *
     * Inserta varias lineas para el pedido INSERT INTO
     * `ordertracker`.`linea_pedido` (`linea_pedido_id`,
     * `linea_pedido_producto_id`, `linea_pedido_cantidad`,
     * `linea_pedido_pedido_id`, `linea_pedido_total`) VALUES (null, 1, 1, 2,
     * 2);
     *
     *
     */
}
