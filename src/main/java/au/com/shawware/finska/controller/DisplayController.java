/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import au.com.shawware.compadmin.scoring.EntrantResult;
import au.com.shawware.finska.entity.FinskaCompetition;
import au.com.shawware.finska.entity.FinskaRound;
import au.com.shawware.finska.entity.Player;
import au.com.shawware.finska.service.DataService;
import au.com.shawware.util.persistence.PersistenceException;

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
     * Displays the latest leader board.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     * 
     * @throws PersistenceException error loading data
     */
    @GetMapping({"", "/", "/table"})
    public String leaderBoard(Model model)
        throws PersistenceException
    {
        List<EntrantResult> leaderboard = mResultsService.getLeaderBoard();
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.leaderboard");
        processLeaderBoard(leaderboard, model);
        return TEMPLATE;
    }

    /**
     * Displays the leader board after the given number of rounds of the specified competition.
     * 
     * @param id the competition ID
     * @param roundNumber the number of rounds
     * @param model the model to add data to
     * 
     * @return The template name.
     * 
     * @throws PersistenceException error loading data
     */
    @GetMapping("/table/{id}/{roundNumber}")
    public String leaderBoard(@PathVariable("id") int id,
                              @PathVariable("roundNumber") int roundNumber,
                              Model model)
        throws PersistenceException
    {
        List<EntrantResult> leaderboard = mResultsService.getLeaderBoard(id, roundNumber);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.leaderboard.round");
        model.addAttribute(VIEW_TITLE_ARG_ONE, roundNumber);
        processLeaderBoard(leaderboard, model);
        return TEMPLATE;
    }

    /**
     * Processes the given leader board and adds the relevant data to the model.
     *
     * @param leaderboard the leader board to process
     * @param model the model to add to
     * 
     * @throws PersistenceException error loading data
     */
    private void processLeaderBoard(List<EntrantResult> leaderboard, Model model)
        throws PersistenceException
    {
        if (!leaderboard.isEmpty())
        {
            model.addAttribute("data", true);
            EntrantResult first = leaderboard.get(0);
            model.addAttribute("spec", first.getResultSpecification());
            model.addAttribute(PLAYERS, mPlayerService.getPlayers());
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
        FinskaCompetition competition = mResultsService.getCurrentCompetition();
        if (competition != null)
        {
            model.addAttribute("data", true);
            List<List<EntrantResult>> roundResults = mResultsService.getRoundResults();
            EntrantResult first = roundResults.get(0).get(0);
            model.addAttribute("spec", first.getResultSpecification());
            model.addAttribute(PLAYERS, competition.getEntrantMap());
            model.addAttribute(COMPETITION, competition);
            model.addAttribute(ROUNDS, competition.getRounds());
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
     * Displays the players' rank history over all rounds.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/history/rank")
    public String rankHistory(Model model)
        throws PersistenceException
    {
        return history(model, mResultsService.getRankHistory(), "sw.finska.page.title.history.rank");
    }

    /**
     * Displays the players' result history over all rounds.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/history/result")
    public String resultHistory(Model model)
        throws PersistenceException
    {
        return history(model, mResultsService.getResultHistory(), "sw.finska.page.title.history.result");
    }

    /**
     * Common processing for history end points.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    private String history(Model model, Map<Integer, Number[]> history, String title)
        throws PersistenceException
    {
        FinskaCompetition competition = mResultsService.getCurrentCompetition();
        if (competition != null)
        {
            model.addAttribute("data", true);
            Map<Integer, Player> players = mPlayerService.getPlayers();
            model.addAttribute(PLAYERS, players);
            model.addAttribute(COMPETITION, competition);
            model.addAttribute(HISTORY, history);
        }
        else
        {
            model.addAttribute("data", false);
        }
        model.addAttribute(VIEW_TITLE, title);
        model.addAttribute(FRAGMENT_FILE_KEY, LEADERBOARD);
        model.addAttribute(FRAGMENT_NAME_KEY, HISTORY);
        return TEMPLATE;
    }

    /**
     * Displays the latest player data.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     * 
     * @throws PersistenceException error loading players
     */
    @GetMapping("/players")
    public String players(Model model)
        throws PersistenceException
    {
        Map<Integer, Player> players = mPlayerService.getPlayers();
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
     * 
     * @throws PersistenceException error loading player
     */
    @GetMapping("/player/{id}")
    public String player(@PathVariable("id") int id, Model model)
        throws PersistenceException
    {
        Player player = mPlayerService.getPlayer(id);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.player");
        model.addAttribute(VIEW_TITLE_ARG_ONE, id);
        model.addAttribute(PLAYER, player);
        model.addAttribute(FRAGMENT_FILE_KEY, PLAYER);
        model.addAttribute(FRAGMENT_NAME_KEY, PLAYER);
        return TEMPLATE;
    }

    /**
     * Displays the list of competitions.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/competitions")
    public String competitions(Model model)
    {
        List<FinskaCompetition> competitions = mResultsService.getCompetitions();
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.competitions");
        model.addAttribute(COMPETITIONS, competitions);
        model.addAttribute(FRAGMENT_FILE_KEY, COMPETITION);
        model.addAttribute(FRAGMENT_NAME_KEY, COMPETITIONS);
        model.addAttribute(ADMIN, true);
        return TEMPLATE;
    }

    /**
     * Displays a specific competition.
     * 
     * @param id the competition ID
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/competition/{id}")
    public String competition(@PathVariable("id") int id, Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition(id);
        List<FinskaRound> rounds = competition.getRounds();
        Collections.reverse(rounds); // Most recent round first.
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.competition");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUNDS, rounds);
        model.addAttribute(PLAYERS, competition.getEntrants());
        model.addAttribute(FRAGMENT_FILE_KEY, COMPETITION);
        model.addAttribute(FRAGMENT_NAME_KEY, COMPETITION);
        model.addAttribute(ADMIN, true);
        return TEMPLATE;
    }

    /**
     * Displays the specified round.
     * 
     * @param id the competition ID
     * @param roundNumber the round's number
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/round/{id}/{roundNumber}")
    public String round(@PathVariable("id") int id,
                        @PathVariable("roundNumber") int roundNumber,
                        Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition(id);
        FinskaRound round = competition.getRound(roundNumber);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.round");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(VIEW_TITLE_ARG_TWO, roundNumber);
        model.addAttribute(ROUND, round);
        model.addAttribute(MATCHES, round.getMatches());
        model.addAttribute(PLAYERS, competition.getEntrantMap());
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, DISPLAY);
        return TEMPLATE;
    }
}
