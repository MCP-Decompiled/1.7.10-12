package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import javax.crypto.SecretKey;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MessageDeserializer;
import net.minecraft.util.MessageDeserializer2;
import net.minecraft.util.MessageSerializer;
import net.minecraft.util.MessageSerializer2;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler
{
    private static final Logger logger = LogManager.getLogger();
    public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
    public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
    public static final Marker logMarkerStat = MarkerManager.getMarker("NETWORK_STAT", logMarkerNetwork);
    public static final AttributeKey attrKeyConnectionState = new AttributeKey("protocol");
    public static final AttributeKey attrKeyReceivable = new AttributeKey("receivable_packets");
    public static final AttributeKey attrKeySendable = new AttributeKey("sendable_packets");
    public static final NioEventLoopGroup eventLoops = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
    public static final NetworkStatistics STATISTICS = new NetworkStatistics();
    private final boolean isClientSide;
    private final Queue receivedPacketsQueue = Queues.newConcurrentLinkedQueue();
    private final Queue outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
    private Channel channel;
    private SocketAddress socketAddress;
    private INetHandler packetListener;
    private EnumConnectionState connectionState;
    private IChatComponent terminationReason;
    private boolean isEncrypted;
    private static final String __OBFID = "CL_00001240";

    public NetworkManager(boolean isClient)
    {
        this.isClientSide = isClient;
    }

    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception
    {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = this.channel.remoteAddress();
        this.setConnectionState(EnumConnectionState.HANDSHAKING);
    }

    public void setConnectionState(EnumConnectionState newState)
    {
        this.connectionState = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).getAndSet(newState);
        this.channel.attr(attrKeyReceivable).set(newState.func_150757_a(this.isClientSide));
        this.channel.attr(attrKeySendable).set(newState.func_150754_b(this.isClientSide));
        this.channel.config().setAutoRead(true);
        logger.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext p_channelInactive_1_)
    {
        this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
    }

    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_)
    {
        ChatComponentTranslation var3;

        if (p_exceptionCaught_2_ instanceof TimeoutException)
        {
            var3 = new ChatComponentTranslation("disconnect.timeout", new Object[0]);
        }
        else
        {
            var3 = new ChatComponentTranslation("disconnect.genericReason", new Object[] {"Internal Exception: " + p_exceptionCaught_2_});
        }

        this.closeChannel(var3);
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_)
    {
        if (this.channel.isOpen())
        {
            if (p_channelRead0_2_.hasPriority())
            {
                p_channelRead0_2_.processPacket(this.packetListener);
            }
            else
            {
                this.receivedPacketsQueue.add(p_channelRead0_2_);
            }
        }
    }

    public void setNetHandler(INetHandler handler)
    {
        Validate.notNull(handler, "packetListener", new Object[0]);
        logger.debug("Set listener of {} to {}", new Object[] {this, handler});
        this.packetListener = handler;
    }

    public void scheduleOutboundPacket(Packet inPacket, GenericFutureListener ... futureListeners)
    {
        if (this.channel != null && this.channel.isOpen())
        {
            this.flushOutboundQueue();
            this.dispatchPacket(inPacket, futureListeners);
        }
        else
        {
            this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(inPacket, futureListeners));
        }
    }

    private void dispatchPacket(final Packet inPacket, final GenericFutureListener[] futureListeners)
    {
        final EnumConnectionState var3 = EnumConnectionState.getFromPacket(inPacket);
        final EnumConnectionState var4 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();

        if (var4 != var3)
        {
            logger.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }

        if (this.channel.eventLoop().inEventLoop())
        {
            if (var3 != var4)
            {
                this.setConnectionState(var3);
            }

            this.channel.writeAndFlush(inPacket).addListeners(futureListeners).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        }
        else
        {
            this.channel.eventLoop().execute(new Runnable()
            {
                private static final String __OBFID = "CL_00001241";
                public void run()
                {
                    if (var3 != var4)
                    {
                        NetworkManager.this.setConnectionState(var3);
                    }

                    NetworkManager.this.channel.writeAndFlush(inPacket).addListeners(futureListeners).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
    }

    private void flushOutboundQueue()
    {
        if (this.channel != null && this.channel.isOpen())
        {
            while (!this.outboundPacketsQueue.isEmpty())
            {
                NetworkManager.InboundHandlerTuplePacketListener var1 = (NetworkManager.InboundHandlerTuplePacketListener)this.outboundPacketsQueue.poll();
                this.dispatchPacket(var1.packet, var1.futureListeners);
            }
        }
    }

    public void processReceivedPackets()
    {
        this.flushOutboundQueue();
        EnumConnectionState var1 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();

        if (this.connectionState != var1)
        {
            if (this.connectionState != null)
            {
                this.packetListener.onConnectionStateTransition(this.connectionState, var1);
            }

            this.connectionState = var1;
        }

        if (this.packetListener != null)
        {
            for (int var2 = 1000; !this.receivedPacketsQueue.isEmpty() && var2 >= 0; --var2)
            {
                Packet var3 = (Packet)this.receivedPacketsQueue.poll();
                var3.processPacket(this.packetListener);
            }

            this.packetListener.onNetworkTick();
        }

        this.channel.flush();
    }

    public SocketAddress getRemoteAddress()
    {
        return this.socketAddress;
    }

    public void closeChannel(IChatComponent message)
    {
        if (this.channel.isOpen())
        {
            this.channel.close();
            this.terminationReason = message;
        }
    }

    public boolean isLocalChannel()
    {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    public static NetworkManager provideLanClient(InetAddress p_150726_0_, int p_150726_1_)
    {
        final NetworkManager var2 = new NetworkManager(true);
        ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(eventLoops)).handler(new ChannelInitializer()
        {
            private static final String __OBFID = "CL_00001242";
            protected void initChannel(Channel p_initChannel_1_)
            {
                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.IP_TOS, Integer.valueOf(24));
                }
                catch (ChannelException var4)
                {
                    ;
                }

                try
                {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(false));
                }
                catch (ChannelException var3)
                {
                    ;
                }

                p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(20)).addLast("splitter", new MessageDeserializer2()).addLast("decoder", new MessageDeserializer(NetworkManager.STATISTICS)).addLast("prepender", new MessageSerializer2()).addLast("encoder", new MessageSerializer(NetworkManager.STATISTICS)).addLast("packet_handler", var2);
            }
        })).channel(NioSocketChannel.class)).connect(p_150726_0_, p_150726_1_).syncUninterruptibly();
        return var2;
    }

    public static NetworkManager provideLocalClient(SocketAddress p_150722_0_)
    {
        final NetworkManager var1 = new NetworkManager(true);
        ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap()).group(eventLoops)).handler(new ChannelInitializer()
        {
            private static final String __OBFID = "CL_00001243";
            protected void initChannel(Channel p_initChannel_1_)
            {
                p_initChannel_1_.pipeline().addLast("packet_handler", var1);
            }
        })).channel(LocalChannel.class)).connect(p_150722_0_).syncUninterruptibly();
        return var1;
    }

    public void enableEncryption(SecretKey key)
    {
        this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.func_151229_a(2, key)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.func_151229_a(1, key)));
        this.isEncrypted = true;
    }

    public boolean isChannelOpen()
    {
        return this.channel != null && this.channel.isOpen();
    }

    public INetHandler getNetHandler()
    {
        return this.packetListener;
    }

    public IChatComponent getExitMessage()
    {
        return this.terminationReason;
    }

    public void disableAutoRead()
    {
        this.channel.config().setAutoRead(false);
    }

    protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_)
    {
        this.channelRead0(p_channelRead0_1_, (Packet)p_channelRead0_2_);
    }

    static class InboundHandlerTuplePacketListener
    {
        private final Packet packet;
        private final GenericFutureListener[] futureListeners;
        private static final String __OBFID = "CL_00001244";

        public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener ... inFutureListeners)
        {
            this.packet = inPacket;
            this.futureListeners = inFutureListeners;
        }
    }
}
