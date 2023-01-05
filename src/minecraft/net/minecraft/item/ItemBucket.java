package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemBucket extends Item
{
    private Block isFull;
    private static final String __OBFID = "CL_00000000";

    public ItemBucket(Block p_i45331_1_)
    {
        this.maxStackSize = 1;
        this.isFull = p_i45331_1_;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        boolean var4 = this.isFull == Blocks.air;
        MovingObjectPosition var5 = this.getMovingObjectPositionFromPlayer(worldIn, player, var4);

        if (var5 == null)
        {
            return itemStackIn;
        }
        else
        {
            if (var5.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int var6 = var5.blockX;
                int var7 = var5.blockY;
                int var8 = var5.blockZ;

                if (!worldIn.canMineBlock(player, var6, var7, var8))
                {
                    return itemStackIn;
                }

                if (var4)
                {
                    if (!player.canPlayerEdit(var6, var7, var8, var5.sideHit, itemStackIn))
                    {
                        return itemStackIn;
                    }

                    Material var9 = worldIn.getBlock(var6, var7, var8).getMaterial();
                    int var10 = worldIn.getBlockMetadata(var6, var7, var8);

                    if (var9 == Material.water && var10 == 0)
                    {
                        worldIn.setBlockToAir(var6, var7, var8);
                        return this.func_150910_a(itemStackIn, player, Items.water_bucket);
                    }

                    if (var9 == Material.lava && var10 == 0)
                    {
                        worldIn.setBlockToAir(var6, var7, var8);
                        return this.func_150910_a(itemStackIn, player, Items.lava_bucket);
                    }
                }
                else
                {
                    if (this.isFull == Blocks.air)
                    {
                        return new ItemStack(Items.bucket);
                    }

                    if (var5.sideHit == 0)
                    {
                        --var7;
                    }

                    if (var5.sideHit == 1)
                    {
                        ++var7;
                    }

                    if (var5.sideHit == 2)
                    {
                        --var8;
                    }

                    if (var5.sideHit == 3)
                    {
                        ++var8;
                    }

                    if (var5.sideHit == 4)
                    {
                        --var6;
                    }

                    if (var5.sideHit == 5)
                    {
                        ++var6;
                    }

                    if (!player.canPlayerEdit(var6, var7, var8, var5.sideHit, itemStackIn))
                    {
                        return itemStackIn;
                    }

                    if (this.tryPlaceContainedLiquid(worldIn, var6, var7, var8) && !player.capabilities.isCreativeMode)
                    {
                        return new ItemStack(Items.bucket);
                    }
                }
            }

            return itemStackIn;
        }
    }

    private ItemStack func_150910_a(ItemStack p_150910_1_, EntityPlayer p_150910_2_, Item p_150910_3_)
    {
        if (p_150910_2_.capabilities.isCreativeMode)
        {
            return p_150910_1_;
        }
        else if (--p_150910_1_.stackSize <= 0)
        {
            return new ItemStack(p_150910_3_);
        }
        else
        {
            if (!p_150910_2_.inventory.addItemStackToInventory(new ItemStack(p_150910_3_)))
            {
                p_150910_2_.dropPlayerItemWithRandomChoice(new ItemStack(p_150910_3_, 1, 0), false);
            }

            return p_150910_1_;
        }
    }

    public boolean tryPlaceContainedLiquid(World p_77875_1_, int p_77875_2_, int p_77875_3_, int p_77875_4_)
    {
        if (this.isFull == Blocks.air)
        {
            return false;
        }
        else
        {
            Material var5 = p_77875_1_.getBlock(p_77875_2_, p_77875_3_, p_77875_4_).getMaterial();
            boolean var6 = !var5.isSolid();

            if (!p_77875_1_.isAirBlock(p_77875_2_, p_77875_3_, p_77875_4_) && !var6)
            {
                return false;
            }
            else
            {
                if (p_77875_1_.provider.isHellWorld && this.isFull == Blocks.flowing_water)
                {
                    p_77875_1_.playSoundEffect((double)((float)p_77875_2_ + 0.5F), (double)((float)p_77875_3_ + 0.5F), (double)((float)p_77875_4_ + 0.5F), "random.fizz", 0.5F, 2.6F + (p_77875_1_.rand.nextFloat() - p_77875_1_.rand.nextFloat()) * 0.8F);

                    for (int var7 = 0; var7 < 8; ++var7)
                    {
                        p_77875_1_.spawnParticle("largesmoke", (double)p_77875_2_ + Math.random(), (double)p_77875_3_ + Math.random(), (double)p_77875_4_ + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                }
                else
                {
                    if (!p_77875_1_.isRemote && var6 && !var5.isLiquid())
                    {
                        p_77875_1_.breakBlock(p_77875_2_, p_77875_3_, p_77875_4_, true);
                    }

                    p_77875_1_.setBlock(p_77875_2_, p_77875_3_, p_77875_4_, this.isFull, 0, 3);
                }

                return true;
            }
        }
    }
}
