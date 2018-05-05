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

import au.com.shawware.finska.persistence.EntityRepository;
import au.com.shawware.finska.persistence.IEntityRepository;
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
    /** The wrapped create service. */
    private CreateService mCreateService;

    /**
     * Constructs a new service.
     */
    public DataService()
    {
        // Nothing to do
    }

    @PostConstruct
    private void initialise()
        throws PersistenceException
    {
        PersistenceFactory factory = PersistenceFactory.getFactory(mDataDir);
        IEntityRepository repository = EntityRepository.getRepository(factory);
        ScoringSystem scoringSystem = new ScoringSystem(3, 1, 1, 1, 0);
        mResultsService = new ResultsService(repository, scoringSystem);
        mResultsService.initialise();
        mCreateService = new CreateService(repository);
    }

    /**
     * Reloads the data into this data service.
     * 
     * @throws PersistenceException error reloading
     */
    public void reload()
        throws PersistenceException
    {
        mResultsService.initialise();
    }

    /**
     * @return The results service.
     */
    public ResultsService getResultsService()
    {
        return mResultsService;
    }

    /**
     * @return The create service.
     */
    public CreateService getCreateService()
    {
        return mCreateService;
    }
}
