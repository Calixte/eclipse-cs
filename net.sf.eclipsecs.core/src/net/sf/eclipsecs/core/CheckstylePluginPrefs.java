//============================================================================
//
// Copyright (C) 2003-2023  David Schneider, Lars Ködderitzsch
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
//
//============================================================================

package net.sf.eclipsecs.core;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

import net.sf.eclipsecs.core.util.CheckstyleLog;

/**
 * Class for handling preferences of the <code>net.sf.eclipsecs.core</code> plugin.
 *
 */
public class CheckstylePluginPrefs extends AbstractPreferenceInitializer {

  /**
   * Preference name indicating if rule names are to be included in violation messages.
   */
  public static final String PREF_INCLUDE_RULE_NAMES = "include.rule.names"; //$NON-NLS-1$

  /**
   * Preference name indicating if module ids are to be included in violation messages.
   */
  public static final String PREF_INCLUDE_MODULE_IDS = "include.module.ids"; //$NON-NLS-1$

  /**
   * Preference name indicating if the number of checkstyle warning generated per file should be
   * limited.
   */
  public static final String PREF_LIMIT_MARKERS_PER_RESOURCE = "limit.markers.per.resource"; //$NON-NLS-1$

  /**
   * Preference name for the preference that stores the limit of markers per resource.
   */
  public static final String PREF_MARKER_AMOUNT_LIMIT = "marker.amount.limit"; //$NON-NLS-1$

  /**
   * Preference name for the preference to execute Checkstyle on full builds in the background.
   */
  public static final String PREF_BACKGROUND_FULL_BUILD = "background.full.build"; //$NON-NLS-1$

  /** Default value for the marker limitation. */
  public static final int MARKER_LIMIT = 100;

  /**
   * Preference checkstyle rule language.
   */
  public static final String PREF_LOCALE_LANGUAGE = "checkstyle_rule_language";

  @Override
  public void initializeDefaultPreferences() {

    IEclipsePreferences prefs = DefaultScope.INSTANCE.getNode(CheckstylePlugin.PLUGIN_ID);
    prefs.putBoolean(PREF_INCLUDE_RULE_NAMES, false);
    prefs.putBoolean(PREF_INCLUDE_MODULE_IDS, false);
    prefs.putBoolean(PREF_LIMIT_MARKERS_PER_RESOURCE, false);
    prefs.putInt(PREF_MARKER_AMOUNT_LIMIT, MARKER_LIMIT);
    prefs.putBoolean(PREF_BACKGROUND_FULL_BUILD, false);

    try {
      prefs.flush();
    } catch (BackingStoreException ex) {
      CheckstyleLog.log(ex);
    }
  }

  /**
   * Returns a string preference for the given preference id.
   *
   * @param prefId
   *          the preference id
   * @return the string result
   */
  public static String getString(String prefId) {
    final IPreferencesService prefs = Platform.getPreferencesService();
    return prefs.getString(CheckstylePlugin.PLUGIN_ID, prefId, null, null);
  }

  /**
   * Returns a boolean preference for the given preference id.
   *
   * @param prefId
   *          the preference id
   * @return the boolean result
   */
  public static boolean getBoolean(String prefId) {

    IPreferencesService prefs = Platform.getPreferencesService();
    return prefs.getBoolean(CheckstylePlugin.PLUGIN_ID, prefId, false, null);
  }

  /**
   * Returns an integer preference for the given preference id.
   *
   * @param prefId
   *          the preference id
   * @return the integer result
   */
  public static int getInt(String prefId) {

    IPreferencesService prefs = Platform.getPreferencesService();
    return prefs.getInt(CheckstylePlugin.PLUGIN_ID, prefId, 0, null);
  }

  /**
   * Set a string preference for the given preference id.
   *
   * @param prefId
   *          the preference id
   * @param value
   *          the string value
   * @throws BackingStoreException
   *           if this operation cannot be completed due to a failure in the
   *           backing store, or inability to communicate with it.
   */
  public static void setString(final String prefId, final String value)
          throws BackingStoreException {
    final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(CheckstylePlugin.PLUGIN_ID);
    prefs.put(prefId, value);
    prefs.flush();
  }

  /**
   * Set a boolean preference for the given preference id.
   *
   * @param prefId
   *          the preference id
   * @param value
   *          the boolean value
   * @throws BackingStoreException
   *           if this operation cannot be completed due to a failure in the backing store, or
   *           inability to communicate with it.
   */
  public static void setBoolean(String prefId, boolean value) throws BackingStoreException {

    IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(CheckstylePlugin.PLUGIN_ID);
    prefs.putBoolean(prefId, value);
    prefs.flush();
  }

  /**
   * Set a int preference for the given preference id.
   *
   * @param prefId
   *          the preference id
   * @param value
   *          the boolean value
   * @throws BackingStoreException
   *           if this operation cannot be completed due to a failure in the backing store, or
   *           inability to communicate with it.
   */
  public static void setInt(String prefId, int value) throws BackingStoreException {

    IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(CheckstylePlugin.PLUGIN_ID);
    prefs.putInt(prefId, value);
    prefs.flush();
  }
}
