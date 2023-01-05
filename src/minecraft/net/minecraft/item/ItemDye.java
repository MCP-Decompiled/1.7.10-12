package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockLog;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemDye extends Item
{
    public static final String[] dyeColorNames = new String[] {"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
    public static final String[] dyeIcons = new String[] {"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white"};
    public static final int[] dyeColors = new int[] {1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};
    private IIcon[] dyeIconArray;
    private static final String __OBFID = "CL_00000022";

    public ItemDye()
    {
        this.setHasSubtypes(true);
        this.setMaxDurability(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    public IIcon getIconFromDamage(int p_77617_1_)
    {
        int var2 = MathHelper.clamp_int(p_77617_1_, 0, 15);
        return this.dyeIconArray[var2];
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        int var2 = MathHelper.clamp_int(stack.getMetadata(), 0, 15);
        return super.getUnlocalizedName() + "." + dyeColorNames[var2];
    }

    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
        {
            return false;
        }
        else
        {
            if (p_77648_1_.getMetadata() == 15)
            {
                if (func_150919_a(p_77648_1_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_))
                {
                    if (!p_77648_3_.isRemote)
                    {
                        p_77648_3_.playAuxSFX(2005, p_77648_4_, p_77648_5_, p_77648_6_, 0);
                    }

                    return true;
                }
            }
            else if (p_77648_1_.getMetadata() == 3)
            {
                Block var11 = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);
                int var12 = p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_);

                if (var11 == Blocks.log && BlockLog.func_150165_c(var12) == 3)
                {
                    if (p_77648_7_ == 0)
                    {
                        return false;
                    }

                    if (p_77648_7_ == 1)
                    {
                        return false;
                    }

                    if (p_77648_7_ == 2)
                    {
                        --p_77648_6_;
                    }

                    if (p_77648_7_ == 3)
                    {
                        ++p_77648_6_;
                    }

                    if (p_77648_7_ == 4)
                    {
                        --p_77648_4_;
                    }

                    if (p_77648_7_ == 5)
                    {
                        ++p_77648_4_;
                    }

                    if (p_77648_3_.isAirBlock(p_77648_4_, p_77648_5_, p_77648_6_))
                    {
                        int var13 = Blocks.cocoa.onBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, 0);
                        p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, Blocks.cocoa, var13, 2);

                        if (!p_77648_2_.capabilities.isCreativeMode)
                        {
                            --p_77648_1_.stackSize;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public static boolean func_150919_a(ItemStack stack, World worldIn, int x, int y, int z)
    {
        Block var5 = worldIn.getBlock(x, y, z);

        if (var5 instanceof IGrowable)
        {
            IGrowable var6 = (IGrowable)var5;

            if (var6.canFertilize(worldIn, x, y, z, worldIn.isRemote))
            {
                if (!worldIn.isRemote)
                {
                    if (var6.shouldFertilize(worldIn, worldIn.rand, x, y, z))
                    {
                        var6.fertilize(worldIn, worldIn.rand, x, y, z);
                    }

                    --stack.stackSize;
                }

                return true;
            }
        }

        return false;
    }

    public static void func_150918_a(World p_150918_0_, int p_150918_1_, int p_150918_2_, int p_150918_3_, int p_150918_4_)
    {
        if (p_150918_4_ == 0)
        {
            p_150918_4_ = 15;
        }

        Block var5 = p_150918_0_.getBlock(p_150918_1_, p_150918_2_, p_150918_3_);

        if (var5.getMaterial() != Material.air)
        {
            var5.setBlockBoundsBasedOnState(p_150918_0_, p_150918_1_, p_150918_2_, p_150918_3_);

            for (int var6 = 0; var6 < p_150918_4_; ++var6)
            {
                double var7 = itemRand.nextGaussian() * 0.02D;
                double var9 = itemRand.nextGaussian() * 0.02D;
                double var11 = itemRand.nextGaussian() * 0.02D;
                p_150918_0_.spawnParticle("happyVillager", (double)((float)p_150918_1_ + itemRand.nextFloat()), (double)p_150918_2_ + (double)itemRand.nextFloat() * var5.getBlockBoundsMaxY(), (double)((float)p_150918_3_ + itemRand.nextFloat()), var7, var9, var11);
            }
        }
    }

    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target)
    {
        if (target instanceof EntitySheep)
        {
            EntitySheep var4 = (EntitySheep)target;
            int var5 = BlockColored.func_150032_b(stack.getMetadata());

            if (!var4.getSheared() && var4.getFleeceColor() != var5)
            {
                var4.setFleeceColor(var5);
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        for (int var4 = 0; var4 < 16; ++var4)
        {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, var4));
        }
    }

    public void registerIcons(IIconRegister register)
    {
        this.dyeIconArray = new IIcon[dyeIcons.length];

        for (int var2 = 0; var2 < dyeIcons.length; ++var2)
        {
            this.dyeIconArray[var2] = register.registerIcon(this.getIconString() + "_" + dyeIcons[var2]);
        }
    }
}
