package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.util.MathHelper;

public class NBTTagFloat extends NBTBase.NBTPrimitive
{
    private float data;
    private static final String __OBFID = "CL_00001220";

    NBTTagFloat() {}

    public NBTTagFloat(float p_i45131_1_)
    {
        this.data = p_i45131_1_;
    }

    void write(DataOutput output) throws IOException
    {
        output.writeFloat(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.addSpaceRead(32L);
        this.data = input.readFloat();
    }

    public byte getId()
    {
        return (byte)5;
    }

    public String toString()
    {
        return "" + this.data + "f";
    }

    public NBTBase copy()
    {
        return new NBTTagFloat(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (super.equals(p_equals_1_))
        {
            NBTTagFloat var2 = (NBTTagFloat)p_equals_1_;
            return this.data == var2.data;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return super.hashCode() ^ Float.floatToIntBits(this.data);
    }

    public long getLong()
    {
        return (long)this.data;
    }

    public int getInt()
    {
        return MathHelper.floor_float(this.data);
    }

    public short getShort()
    {
        return (short)(MathHelper.floor_float(this.data) & 65535);
    }

    public byte getByte()
    {
        return (byte)(MathHelper.floor_float(this.data) & 255);
    }

    public double getDouble()
    {
        return (double)this.data;
    }

    public float getFloat()
    {
        return this.data;
    }
}
