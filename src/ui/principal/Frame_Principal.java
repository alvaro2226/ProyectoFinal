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
package ui.principal;

import java.awt.CardLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import necesario.RSFileChooser;
import net.proteanit.sql.DbUtils;
import pojos.Producto;
import pojos.Usuario;
import threads.Reloj;
import ui.bienvenida.Frame_Login;
import ui.dialogs.Dialog_Confirmar;
import ui.dialogs.Dialog_cambiarContraseña;
import ui.dialogs.Dialog_nuevoUsuario;
import util.FTP_Client;
import util.OperacionesBDD;
import util.Util;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class Frame_Principal extends javax.swing.JFrame {

    private int idPedido = 1;
    private String tablaSeleccionada = "";

    /**
     * Creates new form Frame_Principal
     */
    public Frame_Principal() {
        initComponents();
        iniciarComponentes();

    }
    FTP_Client ftp;

    private void iniciarComponentes() {

        this.setIconImage(Util.getImagenIcono());
        this.lblUsuarioLogeado.setText(Frame_Login.usuarioLogeado);

        setLocationRelativeTo(null);
        tablaInfo.setDefaultEditor(Object.class, null);
        tablaLineas2.setDefaultEditor(Object.class, null);
        new Reloj(labelReloj).start();

        try {

            ftp = new FTP_Client();
            ftp.iniciarConexion();

            OperacionesBDD.iniciarConexion();

            usuarioLogeadoRol = OperacionesBDD.getRolUsuario(Frame_Login.usuarioLogeado);

            if (usuarioLogeadoRol == 1) {
                this.lblUsuarioLogeadoRol.setText("(Admin)");
            } else if (usuarioLogeadoRol == 2) {
                this.lblUsuarioLogeadoRol.setText("(Empleado)");
            }

            //MUESTRA LA TABLA PEDIDOS Y LINEAS 
            tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getPedidos()));
            tablaLineas2.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getLineas(idPedido)));
            tablaSeleccionada = "pedidos";

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.añadirListenersTablas();

        botonAplicarUsuario.setEnabled(false);
        botonAplicarUsuario.setVisible(false);
        botonEliminarUsuario.setEnabled(false);
        botonEliminarUsuario.setVisible(false);
        botonCambiarContraseña.setEnabled(false);
        botonCambiarContraseña.setVisible(false);

        this.lblConsola.setVisible(false);

    }

    private void actualizarTablas() {

        actualizando = true;
        DefaultTableModel dtm1 = (DefaultTableModel) this.tablaInfo.getModel();

        int rowCount = dtm1.getRowCount();

        //System.out.println("Num filas " + rowCount);
        for (int i = 0; i < rowCount; i++) {
            dtm1.removeRow(0);
            //System.out.println(i);
        }

        if (tablaSeleccionada.equals("usuarios")) {
            tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getUsuarios()));
        }

        if (tablaSeleccionada.equals("productos")) {
            tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getProductos()));
        }

        if (tablaSeleccionada.equals("pedidos")) {
            tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getPedidos()));
        }

        actualizando = false;
    }

    boolean actualizando = false;

    private void añadirListenersTablas() {

        //AL PULSAR UN PEDIDO EN LA TABLA SE MUESTRA SUS LINEAS EN LA OTRA TABLA
        this.tablaInfo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (tablaSeleccionada.equals("pedidos") && !actualizando) {

                    System.out.println("Mostrando pedidos...");
                    idPedido = Integer.valueOf(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 0).toString());
                    System.out.println("idPedido = " + idPedido);
                    tablaLineas2.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getLineas(idPedido)));
                    actualizando = false;

                }
            }
        });

        this.tablaInfo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (tablaSeleccionada.equals("productos") && !actualizando) {

                    producto.setId(Integer.valueOf(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 0).toString()));
                    producto.setNombre((tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 1).toString()));
                    producto.setPrecio(Float.valueOf(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 3).toString()));
                    producto.setDescripcion((tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 2).toString()));
                    producto.setStock(Integer.valueOf(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 4).toString()));

                    fieldNombre.setText(producto.getNombre());
                    fieldDesc.setText(producto.getDescripcion());
                    fieldPrecio.setText(producto.getPrecio() + "");
                    fieldStock.setText(producto.getStock() + "");

                    try {
                        String ruta = OperacionesBDD.getRutaImagenProductoSeleccionado(producto.getId());

                        if (ruta != null) {
                            String rutaFinal = "https://programaloalvaro.es/archivos/ordertracker/imagenes/" + ruta;

                            if (rutaFinal != null) {
                                rutaFinal = rutaFinal.trim();
                                rutaFinal = rutaFinal.replaceAll("\\s", "%20");
                            }
                            
                            BufferedImage img = ImageIO.read(new URL(rutaFinal));

                            imagenProducto.setIcon(new ImageIcon(img));
                        } else {
                            imagenProducto.setIcon(new ImageIcon("src\\ui\\images\\logo\\logo_transparent.png"));
                        }

                    } catch (SQLException | IOException ex) {
                        Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    actualizando = false;

                }
            }
        });

        this.tablaInfo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (tablaSeleccionada.equals("usuarios") && !actualizando) {

                    if (tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 0).toString() == null) {
                        usuario.setNombreUsuario("");
                    } else {
                        usuario.setNombreUsuario(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 0).toString());
                    }

                    if (tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 1) == null) {
                        usuario.setEmail("");
                    } else {
                        usuario.setEmail(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 1).toString());
                    }

                    if (tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 2) == null) {
                        usuario.setNombre("");
                    } else {
                        usuario.setNombre(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 2).toString());
                    }

                    if (tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 3) == null) {
                        usuario.setTelefono("");
                    } else {

                        usuario.setTelefono(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 3).toString());
                    }

                    usuario.setRol(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 4).toString());
                    if (tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 4) == null) {
                        usuario.setContra("");
                    } else {
                        usuario.setContra(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 4).toString());
                    }

                    if (tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 6) == null) {
                        usuario.setDni("");
                    } else {
                        usuario.setDni(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 6).toString());
                    }

                    fieldNomUsuario.setText(Objects.toString(usuario.getNombreUsuario(), ""));
                    fieldEmail.setText(Objects.toString(usuario.getEmail(), ""));
                    fieldNom.setText(Objects.toString(usuario.getNombre(), ""));
                    fieldTelefono.setText(Objects.toString(usuario.getTelefono(), ""));
                    //fieldContra.setText(Objects.toString(usuario.getContra(), ""));
                    fieldDni.setText(Objects.toString(usuario.getDni(), ""));

                    labelRolUsuario.setText(usuario.getRol());

                    actualizando = false;

                    //Si el usuario seleccionado es el mismo que el que ha iniciado sesión
                    boolean esElMismo = false;
                    if (Frame_Login.usuarioLogeado.equals(usuario.getNombreUsuario())) {
                        //Puedes editar
                        botonCambiarContraseña.setEnabled(true);
                        botonCambiarContraseña.setVisible(true);
                        fieldDni.setEnabled(true);
                        fieldDni.setEditable(true);
                        fieldNom.setEditable(true);
                        esElMismo = true;

                    } else {
                        //No puedes editar
                        botonCambiarContraseña.setEnabled(false);
                        botonCambiarContraseña.setVisible(false);
                        fieldDni.setEnabled(false);
                        fieldDni.setEditable(false);
                        fieldNom.setEditable(false);
                    }

                    //Si rol es mayor que el del usuario que ha seleccionado puede editarlo   
                    usuarioSeleccionadoRol = OperacionesBDD.getRolUsuario(usuario.getNombreUsuario());

                    System.out.println("usuario logueado -> " + usuarioLogeadoRol + " usuario seleccionado -> " + usuarioSeleccionadoRol);

                    if (usuarioLogeadoRol < usuarioSeleccionadoRol || esElMismo) {

                        puedeEditarUsuarios(true);
                    } else {
                        puedeEditarUsuarios(false);
                    }

                }
            }

        }
        );

    }

    int usuarioSeleccionadoRol;
    int usuarioLogeadoRol;

    private void eliminarListenersTablas() {
        this.tablaInfo.getSelectionModel().removeListSelectionListener(tablaInfo);
        this.tablaLineas2.getSelectionModel().removeListSelectionListener(tablaLineas2);
    }
    Producto producto = new Producto();
    Usuario usuario = new Usuario();

    private void puedeEditarUsuarios(boolean bool) {

        if (bool) {
            fieldNomUsuario.setEditable(true);
            fieldEmail.setEditable(true);
            fieldTelefono.setEditable(true);
            botonCambiarContraseña.setEnabled(true);
            fieldDni.setEditable(true);
            botonAplicarUsuario.setEnabled(true);
            botonAplicarUsuario.setVisible(true);
            botonEliminarUsuario.setEnabled(true);
            botonEliminarUsuario.setVisible(true);
            fieldNom.setEditable(true);
        } else {
            fieldNomUsuario.setEditable(false);
            fieldEmail.setEditable(false);
            fieldTelefono.setEditable(false);
            botonCambiarContraseña.setEnabled(false);
            fieldDni.setEditable(false);
            fieldNom.setEditable(false);
            botonAplicarUsuario.setEnabled(false);
            botonAplicarUsuario.setVisible(false);
            botonEliminarUsuario.setEnabled(false);
            botonEliminarUsuario.setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundPanel = new javax.swing.JPanel();
        panelBotones = new javax.swing.JPanel();
        rSLabelImage1 = new rojerusan.RSLabelImage();
        botonVerProductos = new rojeru_san.RSButtonRiple();
        botonVerUsuarios = new rojeru_san.RSButtonRiple();
        botonVerPedidos = new rojeru_san.RSButtonRiple();
        lblAutor2 = new javax.swing.JLabel();
        lblAutor1 = new javax.swing.JLabel();
        panelInformacion = new javax.swing.JPanel();
        panelSuperior = new rojerusan.RSPanelsSlider();
        panelPedidos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaInfo = new rojerusan.RSTableMetro();
        lblTituloTabla = new javax.swing.JLabel();
        panelInferior = new rojerusan.RSPanelsSlider();
        panelPedidos4 = new javax.swing.JPanel();
        panelLineas_izq2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaLineas2 = new rojerusan.RSTableMetro();
        panelLineas_der = new javax.swing.JPanel();
        labelNombreProducto12 = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        labelNombreProducto14 = new javax.swing.JLabel();
        labelNombreProducto17 = new javax.swing.JLabel();
        labelNombreProducto18 = new javax.swing.JLabel();
        botonAplicarCambios2 = new rojerusan.RSButtonHover();
        botonCancelarPedido = new rojerusan.RSButtonHover();
        lblPago = new javax.swing.JLabel();
        labelNombreProducto15 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        lblRepartidor = new javax.swing.JLabel();
        lblPrecio = new javax.swing.JLabel();
        panelProductos = new javax.swing.JPanel();
        labelNombreProducto = new javax.swing.JLabel();
        fieldNombre = new rojerusan.RSMetroTextFullPlaceHolder();
        fieldDesc = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto1 = new javax.swing.JLabel();
        fieldPrecio = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto2 = new javax.swing.JLabel();
        labelNombreProducto3 = new javax.swing.JLabel();
        imagenProducto = new rojerusan.RSLabelImage();
        botonCambiarImagen = new rojerusan.RSMaterialButtonRound();
        rSButtonMetro1 = new rojerusan.RSButtonMetro();
        rSButtonMetro2 = new rojerusan.RSButtonMetro();
        rSButtonMetro3 = new rojerusan.RSButtonMetro();
        rSButtonMetro4 = new rojerusan.RSButtonMetro();
        fieldStock = new javax.swing.JTextField();
        botonAñadirProducto = new rojerusan.RSButtonHover();
        botonEliminarProducto = new rojerusan.RSButtonHover();
        botonAplicarCambios = new rojerusan.RSButtonHover();
        lblConsola = new javax.swing.JLabel();
        panelUsuarios = new javax.swing.JPanel();
        labelNombreProducto4 = new javax.swing.JLabel();
        fieldNomUsuario = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto5 = new javax.swing.JLabel();
        fieldEmail = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto6 = new javax.swing.JLabel();
        fieldNom = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto7 = new javax.swing.JLabel();
        fieldTelefono = new rojerusan.RSMetroTextFullPlaceHolder();
        labelRolUsuario = new javax.swing.JLabel();
        labelNombreProducto9 = new javax.swing.JLabel();
        labelNombreProducto10 = new javax.swing.JLabel();
        fieldDni = new rojeru_san.RSMPassView();
        botonEliminarUsuario = new rojerusan.RSButtonHover();
        botonAñadirUsuario = new rojerusan.RSButtonHover();
        botonAplicarUsuario = new rojerusan.RSButtonHover();
        botonCambiarContraseña = new rojerusan.RSButtonHover();
        labelReloj = new javax.swing.JLabel();
        lblLog = new javax.swing.JLabel();
        lblUsuarioLogeadoRol = new javax.swing.JLabel();
        lblUsuarioLogeado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        backgroundPanel.setBackground(new java.awt.Color(79, 134, 198));
        backgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBotones.setBackground(new java.awt.Color(79, 134, 198));

        rSLabelImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/logo/logo2.png"))); // NOI18N

        botonVerProductos.setBackground(new java.awt.Color(79, 134, 198));
        botonVerProductos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonVerProductos.setText("Ver productos");
        botonVerProductos.setFont(new java.awt.Font("Source Sans Pro Semibold", 1, 18)); // NOI18N
        botonVerProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerProductosActionPerformed(evt);
            }
        });

        botonVerUsuarios.setBackground(new java.awt.Color(79, 134, 198));
        botonVerUsuarios.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonVerUsuarios.setText("Ver usuarios");
        botonVerUsuarios.setFont(new java.awt.Font("Source Sans Pro Semibold", 1, 18)); // NOI18N
        botonVerUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerUsuariosActionPerformed(evt);
            }
        });

        botonVerPedidos.setBackground(new java.awt.Color(79, 134, 198));
        botonVerPedidos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonVerPedidos.setText("Ver pedidos");
        botonVerPedidos.setFont(new java.awt.Font("Source Sans Pro Semibold", 1, 18)); // NOI18N
        botonVerPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerPedidosActionPerformed(evt);
            }
        });

        lblAutor2.setBackground(new java.awt.Color(255, 255, 255));
        lblAutor2.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        lblAutor2.setForeground(new java.awt.Color(255, 255, 255));
        lblAutor2.setText("   Álvaro Morcillo Barbero");

        lblAutor1.setBackground(new java.awt.Color(255, 255, 255));
        lblAutor1.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        lblAutor1.setForeground(new java.awt.Color(255, 255, 255));
        lblAutor1.setText("Creado por:");

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                                .addComponent(rSLabelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                                .addComponent(botonVerPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                                .addComponent(botonVerUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                        .addComponent(botonVerProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addComponent(lblAutor2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(lblAutor1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(rSLabelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(botonVerProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonVerPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonVerUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 351, Short.MAX_VALUE)
                .addComponent(lblAutor1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAutor2)
                .addContainerGap())
        );

        backgroundPanel.add(panelBotones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 742));

        panelInformacion.setBackground(new java.awt.Color(79, 134, 198));

        panelSuperior.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBorder(null);

        tablaInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaInfo.setColorFilasBackgound2(new java.awt.Color(231, 229, 229));
        tablaInfo.setFont(new java.awt.Font("Source Sans Pro Light", 1, 14)); // NOI18N
        tablaInfo.setFuenteFilas(new java.awt.Font("Source Sans Pro Light", 1, 14)); // NOI18N
        tablaInfo.setFuenteFilasSelect(new java.awt.Font("Source Sans Pro Light", 1, 16)); // NOI18N
        tablaInfo.setFuenteHead(new java.awt.Font("Source Serif Pro Light", 1, 18)); // NOI18N
        tablaInfo.setMaximumSize(new java.awt.Dimension(2, 304));
        tablaInfo.setMinimumSize(new java.awt.Dimension(60, 300));
        tablaInfo.setShowGrid(false);
        jScrollPane1.setViewportView(tablaInfo);

        lblTituloTabla.setBackground(new java.awt.Color(255, 255, 255));
        lblTituloTabla.setFont(new java.awt.Font("Source Serif Pro Light", 1, 24)); // NOI18N
        lblTituloTabla.setText("Pedidos");

        javax.swing.GroupLayout panelPedidosLayout = new javax.swing.GroupLayout(panelPedidos);
        panelPedidos.setLayout(panelPedidosLayout);
        panelPedidosLayout.setHorizontalGroup(
            panelPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelPedidosLayout.createSequentialGroup()
                .addGap(530, 530, 530)
                .addComponent(lblTituloTabla)
                .addContainerGap(561, Short.MAX_VALUE))
        );
        panelPedidosLayout.setVerticalGroup(
            panelPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedidosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelSuperior.add(panelPedidos, "card2");

        panelInferior.setBackground(new java.awt.Color(79, 134, 198));
        panelInferior.setForeground(new java.awt.Color(79, 134, 198));

        panelPedidos4.setBackground(new java.awt.Color(255, 255, 255));

        panelLineas_izq2.setBackground(new java.awt.Color(255, 255, 255));

        tablaLineas2.setAutoCreateColumnsFromModel(false);
        tablaLineas2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaLineas2.setColorFilasBackgound2(new java.awt.Color(231, 229, 229));
        tablaLineas2.setFont(new java.awt.Font("Source Sans Pro Light", 1, 14)); // NOI18N
        tablaLineas2.setFuenteFilas(new java.awt.Font("Source Sans Pro Light", 1, 14)); // NOI18N
        tablaLineas2.setFuenteFilasSelect(new java.awt.Font("Source Sans Pro Light", 1, 16)); // NOI18N
        tablaLineas2.setFuenteHead(new java.awt.Font("Source Serif Pro Light", 1, 18)); // NOI18N
        tablaLineas2.setShowGrid(false);
        jScrollPane4.setViewportView(tablaLineas2);

        javax.swing.GroupLayout panelLineas_izq2Layout = new javax.swing.GroupLayout(panelLineas_izq2);
        panelLineas_izq2.setLayout(panelLineas_izq2Layout);
        panelLineas_izq2Layout.setHorizontalGroup(
            panelLineas_izq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLineas_izq2Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        panelLineas_izq2Layout.setVerticalGroup(
            panelLineas_izq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        panelLineas_der.setBackground(new java.awt.Color(255, 255, 255));

        labelNombreProducto12.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto12.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto12.setText("Precio total");

        lblEstado.setBackground(new java.awt.Color(255, 255, 255));
        lblEstado.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblEstado.setText("estado");

        labelNombreProducto14.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto14.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto14.setText("Repartidor");

        labelNombreProducto17.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto17.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto17.setText("Usuario");

        labelNombreProducto18.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto18.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto18.setText("Pagado:");

        botonAplicarCambios2.setBackground(new java.awt.Color(55, 147, 114));
        botonAplicarCambios2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonAplicarCambios2.setText("Ver factura");
        botonAplicarCambios2.setColorHover(new java.awt.Color(70, 198, 136));
        botonAplicarCambios2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAplicarCambios2ActionPerformed(evt);
            }
        });

        botonCancelarPedido.setBackground(new java.awt.Color(235, 86, 64));
        botonCancelarPedido.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonCancelarPedido.setText("Cancelar pedido");
        botonCancelarPedido.setColorHover(new java.awt.Color(255, 153, 153));
        botonCancelarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarPedidoActionPerformed(evt);
            }
        });

        lblPago.setBackground(new java.awt.Color(255, 255, 255));
        lblPago.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblPago.setForeground(new java.awt.Color(235, 86, 64));
        lblPago.setText("No");

        labelNombreProducto15.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto15.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto15.setText("Estado del pedido");

        lblUsuario.setBackground(new java.awt.Color(255, 255, 255));
        lblUsuario.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblUsuario.setText("estado");

        lblRepartidor.setBackground(new java.awt.Color(255, 255, 255));
        lblRepartidor.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblRepartidor.setText("estado");

        lblPrecio.setBackground(new java.awt.Color(255, 255, 255));
        lblPrecio.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        lblPrecio.setText("estado");

        javax.swing.GroupLayout panelLineas_derLayout = new javax.swing.GroupLayout(panelLineas_der);
        panelLineas_der.setLayout(panelLineas_derLayout);
        panelLineas_derLayout.setHorizontalGroup(
            panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLineas_derLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonCancelarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelLineas_derLayout.createSequentialGroup()
                        .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelNombreProducto15)
                            .addComponent(labelNombreProducto14)
                            .addComponent(labelNombreProducto17)
                            .addComponent(labelNombreProducto12))
                        .addGap(18, 18, 18)
                        .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEstado)
                            .addComponent(lblUsuario)
                            .addComponent(lblRepartidor)
                            .addComponent(lblPrecio))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLineas_derLayout.createSequentialGroup()
                        .addComponent(labelNombreProducto18)
                        .addGap(18, 18, 18)
                        .addComponent(lblPago)
                        .addGap(74, 74, 74))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLineas_derLayout.createSequentialGroup()
                        .addComponent(botonAplicarCambios2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))))
        );
        panelLineas_derLayout.setVerticalGroup(
            panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLineas_derLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombreProducto18, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPago, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNombreProducto15, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombreProducto12, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombreProducto17, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombreProducto14, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRepartidor, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(panelLineas_derLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAplicarCambios2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCancelarPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelPedidos4Layout = new javax.swing.GroupLayout(panelPedidos4);
        panelPedidos4.setLayout(panelPedidos4Layout);
        panelPedidos4Layout.setHorizontalGroup(
            panelPedidos4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedidos4Layout.createSequentialGroup()
                .addComponent(panelLineas_izq2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelLineas_der, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPedidos4Layout.setVerticalGroup(
            panelPedidos4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLineas_der, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelLineas_izq2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelInferior.add(panelPedidos4, "card2");

        panelProductos.setBackground(new java.awt.Color(255, 255, 255));

        labelNombreProducto.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto.setText("<html>Nombre:</html>");

        fieldNombre.setPlaceholder("");
        fieldNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldNombreActionPerformed(evt);
            }
        });

        fieldDesc.setPlaceholder("");

        labelNombreProducto1.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto1.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto1.setText("Descripcion:");

        fieldPrecio.setPlaceholder("");

        labelNombreProducto2.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto2.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto2.setText("Precio:");

        labelNombreProducto3.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto3.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto3.setText("Stock:");

        imagenProducto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        imagenProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/logo/logo_transparent.png"))); // NOI18N

        botonCambiarImagen.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonCambiarImagen.setText("...");
        botonCambiarImagen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCambiarImagenActionPerformed(evt);
            }
        });

        rSButtonMetro1.setBackground(new java.awt.Color(55, 147, 114));
        rSButtonMetro1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonMetro1.setText("++");
        rSButtonMetro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonMetro1ActionPerformed(evt);
            }
        });

        rSButtonMetro2.setBackground(new java.awt.Color(55, 147, 114));
        rSButtonMetro2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonMetro2.setText("+");
        rSButtonMetro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonMetro2ActionPerformed(evt);
            }
        });

        rSButtonMetro3.setBackground(new java.awt.Color(235, 86, 64));
        rSButtonMetro3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonMetro3.setText("-");
        rSButtonMetro3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonMetro3ActionPerformed(evt);
            }
        });

        rSButtonMetro4.setBackground(new java.awt.Color(235, 86, 64));
        rSButtonMetro4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonMetro4.setText("--");
        rSButtonMetro4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonMetro4ActionPerformed(evt);
            }
        });

        fieldStock.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldStock.setText("0");
        fieldStock.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 112, 192), 1, true));

        botonAñadirProducto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonAñadirProducto.setText("Añadir");
        botonAñadirProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAñadirProductoActionPerformed(evt);
            }
        });

        botonEliminarProducto.setBackground(new java.awt.Color(235, 86, 64));
        botonEliminarProducto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonEliminarProducto.setText("Eliminar");
        botonEliminarProducto.setColorHover(new java.awt.Color(255, 153, 153));
        botonEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarProductoActionPerformed(evt);
            }
        });

        botonAplicarCambios.setBackground(new java.awt.Color(55, 147, 114));
        botonAplicarCambios.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonAplicarCambios.setText("Aplicar cambios");
        botonAplicarCambios.setColorHover(new java.awt.Color(70, 198, 136));
        botonAplicarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAplicarCambiosActionPerformed(evt);
            }
        });

        lblConsola.setBackground(new java.awt.Color(255, 255, 255));
        lblConsola.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lblConsola.setForeground(new java.awt.Color(255, 0, 0));
        lblConsola.setText("Compruebe que no hay campos vacios");

        javax.swing.GroupLayout panelProductosLayout = new javax.swing.GroupLayout(panelProductos);
        panelProductos.setLayout(panelProductosLayout);
        panelProductosLayout.setHorizontalGroup(
            panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductosLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProductosLayout.createSequentialGroup()
                        .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelProductosLayout.createSequentialGroup()
                                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(labelNombreProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelNombreProducto1, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(55, 55, 55)
                                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(fieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldDesc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
                                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelNombreProducto2)
                                    .addComponent(labelNombreProducto3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelProductosLayout.createSequentialGroup()
                                        .addComponent(rSButtonMetro4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rSButtonMetro3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fieldStock, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rSButtonMetro2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rSButtonMetro1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(fieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelProductosLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblConsola)
                                .addGap(7, 7, 7)))
                        .addGap(43, 43, 43)
                        .addComponent(botonCambiarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                        .addComponent(imagenProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))
                    .addGroup(panelProductosLayout.createSequentialGroup()
                        .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelProductosLayout.createSequentialGroup()
                                .addGap(231, 231, 231)
                                .addComponent(botonAñadirProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(botonAplicarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))))
        );
        panelProductosLayout.setVerticalGroup(
            panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductosLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelProductosLayout.createSequentialGroup()
                            .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labelNombreProducto2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelNombreProducto3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(rSButtonMetro2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rSButtonMetro1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rSButtonMetro3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rSButtonMetro4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(panelProductosLayout.createSequentialGroup()
                            .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labelNombreProducto)
                                .addComponent(fieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labelNombreProducto1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(fieldDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelProductosLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imagenProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelProductosLayout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(botonCambiarImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAplicarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAñadirProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblConsola, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        panelInferior.add(panelProductos, "card3");

        panelUsuarios.setBackground(new java.awt.Color(255, 255, 255));
        panelUsuarios.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelNombreProducto4.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto4.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto4.setText("DNI");
        panelUsuarios.add(labelNombreProducto4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 80, 49, 42));

        fieldNomUsuario.setPlaceholder("");
        panelUsuarios.add(fieldNomUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 346, -1));

        labelNombreProducto5.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto5.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto5.setText("Email");
        panelUsuarios.add(labelNombreProducto5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 130, 60, 42));

        fieldEmail.setPlaceholder("");
        panelUsuarios.add(fieldEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 346, -1));

        labelNombreProducto6.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto6.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto6.setText("Nombre completo");
        panelUsuarios.add(labelNombreProducto6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, 42));

        fieldNom.setPlaceholder("");
        panelUsuarios.add(fieldNom, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 346, -1));

        labelNombreProducto7.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto7.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto7.setText("Telefono");
        panelUsuarios.add(labelNombreProducto7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, -1, 42));

        fieldTelefono.setPlaceholder("");
        panelUsuarios.add(fieldTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 230, 346, -1));

        labelRolUsuario.setBackground(new java.awt.Color(255, 255, 255));
        labelRolUsuario.setFont(new java.awt.Font("Segoe UI Light", 1, 22)); // NOI18N
        labelRolUsuario.setForeground(new java.awt.Color(255, 51, 51));
        labelRolUsuario.setText("ADMIN");
        panelUsuarios.add(labelRolUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(203, 12, 151, 42));

        labelNombreProducto9.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto9.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto9.setText("Tipo de usuario");
        panelUsuarios.add(labelNombreProducto9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 11, -1, 42));

        labelNombreProducto10.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto10.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        labelNombreProducto10.setText("Nombre usuario");
        panelUsuarios.add(labelNombreProducto10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, -1, 42));

        fieldDni.setEditable(false);
        fieldDni.setPlaceholder("");
        panelUsuarios.add(fieldDni, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 80, -1, -1));

        botonEliminarUsuario.setBackground(new java.awt.Color(235, 86, 64));
        botonEliminarUsuario.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonEliminarUsuario.setText("Eliminar");
        botonEliminarUsuario.setColorHover(new java.awt.Color(255, 153, 153));
        botonEliminarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarUsuarioActionPerformed(evt);
            }
        });
        panelUsuarios.add(botonEliminarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 170, -1, -1));

        botonAñadirUsuario.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonAñadirUsuario.setText("Añadir usuario");
        botonAñadirUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAñadirUsuarioActionPerformed(evt);
            }
        });
        panelUsuarios.add(botonAñadirUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 220, -1, -1));

        botonAplicarUsuario.setBackground(new java.awt.Color(55, 147, 114));
        botonAplicarUsuario.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonAplicarUsuario.setText("Aplicar cambios");
        botonAplicarUsuario.setColorHover(new java.awt.Color(70, 198, 136));
        botonAplicarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAplicarUsuarioActionPerformed(evt);
            }
        });
        panelUsuarios.add(botonAplicarUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 170, -1, -1));

        botonCambiarContraseña.setBackground(new java.awt.Color(221, 160, 21));
        botonCambiarContraseña.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonCambiarContraseña.setText("Cambiar contraseña");
        botonCambiarContraseña.setColorHover(new java.awt.Color(243, 206, 59));
        botonCambiarContraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCambiarContraseñaActionPerformed(evt);
            }
        });
        panelUsuarios.add(botonCambiarContraseña, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 220, -1, -1));

        panelInferior.add(panelUsuarios, "card4");

        labelReloj.setBackground(new java.awt.Color(255, 255, 255));
        labelReloj.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        labelReloj.setForeground(new java.awt.Color(255, 255, 255));
        labelReloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelReloj.setText("XX:XX");

        lblLog.setBackground(new java.awt.Color(255, 255, 255));
        lblLog.setFont(new java.awt.Font("Source Sans Pro Light", 1, 24)); // NOI18N
        lblLog.setForeground(new java.awt.Color(255, 255, 255));
        lblLog.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLog.setText("Sesión iniciada como:");

        lblUsuarioLogeadoRol.setFont(new java.awt.Font("Source Sans Pro Light", 1, 24)); // NOI18N
        lblUsuarioLogeadoRol.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuarioLogeadoRol.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsuarioLogeadoRol.setText("(Admin)");

        lblUsuarioLogeado.setBackground(new java.awt.Color(255, 255, 255));
        lblUsuarioLogeado.setFont(new java.awt.Font("Source Sans Pro Light", 1, 24)); // NOI18N
        lblUsuarioLogeado.setForeground(new java.awt.Color(255, 255, 255));
        lblUsuarioLogeado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsuarioLogeado.setText("Carlos");

        javax.swing.GroupLayout panelInformacionLayout = new javax.swing.GroupLayout(panelInformacion);
        panelInformacion.setLayout(panelInformacionLayout);
        panelInformacionLayout.setHorizontalGroup(
            panelInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInformacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSuperior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelInformacionLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(lblLog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUsuarioLogeado, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUsuarioLogeadoRol)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelReloj)
                        .addGap(35, 35, 35))
                    .addComponent(panelInferior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelInformacionLayout.setVerticalGroup(
            panelInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInformacionLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLog)
                    .addComponent(lblUsuarioLogeado, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUsuarioLogeadoRol, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelReloj))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        backgroundPanel.add(panelInformacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(208, 0, 1200, 740));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1423, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonVerPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerPedidosActionPerformed
        // TODO add your handling code here:

        this.actualizarTablas();
        actualizando = false;
        this.tablaInfo.setVisible(true);
        tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getPedidos()));
        tablaSeleccionada = "pedidos";
        CardLayout cl = (CardLayout) (panelInferior.getLayout());
        cl.show(panelInferior, "card2");

        this.lblTituloTabla.setText("Pedidos");


    }//GEN-LAST:event_botonVerPedidosActionPerformed

    private void botonVerProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerProductosActionPerformed
        // TODO add your handling code here:
        this.actualizarTablas();
        this.imagenProducto.setIcon(new ImageIcon("src\\ui\\images\\logo\\logo_transparent.png"));
        actualizando = false;

        this.tablaInfo.setVisible(true);
        tablaSeleccionada = "productos";
        tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getProductos()));
        CardLayout cl = (CardLayout) (panelInferior.getLayout());
        cl.show(panelInferior, "card3");

        this.lblTituloTabla.setText("Productos");
        this.lblConsola.setVisible(false);

    }//GEN-LAST:event_botonVerProductosActionPerformed


    private void botonVerUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerUsuariosActionPerformed
        this.actualizarTablas();
        actualizando = false;

        this.tablaInfo.setVisible(true);
        tablaSeleccionada = "usuarios";
        tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getUsuarios()));
        CardLayout cl = (CardLayout) (panelInferior.getLayout());
        cl.show(panelInferior, "card4");

        this.lblTituloTabla.setText("Usuarios");

    }//GEN-LAST:event_botonVerUsuariosActionPerformed

    private void botonAplicarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAplicarUsuarioActionPerformed
        // TODO add your handling code here:

        String nombreCompleto = fieldNom.getText();
        String nombre, apellidos;

        nombre = nombreCompleto.substring(0, nombreCompleto.indexOf(" "));
        apellidos = nombreCompleto.substring(nombreCompleto.indexOf(" "), nombreCompleto.length());

        System.out.println(nombre + apellidos);

        Dialog_Confirmar dialog = new Dialog_Confirmar(this, true, 0000);

        dialog.setVisible(true);

        if (dialog.getReturnStatus() == 1) {
            OperacionesBDD.modificarUsuario(usuario.getNombreUsuario(), fieldNomUsuario.getText(), fieldEmail.getText(), nombre, apellidos, fieldTelefono.getText(), fieldDni.getText());
            this.actualizarTablas();
        }


    }//GEN-LAST:event_botonAplicarUsuarioActionPerformed

    private void botonAñadirUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAñadirUsuarioActionPerformed
        // TODO add your handling code here:

        Dialog_nuevoUsuario dialog = new Dialog_nuevoUsuario(this, true);

        dialog.setVisible(true);

        if (dialog.getReturnStatus() == 1) {

        }

        this.actualizarTablas();
    }//GEN-LAST:event_botonAñadirUsuarioActionPerformed

    private void botonEliminarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarUsuarioActionPerformed
        // TODO add your handling code here:

        if (usuarioLogeadoRol == usuarioSeleccionadoRol || usuarioLogeadoRol < usuarioSeleccionadoRol) {
            Dialog_Confirmar dialog = new Dialog_Confirmar(this, true, 0000);

            dialog.setVisible(true);

            if (dialog.getReturnStatus() == 1) {
                System.out.println("Borrando " + usuario.getNombre());
                OperacionesBDD.eliminarUsuario(usuario.getNombreUsuario());
                this.actualizarTablas();
            }

        } else {
            System.out.println("No tienes permiso para eliminar");

        }
    }//GEN-LAST:event_botonEliminarUsuarioActionPerformed

    private void botonAplicarCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAplicarCambiosActionPerformed
        // TODO add your handling code here:
        File imagen = null;

        ArrayList<JTextComponent> componentes = new ArrayList<>();

        componentes.add(this.fieldNombre);
        componentes.add(this.fieldDesc);
        componentes.add(this.fieldPrecio);
        componentes.add(this.fieldStock);

        if (!Util.comprobarCamposVacíos(componentes)) {
            Dialog_Confirmar dialog = new Dialog_Confirmar(this, true, 0000);

            dialog.setVisible(true);

            if (dialog.getReturnStatus() == 1) {
                System.out.println("Producto " + fieldNombre.getText() + " modificado.");
                try {
                    ftp.borrarArchivo(OperacionesBDD.getRutaImagenProductoSeleccionado(producto.getId()));
                    OperacionesBDD.modificarProducto(producto.getId(), fieldNombre.getText(), fieldDesc.getText(), Float.valueOf(fieldPrecio.getText()), rutaImagenSeleccionada, Integer.valueOf(fieldStock.getText()));
                    ftp.subirArchivo(imagenSeleccionada, rutaImagenSeleccionada);
                } catch (SQLException ex) {
                    Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
                }

                this.actualizarTablas();
            }
        } else {

            this.lblConsola.setVisible(true);
            this.lblConsola.setText("Compruebe que no hay campos vacíos");
        }


    }//GEN-LAST:event_botonAplicarCambiosActionPerformed

    private void botonEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarProductoActionPerformed
        // TODO add your handling code here:

        Dialog_Confirmar dialog = new Dialog_Confirmar(this, true, 0000);

        dialog.setVisible(true);

        if (dialog.getReturnStatus() == 1) {
            System.out.println("Borrando " + producto.getNombre());
            try {
                String ruta = OperacionesBDD.getRutaImagenProductoSeleccionado(producto.getId());
                ftp.borrarArchivo(ruta);
                OperacionesBDD.eliminarProducto(producto.getId());

            } catch (SQLException ex) {
                Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.actualizarTablas();
            this.lblConsola.setVisible(false);
        }


    }//GEN-LAST:event_botonEliminarProductoActionPerformed

    private void botonAñadirProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAñadirProductoActionPerformed
        // TODO add your handling code here:

        ArrayList<JTextComponent> componentes = new ArrayList<>();

        componentes.add(this.fieldNombre);
        componentes.add(this.fieldDesc);
        componentes.add(this.fieldPrecio);
        componentes.add(this.fieldStock);

        if (!Util.comprobarCamposVacíos(componentes)) {
            Dialog_Confirmar dialog = new Dialog_Confirmar(this, true, 0000);

            dialog.setVisible(true);

            if (dialog.getReturnStatus() == 1) {
                try {
                    OperacionesBDD.añadirProducto(fieldNombre.getText(), fieldDesc.getText(), Float.valueOf(fieldPrecio.getText()), rutaImagenSeleccionada, Integer.valueOf(fieldStock.getText()));
                    if (imagenSeleccionada != null) {
                        try {
                            System.out.println("Subiendo el archivo " + imagenSeleccionada + " a " + rutaImagenSeleccionada);
                            ftp.subirArchivo(imagenSeleccionada, rutaImagenSeleccionada);

                        } catch (IOException ex) {
                            Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("Producto " + fieldNombre.getText() + " añadido.");
                    this.actualizarTablas();
                    this.lblConsola.setVisible(false);

                } catch (SQLException ex) {
                    Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } else {

            this.lblConsola.setVisible(true);
            this.lblConsola.setText("Compruebe que no hay campos vacíos");
        }


    }//GEN-LAST:event_botonAñadirProductoActionPerformed

    private void rSButtonMetro4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonMetro4ActionPerformed
        // TODO add your handling code here:
        fieldStock.setText(Integer.valueOf(fieldStock.getText()) - 10 + "");
    }//GEN-LAST:event_rSButtonMetro4ActionPerformed

    private void rSButtonMetro3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonMetro3ActionPerformed
        // TODO add your handling code here:
        fieldStock.setText(Integer.valueOf(fieldStock.getText()) - 1 + "");
    }//GEN-LAST:event_rSButtonMetro3ActionPerformed

    private void rSButtonMetro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonMetro2ActionPerformed
        fieldStock.setText(Integer.valueOf(fieldStock.getText()) + 1 + "");
    }//GEN-LAST:event_rSButtonMetro2ActionPerformed

    private void rSButtonMetro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonMetro1ActionPerformed
        // TODO add your handling code here:
        fieldStock.setText(Integer.valueOf(fieldStock.getText()) + 10 + "");
    }//GEN-LAST:event_rSButtonMetro1ActionPerformed

    File imagenSeleccionada = null;
    String rutaImagenSeleccionada = null;
    private void botonCambiarImagenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCambiarImagenActionPerformed
        // TODO add your handling code here:
        RSFileChooser fileChooser = new RSFileChooser();

        FileFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());

        fileChooser.setFileFilter(imageFilter);
        fileChooser.showOpenDialog(this);
        imagenSeleccionada = fileChooser.getSelectedFile();

        rutaImagenSeleccionada = imagenSeleccionada.getName();
        System.out.println("ruta de la imagen seleccionada: " + rutaImagenSeleccionada);

        this.imagenProducto.setIcon(new ImageIcon(imagenSeleccionada.getAbsolutePath()));
    }//GEN-LAST:event_botonCambiarImagenActionPerformed

    private void fieldNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fieldNombreActionPerformed

    private void botonAplicarCambios2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAplicarCambios2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonAplicarCambios2ActionPerformed

    private void botonCancelarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarPedidoActionPerformed
        // TODO add your handling code here:
        Dialog_Confirmar dialog = new Dialog_Confirmar(this, true, 0000);

        dialog.setVisible(true);

        if (dialog.getReturnStatus() == 1) {
            System.out.println("Cancelando el pedido...");
            try {
                OperacionesBDD.cancelarPedido(idPedido);
            } catch (SQLException ex) {
                Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_botonCancelarPedidoActionPerformed

    private void botonCambiarContraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCambiarContraseñaActionPerformed
        // TODO add your handling code here:

        Dialog_cambiarContraseña dialog = new Dialog_cambiarContraseña(this, true, Frame_Login.usuarioLogeado);

        dialog.setVisible(true);

        if (dialog.getReturnStatus() == 1) {

        }
    }//GEN-LAST:event_botonCambiarContraseñaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame_Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame_Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame_Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame_Principal.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame_Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundPanel;
    private rojerusan.RSButtonHover botonAplicarCambios;
    private rojerusan.RSButtonHover botonAplicarCambios2;
    private rojerusan.RSButtonHover botonAplicarUsuario;
    private rojerusan.RSButtonHover botonAñadirProducto;
    private rojerusan.RSButtonHover botonAñadirUsuario;
    private rojerusan.RSButtonHover botonCambiarContraseña;
    private rojerusan.RSMaterialButtonRound botonCambiarImagen;
    private rojerusan.RSButtonHover botonCancelarPedido;
    private rojerusan.RSButtonHover botonEliminarProducto;
    private rojerusan.RSButtonHover botonEliminarUsuario;
    private rojeru_san.RSButtonRiple botonVerPedidos;
    private rojeru_san.RSButtonRiple botonVerProductos;
    private rojeru_san.RSButtonRiple botonVerUsuarios;
    private rojerusan.RSMetroTextFullPlaceHolder fieldDesc;
    private rojeru_san.RSMPassView fieldDni;
    private rojerusan.RSMetroTextFullPlaceHolder fieldEmail;
    private rojerusan.RSMetroTextFullPlaceHolder fieldNom;
    private rojerusan.RSMetroTextFullPlaceHolder fieldNomUsuario;
    private rojerusan.RSMetroTextFullPlaceHolder fieldNombre;
    private rojerusan.RSMetroTextFullPlaceHolder fieldPrecio;
    private javax.swing.JTextField fieldStock;
    private rojerusan.RSMetroTextFullPlaceHolder fieldTelefono;
    private rojerusan.RSLabelImage imagenProducto;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelNombreProducto;
    private javax.swing.JLabel labelNombreProducto1;
    private javax.swing.JLabel labelNombreProducto10;
    private javax.swing.JLabel labelNombreProducto12;
    private javax.swing.JLabel labelNombreProducto14;
    private javax.swing.JLabel labelNombreProducto15;
    private javax.swing.JLabel labelNombreProducto17;
    private javax.swing.JLabel labelNombreProducto18;
    private javax.swing.JLabel labelNombreProducto2;
    private javax.swing.JLabel labelNombreProducto3;
    private javax.swing.JLabel labelNombreProducto4;
    private javax.swing.JLabel labelNombreProducto5;
    private javax.swing.JLabel labelNombreProducto6;
    private javax.swing.JLabel labelNombreProducto7;
    private javax.swing.JLabel labelNombreProducto9;
    private javax.swing.JLabel labelReloj;
    private javax.swing.JLabel labelRolUsuario;
    private javax.swing.JLabel lblAutor1;
    private javax.swing.JLabel lblAutor2;
    private javax.swing.JLabel lblConsola;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblLog;
    private javax.swing.JLabel lblPago;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblRepartidor;
    private javax.swing.JLabel lblTituloTabla;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblUsuarioLogeado;
    private javax.swing.JLabel lblUsuarioLogeadoRol;
    private javax.swing.JPanel panelBotones;
    private rojerusan.RSPanelsSlider panelInferior;
    private javax.swing.JPanel panelInformacion;
    private javax.swing.JPanel panelLineas_der;
    private javax.swing.JPanel panelLineas_izq2;
    private javax.swing.JPanel panelPedidos;
    private javax.swing.JPanel panelPedidos4;
    private javax.swing.JPanel panelProductos;
    private rojerusan.RSPanelsSlider panelSuperior;
    private javax.swing.JPanel panelUsuarios;
    private rojerusan.RSButtonMetro rSButtonMetro1;
    private rojerusan.RSButtonMetro rSButtonMetro2;
    private rojerusan.RSButtonMetro rSButtonMetro3;
    private rojerusan.RSButtonMetro rSButtonMetro4;
    private rojerusan.RSLabelImage rSLabelImage1;
    private rojerusan.RSTableMetro tablaInfo;
    private rojerusan.RSTableMetro tablaLineas2;
    // End of variables declaration//GEN-END:variables
}
