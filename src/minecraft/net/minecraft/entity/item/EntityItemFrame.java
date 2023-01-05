package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class EntityItemFrame extends EntityHanging
{
    private float itemDropChance = 1.0F;
    private static final String __OBFID = "CL_00001547";

    public EntityItemFrame(World p_i1590_1_)
    {
        super(p_i1590_1_);
    }

    public EntityItemFrame(World p_i1591_1_, int p_i1591_2_, int p_i1591_3_, int p_i1591_4_, int p_i1591_5_)
    {
        super(p_i1591_1_, p_i1591_2_, p_i1591_3_, p_i1591_4_, p_i1591_5_);
        this.setDirection(p_i1591_5_);
    }

    protected void entityInit()
    {
        this.getDataWatcher().addObjectByDataType(2, 5);
        this.getDataWatcher().addObject(3, Byte.valueOf((byte)0));
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (this.getDisplayedItem() != null)
        {
            if (!this.worldObj.isRemote)
            {
                this.func_146065_b(source.getEntity(), false);
                this.setDisplayedItem((ItemStack)null);
            }

            return true;
        }
        else
        {
            return super.attackEntityFrom(source, amount);
        }
    }

    public int getWidthPixels()
    {
        return 9;
    }

    public int getHeightPixels()
    {
        return 9;
    }

    public boolean isInRangeToRenderDist(double distance)
    {
        double var3 = 16.0D;
        var3 *= 64.0D * this.renderDistanceWeight;
        return distance < var3 * var3;
    }

    public void onBroken(Entity p_110128_1_)
    {
        this.func_146065_b(p_110128_1_, true);
    }

    public void func_146065_b(Entity p_146065_1_, boolean p_146065_2_)
    {
        ItemStack var3 = this.getDisplayedItem();

        if (p_146065_1_ instanceof EntityPlayer)
        {
            EntityPlayer var4 = (EntityPlayer)p_146065_1_;

            if (var4.capabilities.isCreativeMode)
            {
                this.removeFrameFromMap(var3);
                return;
            }
        }

        if (p_146065_2_)
        {
            this.entityDropItem(new ItemStack(Items.item_frame), 0.0F);
        }

        if (var3 != null && this.rand.nextFloat() < this.itemDropChance)
        {
            var3 = var3.copy();
            this.removeFrameFromMap(var3);
            this.entityDropItem(var3, 0.0F);
        }
    }

    private void removeFrameFromMap(ItemStack p_110131_1_)
    {
        if (p_110131_1_ != null)
        {
            if (p_110131_1_.getItem() == Items.filled_map)
            {
                MapData var2 = ((ItemMap)p_110131_1_.getItem()).getMapData(p_110131_1_, this.worldObj);
                var2.playersVisibleOnMap.remove("frame-" + this.getEntityId());
            }

            p_110131_1_.setItemFrame((EntityItemFrame)null);
        }
    }

    public ItemStack getDisplayedItem()
    {
        return this.getDataWatcher().getWatchableObjectItemStack(2);
    }

    public void setDisplayedItem(ItemStack p_82334_1_)
    {
        if (p_82334_1_ != null)
        {
            p_82334_1_ = p_82334_1_.copy();
            p_82334_1_.stackSize = 1;
            p_82334_1_.setItemFrame(this);
        }

        this.getDataWatcher().updateObject(2, p_82334_1_);
        this.getDataWatcher().setObjectWatched(2);
    }

    public int getRotation()
    {
        return this.getDataWatcher().getWatchableObjectByte(3);
    }

    public void setItemRotation(int p_82336_1_)
    {
        this.getDataWatcher().updateObject(3, Byte.valueOf((byte)(p_82336_1_ % 4)));
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        if (this.getDisplayedItem() != null)
        {
            tagCompound.setTag("Item", this.getDisplayedItem().writeToNBT(new NBTTagCompound()));
            tagCompound.setByte("ItemRotation", (byte)this.getRotation());
            tagCompound.setFloat("ItemDropChance", this.itemDropChance);
        }

        super.writeEntityToNBT(tagCompound);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        NBTTagCompound var2 = tagCompund.getCompoundTag("Item");

        if (var2 != null && !var2.hasNoTags())
        {
            this.setDisplayedItem(ItemStack.loadItemStackFromNBT(var2));
            this.setItemRotation(tagCompund.getByte("ItemRotation"));

            if (tagCompund.hasKey("ItemDropChance", 99))
            {
                this.itemDropChance = tagCompund.getFloat("ItemDropChance");
            }
        }

        super.readEntityFromNBT(tagCompund);
    }

    public boolean interactFirst(EntityPlayer player)
    {
        if (this.getDisplayedItem() == null)
        {
            ItemStack var2 = player.getHeldItem();

            if (var2 != null && !this.worldObj.isRemote)
            {
                this.setDisplayedItem(var2);

                if (!player.capabilities.isCreativeMode && --var2.stackSize <= 0)
                {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                }
            }
        }
        else if (!this.worldObj.isRemote)
        {
            this.setItemRotation(this.getRotation() + 1);
        }

        return true;
    }
}
