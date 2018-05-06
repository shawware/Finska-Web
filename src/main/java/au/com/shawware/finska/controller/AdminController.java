/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import java.time.LocalDate;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import au.com.shawware.finska.entity.FinskaCompetition;
import au.com.shawware.finska.entity.FinskaRound;
import au.com.shawware.finska.service.DataService;
import au.com.shawware.util.persistence.PersistenceException;

/**
 * Administers the competition data.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Controller
@RequestMapping("/admin")
@SuppressWarnings({ "nls" })
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
     * Displays a template for creating a new round.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/create/round")
    public String newRound(Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        model.addAttribute(VIEW_NAME, competition.getKey() + ": Create New Round");
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, CREATE);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(PLAYERS, mResultsService.getPlayers());
        model.addAttribute("date", LocalDate.now());
        return TEMPLATE;
    }

    /**
     * Creates a new round.
     * 
     * @param competitionID the competition ID
     * @param roundDate the new round's date
     * @param playerIDs the IDs of the players participating in this round
     * @param model the model to add data to
     * 
     * @return The template name.
     * 
     * @throws PersistenceException error creating round
     */
    @PostMapping("/create/round")
    public String createRound(
        @RequestParam("competition") int competitionID,
        @RequestParam("round-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate roundDate,
        @RequestParam("players") int[] playerIDs, Model model)
        throws PersistenceException
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        FinskaRound round = mCreateService.createRound(competitionID, roundDate, playerIDs);
        model.addAttribute(VIEW_NAME, competition.getKey() + ": Round " + round.getKey());
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, DISPLAY);
        model.addAttribute(ROUND, round);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(PLAYERS, mResultsService.getPlayers());
        return TEMPLATE;
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
        FinskaRound round = mResultsService.getRound(number);
        Set<Integer> ids = round.getPlayerIds();
        model.addAttribute(VIEW_NAME, competition.getKey() + ": Round " + number);
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, EDIT);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUND, round);
        model.addAttribute("checked", ids);
        model.addAttribute(PLAYERS, mResultsService.getPlayers());
        return TEMPLATE;
    }

    /**
     * Updated the nominated round with the submitted data.
     * 
     * @param number the round number
     * @param players the players selected for this round
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @PostMapping("/update/round/{number}")
    public String updateRound(@PathVariable("number") int number,
        @RequestParam("players") int[] players,
        @RequestParam("round-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate roundDate,
        HttpServletRequest request, Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        FinskaRound round = mResultsService.getRound(number);
        model.addAttribute(VIEW_NAME, competition.getKey() + ": Round " + number);
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, DISPLAY);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUND, round);
        model.addAttribute(PLAYERS, mResultsService.getPlayers());
        return TEMPLATE;
    }
}
