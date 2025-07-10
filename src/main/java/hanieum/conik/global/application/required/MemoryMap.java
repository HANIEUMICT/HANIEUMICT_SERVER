package hanieum.conik.global.application.required;

public interface MemoryMap {
    void setValue(String key, String value, Long timeout);;

    String getValue(String key);

    void deleteValue(String key);

    boolean checkExistsValue(String key);
}
