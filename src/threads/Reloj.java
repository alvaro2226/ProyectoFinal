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

import static java.lang.Thread.sleep;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;

/**
 *
 * @author Álvaro Morcillo Barbero
 */
public class Reloj extends Thread {

    JLabel reloj;
    int hours = 0, minutes = 0, seconds = 0;
    String timeString = "";

    public Reloj(JLabel label) {
        reloj = label;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //System.out.println("reloj");
                Calendar cal = Calendar.getInstance();
                hours = cal.get(Calendar.HOUR_OF_DAY);
                minutes = cal.get(Calendar.MINUTE);
                seconds = cal.get(Calendar.SECOND);

                SimpleDateFormat formatter = new SimpleDateFormat("H:m");
                Date date = cal.getTime();
                timeString = formatter.format(date);

                printTime();

                sleep(1000);  // interval given in milliseconds  
            }
        } catch (Exception e) {
        }
    }

    private void printTime() {
        reloj.setText(timeString);
    }

}
