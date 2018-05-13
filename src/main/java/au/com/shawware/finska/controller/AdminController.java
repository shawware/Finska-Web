/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
@SuppressWarnings({ "nls", "boxing" })
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
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.round.create");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey() );
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
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error creating round
     */
    @PostMapping(value="/create/round", params="action=create")
    public ModelAndView createRound(
        @RequestParam("competition") int competitionID,
        @RequestParam("round-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate roundDate,
        @RequestParam("players") int[] playerIDs)
        throws PersistenceException
    {
        FinskaRound round = mRoundService.createRound(competitionID, roundDate, playerIDs);
        return redirectTo("/admin/edit/round/" + round.getKey());
    }

    /**
     * Cancels the creation of a new round.
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/create/round", params="action=cancel")
    public ModelAndView cancelCreateRound()
    {
        return redirectTo(HOME);
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
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.round.edit");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(VIEW_TITLE_ARG_TWO, number);
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, EDIT);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUND, round);
        model.addAttribute("checked", ids);
        model.addAttribute(MATCHES, round.getMatches());
        model.addAttribute(PLAYERS, mResultsService.getPlayers());
        return TEMPLATE;
    }

    /**
     * Updates the nominated round with the submitted data.
     * 
     * @param number the round number
     * @param competitionID the competition ID
     * @param players the players selected for this round (updated)
     * @param roundDate the updated round date
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error updating round
     */
    @PostMapping(value="/edit/round/{number}", params="action=update")
    public ModelAndView updateRound(@PathVariable("number") int number,
        @RequestParam("competition") int competitionID,
        @RequestParam("players") int[] players,
        @RequestParam("round-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate roundDate)
        throws PersistenceException
    {
        mRoundService.updateRound(competitionID, number, roundDate, players);
        return redirectTo("/display/rounds");
    }

    /**
     * Commences the creation of a new match for the nominated round.
     * 
     * @param number the round number
     * @param competitionID the competition ID
     * @param model the model to add data to
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error accessing data
     */
    @PostMapping(value="/edit/round/{number}", params="action=create")
    public String createRound(@PathVariable("number") int number,
        @RequestParam("competition") int competitionID, Model model)
        throws PersistenceException
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        FinskaRound round = mResultsService.getRound(number);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.match.create");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(VIEW_TITLE_ARG_TWO, number);
        model.addAttribute(FRAGMENT_FILE_KEY, MATCH);
        model.addAttribute(FRAGMENT_NAME_KEY, CREATE);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUND, round);
        return TEMPLATE;
    }

    /**
     * Cancels the editing of a round.
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/edit/round/{number}", params="action=cancel")
    public ModelAndView cancelEditRound()
    {
        return redirectTo(HOME);
    }

    /**
     * Creates a new match.
     * 
     * @param competitionID the competition ID
     * @param number the round number
     * @param winnerIds the IDs of the winning players
     * @param fastWin whether these players had a fast win
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error creating round
     */
    @PostMapping(value="/create/match", params="action=create")
    public ModelAndView createMatch(
        @RequestParam("competition") int competitionID,
        @RequestParam("round") int number,
        @RequestParam(name="winners", required=false) int[] winnerIds,
        @RequestParam(name="fast-winners", defaultValue="false") boolean fastWin)
        throws PersistenceException
    {
        mMatchService.createMatch(competitionID, number, winnerIds, fastWin);
        return redirectTo("/admin/edit/round/" + number);
    }

    /**
     * Cancels the creation of a new match (in the given competition and round).
     * 
     * @param competitionID the competition ID
     * @param number the round number
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/create/match", params="action=cancel")
    public ModelAndView cancelCreateMatch(
            @RequestParam("competition") int competitionID,
            @RequestParam("round") int number)
    {
        return redirectTo("/admin/edit/round/" + number);
    }
}
