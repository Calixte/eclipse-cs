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

package net.sf.eclipsecs.core.config.configtypes;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import com.puppycrawl.tools.checkstyle.PropertyResolver;

/**
 * Property resolver that resolves some eclipse standard variables.
 *
 */
public class StandardPropertyResolver implements PropertyResolver, IContextAware {

  /** constant for the workspace_loc variable. */
  private static final String WORKSPACE_LOC = "workspace_loc"; //$NON-NLS-1$

  /** constant for the project_loc variable. */
  private static final String PROJECT_LOC = "project_loc"; //$NON-NLS-1$

  /** constant for the basedir variable. */
  private static final String BASEDIR_LOC = "basedir"; //$NON-NLS-1$

  /** constant for the samedir variable. */
  private static final String SAMEDIR_LOC = "samedir"; //$NON-NLS-1$

  /** constant for the config_loc variable. */
  private static final String CONFIG_LOC = "config_loc"; //$NON-NLS-1$

  /** the location of the configuration file. */
  private final String mConfigLocation;

  /** the context project. */
  private IProject mProject;

  /**
   * Creates the BuiltInPropertyResolver.
   *
   * @param configLocation
   *          the location of the checkstyle configuration file
   */
  public StandardPropertyResolver(String configLocation) {
    mConfigLocation = configLocation;
  }

  @Override
  public void setProjectContext(IProject project) {
    mProject = project;
  }

  @Override
  public String resolve(String property) {

    String value = null;
    if (WORKSPACE_LOC.equals(property)) {
      value = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
    } else if (mProject != null && (PROJECT_LOC.equals(property) || BASEDIR_LOC.equals(property))) {
      value = mProject.getLocation().toString();
    } else if (mConfigLocation != null
            && (SAMEDIR_LOC.equals(property) || CONFIG_LOC.equals(property))) {
      String configLocWOBackslashes = mConfigLocation.replace('\\', '/');

      int lastSlash = configLocWOBackslashes.lastIndexOf("/"); //$NON-NLS-1$
      if (lastSlash > -1) {
        value = configLocWOBackslashes.substring(0, lastSlash + 1);
      }
    }

    return value;
  }
}
