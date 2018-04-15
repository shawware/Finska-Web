/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import au.com.shawware.finska.entity.Player;
import au.com.shawware.finska.service.DataService;

/**
 * Retrieves and supplies data.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@RestController
@RequestMapping("/data")
public class DataController
{
    private static Logger LOG = LoggerFactory.getLogger(DataController.class);

    /** The injected data service. */
    @Autowired
    private final DataService mDataService;

    /**
     * Constructs a new controller.
     * 
     * @param dataService the data service to use
     */
    public DataController(DataService dataService)
    {
        mDataService = dataService;
    }

    /**
     * Retrieves the player data and returns it without change.
     * 
     * @return The player data.
     */
    @GetMapping("/players")
    public Map<Integer, Player> players()
    {
        Map<Integer, Player> players = mDataService.getResultsService().getPlayers();
        LOG.debug("Players retrieved: " + players.size()); //$NON-NLS-1$
        return players;
    }


    /**
     * Retrieves the player data and returns it without change.
     * 
     * @param id the player's ID
     * 
     * @return The player data.
     */
    @GetMapping("/player/{id}")
    public Player player(@PathVariable("id") int id)
    {
        Player player = mDataService.getResultsService().getPlayer(id);
        LOG.debug("Player retrieved: " + player); //$NON-NLS-1$
        return player;
    }
}
