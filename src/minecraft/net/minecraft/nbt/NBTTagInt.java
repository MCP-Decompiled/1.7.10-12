package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTBase.NBTPrimitive
{
    private int data;
    private static final String __OBFID = "CL_00001223";

    NBTTagInt() {}

    public NBTTagInt(int p_i45133_1_)
    {
        this.data = p_i45133_1_;
    }

    void write(DataOutput output) throws IOException
    {
        output.writeInt(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.addSpaceRead(32L);
        this.data = input.readInt();
    }

    public byte getId()
    {
        return (byte)3;
    }

    public String toString()
    {
        return "" + this.data;
    }

    public NBTBase copy()
    {
        return new NBTTagInt(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (super.equals(p_equals_1_))
        {
            NBTTagInt var2 = (NBTTagInt)p_equals_1_;
            return this.data == var2.data;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.data;
    }

    public long getLong()
    {
        return (long)this.data;
    }

    public int getInt()
    {
        return this.data;
    }

    public short getShort()
    {
        return (short)(this.data & 65535);
    }

    public byte getByte()
    {
        return (byte)(this.data & 255);
    }

    public double getDouble()
    {
        return (double)this.data;
    }

    public float getFloat()
    {
        return (float)this.data;
    }
}
