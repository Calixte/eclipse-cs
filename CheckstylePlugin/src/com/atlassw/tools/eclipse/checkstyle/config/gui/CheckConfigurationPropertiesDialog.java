//============================================================================
//
// Copyright (C) 2002-2006  David Schneider, Lars K�dderitzsch
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

package com.atlassw.tools.eclipse.checkstyle.config.gui;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.atlassw.tools.eclipse.checkstyle.Messages;
import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationWorkingCopy;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfigurationWorkingSet;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ConfigurationTypes;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.ICheckConfigurationEditor;
import com.atlassw.tools.eclipse.checkstyle.config.configtypes.IConfigurationType;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstyleLog;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginImages;

/**
 * Dialog to show/edit the properties (name, location, description) of a check
 * configuration. Also used to create new check configurations.
 * 
 * @author Lars K�dderitzsch
 */
public class CheckConfigurationPropertiesDialog extends TitleAreaDialog
{

    //
    // attributes
    //

    /** the working set. */
    private ICheckConfigurationWorkingSet mWorkingSet;

    /** the check configuration. */
    private CheckConfigurationWorkingCopy mCheckConfig;

    /** the combo box containing the config type. */
    private ComboViewer mConfigType;

    /** place holder for the location editor. */
    private Composite mEditorPlaceHolder;

    /** the editor for the configuration location. */
    private ICheckConfigurationEditor mConfigurationEditor;

    //
    // constructor
    //

    /**
     * Creates the properties dialog for check configurations.
     * 
     * @param parent the parent shell
     * @param checkConfig the check configuration or <code>null</code> if a
     *            new check config should be created
     * @param workingSet the working set the check config is changed in
     */
    public CheckConfigurationPropertiesDialog(Shell parent,
            CheckConfigurationWorkingCopy checkConfig, ICheckConfigurationWorkingSet workingSet)
    {
        super(parent);
        mWorkingSet = workingSet;
        mCheckConfig = checkConfig;
    }

    //
    // methods
    //

