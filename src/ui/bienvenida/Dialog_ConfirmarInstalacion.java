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
package ui.bienvenida;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import pojos.*;
import util.OperacionesBDD;
import util.PropertiesUtil;

/**
 *
 * @author alvaro2226
 */
public class Dialog_ConfirmarInstalacion extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    private Empresa empresa;
    private Database database;
    private AdminUser adminUser;
    private final static Logger logger = Logger.getLogger(Frame_Bienvenida.class.getName());
    private Properties properties = PropertiesUtil.getProperties();
    private Frame_Bienvenida frameBienvenida;

    /**
     * Creates new form NewOkCancelDialog
     *
     * @param parent
     * @param modal
     * @param empresa
     * @param database
     * @param adminUser
     */
    public Dialog_ConfirmarInstalacion(java.awt.Frame parent, boolean modal,
            Empresa empresa, Database database, AdminUser adminUser) {
        super(parent, modal);

        this.adminUser = adminUser;
        this.database = database;
        this.empresa = empresa;
        this.frameBienvenida = (Frame_Bienvenida) parent;

        initComponents();
        setLocationRelativeTo(parent);

        cargarDatos();
        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblURL = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblContra = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblContraAdmin = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        ll21 = new javax.swing.JLabel();
        lblNombreEmpresa = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblFM = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblCIF = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblPaypal = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblProvincia = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblCalle = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        lblLocalidad = new javax.swing.JLabel();
        lblCP = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblPais = new javax.swing.JLabel();
        panelSuperior1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        rSPanelImage3 = new rojerusan.RSPanelImage();
        botonCancelar = new rojerusan.RSButtonRound();
        botonConfirmar = new rojerusan.RSButtonRound();

        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblUsuario.setForeground(new java.awt.Color(102, 102, 102));
        lblUsuario.setText("�Son correctos estos datos?");
        jPanel2.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        jLabel4.setText("URL");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel5.setText("Usuario");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 60, -1));

        lblURL.setForeground(new java.awt.Color(102, 102, 102));
        lblURL.setText("�Son correctos estos datos?");
        jPanel2.add(lblURL, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        jLabel3.setText("Contrase�a");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        lblContra.setForeground(new java.awt.Color(102, 102, 102));
        lblContra.setText("�Son correctos estos datos?");
        jPanel2.add(lblContra, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        tabbedPane.addTab("Base de datos", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setText("Nombre:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        lblNombre.setForeground(new java.awt.Color(102, 102, 102));
        lblNombre.setText("�Son correctos estos datos?");
        jPanel3.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        jLabel11.setText("Contrase�a");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        lblContraAdmin.setForeground(new java.awt.Color(102, 102, 102));
        lblContraAdmin.setText("�Son correctos estos datos?");
        jPanel3.add(lblContraAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        tabbedPane.addTab("Usuario Admin", jPanel3);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ll21.setText("Nombre:");
        jPanel5.add(ll21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        lblNombreEmpresa.setForeground(new java.awt.Color(102, 102, 102));
        lblNombreEmpresa.setText("�Son correctos estos datos?");
        jPanel5.add(lblNombreEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        jLabel12.setText("Forma Jur�dica");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 160, -1));

        lblFM.setForeground(new java.awt.Color(102, 102, 102));
        lblFM.setText("�Son correctos estos datos?");
        jPanel5.add(lblFM, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        jLabel13.setText("CIF");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, -1));

        lblCIF.setForeground(new java.awt.Color(102, 102, 102));
        lblCIF.setText("�Son correctos estos datos?");
        jPanel5.add(lblCIF, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        jLabel16.setText("Tel�fono");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 90, -1));

        lblTelefono.setForeground(new java.awt.Color(102, 102, 102));
        lblTelefono.setText("�Son correctos estos datos?");
        jPanel5.add(lblTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, -1, -1));

        jLabel18.setText("Correo eletr�nico");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 160, -1));

        lblEmail.setForeground(new java.awt.Color(102, 102, 102));
        lblEmail.setText("�Son correctos estos datos?");
        jPanel5.add(lblEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, -1, -1));

        jLabel19.setText("Correo paypal");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 170, -1));

        lblPaypal.setForeground(new java.awt.Color(102, 102, 102));
        lblPaypal.setText("�Son correctos estos datos?");
        jPanel5.add(lblPaypal, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 170, -1, -1));

        tabbedPane.addTab("Datos de tu empresa", jPanel5);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblProvincia.setForeground(new java.awt.Color(102, 102, 102));
        lblProvincia.setText("�Son correctos estos datos?");
        jPanel4.add(lblProvincia, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        jLabel14.setText("Calle");
        jPanel4.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel15.setText("Provincia");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 90, -1));

        lblCalle.setForeground(new java.awt.Color(102, 102, 102));
        lblCalle.setText("�Son correctos estos datos?");
        jPanel4.add(lblCalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        jLabel17.setText("Localidad");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 90, -1));

        lblLocalidad.setForeground(new java.awt.Color(102, 102, 102));
        lblLocalidad.setText("�Son correctos estos datos?");
        jPanel4.add(lblLocalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        lblCP.setForeground(new java.awt.Color(102, 102, 102));
        lblCP.setText("�Son correctos estos datos?");
        jPanel4.add(lblCP, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, -1, -1));

        jLabel20.setText("C�digo Postal");
        jPanel4.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 100, -1));

        jLabel24.setText("Pa�s");
        jPanel4.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 90, -1));

        lblPais.setForeground(new java.awt.Color(102, 102, 102));
        lblPais.setText("�Son correctos estos datos?");
        jPanel4.add(lblPais, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, -1, -1));

        tabbedPane.addTab("Direcci�n de tu empresa", jPanel4);

        panelSuperior1.setBackground(new java.awt.Color(79, 134, 198));

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Segoe UI Light", 0, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Confirma los datos");

        rSPanelImage3.setImagen(new javax.swing.ImageIcon(getClass().getResource("/ui/images/logo/logo_transparent.png"))); // NOI18N

        javax.swing.GroupLayout rSPanelImage3Layout = new javax.swing.GroupLayout(rSPanelImage3);
        rSPanelImage3.setLayout(rSPanelImage3Layout);
        rSPanelImage3Layout.setHorizontalGroup(
            rSPanelImage3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        rSPanelImage3Layout.setVerticalGroup(
            rSPanelImage3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelSuperior1Layout = new javax.swing.GroupLayout(panelSuperior1);
        panelSuperior1.setLayout(panelSuperior1Layout);
        panelSuperior1Layout.setHorizontalGroup(
            panelSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSuperior1Layout.createSequentialGroup()
                .addComponent(rSPanelImage3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jLabel21)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSuperior1Layout.setVerticalGroup(
            panelSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSuperior1Layout.createSequentialGroup()
                .addGroup(panelSuperior1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(rSPanelImage3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        botonCancelar.setBackground(new java.awt.Color(79, 134, 198));
        botonCancelar.setText("Cancelar");
        botonCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        botonConfirmar.setBackground(new java.awt.Color(79, 134, 198));
        botonConfirmar.setText("Confirmar datos");
        botonConfirmar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        botonConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonConfirmarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(botonConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(panelSuperior1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelSuperior1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void botonConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonConfirmarActionPerformed

        PropertiesUtil.init();
        
        try {
            PropertiesUtil.a�adirBDD(database.getURL(),
                    database.getUser(),
                    database.getPassword(),
                    "true");
        } catch (IOException ex) {
            Logger.getLogger(Dialog_ConfirmarInstalacion.class.getName()).log(Level.SEVERE, null, ex);
        }

        persistirDatos();
        //Si los datos se guardan correctamente, se debe cambiar el valor a la
        //variable "app.firstTime" a "false" indicando de esta manera que ya
        //no es la primera vez que ejecutamos la aplicaci�n

        try {
            PropertiesUtil.a�adirBDD(database.getURL(),
                    database.getUser(),
                    database.getPassword(),
                    "false");
        } catch (IOException ex) {
            Logger.getLogger(Dialog_ConfirmarInstalacion.class.getName()).log(Level.SEVERE, null, ex);
        }

        frameBienvenida.dispose();
        new Frame_Login(database).setVisible(true);

        doClose(RET_OK);

    }//GEN-LAST:event_botonConfirmarActionPerformed

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        doClose(RET_CANCEL);
        logger.log(Level.INFO, "No se han confirmado los datos");
    }//GEN-LAST:event_botonCancelarActionPerformed

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    private void cargarDatos() {

        //----------------Datos empresa--------------------
        this.lblCP.setText(empresa.getCodigoPostal());
        this.lblPais.setText(empresa.getPais());
        this.lblProvincia.setText(empresa.getProvincia());
        this.lblCalle.setText(empresa.getCalle());
        this.lblLocalidad.setText(empresa.getLocalidad());

        this.lblTelefono.setText(empresa.getTelefono());
        this.lblNombreEmpresa.setText(empresa.getNombre());
        this.lblCIF.setText(empresa.getCIF());
        this.lblFM.setText(empresa.getFormaJuridica());
        this.lblEmail.setText(empresa.getEmail());
        this.lblPaypal.setText(empresa.getPaypal());

        //---------------Datos Admin--------------------
        this.lblContraAdmin.setText(adminUser.getPassword());
        this.lblNombre.setText(adminUser.getUsername());

        //--------------Datos base de datos----------------------
        this.lblURL.setText(database.getURL());
        this.lblContra.setText(database.getUser());
        this.lblUsuario.setText(database.getPassword());
    }

    private void persistirDatos() {

        try {
            OperacionesBDD.iniciarConexion();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Dialog_ConfirmarInstalacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // CAMBIAR EN EL FICHERO DE CONFIGURACION LA BDD SI HICIESE FALTA
        OperacionesBDD.crearBDD();
        OperacionesBDD.a�adirEmpresa(
                empresa.getNombre(),
                empresa.getFormaJuridica(),
                empresa.getCIF(),
                empresa.getEmail(),
                empresa.getPaypal(),
                empresa.getCalle(),
                empresa.getLocalidad(),
                empresa.getProvincia(),
                empresa.getCodigoPostal(),
                empresa.getPais(),
                empresa.getTelefono());
        OperacionesBDD.a�adirAdmin(adminUser.getUsername(), adminUser.getPassword());
        
        try {
            OperacionesBDD.cerrarConexion();
        } catch (SQLException ex) {
            Logger.getLogger(Dialog_ConfirmarInstalacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSButtonRound botonCancelar;
    private rojerusan.RSButtonRound botonConfirmar;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblCIF;
    private javax.swing.JLabel lblCP;
    private javax.swing.JLabel lblCalle;
    private javax.swing.JLabel lblContra;
    private javax.swing.JLabel lblContraAdmin;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblFM;
    private javax.swing.JLabel lblLocalidad;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombreEmpresa;
    private javax.swing.JLabel lblPais;
    private javax.swing.JLabel lblPaypal;
    private javax.swing.JLabel lblProvincia;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JLabel lblURL;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel ll21;
    private javax.swing.JPanel panelSuperior1;
    private rojerusan.RSPanelImage rSPanelImage3;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;

}
