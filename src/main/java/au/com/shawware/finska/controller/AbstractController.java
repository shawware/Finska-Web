/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.controller;

import org.springframework.beans.factory.annotation.Autowired;

import au.com.shawware.finska.service.DataService;
import au.com.shawware.finska.service.ResultsService;

/**
 * Base class for controllers.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@SuppressWarnings({ "nls" })
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

    /** The injected data service. */
    @Autowired
    private final DataService mDataService;
    /** The derived result service. */
    protected final ResultsService mResultsService;

    /**
     * Constructs a new controller.
     * 
     * @param dataService the data service to use
     */
    public AbstractController(DataService dataService)
    {
        mDataService    = dataService;
        mResultsService = mDataService.getResultsService();
    }
}
