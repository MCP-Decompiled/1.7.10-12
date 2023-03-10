package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ItemMultiTexture extends ItemBlock
{
    protected final Block field_150941_b;
    protected final String[] field_150942_c;
    private static final String __OBFID = "CL_00000051";

    public ItemMultiTexture(Block p_i45346_1_, Block p_i45346_2_, String[] p_i45346_3_)
    {
        super(p_i45346_1_);
        this.field_150941_b = p_i45346_2_;
        this.field_150942_c = p_i45346_3_;
        this.setMaxDurability(0);
        this.setHasSubtypes(true);
    }

    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150941_b.getIcon(2, p_77617_1_);
    }

    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        int var2 = stack.getMetadata();

        if (var2 < 0 || var2 >= this.field_150942_c.length)
        {
            var2 = 0;
        }

        return super.getUnlocalizedName() + "." + this.field_150942_c[var2];
    }
}
