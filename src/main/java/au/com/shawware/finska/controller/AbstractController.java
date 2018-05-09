/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import au.com.shawware.finska.service.DataService;
import au.com.shawware.finska.service.ResultsService;
import au.com.shawware.finska.service.RoundService;

/**
 * Base class for controllers.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings({ "nls", "static-method" })
public abstract class AbstractController
{
    /** The name of the template to use for all views. */
    protected static final String TEMPLATE = "layout";
    /** The name of attribute that identifies the fragment file. */
    protected static final String FRAGMENT_FILE_KEY = "entityType";
    /** The name of attribute that identifies the fragment. */
    protected static final String FRAGMENT_NAME_KEY = "entityView";
    /** The name of attribute that identifies the view. */
    protected static final String VIEW_NAME = "title";

    /** The name of the attribute / file key that is used for a single competition. */
    protected static final String COMPETITION = "competition";
    /** The name of the attribute / file key that is used for a leader board. */
    protected static final String LEADERBOARD = "leaderboard";
    /** The name of the attribute that is used for players. */
    protected static final String PLAYERS = "players";
    /** The name of the attribute / file key that is used for a single player. */
    protected static final String PLAYER = "player";
    /** The name of the attribute that is used for rounds. */
    protected static final String ROUNDS = "rounds";
    /** The name of the attribute / file key that is used for a single round. */
    protected static final String ROUND = "round";

    /** The key used for the display operation. */
    protected static final String DISPLAY = "display";
    /** The key used for the create operation. */
    protected static final String CREATE = "create";
    /** The key used for the edit operation. */
    protected static final String EDIT = "edit";

    /** The home end point. */
    protected static final String HOME = "/";

    /** The injected data service. */
    @Autowired
    protected final DataService mDataService;
    /** The derived result service. */
    protected final ResultsService mResultsService;
    /** The derived round service. */
    protected final RoundService mRoundService;

    /**
     * Constructs a new controller.
     * 
     * @param dataService the data service to use
     */
    public AbstractController(DataService dataService)
    {
        mDataService    = dataService;
        mResultsService = dataService.getResultsService();
        mRoundService   = dataService.getRoundService();
    }

    /**
     * Returns a view that redirects the user to the given end point.
     * 
     * @param endpoint the end point to redirect to
     * 
     * @return The necessary view.
     */
    protected final ModelAndView redirectTo(String endpoint)
    {
        String safeEndpoint = "";
        if (endpoint.charAt(0) != '/')
        {
            safeEndpoint += "/";
        }
        safeEndpoint += endpoint;
        return new ModelAndView("redirect:" + safeEndpoint);
    }
}