    /**
     * Get the check configuration from the editor.
     * 
     * @return the check configuration
     * @throws CheckstylePluginException if the data is not valid
     */
    public CheckConfigurationWorkingCopy getCheckConfiguration() throws CheckstylePluginException
    {
        return mCheckConfig;
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#create()
     */
    public void create()
    {
        super.create();
        initialize();
    }
    
    

    /**
     * Creates the dialogs main contents.
     * 
     * @param parent the parent composite
     */
    protected Control createDialogArea(Composite parent)
    {

        // set the logo
        this.setTitleImage(CheckstylePluginImages.getImage(CheckstylePluginImages.PLUGIN_LOGO));

        Composite composite = (Composite) super.createDialogArea(parent);

        Composite contents = new Composite(composite, SWT.NULL);
        contents.setLayout(new GridLayout(2, false));
        GridData fd = new GridData(GridData.FILL_BOTH);
        contents.setLayoutData(fd);

        Label lblConfigType = new Label(contents, SWT.NULL);
        lblConfigType.setText(Messages.CheckConfigurationPropertiesDialog_lblConfigType);
        fd = new GridData();

        // this is a weird hack to find the longest label
        // this is done to have a nice ordered appearance of the this label
        // and the labels below
        // this is very difficult to do, because they belong to different
        // layouts
        GC gc = new GC(lblConfigType);
        int nameSize = gc.textExtent(Messages.CheckConfigurationPropertiesDialog_lblName).x;
        int locationsSize = gc.textExtent(Messages.CheckConfigurationPropertiesDialog_lblLocation).x;
        int max = Math.max(nameSize, locationsSize);
        gc.dispose();

        fd.widthHint = max;
        lblConfigType.setLayoutData(fd);

        mConfigType = new ComboViewer(contents);
        fd = new GridData();
        mConfigType.getCombo().setLayoutData(fd);
        mConfigType.setContentProvider(new ArrayContentProvider());
        mConfigType.setLabelProvider(new LabelProvider()
        {
            /**
             * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
             */
            public String getText(Object element)
            {
                return ((IConfigurationType) element).getName();
            }

            /**
             * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
             */
            public Image getImage(Object element)
            {
                return ((IConfigurationType) element).getTypeImage();
            }
        });
        mConfigType.addSelectionChangedListener(new ISelectionChangedListener()
        {
            /**
             * @see ISelectionChangedListener#selectionChanged(
             *      org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            public void selectionChanged(SelectionChangedEvent event)
            {
                if (event.getSelection() instanceof IStructuredSelection)
                {
                    IConfigurationType type = (IConfigurationType) ((IStructuredSelection) event
                            .getSelection()).getFirstElement();
                    createConfigurationEditor(type);

                    if (mConfigType.getCombo().isEnabled())
                    {

                        String oldName = mCheckConfig.getName();
                        String oldDescr = mCheckConfig.getDescription();

                        mCheckConfig = mWorkingSet.newWorkingCopy(type);
                        try
                        {
                            mCheckConfig.setName(oldName);
                        }
                        catch (CheckstylePluginException e)
                        {
                            // NOOP
                        }
                        mCheckConfig.setDescription(oldDescr);
                    }
                    mConfigurationEditor.initialize(mCheckConfig);
                }
            }
        });

        mEditorPlaceHolder = new Composite(contents, SWT.NULL);
        GridLayout layout = new GridLayout(1, true);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        mEditorPlaceHolder.setLayout(layout);
        fd = new GridData(GridData.FILL_HORIZONTAL);
        fd.horizontalSpan = 2;
        mEditorPlaceHolder.setLayoutData(fd);

        return composite;
    }

    /**
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell newShell)
    {

        super.configureShell(newShell);
        newShell.setText(Messages.CheckConfigurationPropertiesDialog_titleCheckProperties);
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    protected void okPressed()
    {
        try
        {
            // Check if the configuration is valid
            mCheckConfig = mConfigurationEditor.getEditedWorkingCopy();
            super.okPressed();
        }
        catch (CheckstylePluginException e)
        {
            this.setErrorMessage(e.getLocalizedMessage());
        }
    }

    /**
     * Creates the configuration type specific location editor.
     * 
     * @param configType the configuration type
     */
    private void createConfigurationEditor(IConfigurationType configType)
    {

        Class editorClass = configType.getLocationEditorClass();

        try
        {
            mConfigurationEditor = (ICheckConfigurationEditor) editorClass.newInstance();

            // remove old editor
            Control[] controls = mEditorPlaceHolder.getChildren();
            for (int i = 0; i < controls.length; i++)
            {
                controls[i].dispose();
            }

            mConfigurationEditor.createEditorControl(mEditorPlaceHolder, getShell());

            mEditorPlaceHolder.redraw();
            mEditorPlaceHolder.update();
            mEditorPlaceHolder.layout();

            Point initialSize = this.getInitialSize();
            getShell().setSize(initialSize);

        }
        catch (Exception ex)
        {
            CheckstyleLog.errorDialog(getShell(), ex, true);
        }
    }

    /**
     * Initialize the dialogs controls with the data.
     */
    private void initialize()
    {

        if (mCheckConfig == null)
        {
            this.setTitle(Messages.CheckConfigurationPropertiesDialog_titleCheckConfig);
            this.setMessage(Messages.CheckConfigurationPropertiesDialog_msgCreateNewCheckConfig);

            IConfigurationType[] types = ConfigurationTypes.getCreatableConfigTypes();

            mCheckConfig = mWorkingSet.newWorkingCopy(types[0]);
            mConfigType.setInput(types);
            mConfigType.setSelection(new StructuredSelection(types[0]), true);

            createConfigurationEditor(types[0]);
            mConfigurationEditor.initialize(mCheckConfig);
        }
        else
        {
            this.setTitle(Messages.CheckConfigurationPropertiesDialog_titleCheckConfig);
            this.setMessage(Messages.CheckConfigurationPropertiesDialog_msgEditCheckConfig);

            mConfigType.getCombo().setEnabled(false);
            mConfigType.setInput(new IConfigurationType[] { mCheckConfig.getType() });

            // type of existing configs cannot be changed
            mConfigType.setSelection(new StructuredSelection(mCheckConfig.getType()), true);
            createConfigurationEditor(mCheckConfig.getType());

            mConfigurationEditor.initialize(mCheckConfig);
        }

    }
}