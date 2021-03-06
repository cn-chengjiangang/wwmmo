package au.com.codeka.warworlds.client.util;

import java.util.Locale;

import au.com.codeka.warworlds.client.BuildConfig;

/**
 * Some helper functions for display version information.
 */
public class Version {
  public static String string() {
    return String.format(Locale.US, "%s.%d%s",
        BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.DEBUG ? "-dbg" : "");
  }
}
