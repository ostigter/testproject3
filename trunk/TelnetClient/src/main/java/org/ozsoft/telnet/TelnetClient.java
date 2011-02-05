package org.ozsoft.telnet;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * Simple Telnet client library. <br />
 * <br />
 * 
 * Implemented with the JBoss Netty framework (http://www.jboss.org/netty/) as
 * event-driven abstraction for the multithreaded, multiplexed Java NIO socket
 * handling, for robustness, stability and performance.
 * 
 * @author Oscar Stigter
 */
public class TelnetClient {

    private final ChannelFactory channelFactory;

    private final ClientBootstrap bootstrap;
    
    private final Set<TelnetListener> listeners;

    private Channel channel;

    public TelnetClient() {
        // Create factory for client channels based on Java NIO.
        channelFactory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

        // Create a client bootstrap.
        bootstrap = new ClientBootstrap(channelFactory);

        // Setup message handler for string messages.
        ChannelPipeline pipeline = bootstrap.getPipeline();
        pipeline.addLast("TelnetHandler", new TelnetHandler());

        // Configure TCP/IP settings.
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        
        listeners = new HashSet<TelnetListener>();
    }
    
    public void connect(String host, int port) {
        if (channel != null) {
            disconnect();
        }
        bootstrap.connect(new InetSocketAddress(host, port));
    }
    
    public void disconnect() {
        if (channel != null) {
            channel.close();
            channel = null;
        }
    }
    
    public void sendText(String text) {
        if (channel != null) {
            ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
            buffer.writeBytes(text.getBytes());
            channel.write(buffer);
        }
    }
    
    public void addTelnetListener(TelnetListener listener) {
        listeners.add(listener);
    }
    
    public void removeTelnetListener(TelnetListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Netty ChannelHandler for Telnet messages.
     * 
     * @author Oscar Stigter
     */
    private class TelnetHandler extends FrameDecoder {

        private final ChannelBuffer inBuffer = ChannelBuffers.dynamicBuffer();

        private final ChannelBuffer outBuffer = ChannelBuffers.dynamicBuffer();
        
        private final StringBuilder ansiCode = new StringBuilder();
        
        private boolean inAnsi;
        
        private boolean inCommand;
        
        private int command;
        
        private int option;
        
        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
            channel = e.getChannel();
            inBuffer.clear();
            inAnsi = false;
            inCommand = false;
            command = -1;
            option = -1;
            for (TelnetListener listener : listeners) {
                listener.connected();
            }
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
            for (TelnetListener listener : listeners) {
                listener.disconnected();
            }
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
            for (TelnetListener listener : listeners) {
                listener.telnetExceptionCaught(e.getCause());
            }
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
            while (buffer.readable()) {
                int b = buffer.readUnsignedByte();
                if (inCommand) {
                    // Read command and option bytes.
                    if (command == -1) {
                        command = b;
                        if (command == TelnetConstants.GA) {
                            // No option; exit command.
                            handleCommand(command, option);
                            inCommand = false;
                            command = -1;
                        }
                    } else {
                        option = b;
                        handleCommand(command, option);
                        inCommand = false;
                        command = -1;
                        option = -1;
                    }
                } else if (inAnsi) {
                    if (b == '[') {
                        // Skip
                    } else if (Character.isLowerCase(b)) {
                        // End ANSI code.
                        ansiCode.append((char) b);
                        handleAnsiCode();
                        inAnsi = false;
                        ansiCode.delete(0, ansiCode.length());
                    } else {
                        // Collect ANSI code.
                        ansiCode.append((char) b);
                    }
                } else if (b == TelnetConstants.IAC) {
                    // Begin command.
                    inCommand = true;
                } else if (b == TelnetConstants.ESC) {
                    // Begin ASNI code.
                    inAnsi = true;
                } else {
                    // Collect normal text.
                    inBuffer.writeByte((byte) b);
                }
            }

            // Send any normal text to console.
            if (inBuffer.readableBytes() > 0) {
                StringBuilder sb = new StringBuilder();
                while (inBuffer.readable()) {
                    sb.append((char) inBuffer.readByte());
                }
                for (TelnetListener listener : listeners) {
                    listener.textReceived(sb.toString());
                }
            }

            return null;
        }
        
        private void handleAnsiCode() {
            for (TelnetListener listener : listeners) {
                listener.ansiCodeReceived(ansiCode.toString());
            }
        }
    
        private void handleCommand(int command, int option) {
            if (command == TelnetConstants.DO) {
                sendCommand(TelnetConstants.WONT, option);
            }
            if (command == TelnetConstants.WILL) {
                sendCommand(TelnetConstants.DONT, option);
            }
        }

        private void sendCommand(int command, int option) {
            outBuffer.clear();
            outBuffer.writeByte((byte) TelnetConstants.IAC);
            outBuffer.writeByte((byte) command);
            outBuffer.writeByte((byte) option);
            channel.write(outBuffer);
        }

    }

}
