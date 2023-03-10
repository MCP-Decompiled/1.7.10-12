package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory
{
    private final IMerchant theMerchant;
    private ItemStack[] theInventory = new ItemStack[3];
    private final EntityPlayer thePlayer;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;
    private static final String __OBFID = "CL_00001756";

    public InventoryMerchant(EntityPlayer p_i1820_1_, IMerchant p_i1820_2_)
    {
        this.thePlayer = p_i1820_1_;
        this.theMerchant = p_i1820_2_;
    }

    public int getSizeInventory()
    {
        return this.theInventory.length;
    }

    public ItemStack getStackInSlot(int slotIn)
    {
        return this.theInventory[slotIn];
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.theInventory[index] != null)
        {
            ItemStack var3;

            if (index == 2)
            {
                var3 = this.theInventory[index];
                this.theInventory[index] = null;
                return var3;
            }
            else if (this.theInventory[index].stackSize <= count)
            {
                var3 = this.theInventory[index];
                this.theInventory[index] = null;

                if (this.inventoryResetNeededOnSlotChange(index))
                {
                    this.resetRecipeAndSlots();
                }

                return var3;
            }
            else
            {
                var3 = this.theInventory[index].splitStack(count);

                if (this.theInventory[index].stackSize == 0)
                {
                    this.theInventory[index] = null;
                }

                if (this.inventoryResetNeededOnSlotChange(index))
                {
                    this.resetRecipeAndSlots();
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    private boolean inventoryResetNeededOnSlotChange(int p_70469_1_)
    {
        return p_70469_1_ == 0 || p_70469_1_ == 1;
    }

    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (this.theInventory[index] != null)
        {
            ItemStack var2 = this.theInventory[index];
            this.theInventory[index] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.theInventory[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (this.inventoryResetNeededOnSlotChange(index))
        {
            this.resetRecipeAndSlots();
        }
    }

    public String getInventoryName()
    {
        return "mob.villager";
    }

    public boolean isCustomInventoryName()
    {
        return false;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return this.theMerchant.getCustomer() == player;
    }

    public void openChest() {}

    public void closeChest() {}

    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    public void markDirty()
    {
        this.resetRecipeAndSlots();
    }

    public void resetRecipeAndSlots()
    {
        this.currentRecipe = null;
        ItemStack var1 = this.theInventory[0];
        ItemStack var2 = this.theInventory[1];

        if (var1 == null)
        {
            var1 = var2;
            var2 = null;
        }

        if (var1 == null)
        {
            this.setInventorySlotContents(2, (ItemStack)null);
        }
        else
        {
            MerchantRecipeList var3 = this.theMerchant.getRecipes(this.thePlayer);

            if (var3 != null)
            {
                MerchantRecipe var4 = var3.canRecipeBeUsed(var1, var2, this.currentRecipeIndex);

                if (var4 != null && !var4.isRecipeDisabled())
                {
                    this.currentRecipe = var4;
                    this.setInventorySlotContents(2, var4.getItemToSell().copy());
                }
                else if (var2 != null)
                {
                    var4 = var3.canRecipeBeUsed(var2, var1, this.currentRecipeIndex);

                    if (var4 != null && !var4.isRecipeDisabled())
                    {
                        this.currentRecipe = var4;
                        this.setInventorySlotContents(2, var4.getItemToSell().copy());
                    }
                    else
                    {
                        this.setInventorySlotContents(2, (ItemStack)null);
                    }
                }
                else
                {
                    this.setInventorySlotContents(2, (ItemStack)null);
                }
            }
        }

        this.theMerchant.verifySellingItem(this.getStackInSlot(2));
    }

    public MerchantRecipe getCurrentRecipe()
    {
        return this.currentRecipe;
    }

    public void setCurrentRecipeIndex(int p_70471_1_)
    {
        this.currentRecipeIndex = p_70471_1_;
        this.resetRecipeAndSlots();
    }
}
