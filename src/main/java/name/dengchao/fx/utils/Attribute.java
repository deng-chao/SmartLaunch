package name.dengchao.fx.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Set;

@Slf4j
public class Attribute {

    int offsetToAttributeFromEntryStart = 0;

    // header information
    int attributeTypeIdentifier = 0;
    int lenOfAttr = 0;
    int nonResisdentFlag = 0;
    int lenOfName = 0;
    int offsetToName = 0;
    int flags = 0;
    int attrIdentifier = 0;

    HashMap<String, String> values = new HashMap<String, String>();

    /**
     * Inserts a new attribute value into the attribute HashMap
     * @param key
     * @param value
     */
    public void addValue(String key, String value) {
        values.put(key, value);
    }

    /**
     * Prints out the header information for this attribute
     *
     */
    public void printHeaderInformation() {
        log.info("Attribute Type Identifier\t\t" + this.attributeTypeIdentifier);
        log.info("Length of attribute\t\t" + this.lenOfAttr);
        log.info("Non-resident flag\t\t" + this.nonResisdentFlag);
        log.info("Length of name\t\t" + this.lenOfName);
        log.info("Offset to name\t\t" + this.offsetToName);
        log.info("Flags\t\t\t" + this.flags);
        log.info("Attribute Identifier\t\t" + this.attrIdentifier);
    }

    /**
     * Prints out each key and value for the attribute
     *
     */
    public void printAttributeValues() {
        Set<String> keys = this.values.keySet();
        for(String key : keys) {
            log.info(key + "\t\t" + this.values.get(key));
        }
    }
}