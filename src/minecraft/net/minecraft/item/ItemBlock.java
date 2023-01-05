package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemBlock extends Item
{
    protected final Block blockInstance;
    private IIcon field_150938_b;
    private static final String __OBFID = "CL_00001772";

    public ItemBlock(Block p_i45328_1_)
    {
        this.blockInstance = p_i45328_1_;
    }

    public ItemBlock setUnlocalizedName(String p_77655_1_)
    {
        super.setUnlocalizedName(p_77655_1_);
        return this;
    }

    public int getSpriteNumber()
    {
        return this.blockInstance.getItemIconName() != null ? 1 : 0;
    }

    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150938_b != null ? this.field_150938_b : this.blockInstance.getBlockTextureFromSide(1);
    }

    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        Block var11 = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);

        if (var11 == Blocks.snow_layer && (p_77648_3_.getBlockMetadata(p_77648_4_, p_77648_5_, p_77648_6_) & 7) < 1)
        {
            p_77648_7_ = 1;
        }
        else if (var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush)
        {
            if (p_77648_7_ == 0)
            {
                --p_77648_5_;
            }

            if (p_77648_7_ == 1)
            {
                ++p_77648_5_;
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
        }

        if (p_77648_1_.stackSize == 0)
        {
            return false;
        }
        else if (!p_77648_2_.canPlayerEdit(p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_1_))
        {
            return false;
        }
        else if (p_77648_5_ == 255 && this.blockInstance.getMaterial().isSolid())
        {
            return false;
        }
        else if (p_77648_3_.canPlaceEntityOnSide(this.blockInstance, p_77648_4_, p_77648_5_, p_77648_6_, false, p_77648_7_, p_77648_2_, p_77648_1_))
        {
            int var12 = this.getMetadata(p_77648_1_.getMetadata());
            int var13 = this.blockInstance.onBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_, var12);

            if (p_77648_3_.setBlock(p_77648_4_, p_77648_5_, p_77648_6_, this.blockInstance, var13, 3))
            {
                if (p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_) == this.blockInstance)
                {
                    this.blockInstance.onBlockPlacedBy(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_2_, p_77648_1_);
                    this.blockInstance.onPostBlockPlaced(p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, var13);
                }

                p_77648_3_.playSoundEffect((double)((float)p_77648_4_ + 0.5F), (double)((float)p_77648_5_ + 0.5F), (double)((float)p_77648_6_ + 0.5F), this.blockInstance.stepSound.getPlaceSound(), (this.blockInstance.stepSound.getVolume() + 1.0F) / 2.0F, this.blockInstance.stepSound.getFrequency() * 0.8F);
                --p_77648_1_.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean func_150936_a(World p_150936_1_, int p_150936_2_, int p_150936_3_, int p_150936_4_, int p_150936_5_, EntityPlayer p_150936_6_, ItemStack p_150936_7_)
    {
        Block var8 = p_150936_1_.getBlock(p_150936_2_, p_150936_3_, p_150936_4_);

        if (var8 == Blocks.snow_layer)
        {
            p_150936_5_ = 1;
        }
        else if (var8 != Blocks.vine && var8 != Blocks.tallgrass && var8 != Blocks.deadbush)
        {
            if (p_150936_5_ == 0)
            {
                --p_150936_3_;
            }

            if (p_150936_5_ == 1)
            {
                ++p_150936_3_;
            }

            if (p_150936_5_ == 2)
            {
                --p_150936_4_;
            }

            if (p_150936_5_ == 3)
            {
                ++p_150936_4_;
            }

            if (p_150936_5_ == 4)
            {
                --p_150936_2_;
            }

            if (p_150936_5_ == 5)
            {
                ++p_150936_2_;
            }
        }

        return p_150936_1_.canPlaceEntityOnSide(this.blockInstance, p_150936_2_, p_150936_3_, p_150936_4_, false, p_150936_5_, (Entity)null, p_150936_7_);
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        return this.blockInstance.getUnlocalizedName();
    }

    public String getUnlocalizedName()
    {
        return this.blockInstance.getUnlocalizedName();
    }

    public CreativeTabs getCreativeTab()
    {
        return this.blockInstance.getCreativeTabToDisplayOn();
    }

    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        this.blockInstance.getSubBlocks(p_150895_1_, p_150895_2_, p_150895_3_);
    }

    public void registerIcons(IIconRegister register)
    {
        String var2 = this.blockInstance.getItemIconName();

        if (var2 != null)
        {
            this.field_150938_b = register.registerIcon(var2);
        }
    }
}
