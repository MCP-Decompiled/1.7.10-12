package net.minecraft.network;

import com.google.common.collect.BiMap;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Packet
{
    private static final Logger logger = LogManager.getLogger();
    private static final String __OBFID = "CL_00001272";

    public static Packet generatePacket(BiMap protocolMap, int packetId)
    {
        try
        {
            Class var2 = (Class)protocolMap.get(Integer.valueOf(packetId));
            return var2 == null ? null : (Packet)var2.newInstance();
        }
        catch (Exception var3)
        {
            logger.error("Couldn\'t create packet " + packetId, var3);
            return null;
        }
    }

    public static void writeBlob(ByteBuf buffer, byte[] blob)
    {
        buffer.writeShort(blob.length);
        buffer.writeBytes(blob);
    }

    public static byte[] readBlob(ByteBuf buffer) throws IOException
    {
        short var1 = buffer.readShort();

        if (var1 < 0)
        {
            throw new IOException("Key was smaller than nothing!  Weird key!");
        }
        else
        {
            byte[] var2 = new byte[var1];
            buffer.readBytes(var2);
            return var2;
        }
    }

    public abstract void readPacketData(PacketBuffer data) throws IOException;

    public abstract void writePacketData(PacketBuffer data) throws IOException;

    public abstract void processPacket(INetHandler handler);

    public boolean hasPriority()
    {
        return false;
    }

    public String toString()
    {
        return this.getClass().getSimpleName();
    }

    public String serialize()
    {
        return "";
    }
}
