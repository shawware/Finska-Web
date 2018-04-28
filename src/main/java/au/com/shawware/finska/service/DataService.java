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

import au.com.shawware.finska.persistence.EntityLoader;
import au.com.shawware.finska.persistence.IEntityLoader;
import au.com.shawware.finska.scoring.ScoringSystem;
import au.com.shawware.util.persistence.PersistenceException;
import au.com.shawware.util.persistence.PersistenceFactory;

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
        throws PersistenceException
    {
        PersistenceFactory factory = PersistenceFactory.getFactory(mDataDir);
        IEntityLoader loader = EntityLoader.getLoader(factory);
        ScoringSystem scoringSystem = new ScoringSystem(3, 1, 1, 1, 0);
        mResultsService = new ResultsService(loader, scoringSystem);
        mResultsService.initialise();
    }

    /**
     * @return The results service.
     */
    public ResultsService getResultsService()
    {
        return mResultsService;
    }
}
