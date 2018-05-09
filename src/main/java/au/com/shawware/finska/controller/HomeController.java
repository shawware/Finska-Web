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

import au.com.shawware.finska.service.DataService;

/**
 * Handles requests to the root end point.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Controller
@RequestMapping("/")
@SuppressWarnings({ "nls" })
public class HomeController extends AbstractController
{
    /**
     * Constructs a new controller.
     * 
     * @param dataService the data service to use
     */
    public HomeController(DataService dataService)
    {
        super(dataService);
    }

    /**
     * Redirect from the root end point to the root of the display.
     * 
     * @return The model and view to redirect to.
     */
    @GetMapping({"", "/"})
    public ModelAndView slash()
    {
        return redirectTo("/display");
    }
}
