package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionHelper;

public class TileEntityBrewingStand extends TileEntity implements ISidedInventory
{
    private static final int[] inputSlots = new int[] {3};
    private static final int[] outputSlots = new int[] {0, 1, 2};
    private ItemStack[] brewingItemStacks = new ItemStack[4];
    private int brewTime;
    private int filledSlots;
    private Item ingredientID;
    private String field_145942_n;
    private static final String __OBFID = "CL_00000345";

    public String getInventoryName()
    {
        return this.isCustomInventoryName() ? this.field_145942_n : "container.brewing";
    }

    public boolean isCustomInventoryName()
    {
        return this.field_145942_n != null && this.field_145942_n.length() > 0;
    }

    public void func_145937_a(String p_145937_1_)
    {
        this.field_145942_n = p_145937_1_;
    }

    public int getSizeInventory()
    {
        return this.brewingItemStacks.length;
    }

    public void updateEntity()
    {
        if (this.brewTime > 0)
        {
            --this.brewTime;

            if (this.brewTime == 0)
            {
                this.brewPotions();
                this.markDirty();
            }
            else if (!this.canBrew())
            {
                this.brewTime = 0;
                this.markDirty();
            }
            else if (this.ingredientID != this.brewingItemStacks[3].getItem())
            {
                this.brewTime = 0;
                this.markDirty();
            }
        }
        else if (this.canBrew())
        {
            this.brewTime = 400;
            this.ingredientID = this.brewingItemStacks[3].getItem();
        }

        int var1 = this.getFilledSlots();

        if (var1 != this.filledSlots)
        {
            this.filledSlots = var1;
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, var1, 2);
        }

        super.updateEntity();
    }

    public int getBrewTime()
    {
        return this.brewTime;
    }

    private boolean canBrew()
    {
        if (this.brewingItemStacks[3] != null && this.brewingItemStacks[3].stackSize > 0)
        {
            ItemStack var1 = this.brewingItemStacks[3];

            if (!var1.getItem().isPotionIngredient(var1))
            {
                return false;
            }
            else
            {
                boolean var2 = false;

                for (int var3 = 0; var3 < 3; ++var3)
                {
                    if (this.brewingItemStacks[var3] != null && this.brewingItemStacks[var3].getItem() == Items.potionitem)
                    {
                        int var4 = this.brewingItemStacks[var3].getMetadata();
                        int var5 = this.func_145936_c(var4, var1);

                        if (!ItemPotion.isSplash(var4) && ItemPotion.isSplash(var5))
                        {
                            var2 = true;
                            break;
                        }

                        List var6 = Items.potionitem.getEffects(var4);
                        List var7 = Items.potionitem.getEffects(var5);

                        if ((var4 <= 0 || var6 != var7) && (var6 == null || !var6.equals(var7) && var7 != null) && var4 != var5)
                        {
                            var2 = true;
                            break;
                        }
                    }
                }

                return var2;
            }
        }
        else
        {
            return false;
        }
    }

    private void brewPotions()
    {
        if (this.canBrew())
        {
            ItemStack var1 = this.brewingItemStacks[3];

            for (int var2 = 0; var2 < 3; ++var2)
            {
                if (this.brewingItemStacks[var2] != null && this.brewingItemStacks[var2].getItem() == Items.potionitem)
                {
                    int var3 = this.brewingItemStacks[var2].getMetadata();
                    int var4 = this.func_145936_c(var3, var1);
                    List var5 = Items.potionitem.getEffects(var3);
                    List var6 = Items.potionitem.getEffects(var4);

                    if ((var3 <= 0 || var5 != var6) && (var5 == null || !var5.equals(var6) && var6 != null))
                    {
                        if (var3 != var4)
                        {
                            this.brewingItemStacks[var2].setMetadata(var4);
                        }
                    }
                    else if (!ItemPotion.isSplash(var3) && ItemPotion.isSplash(var4))
                    {
                        this.brewingItemStacks[var2].setMetadata(var4);
                    }
                }
            }

            if (var1.getItem().hasContainerItem())
            {
                this.brewingItemStacks[3] = new ItemStack(var1.getItem().getContainerItem());
            }
            else
            {
                --this.brewingItemStacks[3].stackSize;

                if (this.brewingItemStacks[3].stackSize <= 0)
                {
                    this.brewingItemStacks[3] = null;
                }
            }
        }
    }

    private int func_145936_c(int p_145936_1_, ItemStack p_145936_2_)
    {
        return p_145936_2_ == null ? p_145936_1_ : (p_145936_2_.getItem().isPotionIngredient(p_145936_2_) ? PotionHelper.applyIngredient(p_145936_1_, p_145936_2_.getItem().getPotionEffect(p_145936_2_)) : p_145936_1_);
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList var2 = compound.getTagList("Items", 10);
        this.brewingItemStacks = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.brewingItemStacks.length)
            {
                this.brewingItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.brewTime = compound.getShort("BrewTime");

        if (compound.hasKey("CustomName", 8))
        {
            this.field_145942_n = compound.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("BrewTime", (short)this.brewTime);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.brewingItemStacks.length; ++var3)
        {
            if (this.brewingItemStacks[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.brewingItemStacks[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        compound.setTag("Items", var2);

        if (this.isCustomInventoryName())
        {
            compound.setString("CustomName", this.field_145942_n);
        }
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return slotIn >= 0 && slotIn < this.brewingItemStacks.length ? this.brewingItemStacks[slotIn] : null;
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (index >= 0 && index < this.brewingItemStacks.length)
        {
            ItemStack var3 = this.brewingItemStacks[index];
            this.brewingItemStacks[index] = null;
            return var3;
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (index >= 0 && index < this.brewingItemStacks.length)
        {
            ItemStack var2 = this.brewingItemStacks[index];
            this.brewingItemStacks[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index >= 0 && index < this.brewingItemStacks.length)
        {
            this.brewingItemStacks[index] = stack;
        }
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == 3 ? stack.getItem().isPotionIngredient(stack) : stack.getItem() == Items.potionitem || stack.getItem() == Items.glass_bottle;
    }

    public void setBrewTime(int p_145938_1_)
    {
        this.brewTime = p_145938_1_;
    }

    public int getFilledSlots()
    {
        int var1 = 0;

        for (int var2 = 0; var2 < 3; ++var2)
        {
            if (this.brewingItemStacks[var2] != null)
            {
                var1 |= 1 << var2;
            }
        }

        return var1;
    }

    public int[] getSlotsForFace(int p_94128_1_)
    {
        return p_94128_1_ == 1 ? inputSlots : outputSlots;
    }

    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_)
    {
        return this.isItemValidForSlot(p_102007_1_, p_102007_2_);
    }

    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_)
    {
        return true;
    }
}
