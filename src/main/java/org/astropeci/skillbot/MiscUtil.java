package org.astropeci.skillbot;

import lombok.experimental.UtilityClass;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

@UtilityClass
public class MiscUtil {

    public boolean startsWithIgnoreCase(String source, String prefix) {
        return source.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public SimpleDateFormat getDateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }
}
