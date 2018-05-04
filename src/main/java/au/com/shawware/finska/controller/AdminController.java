/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import au.com.shawware.finska.entity.FinskaCompetition;
import au.com.shawware.finska.service.DataService;
import au.com.shawware.util.persistence.PersistenceException;

/**
 * Administers the competition data.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Controller
@RequestMapping("/admin")
@SuppressWarnings({ "nls", "static-method" })
public class AdminController extends AbstractController
{
    /**
     * Constructs a new controller.
     * 
     * @param dataService the data service to use
     */
    public AdminController(DataService dataService)
    {
        super(dataService);
    }

    /**
     * Re-loads the data from disk. Useful when results are
     * updated outside of the system.
     * 
     * @return The model/view to redirect to.
     * 
     * @throws PersistenceException error reloading
     */
    @GetMapping("/reload")
    public ModelAndView reload()
        throws PersistenceException
    {
        mDataService.reload();
        return new ModelAndView("redirect:/");
    }

    /**
     * Displays the nominated round so that it can be edited.
     * 
     * @param number the round number
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/edit/round/{number}")
    public String editRound(@PathVariable("number") int number, Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        model.addAttribute(VIEW_NAME, competition.getKey() + ": Round " + number);
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, EDIT);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUND, mResultsService.getRound(number));
        model.addAttribute(PLAYERS, mResultsService.getPlayers());
        return TEMPLATE;
    }

    /**
     * Updated the nominated round with the submitted data.
     * 
     * @param number the round number
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @PostMapping("/update/round/{number}")
    public String updateRound(@PathVariable("number") int number,
        @RequestParam("round.number") int roundNumber,
        HttpServletRequest request, Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        Enumeration<String> names = request.getAttributeNames();
        Object value = request.getAttribute("round.number");
        model.addAttribute(VIEW_NAME, competition.getKey() + ": Round " + number);
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, DISPLAY);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUND, mResultsService.getRound(number));
        model.addAttribute(PLAYERS, mResultsService.getPlayers());
        return TEMPLATE;
    }
}
