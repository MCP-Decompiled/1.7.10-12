package net.minecraft.entity.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMinecartTNT extends EntityMinecart
{
    private int minecartTNTFuse = -1;
    private static final String __OBFID = "CL_00001680";

    public EntityMinecartTNT(World p_i1727_1_)
    {
        super(p_i1727_1_);
    }

    public EntityMinecartTNT(World p_i1728_1_, double p_i1728_2_, double p_i1728_4_, double p_i1728_6_)
    {
        super(p_i1728_1_, p_i1728_2_, p_i1728_4_, p_i1728_6_);
    }

    public int getMinecartType()
    {
        return 3;
    }

    public Block getDefaultDisplayTile()
    {
        return Blocks.tnt;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (this.minecartTNTFuse > 0)
        {
            --this.minecartTNTFuse;
            this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        else if (this.minecartTNTFuse == 0)
        {
            this.explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
        }

        if (this.isCollidedHorizontally)
        {
            double var1 = this.motionX * this.motionX + this.motionZ * this.motionZ;

            if (var1 >= 0.009999999776482582D)
            {
                this.explodeCart(var1);
            }
        }
    }

    public void killMinecart(DamageSource p_94095_1_)
    {
        super.killMinecart(p_94095_1_);
        double var2 = this.motionX * this.motionX + this.motionZ * this.motionZ;

        if (!p_94095_1_.isExplosion())
        {
            this.entityDropItem(new ItemStack(Blocks.tnt, 1), 0.0F);
        }

        if (p_94095_1_.isFireDamage() || p_94095_1_.isExplosion() || var2 >= 0.009999999776482582D)
        {
            this.explodeCart(var2);
        }
    }

    protected void explodeCart(double p_94103_1_)
    {
        if (!this.worldObj.isRemote)
        {
            double var3 = Math.sqrt(p_94103_1_);

            if (var3 > 5.0D)
            {
                var3 = 5.0D;
            }

            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float)(4.0D + this.rand.nextDouble() * 1.5D * var3), true);
            this.setDead();
        }
    }

    protected void fall(float distance)
    {
        if (distance >= 3.0F)
        {
            float var2 = distance / 10.0F;
            this.explodeCart((double)(var2 * var2));
        }

        super.fall(distance);
    }

    public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_)
    {
        if (p_96095_4_ && this.minecartTNTFuse < 0)
        {
            this.ignite();
        }
    }

    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 10)
        {
            this.ignite();
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public void ignite()
    {
        this.minecartTNTFuse = 80;

        if (!this.worldObj.isRemote)
        {
            this.worldObj.setEntityState(this, (byte)10);
            this.worldObj.playSoundAtEntity(this, "game.tnt.primed", 1.0F, 1.0F);
        }
    }

    public int func_94104_d()
    {
        return this.minecartTNTFuse;
    }

    public boolean isIgnited()
    {
        return this.minecartTNTFuse > -1;
    }

    public float getExplosionResistance(Explosion explosionIn, World worldIn, int x, int y, int z, Block blockIn)
    {
        return this.isIgnited() && (BlockRailBase.isRailBlock(blockIn) || BlockRailBase.isRailBlockAt(worldIn, x, y + 1, z)) ? 0.0F : super.getExplosionResistance(explosionIn, worldIn, x, y, z, blockIn);
    }

    public boolean func_145774_a(Explosion explosionIn, World worldIn, int x, int y, int z, Block blockIn, float unused)
    {
        return this.isIgnited() && (BlockRailBase.isRailBlock(blockIn) || BlockRailBase.isRailBlockAt(worldIn, x, y + 1, z)) ? false : super.func_145774_a(explosionIn, worldIn, x, y, z, blockIn, unused);
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);

        if (tagCompund.hasKey("TNTFuse", 99))
        {
            this.minecartTNTFuse = tagCompund.getInteger("TNTFuse");
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("TNTFuse", this.minecartTNTFuse);
    }
}
