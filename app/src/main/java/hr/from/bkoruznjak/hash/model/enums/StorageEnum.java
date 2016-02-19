package hr.from.bkoruznjak.hash.model.enums;

/**
 * Created by Borna on 19.2.16.
 */
public enum StorageEnum {
    DATABASE("DATABASE"),
    SHARED_PREFERENCES("SHARED PREFERENCES");

    private final String text;

    private StorageEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
