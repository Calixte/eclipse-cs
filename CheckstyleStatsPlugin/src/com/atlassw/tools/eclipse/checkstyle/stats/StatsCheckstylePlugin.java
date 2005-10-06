//============================================================================
//
// Copyright (C) 2002-2005  David Schneider, Lars K�dderitzsch, Fabrice Bellingard
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
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//============================================================================
package com.atlassw.tools.eclipse.checkstyle.stats;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Fabrice BELLINGARD
 */
public class StatsCheckstylePlugin extends AbstractUIPlugin
{

    //
    // constants
    //

    /** The plugin id. */
    public static final String PLUGIN_ID = "com.atlassw.tools.eclipse.checkstyle.stats";

    /**
     * The shared instance.
     */
    private static StatsCheckstylePlugin sPlugin;

    //
    // constructors
    //

    /**
     * The constructor.
     */
    public StatsCheckstylePlugin()
    {
        super();
        sPlugin = this;
    }

    //
    // methods
    //

    /**
     * Returns the shared instance.
     * 
     * @return lthe shared instance
     */
    public static StatsCheckstylePlugin getDefault()
    {
        return sPlugin;
    }

    /**
     * Permet de loguer plus facilement dans la log du plugin.
     * 
     * @param severity :
     *            la gravit�
     * @param message :
     *            le message � loguer
     * @param throwable :
     *            l'exception � loguer
     */
    public static void log(int severity, String message, Throwable throwable)
    {
        IStatus status = new Status(severity, getDefault().getBundle()
            .getSymbolicName(), IStatus.OK, message, throwable);
        getDefault().getLog().log(status);
    }
}