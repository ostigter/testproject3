/*
 * Created on 12-Jan-2005
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

package com.aelitis.azureus.core.dht.transport.loopback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gudy.azureus2.core3.util.AEMonitor;
import org.gudy.azureus2.core3.util.AERunnable;
import org.gudy.azureus2.core3.util.AESemaphore;
import org.gudy.azureus2.core3.util.AEThread;
import org.gudy.azureus2.core3.util.HashWrapper;
import org.gudy.azureus2.core3.util.SHA1Simple;

import com.aelitis.azureus.core.dht.DHT;
import com.aelitis.azureus.core.dht.transport.DHTTransport;
import com.aelitis.azureus.core.dht.transport.DHTTransportContact;
import com.aelitis.azureus.core.dht.transport.DHTTransportException;
import com.aelitis.azureus.core.dht.transport.DHTTransportFindValueReply;
import com.aelitis.azureus.core.dht.transport.DHTTransportFullStats;
import com.aelitis.azureus.core.dht.transport.DHTTransportListener;
import com.aelitis.azureus.core.dht.transport.DHTTransportProgressListener;
import com.aelitis.azureus.core.dht.transport.DHTTransportReplyHandler;
import com.aelitis.azureus.core.dht.transport.DHTTransportRequestHandler;
import com.aelitis.azureus.core.dht.transport.DHTTransportStats;
import com.aelitis.azureus.core.dht.transport.DHTTransportStoreReply;
import com.aelitis.azureus.core.dht.transport.DHTTransportTransferHandler;
import com.aelitis.azureus.core.dht.transport.DHTTransportValue;
import com.aelitis.azureus.core.dht.transport.util.DHTTransportRequestCounter;
import com.aelitis.azureus.core.dht.transport.util.DHTTransportStatsImpl;

/**
 * @author parg
 * 
 */

public class DHTTransportLoopbackImpl implements DHTTransport {
    public static byte VERSION = 1;

    public static int LATENCY = 0;
    public static int FAIL_PERCENTAGE = 0;

    public byte getProtocolVersion() {
        return (VERSION);
    }

    public byte getMinimumProtocolVersion() {
        return (VERSION);
    }

    public int getNetwork() {
        return (DHT.NW_MAIN);
    }

    public boolean isIPV6() {
        return (false);
    }

    public static void setLatency(int _latency) {
        LATENCY = _latency;
    }

    public static void setFailPercentage(int p) {
        FAIL_PERCENTAGE = p;
    }

    private static long node_id_seed_next = 0;
    private static Map node_map = new HashMap();

    private static List dispatch_queue = new ArrayList();
    private static AESemaphore dispatch_queue_sem = new AESemaphore("DHTTransportLoopback");

    private static AEMonitor class_mon = new AEMonitor("DHTTransportLoopback:class");

    static {
        AEThread dispatcher = new AEThread("DHTTransportLoopback") {
            public void runSupport() {
                while (true) {

                    dispatch_queue_sem.reserve();

                    Runnable r;

                    try {
                        class_mon.enter();

                        r = (Runnable) dispatch_queue.remove(0);

                    } finally {

                        class_mon.exit();
                    }

                    if (LATENCY > 0) {

                        try {
                            Thread.sleep(LATENCY);

                        } catch (Throwable e) {

                        }
                    }

                    r.run();
                }
            }
        };

        dispatcher.start();
    }

    private byte[] node_id;
    private DHTTransportContact local_contact;

    private int id_byte_length;

    private DHTTransportRequestHandler request_handler;

    private DHTTransportStatsImpl stats = new DHTTransportLoopbackStatsImpl(VERSION);

    private List listeners = new ArrayList();

