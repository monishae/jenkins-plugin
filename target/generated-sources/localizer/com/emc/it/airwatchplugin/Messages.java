// CHECKSTYLE:OFF

package com.emc.it.airwatchplugin;

import org.jvnet.localizer.Localizable;
import org.jvnet.localizer.ResourceBundleHolder;

@SuppressWarnings({
    "",
    "PMD"
})
public class Messages {

    private final static ResourceBundleHolder holder = ResourceBundleHolder.get(Messages.class);

    /**
     * Publish to Airwatch
     * 
     */
    public static String AirwatchPublisher_description() {
        return holder.format("AirwatchPublisher.description");
    }

    /**
     * Publish to Airwatch
     * 
     */
    public static Localizable _AirwatchPublisher_description() {
        return new Localizable(holder, "AirwatchPublisher.description");
    }

}
