package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityHanging extends Entity
{
    private int tickCounter1;
    public int hangingDirection;
    public int field_146063_b;
    public int field_146064_c;
    public int field_146062_d;
    private static final String __OBFID = "CL_00001546";

    public EntityHanging(World p_i1588_1_)
    {
        super(p_i1588_1_);
        this.yOffset = 0.0F;
        this.setSize(0.5F, 0.5F);
    }

    public EntityHanging(World p_i1589_1_, int p_i1589_2_, int p_i1589_3_, int p_i1589_4_, int p_i1589_5_)
    {
        this(p_i1589_1_);
        this.field_146063_b = p_i1589_2_;
        this.field_146064_c = p_i1589_3_;
        this.field_146062_d = p_i1589_4_;
    }

    protected void entityInit() {}

    public void setDirection(int p_82328_1_)
    {
        this.hangingDirection = p_82328_1_;
        this.prevRotationYaw = this.rotationYaw = (float)(p_82328_1_ * 90);
        float var2 = (float)this.getWidthPixels();
        float var3 = (float)this.getHeightPixels();
        float var4 = (float)this.getWidthPixels();

        if (p_82328_1_ != 2 && p_82328_1_ != 0)
        {
            var2 = 0.5F;
        }
        else
        {
            var4 = 0.5F;
            this.rotationYaw = this.prevRotationYaw = (float)(Direction.rotateOpposite[p_82328_1_] * 90);
        }

        var2 /= 32.0F;
        var3 /= 32.0F;
        var4 /= 32.0F;
        float var5 = (float)this.field_146063_b + 0.5F;
        float var6 = (float)this.field_146064_c + 0.5F;
        float var7 = (float)this.field_146062_d + 0.5F;
        float var8 = 0.5625F;

        if (p_82328_1_ == 2)
        {
            var7 -= var8;
        }

        if (p_82328_1_ == 1)
        {
            var5 -= var8;
        }

        if (p_82328_1_ == 0)
        {
            var7 += var8;
        }

        if (p_82328_1_ == 3)
        {
            var5 += var8;
        }

        if (p_82328_1_ == 2)
        {
            var5 -= this.func_70517_b(this.getWidthPixels());
        }

        if (p_82328_1_ == 1)
        {
            var7 += this.func_70517_b(this.getWidthPixels());
        }

        if (p_82328_1_ == 0)
        {
            var5 += this.func_70517_b(this.getWidthPixels());
        }

        if (p_82328_1_ == 3)
        {
            var7 -= this.func_70517_b(this.getWidthPixels());
        }

        var6 += this.func_70517_b(this.getHeightPixels());
        this.setPosition((double)var5, (double)var6, (double)var7);
        float var9 = -0.03125F;
        this.boundingBox.setBounds((double)(var5 - var2 - var9), (double)(var6 - var3 - var9), (double)(var7 - var4 - var9), (double)(var5 + var2 + var9), (double)(var6 + var3 + var9), (double)(var7 + var4 + var9));
    }

    private float func_70517_b(int p_70517_1_)
    {
        return p_70517_1_ == 32 ? 0.5F : (p_70517_1_ == 64 ? 0.5F : 0.0F);
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.tickCounter1++ == 100 && !this.worldObj.isRemote)
        {
            this.tickCounter1 = 0;

            if (!this.isDead && !this.onValidSurface())
            {
                this.setDead();
                this.onBroken((Entity)null);
            }
        }
    }

    public boolean onValidSurface()
    {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty())
        {
            return false;
        }
        else
        {
            int var1 = Math.max(1, this.getWidthPixels() / 16);
            int var2 = Math.max(1, this.getHeightPixels() / 16);
            int var3 = this.field_146063_b;
            int var4 = this.field_146064_c;
            int var5 = this.field_146062_d;

            if (this.hangingDirection == 2)
            {
                var3 = MathHelper.floor_double(this.posX - (double)((float)this.getWidthPixels() / 32.0F));
            }

            if (this.hangingDirection == 1)
            {
                var5 = MathHelper.floor_double(this.posZ - (double)((float)this.getWidthPixels() / 32.0F));
            }

            if (this.hangingDirection == 0)
            {
                var3 = MathHelper.floor_double(this.posX - (double)((float)this.getWidthPixels() / 32.0F));
            }

            if (this.hangingDirection == 3)
            {
                var5 = MathHelper.floor_double(this.posZ - (double)((float)this.getWidthPixels() / 32.0F));
            }

            var4 = MathHelper.floor_double(this.posY - (double)((float)this.getHeightPixels() / 32.0F));

            for (int var6 = 0; var6 < var1; ++var6)
            {
                for (int var7 = 0; var7 < var2; ++var7)
                {
                    Material var8;

                    if (this.hangingDirection != 2 && this.hangingDirection != 0)
                    {
                        var8 = this.worldObj.getBlock(this.field_146063_b, var4 + var7, var5 + var6).getMaterial();
                    }
                    else
                    {
                        var8 = this.worldObj.getBlock(var3 + var6, var4 + var7, this.field_146062_d).getMaterial();
                    }

                    if (!var8.isSolid())
                    {
                        return false;
                    }
                }
            }

            List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);
            Iterator var10 = var9.iterator();
            Entity var11;

            do
            {
                if (!var10.hasNext())
                {
                    return true;
                }

                var11 = (Entity)var10.next();
            }
            while (!(var11 instanceof EntityHanging));

            return false;
        }
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public boolean hitByEntity(Entity entityIn)
    {
        return entityIn instanceof EntityPlayer ? this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0F) : false;
    }

    public void func_145781_i(int p_145781_1_)
    {
        this.worldObj.func_147450_X();
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.worldObj.isRemote)
            {
                this.setDead();
                this.setBeenAttacked();
                this.onBroken(source.getEntity());
            }

            return true;
        }
    }

    public void moveEntity(double x, double y, double z)
    {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D)
        {
            this.setDead();
            this.onBroken((Entity)null);
        }
    }

    public void addVelocity(double x, double y, double z)
    {
        if (!this.worldObj.isRemote && !this.isDead && x * x + y * y + z * z > 0.0D)
        {
            this.setDead();
            this.onBroken((Entity)null);
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setByte("Direction", (byte)this.hangingDirection);
        tagCompound.setInteger("TileX", this.field_146063_b);
        tagCompound.setInteger("TileY", this.field_146064_c);
        tagCompound.setInteger("TileZ", this.field_146062_d);

        switch (this.hangingDirection)
        {
            case 0:
                tagCompound.setByte("Dir", (byte)2);
                break;

            case 1:
                tagCompound.setByte("Dir", (byte)1);
                break;

            case 2:
                tagCompound.setByte("Dir", (byte)0);
                break;

            case 3:
                tagCompound.setByte("Dir", (byte)3);
        }
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        if (tagCompund.hasKey("Direction", 99))
        {
            this.hangingDirection = tagCompund.getByte("Direction");
        }
        else
        {
            switch (tagCompund.getByte("Dir"))
            {
                case 0:
                    this.hangingDirection = 2;
                    break;

                case 1:
                    this.hangingDirection = 1;
                    break;

                case 2:
                    this.hangingDirection = 0;
                    break;

                case 3:
                    this.hangingDirection = 3;
            }
        }

        this.field_146063_b = tagCompund.getInteger("TileX");
        this.field_146064_c = tagCompund.getInteger("TileY");
        this.field_146062_d = tagCompund.getInteger("TileZ");
        this.setDirection(this.hangingDirection);
    }

    public abstract int getWidthPixels();

    public abstract int getHeightPixels();

    public abstract void onBroken(Entity p_110128_1_);

    protected boolean shouldSetPosAfterLoading()
    {
        return false;
    }
}
