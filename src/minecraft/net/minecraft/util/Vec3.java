package net.minecraft.util;

public class Vec3
{
    public double xCoord;
    public double yCoord;
    public double zCoord;
    private static final String __OBFID = "CL_00000612";

    public static Vec3 createVectorHelper(double x, double y, double z)
    {
        return new Vec3(x, y, z);
    }

    protected Vec3(double x, double y, double z)
    {
        if (x == -0.0D)
        {
            x = 0.0D;
        }

        if (y == -0.0D)
        {
            y = 0.0D;
        }

        if (z == -0.0D)
        {
            z = 0.0D;
        }

        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    protected Vec3 setComponents(double x, double y, double z)
    {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        return this;
    }

    public Vec3 subtract(Vec3 vec)
    {
        return createVectorHelper(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    public Vec3 normalize()
    {
        double var1 = (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return var1 < 1.0E-4D ? createVectorHelper(0.0D, 0.0D, 0.0D) : createVectorHelper(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
    }

    public double dotProduct(Vec3 vec)
    {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }

    public Vec3 crossProduct(Vec3 vec)
    {
        return createVectorHelper(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public Vec3 addVector(double x, double y, double z)
    {
        return createVectorHelper(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    public double distanceTo(Vec3 vec)
    {
        double var2 = vec.xCoord - this.xCoord;
        double var4 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        return (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
    }

    public double squareDistanceTo(Vec3 vec)
    {
        double var2 = vec.xCoord - this.xCoord;
        double var4 = vec.yCoord - this.yCoord;
        double var6 = vec.zCoord - this.zCoord;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    public double squareDistanceTo(double x, double y, double z)
    {
        double var7 = x - this.xCoord;
        double var9 = y - this.yCoord;
        double var11 = z - this.zCoord;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double lengthVector()
    {
        return (double)MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    public Vec3 getIntermediateWithXValue(Vec3 vec, double x)
    {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;

        if (var4 * var4 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double var10 = (x - this.xCoord) / var4;
            return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public Vec3 getIntermediateWithYValue(Vec3 vec, double y)
    {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;

        if (var6 * var6 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double var10 = (y - this.yCoord) / var6;
            return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public Vec3 getIntermediateWithZValue(Vec3 vec, double z)
    {
        double var4 = vec.xCoord - this.xCoord;
        double var6 = vec.yCoord - this.yCoord;
        double var8 = vec.zCoord - this.zCoord;

        if (var8 * var8 < 1.0000000116860974E-7D)
        {
            return null;
        }
        else
        {
            double var10 = (z - this.zCoord) / var8;
            return var10 >= 0.0D && var10 <= 1.0D ? createVectorHelper(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10) : null;
        }
    }

    public String toString()
    {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public void rotateAroundX(float angle)
    {
        float var2 = MathHelper.cos(angle);
        float var3 = MathHelper.sin(angle);
        double var4 = this.xCoord;
        double var6 = this.yCoord * (double)var2 + this.zCoord * (double)var3;
        double var8 = this.zCoord * (double)var2 - this.yCoord * (double)var3;
        this.setComponents(var4, var6, var8);
    }

    public void rotateAroundY(float angle)
    {
        float var2 = MathHelper.cos(angle);
        float var3 = MathHelper.sin(angle);
        double var4 = this.xCoord * (double)var2 + this.zCoord * (double)var3;
        double var6 = this.yCoord;
        double var8 = this.zCoord * (double)var2 - this.xCoord * (double)var3;
        this.setComponents(var4, var6, var8);
    }

    public void rotateAroundZ(float angle)
    {
        float var2 = MathHelper.cos(angle);
        float var3 = MathHelper.sin(angle);
        double var4 = this.xCoord * (double)var2 + this.yCoord * (double)var3;
        double var6 = this.yCoord * (double)var2 - this.xCoord * (double)var3;
        double var8 = this.zCoord;
        this.setComponents(var4, var6, var8);
    }
}
