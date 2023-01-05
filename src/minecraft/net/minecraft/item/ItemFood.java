package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFood extends Item
{
    public final int itemUseDuration;
    private final int healAmount;
    private final float saturationModifier;
    private final boolean isWolfsFavoriteMeat;
    private boolean alwaysEdible;
    private int potionId;
    private int potionDuration;
    private int potionAmplifier;
    private float potionEffectProbability;
    private static final String __OBFID = "CL_00000036";

    public ItemFood(int p_i45339_1_, float p_i45339_2_, boolean p_i45339_3_)
    {
        this.itemUseDuration = 32;
        this.healAmount = p_i45339_1_;
        this.isWolfsFavoriteMeat = p_i45339_3_;
        this.saturationModifier = p_i45339_2_;
        this.setCreativeTab(CreativeTabs.tabFood);
    }

    public ItemFood(int p_i45340_1_, boolean p_i45340_2_)
    {
        this(p_i45340_1_, 0.6F, p_i45340_2_);
    }

    public ItemStack onItemUseFinish(ItemStack p_77654_1_, World p_77654_2_, EntityPlayer p_77654_3_)
    {
        --p_77654_1_.stackSize;
        p_77654_3_.getFoodStats().addStats(this, p_77654_1_);
        p_77654_2_.playSoundAtEntity(p_77654_3_, "random.burp", 0.5F, p_77654_2_.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(p_77654_1_, p_77654_2_, p_77654_3_);
        return p_77654_1_;
    }

    protected void onFoodEaten(ItemStack p_77849_1_, World p_77849_2_, EntityPlayer p_77849_3_)
    {
        if (!p_77849_2_.isRemote && this.potionId > 0 && p_77849_2_.rand.nextFloat() < this.potionEffectProbability)
        {
            p_77849_3_.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
        }
    }

    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.eat;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        if (player.canEat(this.alwaysEdible))
        {
            player.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        }

        return itemStackIn;
    }

    public int getHealAmount(ItemStack itemStackIn)
    {
        return this.healAmount;
    }

    public float getSaturationModifier(ItemStack itemStackIn)
    {
        return this.saturationModifier;
    }

    public boolean isWolfsFavoriteMeat()
    {
        return this.isWolfsFavoriteMeat;
    }

    public ItemFood setPotionEffect(int p_77844_1_, int duration, int amplifier, float probability)
    {
        this.potionId = p_77844_1_;
        this.potionDuration = duration;
        this.potionAmplifier = amplifier;
        this.potionEffectProbability = probability;
        return this;
    }

    public ItemFood setAlwaysEdible()
    {
        this.alwaysEdible = true;
        return this;
    }
}
