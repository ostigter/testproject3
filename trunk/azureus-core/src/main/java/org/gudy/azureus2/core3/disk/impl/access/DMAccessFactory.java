/*
 * Created on 31-Jul-2004
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

package org.gudy.azureus2.core3.disk.impl.access;

/**
 * @author parg
 *
 */

import org.gudy.azureus2.core3.disk.impl.DiskManagerHelper;
import org.gudy.azureus2.core3.disk.impl.access.impl.DMCheckerImpl;
import org.gudy.azureus2.core3.disk.impl.access.impl.DMReaderImpl;
import org.gudy.azureus2.core3.disk.impl.access.impl.DMWriterImpl;

public class DMAccessFactory {
    public static DMReader createReader(DiskManagerHelper adapter) {
        return (new DMReaderImpl(adapter));
    }

    public static DMWriter createWriter(DiskManagerHelper disk_manager) {
        return (new DMWriterImpl(disk_manager));
    }

    public static DMChecker createChecker(DiskManagerHelper disk_manager) {
        return (new DMCheckerImpl(disk_manager));
    }
}
