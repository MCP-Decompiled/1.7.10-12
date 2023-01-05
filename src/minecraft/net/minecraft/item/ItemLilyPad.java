package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemLilyPad extends ItemColored
{
    private static final String __OBFID = "CL_00000074";

    public ItemLilyPad(Block p_i45357_1_)
    {
        super(p_i45357_1_, false);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(worldIn, player, true);

        if (var4 == null)
        {
            return itemStackIn;
        }
        else
        {
            if (var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int var5 = var4.blockX;
                int var6 = var4.blockY;
                int var7 = var4.blockZ;

                if (!worldIn.canMineBlock(player, var5, var6, var7))
                {
                    return itemStackIn;
                }

                if (!player.canPlayerEdit(var5, var6, var7, var4.sideHit, itemStackIn))
                {
                    return itemStackIn;
                }

                if (worldIn.getBlock(var5, var6, var7).getMaterial() == Material.water && worldIn.getBlockMetadata(var5, var6, var7) == 0 && worldIn.isAirBlock(var5, var6 + 1, var7))
                {
                    worldIn.setBlock(var5, var6 + 1, var7, Blocks.waterlily);

                    if (!player.capabilities.isCreativeMode)
                    {
                        --itemStackIn.stackSize;
                    }
                }
            }

            return itemStackIn;
        }
    }

    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        return Blocks.waterlily.getRenderColor(p_82790_1_.getMetadata());
    }
}
