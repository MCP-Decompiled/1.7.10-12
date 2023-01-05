package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class ItemShears extends Item
{
    private static final String __OBFID = "CL_00000062";

    public ItemShears()
    {
        this.setMaxStackSize(1);
        this.setMaxDurability(238);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_)
    {
        if (blockIn.getMaterial() != Material.leaves && blockIn != Blocks.web && blockIn != Blocks.tallgrass && blockIn != Blocks.vine && blockIn != Blocks.tripwire)
        {
            return super.onBlockDestroyed(stack, worldIn, blockIn, p_150894_4_, p_150894_5_, p_150894_6_, p_150894_7_);
        }
        else
        {
            stack.damageItem(1, p_150894_7_);
            return true;
        }
    }

    public boolean canItemHarvestBlock(Block p_150897_1_)
    {
        return p_150897_1_ == Blocks.web || p_150897_1_ == Blocks.redstone_wire || p_150897_1_ == Blocks.tripwire;
    }

    public float getStrVsBlock(ItemStack p_150893_1_, Block p_150893_2_)
    {
        return p_150893_2_ != Blocks.web && p_150893_2_.getMaterial() != Material.leaves ? (p_150893_2_ == Blocks.wool ? 5.0F : super.getStrVsBlock(p_150893_1_, p_150893_2_)) : 15.0F;
    }
}
