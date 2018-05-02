/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests to the root end point.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Controller
@RequestMapping("/")
@SuppressWarnings({ "nls", "static-method" })
public class HomeController
{
    /**
     * Redirect from the root end point to the root of the display.
     * 
     * @return The model and view to redirect to.
     */
    @GetMapping({"", "/"})
    public ModelAndView slash()
    {
        return new ModelAndView("redirect:/display");
    }
}
