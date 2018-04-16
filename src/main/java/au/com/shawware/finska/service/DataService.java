/*
 * Copyright (C) 2018 shawware.com.au
 *
 * License: GNU General Public License V3 (or later)
 * https://www.gnu.org/copyleft/gpl.html
 */

package au.com.shawware.finska.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import au.com.shawware.finska.persistence.CompetitionLoader;
import au.com.shawware.finska.persistence.PersistenceFactory;
import au.com.shawware.finska.scoring.ScoringSystem;

/**
 * Makes data available in this web application.
 *
 * @author <a href="mailto:david.shaw@shawware.com.au">David Shaw</a>
 */
@Service
public class DataService
{
    /** Where to get the data from. */
    @Value("${au.com.shawware.finska.datadir}")
    private String mDataDir;

    /** The wrapped results service. */
    private ResultsService mResultsService;

    /**
     * Constructs a new service.
     */
    public DataService()
    {
    }

    @PostConstruct
    private void initialise()
    {
        PersistenceFactory factory = PersistenceFactory.getFactory(mDataDir);
        CompetitionLoader loader = CompetitionLoader.getLoader(factory);
        ScoringSystem scoringSystem = new ScoringSystem(3, 1, 1, 1, 0);
        mResultsService = new ResultsService(loader, scoringSystem);
    }

    /**
     * @return The results service.
     */
    public ResultsService getResultsService()
    {
        return mResultsService;
    }
}
