package org.ozsoft.projectbase.entities;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class VersionNumber implements Comparable<VersionNumber> {
    
//    private static final Pattern PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)(?:\\.(\\d+))?(?:-.+)$");
    private static final Pattern PATTERN = Pattern.compile("(\\d+)\\.(\\d+)(?:\\.(\\d+))?");
    
    @Basic
    private int major;
    
    @Basic
    private int minor;
    
    @Basic
    private int increment;
    
    @Basic
    private String qualifier = "";
    
    private VersionNumber() {
        // Empty implementation.
    }
    
    public int getMajor() {
        return major;
    }
    
    public int getMinor() {
        return minor;
    }
    
    public int getIncrement() {
        return increment;
    }
    
    public String getQualifier() {
        return qualifier;
    }
    
    public static VersionNumber parse(String text) throws ParseException {
        Matcher m = PATTERN.matcher(text);
        if (m.find()) {
            VersionNumber vn = new VersionNumber();
            try {
                vn.major = Integer.parseInt(m.group(1));
                vn.minor = Integer.parseInt(m.group(2));
                String increment = m.group(3);
                if (increment != null) {
                    vn.increment = Integer.parseInt(increment);
                } else {
                    vn.increment = 0;
                }
//                String qualifier = m.group(4);
//                if (qualifier == null) {
//                    qualifier = "";
//                }
                return vn;
            } catch (NumberFormatException e) {
                throw new ParseException("Invalid version number: " + text, 0);
            }
        } else {
            throw new ParseException("Invalid version number: " + text, 0);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VersionNumber) {
            VersionNumber other = (VersionNumber) obj;
            return (other.major == major && other.minor == minor && other.increment == increment);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d.%d.%d", major, minor, increment));
        if (qualifier.length() > 0) {
            sb.append('-');
            sb.append(qualifier);
        }
        return sb.toString();
    }

    @Override
    public int compareTo(VersionNumber other) {
        if (other.major == major) {
            if (other.minor == minor) {
                if (other.increment == increment) {
                    return 0;
                } else {
                    return Integer.valueOf(increment).compareTo(Integer.valueOf(other.increment));
                }
            } else {
                return Integer.valueOf(minor).compareTo(Integer.valueOf(other.minor));
            }
        } else {
            return Integer.valueOf(major).compareTo(Integer.valueOf(other.major));
        }
    }

}
