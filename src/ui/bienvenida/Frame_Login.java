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

import java.awt.event.KeyEvent;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import log.MyLogger;
import pojos.AdminUser;
import pojos.Database;
import pojos.Empresa;
import threads.ProgressDialogLogin;
import ui.principal.Frame_Principal;
import util.OperacionesBDD;
import util.PropertiesUtil;
import util.Util;

/**
 *
 * @author �lvaro Morcillo Barbero
 */
public class Frame_Login extends javax.swing.JFrame {

    private final static Logger logger = Logger.getLogger(MyLogger.class.getName());
    private Database database = null;

    /**
     * Creates new form FrameLogin
     */
    public Frame_Login(Database database) {
        initComponents();
        this.setIconImage(Util.getImagenIcono());
        this.setLocationRelativeTo(null);
        this.lblConsola.setVisible(false);
        this.rSProgressCircleAnimated1.setVisible(false);

        this.lblUsuario.setNextFocusableComponent(lblContra);
        this.rSButtonHover1.setMnemonic(KeyEvent.VK_ACCEPT);
        this.getRootPane().setDefaultButton(rSButtonHover1);
        
        this.database = database;
                
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
        panelSuperior = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        rSPanelImage2 = new rojerusan.RSPanelImage();
        rSLabelImage1 = new rojerusan.RSLabelImage();
        lblUsuario = new rojeru_san.RSMTextFull();
        rSLabelImage2 = new rojerusan.RSLabelImage();
        lblContra = new rojeru_san.RSMPassView();
        rSButtonHover1 = new rojerusan.RSButtonHover();
        lblConsola = new javax.swing.JLabel();
        rSProgressCircleAnimated1 = new rojerusan.RSProgressCircleAnimated();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backgroundPanel.setBackground(new java.awt.Color(255, 255, 255));
        backgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelSuperior.setBackground(new java.awt.Color(79, 134, 198));

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Segoe UI Light", 0, 36)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Inicio de sesi�n");

        rSPanelImage2.setImagen(new javax.swing.ImageIcon(getClass().getResource("/ui/images/logo/logo2.png"))); // NOI18N

        javax.swing.GroupLayout rSPanelImage2Layout = new javax.swing.GroupLayout(rSPanelImage2);
        rSPanelImage2.setLayout(rSPanelImage2Layout);
        rSPanelImage2Layout.setHorizontalGroup(
            rSPanelImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        rSPanelImage2Layout.setVerticalGroup(
            rSPanelImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 76, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelSuperiorLayout = new javax.swing.GroupLayout(panelSuperior);
        panelSuperior.setLayout(panelSuperiorLayout);
        panelSuperiorLayout.setHorizontalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSuperiorLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(rSPanelImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel19)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        panelSuperiorLayout.setVerticalGroup(
            panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSuperiorLayout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(panelSuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(rSPanelImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        backgroundPanel.add(panelSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 100));

        rSLabelImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/user_40px.png"))); // NOI18N
        backgroundPanel.add(rSLabelImage1, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 166, 40, 40));

        lblUsuario.setPlaceholder("Nombre de usuario");
        lblUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblUsuarioActionPerformed(evt);
            }
        });
        backgroundPanel.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 156, 230, 50));

        rSLabelImage2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ui/images/lock_40px.png"))); // NOI18N
        rSLabelImage2.setPreferredSize(new java.awt.Dimension(40, 40));
        backgroundPanel.add(rSLabelImage2, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 263, -1, -1));

        lblContra.setPlaceholder("Contrase�a");
        backgroundPanel.add(lblContra, new org.netbeans.lib.awtextra.AbsoluteConstraints(106, 263, 230, -1));

        rSButtonHover1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover1.setText("Iniciar sesi�n");
        rSButtonHover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover1ActionPerformed(evt);
            }
        });
        backgroundPanel.add(rSButtonHover1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 420, -1, -1));

        lblConsola.setBackground(new java.awt.Color(255, 255, 255));
        lblConsola.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lblConsola.setForeground(new java.awt.Color(255, 0, 0));
        lblConsola.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConsola.setText("Compruebe de nuevo el usuario y la contrase�a");
        backgroundPanel.add(lblConsola, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 364, -1));

        rSProgressCircleAnimated1.setString("");
        rSProgressCircleAnimated1.setVelocidad(20);
        backgroundPanel.add(rSProgressCircleAnimated1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 420, 50, 50));

        getContentPane().add(backgroundPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 490));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static String usuarioLogeado;
    public static String usuarioLogeadoContra;
    public static int usuarioLogeadoRol;

    private void rSButtonHover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover1ActionPerformed

        //this.rSProgressCircleAnimated1.setVisible(true);
        new ProgressDialogLogin(rSProgressCircleAnimated1, lblConsola).start();
        boolean inicioCorrecto = false;
        try {
            inicioCorrecto = OperacionesBDD.iniciarSesion(lblUsuario.getText(), this.lblContra.getText());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Frame_Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (inicioCorrecto) {
            //this.lblConsola.setVisible(false);
            logger.info("Se ha iniciado sesi�n");
            dispose();
            try {
                sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Frame_Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            usuarioLogeado = lblUsuario.getText();
            usuarioLogeadoContra = lblContra.getText();

            if(database != null){
                try {
                PropertiesUtil.a�adirBDD(database.getURL(),
                        database.getUser(),
                        database.getPassword(),
                        "false");
            } catch (IOException ex) {
                Logger.getLogger(Dialog_ConfirmarInstalacion.class.getName()).log(Level.SEVERE, null, ex);
            }

            }
            
            new Frame_Principal().setVisible(true);

        } else {
            //this.lblConsola.setVisible(true);
            logger.info("No se ha podido iniciar sesi�n");
        }
        //this.rSProgressCircleAnimated1.setVisible(false);
    }//GEN-LAST:event_rSButtonHover1ActionPerformed

    private void lblUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblUsuarioActionPerformed

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
            java.util.logging.Logger.getLogger(Frame_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame_Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame_Login(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel lblConsola;
    private rojeru_san.RSMPassView lblContra;
    private rojeru_san.RSMTextFull lblUsuario;
    private javax.swing.JPanel panelSuperior;
    private rojerusan.RSButtonHover rSButtonHover1;
    private rojerusan.RSLabelImage rSLabelImage1;
    private rojerusan.RSLabelImage rSLabelImage2;
    private rojerusan.RSPanelImage rSPanelImage2;
    private rojerusan.RSProgressCircleAnimated rSProgressCircleAnimated1;
    // End of variables declaration//GEN-END:variables
}
