/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.shawware.compadmin.scoring.EntrantResult;
import au.com.shawware.finska.entity.FinskaCompetition;
import au.com.shawware.finska.entity.Player;
import au.com.shawware.finska.service.DataService;

/**
 * Displays competition data.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Controller
@RequestMapping("/display")
@SuppressWarnings({ "nls", "boxing" })
public class DisplayController extends AbstractController
{
    /**
     * Constructs a new controller.
     * 
     * @param dataService the data service to use
     */
    public DisplayController(DataService dataService)
    {
        super(dataService);
    }

    /**
     * Displays the latest leader board and player data.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping({"", "/", "/table"})
    public String leaderBoard(Model model)
    {
        List<EntrantResult> leaderboard = mResultsService.getLeaderBoard();
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.leaderboard");
        processLeaderBoard(leaderboard, model);
        return TEMPLATE;
    }

    /**
     * Displays the leader board and player data for the given number of rounds.
     * 
     * @param number the number of rounds
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/table/{number}")
    public String leaderBoard(@PathVariable("number") int number, Model model)
    {
        List<EntrantResult> leaderboard = mResultsService.getLeaderBoard(number);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.leaderboard.round");
        model.addAttribute(VIEW_TITLE_ARG_ONE, number);
        processLeaderBoard(leaderboard, model);
        return TEMPLATE;
    }

    /**
     * Processes the given leader board and adds the relevant data to the model.
     *
     * @param leaderboard the leader board to process
     * @param model the model to add to 
     */
    private void processLeaderBoard(List<EntrantResult> leaderboard, Model model)
    {
        if (!leaderboard.isEmpty())
        {
            model.addAttribute("data", true);
            EntrantResult first = leaderboard.get(0);
            model.addAttribute("spec", first.getResultSpecification());
            model.addAttribute(PLAYERS, mResultsService.getPlayers());
            model.addAttribute(LEADERBOARD, leaderboard);
        }
        else
        {
            model.addAttribute("data", false);
        }
        model.addAttribute(FRAGMENT_FILE_KEY, LEADERBOARD);
        model.addAttribute(FRAGMENT_NAME_KEY, LEADERBOARD);
    }

    /**
     * Displays the list of rounds and running totals.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/rounds")
    public String rounds(Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        if (competition != null)
        {
            model.addAttribute("data", true);
            List<List<EntrantResult>> roundResults = mResultsService.getRoundResults();
            EntrantResult first = roundResults.get(0).get(0);
            model.addAttribute("spec", first.getResultSpecification());
            model.addAttribute(PLAYERS, mResultsService.getPlayers());
            model.addAttribute(COMPETITION, competition);
            model.addAttribute(ROUNDS, mResultsService.getRounds());
            model.addAttribute("results", roundResults);
        }
        else
        {
            model.addAttribute("data", false);
        }
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.rounds");
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, ROUNDS);
        return TEMPLATE;
    }

    /**
     * Displays the latest player data.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/players")
    public String players(Model model)
    {
        Map<Integer, Player> players = mResultsService.getPlayers();
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.players");
        model.addAttribute(PLAYERS, players);
        model.addAttribute(FRAGMENT_FILE_KEY, PLAYER);
        model.addAttribute(FRAGMENT_NAME_KEY, PLAYERS);
        return TEMPLATE;
    }

    /**
     * Displays the specified player.
     * 
     * @param id the player's ID
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/player/{id}")
    public String player(@PathVariable("id") int id, Model model)
    {
        Player player = mResultsService.getPlayer(id);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.player");
        model.addAttribute(VIEW_TITLE_ARG_ONE, id);
        model.addAttribute(PLAYER, player);
        model.addAttribute(FRAGMENT_FILE_KEY, PLAYER);
        model.addAttribute(FRAGMENT_NAME_KEY, PLAYER);
        return TEMPLATE;
    }

    /**
     * Displays the current competition.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/competition")
    public String competition(Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.competition");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUNDS, mResultsService.getRounds());
        model.addAttribute(PLAYERS, mResultsService.getPlayers());
        model.addAttribute(FRAGMENT_FILE_KEY, COMPETITION);
        model.addAttribute(FRAGMENT_NAME_KEY, COMPETITION);
        return TEMPLATE;
    }

    /**
     * Displays the specified round.
     * 
     * @param number the round's number
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/round/{number}")
    public String round(@PathVariable("number") int number, Model model)
    {
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.round");
        model.addAttribute(VIEW_TITLE_ARG_ONE, number);
        model.addAttribute(ROUND, mResultsService.getRound(number));
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, DISPLAY);
        return TEMPLATE;
    }
}
