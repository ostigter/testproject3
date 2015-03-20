/*
 * Created on 11-Jan-2005
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

package com.aelitis.azureus.core.dht;

import java.util.Properties;

import com.aelitis.azureus.core.dht.db.DHTDB;
import com.aelitis.azureus.core.dht.impl.DHTImpl;
import com.aelitis.azureus.core.dht.nat.DHTNATPuncherAdapter;
import com.aelitis.azureus.core.dht.router.DHTRouter;
import com.aelitis.azureus.core.dht.transport.DHTTransport;

/**
 * @author parg
 * 
 */

public class DHTFactory {
    public static DHT create(DHTTransport transport, Properties properties, DHTStorageAdapter storage_adapter, DHTNATPuncherAdapter nat_adapter,
            DHTLogger logger) {
        return (new DHTImpl(transport, properties, storage_adapter, nat_adapter, logger));
    }

    public static DHT create(DHTTransport transport, DHTRouter router, DHTDB database, Properties properties, DHTStorageAdapter storage_adapter,
            DHTLogger logger) {
        return (new DHTImpl(transport, router, database, properties, storage_adapter, logger));
    }
}
