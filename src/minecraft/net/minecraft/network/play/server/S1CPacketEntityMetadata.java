package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S1CPacketEntityMetadata extends Packet
{
    private int field_149379_a;
    private List field_149378_b;
    private static final String __OBFID = "CL_00001326";

    public S1CPacketEntityMetadata() {}

    public S1CPacketEntityMetadata(int p_i45217_1_, DataWatcher p_i45217_2_, boolean p_i45217_3_)
    {
        this.field_149379_a = p_i45217_1_;

        if (p_i45217_3_)
        {
            this.field_149378_b = p_i45217_2_.getAllWatched();
        }
        else
        {
            this.field_149378_b = p_i45217_2_.getChanged();
        }
    }

    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149379_a = data.readInt();
        this.field_149378_b = DataWatcher.readWatchedListFromPacketBuffer(data);
    }

    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeInt(this.field_149379_a);
        DataWatcher.writeWatchedListToPacketBuffer(this.field_149378_b, data);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityMetadata(this);
    }

    public List func_149376_c()
    {
        return this.field_149378_b;
    }

    public int func_149375_d()
    {
        return this.field_149379_a;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
