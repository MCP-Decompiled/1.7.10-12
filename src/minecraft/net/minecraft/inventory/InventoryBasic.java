package net.minecraft.inventory;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryBasic implements IInventory
{
    private String inventoryTitle;
    private int slotsCount;
    private ItemStack[] inventoryContents;
    private List field_70480_d;
    private boolean hasCustomName;
    private static final String __OBFID = "CL_00001514";

    public InventoryBasic(String p_i1561_1_, boolean p_i1561_2_, int p_i1561_3_)
    {
        this.inventoryTitle = p_i1561_1_;
        this.hasCustomName = p_i1561_2_;
        this.slotsCount = p_i1561_3_;
        this.inventoryContents = new ItemStack[p_i1561_3_];
    }

    public void func_110134_a(IInvBasic p_110134_1_)
    {
        if (this.field_70480_d == null)
        {
            this.field_70480_d = new ArrayList();
        }

        this.field_70480_d.add(p_110134_1_);
    }

    public void func_110132_b(IInvBasic p_110132_1_)
    {
        this.field_70480_d.remove(p_110132_1_);
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return slotIn >= 0 && slotIn < this.inventoryContents.length ? this.inventoryContents[slotIn] : null;
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.inventoryContents[index] != null)
        {
            ItemStack var3;

            if (this.inventoryContents[index].stackSize <= count)
            {
                var3 = this.inventoryContents[index];
                this.inventoryContents[index] = null;
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = this.inventoryContents[index].splitStack(count);

                if (this.inventoryContents[index].stackSize == 0)
                {
                    this.inventoryContents[index] = null;
                }

                this.markDirty();
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
        if (this.inventoryContents[index] != null)
        {
            ItemStack var2 = this.inventoryContents[index];
            this.inventoryContents[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.inventoryContents[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        this.markDirty();
    }

    public int getSizeInventory()
    {
        return this.slotsCount;
    }

    public String getInventoryName()
    {
        return this.inventoryTitle;
    }

    public boolean isCustomInventoryName()
    {
        return this.hasCustomName;
    }

    public void func_110133_a(String p_110133_1_)
    {
        this.hasCustomName = true;
        this.inventoryTitle = p_110133_1_;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void markDirty()
    {
        if (this.field_70480_d != null)
        {
            for (int var1 = 0; var1 < this.field_70480_d.size(); ++var1)
            {
                ((IInvBasic)this.field_70480_d.get(var1)).onInventoryChanged(this);
            }
        }
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }
}
