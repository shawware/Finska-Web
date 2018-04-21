/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.shawware.compadmin.scoring.EntrantResult;
import au.com.shawware.finska.entity.Competition;
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
public class DisplayController
{
    private static Logger LOG = LoggerFactory.getLogger(DisplayController.class);

    /** The name of the template to use for all views. */
    private static final String TEMPLATE = "layout";
    /** The name of attribute that identifies the fragment file. */
    private static final String FRAGMENT_FILE_KEY = "entityType";
    /** The name of attribute that identifies the fragment. */
    private static final String FRAGMENT_NAME_KEY = "entityView";
    /** The name of attribute that identifies the view. */
    private static final String VIEW_NAME = "title";

    /** The injected data service. */
    @Autowired
    private final DataService mDataService;

    /**
     * Constructs a new controller.
     * 
     * @param dataService the data service to use
     */
    public DisplayController(DataService dataService)
    {
        mDataService = dataService;
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
        List<EntrantResult> leaderboard = mDataService.getResultsService().getLeaderBoard();
        if (!leaderboard.isEmpty())
        {
            model.addAttribute("data", true);
            EntrantResult first = leaderboard.get(0);
            model.addAttribute("spec", first.getResultSpecification());
            model.addAttribute("players", mDataService.getResultsService().getPlayers());
            model.addAttribute("leaderboard", leaderboard);
        }
        else
        {
            model.addAttribute("data", false);
        }
        model.addAttribute(VIEW_NAME, "Current Leaderboard");
        model.addAttribute(FRAGMENT_FILE_KEY, "leaderboard");
        model.addAttribute(FRAGMENT_NAME_KEY, "leaderboard");
        return TEMPLATE;
    }

    /**
     * Displays the list of matches and running totals.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping({"/matches"})
    public String matches(Model model)
    {
        Competition competition = mDataService.getResultsService().getCompetition();
        if (competition != null)
        {
            model.addAttribute("data", true);
            List<List<EntrantResult>> matchesResults = mDataService.getResultsService().getRoundResults();
            EntrantResult first = matchesResults.get(0).get(0);
            model.addAttribute("spec", first.getResultSpecification());
            model.addAttribute("players", mDataService.getResultsService().getPlayers());
            model.addAttribute("competition", competition);
            model.addAttribute("matches", mDataService.getResultsService().getMatches());
            model.addAttribute("results", matchesResults);
        }
        else
        {
            model.addAttribute("data", false);
        }
        model.addAttribute(VIEW_NAME, "Match Results");
        model.addAttribute(FRAGMENT_FILE_KEY, "match");
        model.addAttribute(FRAGMENT_NAME_KEY, "matches");
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
        Map<Integer, Player> players = mDataService.getResultsService().getPlayers();
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
        Player player = mDataService.getResultsService().getPlayer(id);
        model.addAttribute(VIEW_NAME, "Player " + id);
        model.addAttribute("player", player);
        model.addAttribute(FRAGMENT_FILE_KEY, "player");
        model.addAttribute(FRAGMENT_NAME_KEY, "player");
        return TEMPLATE;
    }
}
