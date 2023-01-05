package net.minecraft.network.play.server;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3FPacketCustomPayload extends Packet
{
    private String channel;
    private byte[] data;
    private static final String __OBFID = "CL_00001297";

    public S3FPacketCustomPayload() {}

    public S3FPacketCustomPayload(String channelName, ByteBuf dataIn)
    {
        this(channelName, dataIn.array());
    }

    public S3FPacketCustomPayload(String channelName, byte[] dataIn)
    {
        this.channel = channelName;
        this.data = dataIn;

        if (dataIn.length >= 1048576)
        {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.channel = data.readStringFromBuffer(20);
        this.data = new byte[data.readUnsignedShort()];
        data.readBytes(this.data);
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.channel);
        data.writeShort(this.data.length);
        data.writeBytes(this.data);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCustomPayload(this);
    }

    public String func_149169_c()
    {
        return this.channel;
    }

    public byte[] func_149168_d()
    {
        return this.data;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
