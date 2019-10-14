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
package threads;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import rojerusan.RSProgressCircleAnimated;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class ProgressDialogLogin extends Thread{
    
    RSProgressCircleAnimated progressCircle;
    JLabel lblConsola;
    
    public ProgressDialogLogin (RSProgressCircleAnimated progressCircle, JLabel lblConsola){
        this.progressCircle = progressCircle;
        this.lblConsola = lblConsola;
        this.progressCircle.setVelocidad(20);
    }

    @Override
    public void run() {
        
        this.lblConsola.setVisible(false);
        this.progressCircle.setVisible(true);
        try {
           this.sleep(2000);//
        } catch (InterruptedException ex) {
            Logger.getLogger(ProgressDialogLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       this.progressCircle.setVisible(false);
       this.lblConsola.setVisible(true);
       
    }
    
    

}
