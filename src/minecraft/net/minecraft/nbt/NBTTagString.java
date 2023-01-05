package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase
{
    private String data;
    private static final String __OBFID = "CL_00001228";

    public NBTTagString()
    {
        this.data = "";
    }

    public NBTTagString(String p_i1389_1_)
    {
        this.data = p_i1389_1_;

        if (p_i1389_1_ == null)
        {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    void write(DataOutput output) throws IOException
    {
        output.writeUTF(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        this.data = input.readUTF();
        sizeTracker.addSpaceRead((long)(16 * this.data.length()));
    }

    public byte getId()
    {
        return (byte)8;
    }

    public String toString()
    {
        return "\"" + this.data + "\"";
    }

    public NBTBase copy()
    {
        return new NBTTagString(this.data);
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!super.equals(p_equals_1_))
        {
            return false;
        }
        else
        {
            NBTTagString var2 = (NBTTagString)p_equals_1_;
            return this.data == null && var2.data == null || this.data != null && this.data.equals(var2.data);
        }
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.data.hashCode();
    }

    public String getString()
    {
        return this.data;
    }
}
