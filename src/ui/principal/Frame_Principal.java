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
import java.io.File;
import static java.lang.Thread.sleep;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;
import necesario.RSFileChooser;
import net.proteanit.sql.DbUtils;
import pojos.Producto;
import threads.Reloj;
import util.OperacionesBDD;

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

    private void iniciarComponentes() {
        setLocationRelativeTo(null);
        tablaInfo.setDefaultEditor(Object.class, null);
        tablaLineas2.setDefaultEditor(Object.class, null);
        new Reloj(labelReloj).start();

        try {
            OperacionesBDD.iniciarConexion();

            //MUESTRA LA TABLA PEDIDOS Y LINEAS 
            tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getPedidos()));
            tablaLineas2.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getLineas(idPedido)));
            tablaSeleccionada = "pedidos";

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Frame_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.añadirListenersTablas();

    }

    private void actualizarTablas() {
        DefaultTableModel dtm1 = (DefaultTableModel) this.tablaInfo.getModel();

        dtm1.setRowCount(0);

        tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getProductos()));

    }

    private void añadirListenersTablas() {

        //AL PULSAR UN PEDIDO EN LA TABLA SE MUESTRA SUS LINEAS EN LA OTRA TABLA
        this.tablaInfo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (tablaSeleccionada.equals("pedidos")) {

                    idPedido = Integer.valueOf(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 0).toString());
                    tablaLineas2.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getLineas(idPedido)));
                }
            }
        });

        this.tablaInfo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (tablaSeleccionada.equals("productos")) {

                    producto.setId(Integer.valueOf(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 0).toString()));
                    producto.setNombre((tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 1).toString()));
                    producto.setPrecio(Float.valueOf(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 3).toString()));
                    producto.setDescripcion((tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 2).toString()));
                    producto.setStock(Integer.valueOf(tablaInfo.getValueAt(tablaInfo.getSelectedRow(), 4).toString()));

                    fieldNombre.setText(producto.getNombre());
                    fieldDesc.setText(producto.getDescripcion());
                    fieldPrecio.setText(producto.getPrecio() + "");
                    fieldStock.setText(producto.getStock() + "");
                }
            }
        });
    }

    private void eliminarListenersTablas() {
        this.tablaInfo.getSelectionModel().removeListSelectionListener(tablaInfo);
        this.tablaLineas2.getSelectionModel().removeListSelectionListener(tablaLineas2);
    }
    Producto producto = new Producto();

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
        panelInformacion = new javax.swing.JPanel();
        panelSuperior = new rojerusan.RSPanelsSlider();
        panelPedidos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaInfo = new rojerusan.RSTableMetro();
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
        rSButtonHover5 = new rojerusan.RSButtonHover();
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
        rSMaterialButtonRound1 = new rojerusan.RSMaterialButtonRound();
        rSButtonMetro1 = new rojerusan.RSButtonMetro();
        rSButtonMetro2 = new rojerusan.RSButtonMetro();
        rSButtonMetro3 = new rojerusan.RSButtonMetro();
        rSButtonMetro4 = new rojerusan.RSButtonMetro();
        fieldStock = new javax.swing.JTextField();
        botonAñadirProducto = new rojerusan.RSButtonHover();
        botonEliminarProducto = new rojerusan.RSButtonHover();
        botonAplicarCambios = new rojerusan.RSButtonHover();
        panelUsuarios = new javax.swing.JPanel();
        labelNombreProducto4 = new javax.swing.JLabel();
        fieldNomUsuario = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto5 = new javax.swing.JLabel();
        fieldEmail = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto6 = new javax.swing.JLabel();
        fieldEmail1 = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto7 = new javax.swing.JLabel();
        fieldEmail2 = new rojerusan.RSMetroTextFullPlaceHolder();
        labelNombreProducto8 = new javax.swing.JLabel();
        labelNombreProducto9 = new javax.swing.JLabel();
        labelNombreProducto10 = new javax.swing.JLabel();
        lblContra = new rojeru_san.RSMPassView();
        labelNombreProducto11 = new javax.swing.JLabel();
        lblDNI = new rojeru_san.RSMPassView();
        rSButtonHover3 = new rojerusan.RSButtonHover();
        rSButtonHover4 = new rojerusan.RSButtonHover();
        botonAplicarCambios1 = new rojerusan.RSButtonHover();
        labelReloj = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        backgroundPanel.setBackground(new java.awt.Color(79, 134, 198));
        backgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelBotones.setBackground(new java.awt.Color(79, 134, 198));

        rSLabelImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/logo/logo_transparent.png"))); // NOI18N

        botonVerProductos.setBackground(new java.awt.Color(79, 134, 198));
        botonVerProductos.setBorder(null);
        botonVerProductos.setText("Ver productos");
        botonVerProductos.setFont(new java.awt.Font("Roboto Bold", 1, 18)); // NOI18N
        botonVerProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerProductosActionPerformed(evt);
            }
        });

        botonVerUsuarios.setBackground(new java.awt.Color(79, 134, 198));
        botonVerUsuarios.setBorder(null);
        botonVerUsuarios.setText("Ver usuarios");
        botonVerUsuarios.setFont(new java.awt.Font("Roboto Bold", 1, 18)); // NOI18N
        botonVerUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerUsuariosActionPerformed(evt);
            }
        });

        botonVerPedidos.setBackground(new java.awt.Color(79, 134, 198));
        botonVerPedidos.setBorder(null);
        botonVerPedidos.setText("Ver pedidos");
        botonVerPedidos.setFont(new java.awt.Font("Roboto Bold", 1, 18)); // NOI18N
        botonVerPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonVerPedidosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBotonesLayout = new javax.swing.GroupLayout(panelBotones);
        panelBotones.setLayout(panelBotonesLayout);
        panelBotonesLayout.setHorizontalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(botonVerUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBotonesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rSLabelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addComponent(botonVerPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(botonVerProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelBotonesLayout.setVerticalGroup(
            panelBotonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBotonesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rSLabelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(botonVerProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonVerPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonVerUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        backgroundPanel.add(panelBotones, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 742));

        panelInformacion.setBackground(new java.awt.Color(79, 134, 198));

        panelSuperior.setBackground(new java.awt.Color(255, 255, 255));

        tablaInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
        jScrollPane1.setViewportView(tablaInfo);

        javax.swing.GroupLayout panelPedidosLayout = new javax.swing.GroupLayout(panelPedidos);
        panelPedidos.setLayout(panelPedidosLayout);
        panelPedidosLayout.setHorizontalGroup(
            panelPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1176, Short.MAX_VALUE)
        );
        panelPedidosLayout.setVerticalGroup(
            panelPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
        );

        panelSuperior.add(panelPedidos, "card2");

        panelInferior.setBackground(new java.awt.Color(79, 134, 198));
        panelInferior.setForeground(new java.awt.Color(79, 134, 198));

        panelPedidos4.setBackground(new java.awt.Color(255, 255, 255));

        panelLineas_izq2.setBackground(new java.awt.Color(255, 255, 255));

        tablaLineas2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
        jScrollPane4.setViewportView(tablaLineas2);

        javax.swing.GroupLayout panelLineas_izq2Layout = new javax.swing.GroupLayout(panelLineas_izq2);
        panelLineas_izq2.setLayout(panelLineas_izq2Layout);
        panelLineas_izq2Layout.setHorizontalGroup(
            panelLineas_izq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
        );
        panelLineas_izq2Layout.setVerticalGroup(
            panelLineas_izq2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLineas_izq2Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
        botonAplicarCambios2.setText("Ver factura");
        botonAplicarCambios2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAplicarCambios2ActionPerformed(evt);
            }
        });

        rSButtonHover5.setBackground(new java.awt.Color(235, 86, 64));
        rSButtonHover5.setText("Cancelar pedido");

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
                    .addComponent(rSButtonHover5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
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
                    .addComponent(rSButtonHover5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelPedidos4Layout = new javax.swing.GroupLayout(panelPedidos4);
        panelPedidos4.setLayout(panelPedidos4Layout);
        panelPedidos4Layout.setHorizontalGroup(
            panelPedidos4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPedidos4Layout.createSequentialGroup()
                .addComponent(panelLineas_izq2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelLineas_der, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelPedidos4Layout.setVerticalGroup(
            panelPedidos4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelLineas_izq2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelLineas_der, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelInferior.add(panelPedidos4, "card2");

        panelProductos.setBackground(new java.awt.Color(255, 255, 255));
        panelProductos.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));

        labelNombreProducto.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto.setText("<html>Nombre:</html>");

        fieldNombre.setPlaceholder("");

        fieldDesc.setPlaceholder("");

        labelNombreProducto1.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto1.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto1.setText("Descripcion:");

        fieldPrecio.setPlaceholder("");

        labelNombreProducto2.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto2.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto2.setText("Precio:");

        labelNombreProducto3.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto3.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto3.setText("Stock:");

        imagenProducto.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        imagenProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/logo/logo_transparent.png"))); // NOI18N

        rSMaterialButtonRound1.setText("...");
        rSMaterialButtonRound1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSMaterialButtonRound1ActionPerformed(evt);
            }
        });

        rSButtonMetro1.setBackground(new java.awt.Color(55, 147, 114));
        rSButtonMetro1.setText("++");
        rSButtonMetro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonMetro1ActionPerformed(evt);
            }
        });

        rSButtonMetro2.setBackground(new java.awt.Color(55, 147, 114));
        rSButtonMetro2.setText("+");
        rSButtonMetro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonMetro2ActionPerformed(evt);
            }
        });

        rSButtonMetro3.setBackground(new java.awt.Color(235, 86, 64));
        rSButtonMetro3.setText("-");
        rSButtonMetro3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonMetro3ActionPerformed(evt);
            }
        });

        rSButtonMetro4.setBackground(new java.awt.Color(235, 86, 64));
        rSButtonMetro4.setText("--");
        rSButtonMetro4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonMetro4ActionPerformed(evt);
            }
        });

        fieldStock.setEditable(false);
        fieldStock.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldStock.setText("0");
        fieldStock.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 112, 192), 1, true));

        botonAñadirProducto.setText("Añadir");
        botonAñadirProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAñadirProductoActionPerformed(evt);
            }
        });

        botonEliminarProducto.setBackground(new java.awt.Color(235, 86, 64));
        botonEliminarProducto.setText("Eliminar");
        botonEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarProductoActionPerformed(evt);
            }
        });

        botonAplicarCambios.setBackground(new java.awt.Color(55, 147, 114));
        botonAplicarCambios.setText("Aplicar cambios");
        botonAplicarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAplicarCambiosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelProductosLayout = new javax.swing.GroupLayout(panelProductos);
        panelProductos.setLayout(panelProductosLayout);
        panelProductosLayout.setHorizontalGroup(
            panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelProductosLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelProductosLayout.createSequentialGroup()
                        .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelNombreProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelNombreProducto1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(55, 55, 55)
                        .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldDesc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
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
                            .addComponent(fieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addComponent(rSMaterialButtonRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(imagenProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54))
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
                    .addComponent(imagenProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelProductosLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(rSMaterialButtonRound1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAplicarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonAñadirProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        panelInferior.add(panelProductos, "card3");

        panelUsuarios.setBackground(new java.awt.Color(255, 255, 255));

        labelNombreProducto4.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto4.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto4.setText("DNI");

        fieldNomUsuario.setPlaceholder("");

        labelNombreProducto5.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto5.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto5.setText("Email");

        fieldEmail.setPlaceholder("");

        labelNombreProducto6.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto6.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto6.setText("Nombre");

        fieldEmail1.setPlaceholder("");

        labelNombreProducto7.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto7.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto7.setText("Telefono");

        fieldEmail2.setPlaceholder("");

        labelNombreProducto8.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto8.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto8.setForeground(new java.awt.Color(255, 51, 51));
        labelNombreProducto8.setText("admin");

        labelNombreProducto9.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto9.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto9.setText("Tipo de usuario");

        labelNombreProducto10.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto10.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto10.setText("Usuario");

        lblContra.setEditable(false);
        lblContra.setPlaceholder("");

        labelNombreProducto11.setBackground(new java.awt.Color(255, 255, 255));
        labelNombreProducto11.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        labelNombreProducto11.setText("Contraseña");

        lblDNI.setEditable(false);
        lblDNI.setPlaceholder("");

        rSButtonHover3.setBackground(new java.awt.Color(235, 86, 64));
        rSButtonHover3.setText("Eliminar");

        rSButtonHover4.setText("Añadir");

        botonAplicarCambios1.setBackground(new java.awt.Color(55, 147, 114));
        botonAplicarCambios1.setText("Aplicar cambios");
        botonAplicarCambios1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonAplicarCambios1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUsuariosLayout = new javax.swing.GroupLayout(panelUsuarios);
        panelUsuarios.setLayout(panelUsuariosLayout);
        panelUsuariosLayout.setHorizontalGroup(
            panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuariosLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(labelNombreProducto9)
                .addGap(18, 18, 18)
                .addComponent(labelNombreProducto8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelUsuariosLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelNombreProducto7)
                    .addComponent(labelNombreProducto10)
                    .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(labelNombreProducto5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelNombreProducto6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(43, 43, 43)
                .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fieldNomUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                    .addComponent(fieldEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fieldEmail1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fieldEmail2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelUsuariosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelUsuariosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonAplicarCambios1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(143, 143, 143))
                    .addGroup(panelUsuariosLayout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelNombreProducto11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelNombreProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblContra, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(114, Short.MAX_VALUE))))
        );
        panelUsuariosLayout.setVerticalGroup(
            panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombreProducto9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNombreProducto8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelUsuariosLayout.createSequentialGroup()
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelNombreProducto10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldNomUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelNombreProducto5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fieldEmail1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelNombreProducto6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelNombreProducto7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldEmail2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelUsuariosLayout.createSequentialGroup()
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelNombreProducto11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblContra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelNombreProducto4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addComponent(botonAplicarCambios1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        panelInferior.add(panelUsuarios, "card4");

        labelReloj.setFont(new java.awt.Font("Trebuchet MS", 1, 22)); // NOI18N
        labelReloj.setForeground(new java.awt.Color(255, 255, 255));
        labelReloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelReloj.setText("XX:XX:XX");

        jButton1.setText("actualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInformacionLayout = new javax.swing.GroupLayout(panelInformacion);
        panelInformacion.setLayout(panelInformacionLayout);
        panelInformacionLayout.setHorizontalGroup(
            panelInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInformacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelInferior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(panelSuperior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelInformacionLayout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelReloj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelInformacionLayout.setVerticalGroup(
            panelInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInformacionLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelInformacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelReloj)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
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
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonVerPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerPedidosActionPerformed
        // TODO add your handling code here:
        this.tablaInfo.setVisible(true);
        tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getPedidos()));
        tablaSeleccionada = "pedidos";
        CardLayout cl = (CardLayout) (panelInferior.getLayout());
        cl.show(panelInferior, "card2");
    }//GEN-LAST:event_botonVerPedidosActionPerformed

    private void botonVerProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerProductosActionPerformed
        // TODO add your handling code here:
        this.tablaInfo.setVisible(true);
        tablaSeleccionada = "productos";
        tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getProductos()));
        CardLayout cl = (CardLayout) (panelInferior.getLayout());
        cl.show(panelInferior, "card3");

    }//GEN-LAST:event_botonVerProductosActionPerformed

    private void botonVerUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonVerUsuariosActionPerformed
        this.tablaInfo.setVisible(true);
        tablaSeleccionada = "usuarios";
        tablaInfo.setModel(DbUtils.resultSetToTableModel(OperacionesBDD.getUsuarios()));
        CardLayout cl = (CardLayout) (panelInferior.getLayout());
        cl.show(panelInferior, "card4");

    }//GEN-LAST:event_botonVerUsuariosActionPerformed

    private void rSButtonMetro4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonMetro4ActionPerformed
        // TODO add your handling code here:
        fieldStock.setText(Integer.valueOf(fieldStock.getText()) - 10 + "");
    }//GEN-LAST:event_rSButtonMetro4ActionPerformed

    private void rSButtonMetro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonMetro2ActionPerformed
        fieldStock.setText(Integer.valueOf(fieldStock.getText()) + 1 + "");
    }//GEN-LAST:event_rSButtonMetro2ActionPerformed

    private void rSButtonMetro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonMetro1ActionPerformed
        // TODO add your handling code here:
        fieldStock.setText(Integer.valueOf(fieldStock.getText()) + 10 + "");
    }//GEN-LAST:event_rSButtonMetro1ActionPerformed

    private void rSButtonMetro3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonMetro3ActionPerformed
        // TODO add your handling code here:
        fieldStock.setText(Integer.valueOf(fieldStock.getText()) - 1 + "");
    }//GEN-LAST:event_rSButtonMetro3ActionPerformed

    private void rSMaterialButtonRound1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSMaterialButtonRound1ActionPerformed
        // TODO add your handling code here:
        RSFileChooser fileChooser = new RSFileChooser();

        FileFilter imageFilter = new FileNameExtensionFilter(
                "Image files", ImageIO.getReaderFileSuffixes());

        fileChooser.setFileFilter(imageFilter);
        fileChooser.showOpenDialog(this);
        File imagenSeleccionada = fileChooser.getSelectedFile();
        this.imagenProducto.setIcon(new ImageIcon(imagenSeleccionada.getAbsolutePath()));
    }//GEN-LAST:event_rSMaterialButtonRound1ActionPerformed

    private void botonAplicarCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAplicarCambiosActionPerformed
        // TODO add your handling code here:
        File imagen = null;
        OperacionesBDD.modificarProducto(producto.getId(), fieldNombre.getText(), fieldDesc.getText(), Float.valueOf(fieldPrecio.getText()), imagen, Integer.valueOf(fieldStock.getText()));
        this.actualizarTablas();
    }//GEN-LAST:event_botonAplicarCambiosActionPerformed

    private void botonAplicarCambios1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAplicarCambios1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonAplicarCambios1ActionPerformed

    private void botonAplicarCambios2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAplicarCambios2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonAplicarCambios2ActionPerformed

    private void botonAñadirProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonAñadirProductoActionPerformed
        // TODO add your handling code here:
        File imagen = null;
        OperacionesBDD.añadirProducto(fieldNombre.getText(), fieldDesc.getText(), Float.valueOf(fieldPrecio.getText()), imagen, Integer.valueOf(fieldStock.getText()));

        this.actualizarTablas();
    }//GEN-LAST:event_botonAñadirProductoActionPerformed

    private void botonEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarProductoActionPerformed
        // TODO add your handling code here:
        OperacionesBDD.eliminarProducto(producto.getId());
        this.actualizarTablas();
    }//GEN-LAST:event_botonEliminarProductoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.actualizarTablas();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Frame_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private rojerusan.RSButtonHover botonAplicarCambios1;
    private rojerusan.RSButtonHover botonAplicarCambios2;
    private rojerusan.RSButtonHover botonAñadirProducto;
    private rojerusan.RSButtonHover botonEliminarProducto;
    private rojeru_san.RSButtonRiple botonVerPedidos;
    private rojeru_san.RSButtonRiple botonVerProductos;
    private rojeru_san.RSButtonRiple botonVerUsuarios;
    private rojerusan.RSMetroTextFullPlaceHolder fieldDesc;
    private rojerusan.RSMetroTextFullPlaceHolder fieldEmail;
    private rojerusan.RSMetroTextFullPlaceHolder fieldEmail1;
    private rojerusan.RSMetroTextFullPlaceHolder fieldEmail2;
    private rojerusan.RSMetroTextFullPlaceHolder fieldNomUsuario;
    private rojerusan.RSMetroTextFullPlaceHolder fieldNombre;
    private rojerusan.RSMetroTextFullPlaceHolder fieldPrecio;
    private javax.swing.JTextField fieldStock;
    private rojerusan.RSLabelImage imagenProducto;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelNombreProducto;
    private javax.swing.JLabel labelNombreProducto1;
    private javax.swing.JLabel labelNombreProducto10;
    private javax.swing.JLabel labelNombreProducto11;
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
    private javax.swing.JLabel labelNombreProducto8;
    private javax.swing.JLabel labelNombreProducto9;
    private javax.swing.JLabel labelReloj;
    private rojeru_san.RSMPassView lblContra;
    private rojeru_san.RSMPassView lblDNI;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblPago;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblRepartidor;
    private javax.swing.JLabel lblUsuario;
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
    private rojerusan.RSButtonHover rSButtonHover3;
    private rojerusan.RSButtonHover rSButtonHover4;
    private rojerusan.RSButtonHover rSButtonHover5;
    private rojerusan.RSButtonMetro rSButtonMetro1;
    private rojerusan.RSButtonMetro rSButtonMetro2;
    private rojerusan.RSButtonMetro rSButtonMetro3;
    private rojerusan.RSButtonMetro rSButtonMetro4;
    private rojerusan.RSLabelImage rSLabelImage1;
    private rojerusan.RSMaterialButtonRound rSMaterialButtonRound1;
    private rojerusan.RSTableMetro tablaInfo;
    private rojerusan.RSTableMetro tablaLineas2;
    // End of variables declaration//GEN-END:variables
}