    public static DHTTransportStats getOverallStats() {
        try {
            class_mon.enter();

            DHTTransportStatsImpl overall_stats = new DHTTransportLoopbackStatsImpl(VERSION);

            Iterator it = node_map.values().iterator();

            while (it.hasNext()) {

                overall_stats.add((DHTTransportStatsImpl) ((DHTTransportLoopbackImpl) it.next()).getStats());
            }

            return (overall_stats);

        } finally {

            class_mon.exit();
        }
    }

    public DHTTransportLoopbackImpl(int _id_byte_length) {
        id_byte_length = _id_byte_length;

        try {
            class_mon.enter();

            byte[] temp = new SHA1Simple().calculateHash(("" + (node_id_seed_next++)).getBytes());

            node_id = new byte[id_byte_length];

            System.arraycopy(temp, 0, node_id, 0, id_byte_length);

            node_map.put(new HashWrapper(node_id), this);

            local_contact = new DHTTransportLoopbackContactImpl(this, node_id);
        } finally {

            class_mon.exit();
        }
    }

    public DHTTransportContact getLocalContact() {
        return (local_contact);
    }

    public void setPort(int port) {
    }

    public int getPort() {
        return (0);
    }

    public byte getGenericFlags() {
        return 0;
    }

    public void setGenericFlag(byte flag, boolean value) {
    }

    public void setSuspended(boolean susp) {
    }

    public long getTimeout() {
        return (0);
    }

    public void setTimeout(long millis) {
    }

    public boolean isReachable() {
        return (true);
    }

    public DHTTransportContact[] getReachableContacts() {
        return (new DHTTransportContact[0]);
    }

    public DHTTransportContact[] getRecentContacts() {
        return (new DHTTransportContact[0]);
    }

    protected DHTTransportLoopbackImpl findTarget(byte[] id) {
        try {
            class_mon.enter();

            return ((DHTTransportLoopbackImpl) node_map.get(new HashWrapper(id)));
        } finally {

            class_mon.exit();
        }
    }

    public void setRequestHandler(DHTTransportRequestHandler _request_handler) {
        request_handler = new DHTTransportRequestCounter(_request_handler, stats);

    }

    protected DHTTransportRequestHandler getRequestHandler() {
        return (request_handler);
    }

    public Map<String, Object> exportContactToMap(DHTTransportContact contact) {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("i", contact.getID());

        return (result);
    }

    public void exportContact(DHTTransportContact contact, DataOutputStream os)

    throws IOException {
        os.writeInt(VERSION);

        os.writeInt(id_byte_length);

        os.write(contact.getID());
    }

    public DHTTransportContact importContact(DataInputStream is, boolean is_bootstrap)

    throws IOException {
        int version = is.readInt();

        if (version != VERSION) {

            throw (new IOException("Unsuported version"));

        }
        int id_len = is.readInt();

        if (id_len != id_byte_length) {

            throw (new IOException("Imported contact has incorrect ID length"));
        }

        byte[] id = new byte[id_byte_length];

        is.read(id);

        DHTTransportContact contact = new DHTTransportLoopbackContactImpl(this, id);

        request_handler.contactImported(contact, is_bootstrap);

        return (contact);
    }

    public void removeContact(DHTTransportContact contact) {
    }

    protected void run(final AERunnable r) {
        try {
            class_mon.enter();

            dispatch_queue.add(r);

        } finally {

            class_mon.exit();
        }

        dispatch_queue_sem.release();
    }

    public DHTTransportStats getStats() {
        return (stats);
    }

    // transport

    // PING

    public void sendPing(final DHTTransportContact contact, final DHTTransportReplyHandler handler) {
        AERunnable runnable = new AERunnable() {
            public void runSupport() {
                sendPingSupport(contact, handler);
            }
        };

        run(runnable);
    }

    public void sendPingSupport(DHTTransportContact contact, DHTTransportReplyHandler handler) {
        DHTTransportLoopbackImpl target = findTarget(contact.getID());

        stats.pingSent(null);

        if (target == null || triggerFailure()) {

            stats.pingFailed();

            handler.failed(contact, new Exception("failed"));

        } else {

            stats.pingOK();

            target.getRequestHandler().pingRequest(new DHTTransportLoopbackContactImpl(target, node_id));

            handler.pingReply(contact, 0);
        }
    }

