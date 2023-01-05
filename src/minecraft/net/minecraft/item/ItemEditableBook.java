package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ItemEditableBook extends Item
{
    private static final String __OBFID = "CL_00000077";

    public ItemEditableBook()
    {
        this.setMaxStackSize(1);
    }

    public static boolean validBookTagContents(NBTTagCompound p_77828_0_)
    {
        if (!ItemWritableBook.validBookPageTagContents(p_77828_0_))
        {
            return false;
        }
        else if (!p_77828_0_.hasKey("title", 8))
        {
            return false;
        }
        else
        {
            String var1 = p_77828_0_.getString("title");
            return var1 != null && var1.length() <= 16 ? p_77828_0_.hasKey("author", 8) : false;
        }
    }

    public String getItemStackDisplayName(ItemStack p_77653_1_)
    {
        if (p_77653_1_.hasTagCompound())
        {
            NBTTagCompound var2 = p_77653_1_.getTagCompound();
            String var3 = var2.getString("title");

            if (!StringUtils.isNullOrEmpty(var3))
            {
                return var3;
            }
        }

        return super.getItemStackDisplayName(p_77653_1_);
    }

    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
    {
        if (p_77624_1_.hasTagCompound())
        {
            NBTTagCompound var5 = p_77624_1_.getTagCompound();
            String var6 = var5.getString("author");

            if (!StringUtils.isNullOrEmpty(var6))
            {
                p_77624_3_.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] {var6}));
            }
        }
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        player.displayGUIBook(itemStackIn);
        return itemStackIn;
    }

    public boolean getShareTag()
    {
        return true;
    }

    public boolean hasEffect(ItemStack p_77636_1_)
    {
        return true;
    }
}
