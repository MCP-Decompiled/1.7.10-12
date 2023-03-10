package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.util.IIcon;

public class ItemCloth extends ItemBlock
{
    private static final String __OBFID = "CL_00000075";

    public ItemCloth(Block p_i45358_1_)
    {
        super(p_i45358_1_);
        this.setMaxDurability(0);
        this.setHasSubtypes(true);
    }

    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.blockInstance.getItemIcon(2, BlockColored.func_150032_b(p_77617_1_));
    }

    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + ItemDye.dyeColorNames[BlockColored.func_150032_b(stack.getMetadata())];
    }
}