    public void sendKeyBlock(final DHTTransportContact contact, final DHTTransportReplyHandler handler, final byte[] request, final byte[] sig) {
        AERunnable runnable = new AERunnable() {
            public void runSupport() {
                sendKeyBlockSupport(contact, handler, request, sig);
            }
        };

        run(runnable);
    }

    public void sendKeyBlockSupport(DHTTransportContact contact, DHTTransportReplyHandler handler, byte[] request, byte[] sig) {
        DHTTransportLoopbackImpl target = findTarget(contact.getID());

        stats.keyBlockSent(null);

        if (target == null || triggerFailure()) {

            stats.keyBlockFailed();

            handler.failed(contact, new Exception("failed"));

        } else {

            stats.keyBlockOK();

            target.getRequestHandler().keyBlockRequest(new DHTTransportLoopbackContactImpl(target, node_id), request, sig);

            handler.keyBlockReply(contact);
        }
    }

    // STATS

    public void sendStats(final DHTTransportContact contact, final DHTTransportReplyHandler handler) {
        AERunnable runnable = new AERunnable() {
            public void runSupport() {
                sendStatsSupport(contact, handler);
            }
        };

        run(runnable);
    }

    public void sendStatsSupport(DHTTransportContact contact, DHTTransportReplyHandler handler) {
        DHTTransportLoopbackImpl target = findTarget(contact.getID());

        stats.statsSent(null);

        if (target == null || triggerFailure()) {

            stats.statsFailed();

            handler.failed(contact, new Exception("failed"));

        } else {

            stats.statsOK();

            DHTTransportFullStats res = target.getRequestHandler().statsRequest(new DHTTransportLoopbackContactImpl(target, node_id));

            handler.statsReply(contact, res);
        }
    }

    // STORE

    public void sendStore(final DHTTransportContact contact, final DHTTransportReplyHandler handler, final byte[][] keys,
            final DHTTransportValue[][] value_sets, final boolean immediate) {
        AERunnable runnable = new AERunnable() {
            public void runSupport() {
                sendStoreSupport(contact, handler, keys, value_sets);
            }
        };

        run(runnable);
    }

    public void sendStoreSupport(DHTTransportContact contact, DHTTransportReplyHandler handler, byte[][] keys, DHTTransportValue[][] value_sets) {
        DHTTransportLoopbackImpl target = findTarget(contact.getID());

        stats.storeSent(null);

        if (target == null || triggerFailure()) {

            stats.storeFailed();

            handler.failed(contact, new Exception("failed"));

        } else {

            stats.storeOK();

            DHTTransportContact temp = new DHTTransportLoopbackContactImpl(target, node_id);

            temp.setRandomID(contact.getRandomID());

            DHTTransportStoreReply rep = target.getRequestHandler().storeRequest(temp, keys, value_sets);

            if (rep.blocked()) {

                handler.keyBlockRequest(contact, rep.getBlockRequest(), rep.getBlockSignature());

                handler.failed(contact, new Throwable("key blocked"));

            } else {

                handler.storeReply(contact, rep.getDiversificationTypes());
            }
        }
    }

    // QUERY STORE

    public void sendQueryStore(DHTTransportContact contact, DHTTransportReplyHandler handler, int header_length, List<Object[]> key_details) {
        handler.failed(contact, new Throwable("not implemented"));
    }

    // FIND NODE

    public void sendFindNode(final DHTTransportContact contact, final DHTTransportReplyHandler handler, final byte[] nid) {
        AERunnable runnable = new AERunnable() {
            public void runSupport() {
                sendFindNodeSupport(contact, handler, nid);
            }
        };

        run(runnable);
    }

