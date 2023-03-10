package net.minecraft.enchantment;

public class EnchantmentArrowKnockback extends Enchantment
{
    private static final String __OBFID = "CL_00000101";

    public EnchantmentArrowKnockback(int p_i1922_1_, int p_i1922_2_)
    {
        super(p_i1922_1_, p_i1922_2_, EnumEnchantmentType.bow);
        this.setName("arrowKnockback");
    }

    public int getMinEnchantability(int p_77321_1_)
    {
        return 12 + (p_77321_1_ - 1) * 20;
    }

    public int getMaxEnchantability(int p_77317_1_)
    {
        return this.getMinEnchantability(p_77317_1_) + 25;
    }

    public int getMaxLevel()
    {
        return 2;
    }
}
