/*
 * Created on 27-Apr-2004
 * Created by Paul Gardner
 * Copyright (C) Azureus Software, Inc, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package org.gudy.azureus2.pluginsimpl.local.ui.components;

/**
 * @author parg
 *
 */

import java.util.Properties;

import org.gudy.azureus2.plugins.ui.components.UIComponent;
import org.gudy.azureus2.plugins.ui.components.UIPropertyChangeEvent;
import org.gudy.azureus2.plugins.ui.components.UIPropertyChangeListener;

import com.aelitis.azureus.core.util.CopyOnWriteList;

public class UIComponentImpl implements UIComponent {
    protected Properties properties = new Properties();

    protected CopyOnWriteList<UIPropertyChangeListener> listeners = new CopyOnWriteList<UIPropertyChangeListener>();

    protected UIComponentImpl() {
        properties.put(PT_ENABLED, new Boolean(true));
        properties.put(PT_VISIBLE, new Boolean(true));
    }

    public void setEnabled(boolean enabled) {
        setProperty(PT_ENABLED, new Boolean(enabled));
    }

    public boolean getEnabled() {
        return (((Boolean) getProperty(PT_ENABLED)).booleanValue());
    }

    public void setVisible(boolean visible) {
        setProperty(PT_VISIBLE, new Boolean(visible));
    }

    public boolean getVisible() {
        return (((Boolean) getProperty(PT_VISIBLE)).booleanValue());
    }

    public void setProperty(final String property_type, final Object property_value) {
        final Object old_value = properties.get(property_type);

        properties.put(property_type, property_value);

        UIPropertyChangeEvent ev = new UIPropertyChangeEvent() {
            public UIComponent getSource() {
                return (UIComponentImpl.this);
            }

            public String getPropertyType() {
                return (property_type);
            }

            public Object getNewPropertyValue() {
                return (property_value);
            }

            public Object getOldPropertyValue() {
                return (old_value);
            }
        };

        for (UIPropertyChangeListener listener : listeners) {

            listener.propertyChanged(ev);
        }
    }

    public Object getProperty(String property_type) {
        return (properties.get(property_type));
    }

    public void addPropertyChangeListener(UIPropertyChangeListener l) {
        listeners.add(l);
    }

    public void removePropertyChangeListener(UIPropertyChangeListener l) {
        listeners.remove(l);
    }
}
