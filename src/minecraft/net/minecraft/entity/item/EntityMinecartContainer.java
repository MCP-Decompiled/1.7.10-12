package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityMinecartContainer extends EntityMinecart implements IInventory
{
    private ItemStack[] minecartContainerItems = new ItemStack[36];
    private boolean dropContentsWhenDead = true;
    private static final String __OBFID = "CL_00001674";

    public EntityMinecartContainer(World p_i1716_1_)
    {
        super(p_i1716_1_);
    }

    public EntityMinecartContainer(World p_i1717_1_, double p_i1717_2_, double p_i1717_4_, double p_i1717_6_)
    {
        super(p_i1717_1_, p_i1717_2_, p_i1717_4_, p_i1717_6_);
    }

    public void killMinecart(DamageSource p_94095_1_)
    {
        super.killMinecart(p_94095_1_);

        for (int var2 = 0; var2 < this.getSizeInventory(); ++var2)
        {
            ItemStack var3 = this.getStackInSlot(var2);

            if (var3 != null)
            {
                float var4 = this.rand.nextFloat() * 0.8F + 0.1F;
                float var5 = this.rand.nextFloat() * 0.8F + 0.1F;
                float var6 = this.rand.nextFloat() * 0.8F + 0.1F;

                while (var3.stackSize > 0)
                {
                    int var7 = this.rand.nextInt(21) + 10;

                    if (var7 > var3.stackSize)
                    {
                        var7 = var3.stackSize;
                    }

                    var3.stackSize -= var7;
                    EntityItem var8 = new EntityItem(this.worldObj, this.posX + (double)var4, this.posY + (double)var5, this.posZ + (double)var6, new ItemStack(var3.getItem(), var7, var3.getMetadata()));
                    float var9 = 0.05F;
                    var8.motionX = (double)((float)this.rand.nextGaussian() * var9);
                    var8.motionY = (double)((float)this.rand.nextGaussian() * var9 + 0.2F);
                    var8.motionZ = (double)((float)this.rand.nextGaussian() * var9);
                    this.worldObj.spawnEntityInWorld(var8);
                }
            }
        }
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return this.minecartContainerItems[slotIn];
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.minecartContainerItems[index] != null)
        {
            ItemStack var3;

            if (this.minecartContainerItems[index].stackSize <= count)
            {
                var3 = this.minecartContainerItems[index];
                this.minecartContainerItems[index] = null;
                return var3;
            }
            else
            {
                var3 = this.minecartContainerItems[index].splitStack(count);

                if (this.minecartContainerItems[index].stackSize == 0)
                {
                    this.minecartContainerItems[index] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.minecartContainerItems[index] != null)
        {
            ItemStack var2 = this.minecartContainerItems[index];
            this.minecartContainerItems[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.minecartContainerItems[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    public void markDirty() {}

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.isDead ? false : player.getDistanceSqToEntity(this) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public String getInventoryName()
    {
        return this.isCustomInventoryName() ? this.func_95999_t() : "container.minecart";
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void travelToDimension(int dimensionId)
    {
        this.dropContentsWhenDead = false;
        super.travelToDimension(dimensionId);
    }

    public void setDead()
    {
        if (this.dropContentsWhenDead)
        {
            for (int var1 = 0; var1 < this.getSizeInventory(); ++var1)
            {
                ItemStack var2 = this.getStackInSlot(var1);

                if (var2 != null)
                {
                    float var3 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var4 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var5 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (var2.stackSize > 0)
                    {
                        int var6 = this.rand.nextInt(21) + 10;

                        if (var6 > var2.stackSize)
                        {
                            var6 = var2.stackSize;
                        }

                        var2.stackSize -= var6;
                        EntityItem var7 = new EntityItem(this.worldObj, this.posX + (double)var3, this.posY + (double)var4, this.posZ + (double)var5, new ItemStack(var2.getItem(), var6, var2.getMetadata()));

                        if (var2.hasTagCompound())
                        {
                            var7.getEntityItem().setTagCompound((NBTTagCompound)var2.getTagCompound().copy());
                        }

                        float var8 = 0.05F;
                        var7.motionX = (double)((float)this.rand.nextGaussian() * var8);
                        var7.motionY = (double)((float)this.rand.nextGaussian() * var8 + 0.2F);
                        var7.motionZ = (double)((float)this.rand.nextGaussian() * var8);
                        this.worldObj.spawnEntityInWorld(var7);
                    }
                }
            }
        }

        super.setDead();
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.minecartContainerItems.length; ++var3)
        {
            if (this.minecartContainerItems[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.minecartContainerItems[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        tagCompound.setTag("Items", var2);
    }

    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        NBTTagList var2 = tagCompund.getTagList("Items", 10);
        this.minecartContainerItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.minecartContainerItems.length)
            {
                this.minecartContainerItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    public boolean interactFirst(EntityPlayer player)
    {
        if (!this.worldObj.isRemote)
        {
            player.displayGUIChest(this);
        }

        return true;
    }

    protected void applyDrag()
    {
        int var1 = 15 - Container.calcRedstoneFromInventory(this);
        float var2 = 0.98F + (float)var1 * 0.001F;
        this.motionX *= (double)var2;
        this.motionY *= 0.0D;
        this.motionZ *= (double)var2;
    }
}
