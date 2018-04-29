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
        model.addAttribute(VIEW_NAME, "Current Leaderboard");
        processLeaderBoard(leaderboard, model);
        return TEMPLATE;
    }

    /**
     * Displays the leader board and player data for the given number of rounds.
     * 
     * @param round the number of rounds
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/table/{round}")
    public String leaderBoard(@PathVariable("round") int round, Model model)
    {
        List<EntrantResult> leaderboard = mResultsService.getLeaderBoard(round);
        model.addAttribute(VIEW_NAME, "Leaderboard After Round " + round);
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
            model.addAttribute("players", mResultsService.getPlayers());
            model.addAttribute("leaderboard", leaderboard);
        }
        else
        {
            model.addAttribute("data", false);
        }
        model.addAttribute(FRAGMENT_FILE_KEY, "leaderboard");
        model.addAttribute(FRAGMENT_NAME_KEY, "leaderboard");
    }

    /**
     * Displays the list of rounds and running totals.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping({"/rounds"})
    public String rounds(Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        if (competition != null)
        {
            model.addAttribute("data", true);
            List<List<EntrantResult>> roundResults = mResultsService.getRoundResults();
            EntrantResult first = roundResults.get(0).get(0);
            model.addAttribute("spec", first.getResultSpecification());
            model.addAttribute("players", mResultsService.getPlayers());
            model.addAttribute("competition", competition);
            model.addAttribute("rounds", mResultsService.getRounds());
            model.addAttribute("results", roundResults);
        }
        else
        {
            model.addAttribute("data", false);
        }
        model.addAttribute(VIEW_NAME, "Round Summaries");
        model.addAttribute(FRAGMENT_FILE_KEY, "round");
        model.addAttribute(FRAGMENT_NAME_KEY, "rounds");
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
        model.addAttribute(VIEW_NAME, "Players");
        model.addAttribute("players", players);
        model.addAttribute(FRAGMENT_FILE_KEY, "player");
        model.addAttribute(FRAGMENT_NAME_KEY, "players");
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
        model.addAttribute(VIEW_NAME, "Player " + id);
        model.addAttribute("player", player);
        model.addAttribute(FRAGMENT_FILE_KEY, "player");
        model.addAttribute(FRAGMENT_NAME_KEY, "player");
        return TEMPLATE;
    }

    /**
     * Displays the current competition.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping({"/competition"})
    public String competition(Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition();
        model.addAttribute(VIEW_NAME, "Current Competition: " + competition.getKey());
        model.addAttribute("competition", competition);
        model.addAttribute("rounds", mResultsService.getRounds());
        model.addAttribute("players", mResultsService.getPlayers());
        model.addAttribute(FRAGMENT_FILE_KEY, "competition");
        model.addAttribute(FRAGMENT_NAME_KEY, "competition");
        return TEMPLATE;
    }
}