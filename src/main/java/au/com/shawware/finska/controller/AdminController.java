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
import au.com.shawware.finska.entity.Player;
import au.com.shawware.finska.service.DataService;
import au.com.shawware.util.persistence.PersistenceException;

/**
 * Administers the competition data.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Controller
@RequestMapping("/admin")
@SuppressWarnings({ "nls", "boxing", "static-method" })
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
     * Displays a current players with update options.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     * 
     * @throws PersistenceException error accessing players
     */
    @GetMapping("/players")
    public String players(Model model)
        throws PersistenceException
    {
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.players");
        model.addAttribute(FRAGMENT_FILE_KEY, PLAYER);
        model.addAttribute(FRAGMENT_NAME_KEY, PLAYERS);
        model.addAttribute(PLAYERS, mPlayerService.getPlayers());
        model.addAttribute(ADMIN, true);
        return TEMPLATE;
    }

    /**
     * Displays a template for creating a new player.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/create/player")
    public String newPlayer(Model model)
    {
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.player.create");
        model.addAttribute(FRAGMENT_FILE_KEY, PLAYER);
        model.addAttribute(FRAGMENT_NAME_KEY, CREATE);
        return TEMPLATE;
    }

    /**
     * Creates a new player.
     * 
     * @param name the new player's name
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error creating player
     */
    @PostMapping(value="/create/player", params="action=create")
    public ModelAndView createPlayer(@RequestParam("name") String name)
        throws PersistenceException
    {
        mPlayerService.createPlayer(name);
        return redirectTo("/admin/players");
    }

    /**
     * Cancels the creation of a new player.
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/create/player", params="action=cancel")
    public ModelAndView cancelCreatePlayer()
    {
        return redirectTo("/admin/players");
    }

    /**
     * Displays the nominated player so that they can be updated.
     * 
     * @param id the player ID
     * @param model the model to add data to
     * 
     * @return The template name.
     * 
     * @throws PersistenceException error retrieving player
     */
    @GetMapping("/update/player/{id}")
    public String updatePlayer(@PathVariable("id") int id, Model model)
        throws PersistenceException
    {
        Player player = mPlayerService.getPlayer(id);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.player.update");
        model.addAttribute(VIEW_TITLE_ARG_ONE, player.getId());
        model.addAttribute(FRAGMENT_FILE_KEY, PLAYER);
        model.addAttribute(FRAGMENT_NAME_KEY, UPDATE);
        model.addAttribute(PLAYER, player);
        return TEMPLATE;
    }

    /**
     * Updates the nominated player with the submitted data.
     * 
     * @param id the player ID
     * @param name the player's updated name
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error updating player
     */
    @PostMapping(value="/update/player/{id}", params="action=update")
    public ModelAndView updatePlayer(@PathVariable("id") int id, @RequestParam("name") String name)
        throws PersistenceException
    {
        mPlayerService.updatePlayer(id, name);
        return redirectTo("/admin/players");
    }

    /**
     * Cancels the update of a player.
     * 
     * @param id the player ID
     * @param name the player's updated name
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error updating player
     */
    @PostMapping(value="/update/player/{id}", params="action=cancel")
    public ModelAndView cancelUpdatePlayer(@PathVariable("id") int id, @RequestParam("name") String name)
        throws PersistenceException
    {
        return redirectTo("/admin/players");
    }

    /**
     * Displays a template for creating a new competition.
     * 
     * @param model the model to add data to
     * 
     * @return The template name.
     * 
     * @throws PersistenceException error loading data
     */
    @GetMapping("/create/competition")
    public String createCompetition(Model model)
        throws PersistenceException
    {
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.competition.create");
        model.addAttribute(FRAGMENT_FILE_KEY, COMPETITION);
        model.addAttribute(FRAGMENT_NAME_KEY, CREATE);
        model.addAttribute(PLAYERS, mPlayerService.getPlayers());
        model.addAttribute("date", LocalDate.now());
        return TEMPLATE;
    }

    /**
     * Creates a new competition with the submitted data.
     * 
     * @param name the updated name
     * @param startDate the updated start date
     * @param players the players selected for this competition
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error creating competition
     */
    @PostMapping(value="/create/competition", params="action=create")
    public ModelAndView createCompetition(@RequestParam("name") String name,
                                          @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                          @RequestParam("players") int[] players)
        throws PersistenceException
    {
        FinskaCompetition competition = mCompetitionService.createCompetition(name, startDate, players);
        return redirectTo("/admin/update/competition", competition.getId());
    }

    /**
     * Cancels the creation of a new competition.
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/create/competition", params="action=cancel")
    public ModelAndView createCompetition()
    {
        return redirectTo("/display/competitions");
    }

    /**
     * Displays the nominated competition so that it can be updated.
     * 
     * @param id the competition ID
     * @param model the model to add data to
     * 
     * @return The template name.
     * 
     * @throws PersistenceException error loading data
     */
    @GetMapping("/update/competition/{id}")
    public String updateCompetition(@PathVariable("id") int id, Model model)
        throws PersistenceException
    {
        FinskaCompetition competition = mResultsService.getCompetition(id);
        Set<Integer> ids = competition.getEntrantIds();
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.competition.update");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(FRAGMENT_FILE_KEY, COMPETITION);
        model.addAttribute(FRAGMENT_NAME_KEY, UPDATE);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute("checked", ids);
        model.addAttribute(ROUNDS, competition.getRounds());
        model.addAttribute(PLAYERS, mPlayerService.getPlayers());
        return TEMPLATE;
    }

    /**
     * Updates the nominated competition with the submitted data.
     * 
     * @param id the competition ID
     * @param name the updated name
     * @param startDate the updated start date
     * @param players the players selected for this competition (updated)
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error updating competition
     */
    @PostMapping(value="/update/competition/{id}", params="action=update")
    public ModelAndView updateCompetition(@PathVariable("id") int id,
        @RequestParam("name") String name,
        @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("players") int[] players)
        throws PersistenceException
    {
        mCompetitionService.updateCompetition(id, name, startDate, players);
        return redirectTo("/admin/update/competition", id);
    }

    /**
     * Adds a new round to the nominated competition.
     * 
     * @param id the competition ID
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error updating competition
     */
    @PostMapping(value="/update/competition/{id}", params="action=create")
    public ModelAndView updateCompetition(@PathVariable("id") int id)
        throws PersistenceException
    {
        return redirectTo("/admin/create/round", id);
    }

    /**
     * Cancels the updating of a competition.
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/update/competition/{id}", params="action=done")
    public ModelAndView cancelUpdateCompetition()
    {
        return redirectTo("/display/competitions");
    }

    /**
     * Displays a template for creating a new round.
     * 
     * @param id the competition ID
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/create/round/{id}")
    public String newRound(@PathVariable("id") int id,
                           Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition(id);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.round.create");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, CREATE);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(PLAYERS, competition.getEntrants());
        model.addAttribute("date", LocalDate.now());
        return TEMPLATE;
    }

    /**
     * Creates a new round.
     * 
     * @param id the competition ID
     * @param roundDate the new round's date
     * @param playerIDs the IDs of the players participating in this round
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error creating round
     */
    @PostMapping(value="/create/round/{id}", params="action=create")
    public ModelAndView createRound(@PathVariable("id") int id,
                                    @RequestParam("round-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate roundDate,
                                    @RequestParam(name="players", required=false) int[] playerIDs)
        throws PersistenceException
    {
        FinskaRound round = mRoundService.createRound(id, roundDate, playerIDs);
        return redirectTo("/admin/update/round", id, round.getKey());
    }

    /**
     * Cancels the creation of a new round.
     * 
     * @param id the competition ID
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/create/round/{id}", params="action=cancel")
    public ModelAndView cancelCreateRound(@PathVariable("id") int id)
    {
        return redirectTo("/admin/update/competition", id);
    }

    /**
     * Displays the nominated round so that it can be updated.
     * 
     * @param id the competition ID
     * @param roundNumber the round number
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/update/round/{id}/{roundNumber}")
    public String updateRound(@PathVariable("id") int id,
                              @PathVariable("roundNumber") int roundNumber,
                              Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition(id);
        FinskaRound round = competition.getRound(roundNumber);
        Set<Integer> ids = round.getPlayerIds();
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.round.update");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(VIEW_TITLE_ARG_TWO, roundNumber);
        model.addAttribute(FRAGMENT_FILE_KEY, ROUND);
        model.addAttribute(FRAGMENT_NAME_KEY, UPDATE);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUND, round);
        model.addAttribute("checked", ids);
        model.addAttribute(MATCHES, round.getMatches());
        model.addAttribute(PLAYERS, competition.getEntrants());
        return TEMPLATE;
    }

    /**
     * Updates the nominated round with the submitted data.
     * 
     * @param id the competition ID
     * @param roundNumber the round number
     * @param players the players selected for this round (updated)
     * @param roundDate the updated round date
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error updating round
     */
    @PostMapping(value="/update/round/{id}/{roundNumber}", params="action=update")
    public ModelAndView updateRound(@PathVariable("id") int id,
                                    @PathVariable("roundNumber") int roundNumber,
                                    @RequestParam("players") int[] players,
                                    @RequestParam("round-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate roundDate)
        throws PersistenceException
    {
        mRoundService.updateRound(id, roundNumber, roundDate, players);
        return redirectTo("/admin/update/round", id, roundNumber);
    }

    /**
     * Commences the creation of a new match for the nominated round.
     * 
     * @param id the competition ID
     * @param roundNumber the round number
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error accessing data
     */
    @PostMapping(value="/update/round/{id}/{roundNumber}", params="action=create")
    public ModelAndView createMatch(@PathVariable("id") int id,
                                    @PathVariable("roundNumber") int roundNumber)
        throws PersistenceException
    {
        return redirectTo("/admin/create/match", id, roundNumber);
    }

    /**
     * Cancels the updating of a round.
     * 
     * @param id the competition ID
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/update/round/{id}/{roundNumber}", params="action=done")
    public ModelAndView cancelUpdateRound(@PathVariable("id") int id)
    {
        return redirectTo("/admin/update/competition", id);
    }

    /**
     * Displays a template for creating a new round.
     * 
     * @param id the competition ID
     * @param roundNumber the round number
     * @param model the model to add data to
     * 
     * @return The template name.
     */
    @GetMapping("/create/match/{id}/{roundNumber}")
    public String newMatch(@PathVariable("id") int id,
                           @PathVariable("roundNumber") int roundNumber,
                           Model model)
    {
        FinskaCompetition competition = mResultsService.getCompetition(id);
        FinskaRound round = competition.getRound(roundNumber);
        model.addAttribute(VIEW_TITLE, "sw.finska.page.title.match.create");
        model.addAttribute(VIEW_TITLE_ARG_ONE, competition.getKey());
        model.addAttribute(VIEW_TITLE_ARG_TWO, roundNumber);
        model.addAttribute(FRAGMENT_FILE_KEY, MATCH);
        model.addAttribute(FRAGMENT_NAME_KEY, CREATE);
        model.addAttribute(COMPETITION, competition);
        model.addAttribute(ROUND, round);
        return TEMPLATE;
    }

    /**
     * Creates a new match.
     * 
     * @param id the competition ID
     * @param roundNumber the round number
     * @param winnerIds the IDs of the winning players
     * @param fastWin whether these players had a fast win
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error creating round
     */
    @PostMapping(value="/create/match/{id}/{roundNumber}", params="action=create")
    public ModelAndView createMatch(@PathVariable("id") int id,
                                    @PathVariable("roundNumber") int roundNumber,
                                    @RequestParam(name="winners", required=false) int[] winnerIds,
                                    @RequestParam(name="fast-winners", defaultValue="false") boolean fastWin)
        throws PersistenceException
    {
        mMatchService.createMatch(id, roundNumber, winnerIds, fastWin);
        return redirectTo("/admin/update/round", id, roundNumber);
    }

    /**
     * Cancels the creation of a new match (in the given competition and round).
     * 
     * @param id the competition ID
     * @param roundNumber the round number
     * 
     * @return The next page to display.
     */
    @PostMapping(value="/create/match/{id}/{roundNumber}", params="action=cancel")
    public ModelAndView cancelCreateMatch(@PathVariable("id") int id,
                                          @PathVariable("roundNumber") int roundNumber)
    {
        return redirectTo("/admin/update/round", id, roundNumber);
    }

    /**
     * Updates the nominated match with the submitted data.
     * 
     * @param id the competition ID
     * @param roundNumber the round number
     * @param matchNumber the match number
     * @param winnerIds the updated IDs of the winners
     * @param fastWin the updated fast win setting
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error updating match
     */
    @PostMapping(value="/update/match/{id}/{roundNumber}/{matchNumber}", params="action=update")
    public ModelAndView updateMatch(@PathVariable("id") int id,
                                    @PathVariable("roundNumber") int roundNumber,
                                    @PathVariable("matchNumber") int matchNumber,
                                    @RequestParam(name="winners", required=false) int[] winnerIds,
                                    @RequestParam(name="fast-winners", defaultValue="false") boolean fastWin)
        throws PersistenceException
    {
        mMatchService.updateMatch(id, roundNumber, matchNumber, winnerIds, fastWin);
        return redirectTo("/admin/update/round", id, roundNumber);
    }

    /**
     * Adds another match to the given round.
     * 
     * @param id the competition ID
     * @param roundNumber the round number
     * @param matchNumber the match number
     * 
     * @return The next page to display.
     * 
     * @throws PersistenceException error updating match
     */
    @PostMapping(value="/update/match/{id}/{roundNumber}/{matchNumber}", params="action=create")
    public ModelAndView updateMatch(@PathVariable("id") int id,
                                    @PathVariable("roundNumber") int roundNumber,
                                    @PathVariable("matchNumber") int matchNumber)
        throws PersistenceException
    {
        return redirectTo("/admin/create/match", id, roundNumber);
    }
}
