/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The Finska web application.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SpringBootApplication(scanBasePackages="au.com.shawware.finska")
public class FinskaWebApp
{
    /**
     * The starting point.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        SpringApplication.run(FinskaWebApp.class, args);
    }
}