    public void sendFindNodeSupport(DHTTransportContact contact, DHTTransportReplyHandler handler, byte[] nid) {
        DHTTransportLoopbackImpl target = findTarget(contact.getID());

        stats.findNodeSent(null);

        if (target == null || triggerFailure()) {

            stats.findNodeFailed();

            handler.failed(contact, new Exception("failed"));

        } else {

            stats.findNodeOK();

            DHTTransportContact temp = new DHTTransportLoopbackContactImpl(target, node_id);

            DHTTransportContact[] res = target.getRequestHandler().findNodeRequest(temp, nid);

            contact.setRandomID(temp.getRandomID());

            DHTTransportContact[] trans_res = new DHTTransportContact[res.length];

            for (int i = 0; i < res.length; i++) {

                trans_res[i] = new DHTTransportLoopbackContactImpl(this, res[i].getID());
            }

            handler.findNodeReply(contact, trans_res);
        }
    }

    // FIND VALUE

    public void sendFindValue(final DHTTransportContact contact, final DHTTransportReplyHandler handler, final byte[] key, final int max,
            final short flags) {
        AERunnable runnable = new AERunnable() {
            public void runSupport() {
                sendFindValueSupport(contact, handler, key, max, flags);
            }
        };

        run(runnable);
    }

    public void sendFindValueSupport(DHTTransportContact contact, DHTTransportReplyHandler handler, byte[] key, int max, short flags) {
        DHTTransportLoopbackImpl target = findTarget(contact.getID());

        stats.findValueSent(null);

        if (target == null || triggerFailure()) {

            stats.findValueFailed();

            handler.failed(contact, new Exception("failed"));

        } else {

            stats.findValueOK();

            DHTTransportFindValueReply find_res =
                    target.getRequestHandler().findValueRequest(new DHTTransportLoopbackContactImpl(target, node_id), key, max, flags);

            if (find_res.hit()) {

                handler.findValueReply(contact, find_res.getValues(), find_res.getDiversificationType(), false);

            } else if (find_res.blocked()) {

                handler.keyBlockRequest(contact, find_res.getBlockedKey(), find_res.getBlockedSignature());

                handler.failed(contact, new Throwable("key blocked"));

            } else {

                DHTTransportContact[] res = find_res.getContacts();

                DHTTransportContact[] trans_res = new DHTTransportContact[res.length];

                for (int i = 0; i < res.length; i++) {

                    trans_res[i] = new DHTTransportLoopbackContactImpl(this, res[i].getID());
                }

                handler.findValueReply(contact, trans_res);

            }
        }
    }

    protected boolean triggerFailure() {
        return (Math.random() * 100 < FAIL_PERCENTAGE);
    }

    public void registerTransferHandler(byte[] handler_key, DHTTransportTransferHandler handler) {
    }

    public void registerTransferHandler(byte[] handler_key, DHTTransportTransferHandler handler, Map<String, Object> options) {
    }

    public void unregisterTransferHandler(byte[] handler_key, DHTTransportTransferHandler handler) {
    }

    public byte[] readTransfer(DHTTransportProgressListener listener, DHTTransportContact target, byte[] handler_key, byte[] key, long timeout)

    throws DHTTransportException {
        throw (new DHTTransportException("not imp"));
    }

    public void writeTransfer(DHTTransportProgressListener listener, DHTTransportContact target, byte[] handler_key, byte[] key, byte[] data,
            long timeout)

    throws DHTTransportException {
        throw (new DHTTransportException("not imp"));
    }

    public byte[]
            writeReadTransfer(DHTTransportProgressListener listener, DHTTransportContact target, byte[] handler_key, byte[] data, long timeout)

            throws DHTTransportException {
        throw (new DHTTransportException("not imp"));
    }

    public boolean supportsStorage() {
        return (true);
    }

    public void addListener(DHTTransportListener l) {
        listeners.add(l);
    }

    public void removeListener(DHTTransportListener l) {
        listeners.remove(l);
    }
}
