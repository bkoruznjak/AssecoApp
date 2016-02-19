package hr.from.bkoruznjak.hash.model.pojo;

import java.io.Serializable;
import java.util.UUID;

import hr.from.bkoruznjak.hash.model.enums.StorageEnum;

/**
 * Created by Borna on 19.2.16.
 */
public class Website implements Serializable {

    private String id;
    private String websiteURL;
    private String hashCode;
    private String storage;

    public Website(String websiteURL, String hashCode, String storage) {
        this.id = UUID.randomUUID().toString();
        this.websiteURL = websiteURL;
        this.hashCode = hashCode;
        this.storage = storage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage.equals(StorageEnum.DATABASE.toString()) ? "SQLite" : "SharedPreferences";
    }
}
