/*
 * Copyright (C) 2019 alvaro2226
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

import javax.swing.JPasswordField;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alvaro2226
 */
public class UtilTest {

    public UtilTest() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validarEmail method, of class Util.
     */
    @Test
    public void testValidarEmail() {
        System.out.println("validarEmail");
        String email = "";
        boolean expResult = false;
        boolean result = Util.validarEmail(email);
        assertEquals(expResult, result);
    }

    /**
     * Test of comprobarContraseñas method, of class Util.
     */
    @Test
    public void testComprobarContraseñas() {
        System.out.println("comprobarContrase\u00f1as");

        JPasswordField password1 = new JPasswordField();
        JPasswordField password2 = new JPasswordField();

        password1.setText("claro");
        password1.setText("que no");

        boolean expResult = false;
        boolean result = Util.comprobarContraseñas(password1, password2);
        assertEquals(expResult, result);
    }

}
