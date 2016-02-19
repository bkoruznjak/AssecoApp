package hr.from.bkoruznjak.hash.model;

import hr.from.bkoruznjak.hash.model.pojo.Website;

/**
 * Created by Borna on 19.2.16.
 */
public interface WebsiteHandler {

    /**
     * Method adds website to storage
     *
     * @param website
     */
    public void addWebsite(Website website);

    /**
     * Method gets website based on website URL
     *
     * @param websiteURL
     * @return Website
     */
    public Website getWebsite(String websiteURL);
}
