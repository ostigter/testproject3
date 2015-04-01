/*
 * Created on 14-Sep-2006
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

package com.aelitis.azureus.util;

import org.gudy.azureus2.core3.download.DownloadManagerState;
import org.gudy.azureus2.core3.download.DownloadManagerStateAttributeListener;
import org.gudy.azureus2.plugins.PluginInterface;
import org.gudy.azureus2.plugins.download.Download;
import org.gudy.azureus2.plugins.download.DownloadManager;
import org.gudy.azureus2.plugins.download.DownloadManagerListener;
import org.gudy.azureus2.plugins.download.DownloadWillBeAddedListener;
import org.gudy.azureus2.plugins.torrent.Torrent;
import org.gudy.azureus2.pluginsimpl.local.PluginCoreUtils;
import org.gudy.azureus2.pluginsimpl.local.PluginInitializer;

import com.aelitis.azureus.core.AzureusCore;
import com.aelitis.azureus.core.download.DownloadManagerEnhancer;
import com.aelitis.azureus.core.peer.cache.CacheDiscovery;
import com.aelitis.azureus.core.torrent.PlatformTorrentUtils;

public class InitialisationFunctions {
    private static final String EXTENSION_PREFIX = "azid";

    public static void earlyInitialisation(AzureusCore core) {

        DownloadUtils.initialise();

        DownloadManagerEnhancer dme = DownloadManagerEnhancer.initialise(core);

        hookDownloadAddition();

        // AzureusPlatformContentDirectory.register();

        CacheDiscovery.initialise(dme);

        // ContentNetworkManagerFactory.preInitialise();

        // MetaSearchManagerFactory.preInitialise();

        // SubscriptionManagerFactory.preInitialise();

        // DeviceManagerFactory.preInitialise();

        NavigationHelper.initialise();

        // RelatedContentManager.preInitialise(core);
    }

    public static void lateInitialisation(AzureusCore core) {
        ExternalStimulusHandler.initialise(core);

        PluginInitializer.getDefaultInterface().getUtilities().createDelayedTask(new Runnable() {
            public void run() {
                // MetaSearchManagerFactory.getSingleton();

                // SubscriptionManagerFactory.getSingleton();

                // try {
                // RelatedContentManager.getSingleton();
                //
                // } catch (Throwable e) {
                //
                // Debug.out(e);
                // }
                //
                // try {
                // MetaSearchManagerFactory.getSingleton().addListener(new MetaSearchManagerListener() {
                // public void searchRequest(String term) {
                // UIFunctionsManager.getUIFunctions().doSearch(term);
                // }
                // });
                // } catch (Throwable e) {
                //
                // Debug.out(e);
                // }
            }
        }).queue();
    }

    protected static void hookDownloadAddition() {
        PluginInterface pi = PluginInitializer.getDefaultInterface();

        DownloadManager dm = pi.getDownloadManager();

        // need to get in early to ensure property present on initial announce

        dm.addDownloadWillBeAddedListener(new DownloadWillBeAddedListener() {
            public void initialised(Download download) {
                // unfortunately the has-been-opened state is updated by azureus when a user opens content
                // but is also preserved across torrent export/import (e.g. when downloaded via magnet
                // URL. So reset it here if it is found to be set

                org.gudy.azureus2.core3.download.DownloadManager dm = PluginCoreUtils.unwrap(download);

                if (PlatformTorrentUtils.getHasBeenOpened(dm)) {

                    PlatformTorrentUtils.setHasBeenOpened(dm, false);
                }

                register(download);
            }
        });

        dm.addListener(new DownloadManagerListener() {
            public void downloadAdded(Download download) {
                register(download);
            }

            public void downloadRemoved(Download download) {
            }
        });
    }

    protected static void register(final Download download) {
        // only add the azid to platform content

        DownloadManagerStateAttributeListener dmsal = new DownloadManagerStateAttributeListener() {
            public void attributeEventOccurred(org.gudy.azureus2.core3.download.DownloadManager dm, String attribute_name, int event_type) {
                try {
                    Torrent t = download.getTorrent();
                    if (t == null) {
                        return;
                    }
                    if (!PlatformTorrentUtils.isContent(t, true)) {
                        return;
                    }
                    DownloadUtils.addTrackerExtension(download, EXTENSION_PREFIX, ConstantsVuze.AZID);

                    // allow the tracker to manipulate peer sources for dead/unauthorised torrents
                    download.setFlag(Download.FLAG_ALLOW_PERMITTED_PEER_SOURCE_CHANGES, true);
                } finally {
                    dm.getDownloadState().removeListener(this, DownloadManagerState.AT_TRACKER_CLIENT_EXTENSIONS,
                            DownloadManagerStateAttributeListener.WILL_BE_READ);
                }
            }
        };

        PluginCoreUtils.unwrap(download).getDownloadState().addListener(dmsal, DownloadManagerState.AT_TRACKER_CLIENT_EXTENSIONS,
                DownloadManagerStateAttributeListener.WILL_BE_READ);

    }
}
