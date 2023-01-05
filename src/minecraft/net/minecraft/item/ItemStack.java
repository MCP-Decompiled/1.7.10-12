package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public final class ItemStack
{
    public static final DecimalFormat field_111284_a = new DecimalFormat("#.###");
    public int stackSize;
    public int animationsToGo;
    private Item theItem;
    public NBTTagCompound stackTagCompound;
    private int metadata;
    private EntityItemFrame itemFrame;
    private static final String __OBFID = "CL_00000043";

    public ItemStack(Block p_i1876_1_)
    {
        this(p_i1876_1_, 1);
    }

    public ItemStack(Block p_i1877_1_, int p_i1877_2_)
    {
        this(p_i1877_1_, p_i1877_2_, 0);
    }

    public ItemStack(Block p_i1878_1_, int p_i1878_2_, int p_i1878_3_)
    {
        this(Item.getItemFromBlock(p_i1878_1_), p_i1878_2_, p_i1878_3_);
    }

    public ItemStack(Item p_i1879_1_)
    {
        this(p_i1879_1_, 1);
    }

    public ItemStack(Item p_i1880_1_, int p_i1880_2_)
    {
        this(p_i1880_1_, p_i1880_2_, 0);
    }

    public ItemStack(Item p_i1881_1_, int p_i1881_2_, int p_i1881_3_)
    {
        this.theItem = p_i1881_1_;
        this.stackSize = p_i1881_2_;
        this.metadata = p_i1881_3_;

        if (this.metadata < 0)
        {
            this.metadata = 0;
        }
    }

    public static ItemStack loadItemStackFromNBT(NBTTagCompound p_77949_0_)
    {
        ItemStack var1 = new ItemStack();
        var1.readFromNBT(p_77949_0_);
        return var1.getItem() != null ? var1 : null;
    }

    private ItemStack() {}

    public ItemStack splitStack(int p_77979_1_)
    {
        ItemStack var2 = new ItemStack(this.theItem, p_77979_1_, this.metadata);

        if (this.stackTagCompound != null)
        {
            var2.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }

        this.stackSize -= p_77979_1_;
        return var2;
    }

    public Item getItem()
    {
        return this.theItem;
    }

    public IIcon getIconIndex()
    {
        return this.getItem().getIconIndex(this);
    }

    public int getItemSpriteNumber()
    {
        return this.getItem().getSpriteNumber();
    }

    public boolean tryPlaceItemIntoWorld(EntityPlayer p_77943_1_, World p_77943_2_, int p_77943_3_, int p_77943_4_, int p_77943_5_, int p_77943_6_, float p_77943_7_, float p_77943_8_, float p_77943_9_)
    {
        boolean var10 = this.getItem().onItemUse(this, p_77943_1_, p_77943_2_, p_77943_3_, p_77943_4_, p_77943_5_, p_77943_6_, p_77943_7_, p_77943_8_, p_77943_9_);

        if (var10)
        {
            p_77943_1_.addStat(StatList.objectUseStats[Item.getIdFromItem(this.theItem)], 1);
        }

        return var10;
    }

    public float getStrVsBlock(Block p_150997_1_)
    {
        return this.getItem().getStrVsBlock(this, p_150997_1_);
    }

    public ItemStack useItemRightClick(World p_77957_1_, EntityPlayer p_77957_2_)
    {
        return this.getItem().onItemRightClick(this, p_77957_1_, p_77957_2_);
    }

    public ItemStack onItemUseFinish(World p_77950_1_, EntityPlayer p_77950_2_)
    {
        return this.getItem().onItemUseFinish(this, p_77950_1_, p_77950_2_);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound p_77955_1_)
    {
        p_77955_1_.setShort("id", (short)Item.getIdFromItem(this.theItem));
        p_77955_1_.setByte("Count", (byte)this.stackSize);
        p_77955_1_.setShort("Damage", (short)this.metadata);

        if (this.stackTagCompound != null)
        {
            p_77955_1_.setTag("tag", this.stackTagCompound);
        }

        return p_77955_1_;
    }

    public void readFromNBT(NBTTagCompound p_77963_1_)
    {
        this.theItem = Item.getItemById(p_77963_1_.getShort("id"));
        this.stackSize = p_77963_1_.getByte("Count");
        this.metadata = p_77963_1_.getShort("Damage");

        if (this.metadata < 0)
        {
            this.metadata = 0;
        }

        if (p_77963_1_.hasKey("tag", 10))
        {
            this.stackTagCompound = p_77963_1_.getCompoundTag("tag");
        }
    }

    public int getMaxStackSize()
    {
        return this.getItem().getItemStackLimit();
    }

    public boolean isStackable()
    {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }

    public boolean isItemStackDamageable()
    {
        return this.theItem.getMaxDurability() <= 0 ? false : !this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable");
    }

    public boolean getHasSubtypes()
    {
        return this.theItem.getHasSubtypes();
    }

    public boolean isItemDamaged()
    {
        return this.isItemStackDamageable() && this.metadata > 0;
    }

    public int getCurrentDurability()
    {
        return this.metadata;
    }

    public int getMetadata()
    {
        return this.metadata;
    }

    public void setMetadata(int p_77964_1_)
    {
        this.metadata = p_77964_1_;

        if (this.metadata < 0)
        {
            this.metadata = 0;
        }
    }

    public int getMaxDurability()
    {
        return this.theItem.getMaxDurability();
    }

    public boolean attemptDamageItem(int p_96631_1_, Random p_96631_2_)
    {
        if (!this.isItemStackDamageable())
        {
            return false;
        }
        else
        {
            if (p_96631_1_ > 0)
            {
                int var3 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, this);
                int var4 = 0;

                for (int var5 = 0; var3 > 0 && var5 < p_96631_1_; ++var5)
                {
                    if (EnchantmentDurability.negateDamage(this, var3, p_96631_2_))
                    {
                        ++var4;
                    }
                }

                p_96631_1_ -= var4;

                if (p_96631_1_ <= 0)
                {
                    return false;
                }
            }

            this.metadata += p_96631_1_;
            return this.metadata > this.getMaxDurability();
        }
    }

    public void damageItem(int p_77972_1_, EntityLivingBase p_77972_2_)
    {
        if (!(p_77972_2_ instanceof EntityPlayer) || !((EntityPlayer)p_77972_2_).capabilities.isCreativeMode)
        {
            if (this.isItemStackDamageable())
            {
                if (this.attemptDamageItem(p_77972_1_, p_77972_2_.getRNG()))
                {
                    p_77972_2_.renderBrokenItemStack(this);
                    --this.stackSize;

                    if (p_77972_2_ instanceof EntityPlayer)
                    {
                        EntityPlayer var3 = (EntityPlayer)p_77972_2_;
                        var3.addStat(StatList.objectBreakStats[Item.getIdFromItem(this.theItem)], 1);

                        if (this.stackSize == 0 && this.getItem() instanceof ItemBow)
                        {
                            var3.destroyCurrentEquippedItem();
                        }
                    }

                    if (this.stackSize < 0)
                    {
                        this.stackSize = 0;
                    }

                    this.metadata = 0;
                }
            }
        }
    }

    public void hitEntity(EntityLivingBase p_77961_1_, EntityPlayer p_77961_2_)
    {
        boolean var3 = this.theItem.hitEntity(this, p_77961_1_, p_77961_2_);

        if (var3)
        {
            p_77961_2_.addStat(StatList.objectUseStats[Item.getIdFromItem(this.theItem)], 1);
        }
    }

    public void onBlockDestroyed(World p_150999_1_, Block p_150999_2_, int p_150999_3_, int p_150999_4_, int p_150999_5_, EntityPlayer p_150999_6_)
    {
        boolean var7 = this.theItem.onBlockDestroyed(this, p_150999_1_, p_150999_2_, p_150999_3_, p_150999_4_, p_150999_5_, p_150999_6_);

        if (var7)
        {
            p_150999_6_.addStat(StatList.objectUseStats[Item.getIdFromItem(this.theItem)], 1);
        }
    }

    public boolean canItemHarvestBlock(Block p_150998_1_)
    {
        return this.theItem.canItemHarvestBlock(p_150998_1_);
    }

    public boolean interactWithEntity(EntityPlayer p_111282_1_, EntityLivingBase p_111282_2_)
    {
        return this.theItem.itemInteractionForEntity(this, p_111282_1_, p_111282_2_);
    }

    public ItemStack copy()
    {
        ItemStack var1 = new ItemStack(this.theItem, this.stackSize, this.metadata);

        if (this.stackTagCompound != null)
        {
            var1.stackTagCompound = (NBTTagCompound)this.stackTagCompound.copy();
        }

        return var1;
    }

    public static boolean areItemStackTagsEqual(ItemStack p_77970_0_, ItemStack p_77970_1_)
    {
        return p_77970_0_ == null && p_77970_1_ == null ? true : (p_77970_0_ != null && p_77970_1_ != null ? (p_77970_0_.stackTagCompound == null && p_77970_1_.stackTagCompound != null ? false : p_77970_0_.stackTagCompound == null || p_77970_0_.stackTagCompound.equals(p_77970_1_.stackTagCompound)) : false);
    }

    public static boolean areItemStacksEqual(ItemStack p_77989_0_, ItemStack p_77989_1_)
    {
        return p_77989_0_ == null && p_77989_1_ == null ? true : (p_77989_0_ != null && p_77989_1_ != null ? p_77989_0_.isItemStackEqual(p_77989_1_) : false);
    }

    private boolean isItemStackEqual(ItemStack p_77959_1_)
    {
        return this.stackSize != p_77959_1_.stackSize ? false : (this.theItem != p_77959_1_.theItem ? false : (this.metadata != p_77959_1_.metadata ? false : (this.stackTagCompound == null && p_77959_1_.stackTagCompound != null ? false : this.stackTagCompound == null || this.stackTagCompound.equals(p_77959_1_.stackTagCompound))));
    }

    public boolean isItemEqual(ItemStack p_77969_1_)
    {
        return this.theItem == p_77969_1_.theItem && this.metadata == p_77969_1_.metadata;
    }

    public String getUnlocalizedName()
    {
        return this.theItem.getUnlocalizedName(this);
    }

    public static ItemStack copyItemStack(ItemStack p_77944_0_)
    {
        return p_77944_0_ == null ? null : p_77944_0_.copy();
    }

    public String toString()
    {
        return this.stackSize + "x" + this.theItem.getUnlocalizedName() + "@" + this.metadata;
    }

    public void updateAnimation(World p_77945_1_, Entity p_77945_2_, int p_77945_3_, boolean p_77945_4_)
    {
        if (this.animationsToGo > 0)
        {
            --this.animationsToGo;
        }

        this.theItem.onUpdate(this, p_77945_1_, p_77945_2_, p_77945_3_, p_77945_4_);
    }

    public void onCrafting(World p_77980_1_, EntityPlayer p_77980_2_, int p_77980_3_)
    {
        p_77980_2_.addStat(StatList.objectCraftStats[Item.getIdFromItem(this.theItem)], p_77980_3_);
        this.theItem.onCreated(this, p_77980_1_, p_77980_2_);
    }

    public int getMaxItemUseDuration()
    {
        return this.getItem().getMaxItemUseDuration(this);
    }

    public EnumAction getItemUseAction()
    {
        return this.getItem().getItemUseAction(this);
    }

    public void onPlayerStoppedUsing(World p_77974_1_, EntityPlayer p_77974_2_, int p_77974_3_)
    {
        this.getItem().onPlayerStoppedUsing(this, p_77974_1_, p_77974_2_, p_77974_3_);
    }

    public boolean hasTagCompound()
    {
        return this.stackTagCompound != null;
    }

    public NBTTagCompound getTagCompound()
    {
        return this.stackTagCompound;
    }

    public NBTTagList getEnchantmentTagList()
    {
        return this.stackTagCompound == null ? null : this.stackTagCompound.getTagList("ench", 10);
    }

    public void setTagCompound(NBTTagCompound p_77982_1_)
    {
        this.stackTagCompound = p_77982_1_;
    }

    public String getDisplayName()
    {
        String var1 = this.getItem().getItemStackDisplayName(this);

        if (this.stackTagCompound != null && this.stackTagCompound.hasKey("display", 10))
        {
            NBTTagCompound var2 = this.stackTagCompound.getCompoundTag("display");

            if (var2.hasKey("Name", 8))
            {
                var1 = var2.getString("Name");
            }
        }

        return var1;
    }

    public ItemStack setStackDisplayName(String p_151001_1_)
    {
        if (this.stackTagCompound == null)
        {
            this.stackTagCompound = new NBTTagCompound();
        }

        if (!this.stackTagCompound.hasKey("display", 10))
        {
            this.stackTagCompound.setTag("display", new NBTTagCompound());
        }

        this.stackTagCompound.getCompoundTag("display").setString("Name", p_151001_1_);
        return this;
    }

    public void func_135074_t()
    {
        if (this.stackTagCompound != null)
        {
            if (this.stackTagCompound.hasKey("display", 10))
            {
                NBTTagCompound var1 = this.stackTagCompound.getCompoundTag("display");
                var1.removeTag("Name");

                if (var1.hasNoTags())
                {
                    this.stackTagCompound.removeTag("display");

                    if (this.stackTagCompound.hasNoTags())
                    {
                        this.setTagCompound((NBTTagCompound)null);
                    }
                }
            }
        }
    }

    public boolean hasDisplayName()
    {
        return this.stackTagCompound == null ? false : (!this.stackTagCompound.hasKey("display", 10) ? false : this.stackTagCompound.getCompoundTag("display").hasKey("Name", 8));
    }

    public List getTooltip(EntityPlayer p_82840_1_, boolean p_82840_2_)
    {
        ArrayList var3 = new ArrayList();
        String var4 = this.getDisplayName();

        if (this.hasDisplayName())
        {
            var4 = EnumChatFormatting.ITALIC + var4 + EnumChatFormatting.RESET;
        }

        int var6;

        if (p_82840_2_)
        {
            String var5 = "";

            if (var4.length() > 0)
            {
                var4 = var4 + " (";
                var5 = ")";
            }

            var6 = Item.getIdFromItem(this.theItem);

            if (this.getHasSubtypes())
            {
                var4 = var4 + String.format("#%04d/%d%s", new Object[] {Integer.valueOf(var6), Integer.valueOf(this.metadata), var5});
            }
            else
            {
                var4 = var4 + String.format("#%04d%s", new Object[] {Integer.valueOf(var6), var5});
            }
        }
        else if (!this.hasDisplayName() && this.theItem == Items.filled_map)
        {
            var4 = var4 + " #" + this.metadata;
        }

        var3.add(var4);
        this.theItem.addInformation(this, p_82840_1_, var3, p_82840_2_);

        if (this.hasTagCompound())
        {
            NBTTagList var13 = this.getEnchantmentTagList();

            if (var13 != null)
            {
                for (var6 = 0; var6 < var13.tagCount(); ++var6)
                {
                    short var7 = var13.getCompoundTagAt(var6).getShort("id");
                    short var8 = var13.getCompoundTagAt(var6).getShort("lvl");

                    if (Enchantment.enchantmentsList[var7] != null)
                    {
                        var3.add(Enchantment.enchantmentsList[var7].getTranslatedName(var8));
                    }
                }
            }

            if (this.stackTagCompound.hasKey("display", 10))
            {
                NBTTagCompound var15 = this.stackTagCompound.getCompoundTag("display");

                if (var15.hasKey("color", 3))
                {
                    if (p_82840_2_)
                    {
                        var3.add("Color: #" + Integer.toHexString(var15.getInteger("color")).toUpperCase());
                    }
                    else
                    {
                        var3.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("item.dyed"));
                    }
                }

                if (var15.getTagId("Lore") == 9)
                {
                    NBTTagList var17 = var15.getTagList("Lore", 8);

                    if (var17.tagCount() > 0)
                    {
                        for (int var19 = 0; var19 < var17.tagCount(); ++var19)
                        {
                            var3.add(EnumChatFormatting.DARK_PURPLE + "" + EnumChatFormatting.ITALIC + var17.getStringTagAt(var19));
                        }
                    }
                }
            }
        }

        Multimap var14 = this.getAttributeModifiers();

        if (!var14.isEmpty())
        {
            var3.add("");
            Iterator var16 = var14.entries().iterator();

            while (var16.hasNext())
            {
                Entry var18 = (Entry)var16.next();
                AttributeModifier var20 = (AttributeModifier)var18.getValue();
                double var9 = var20.getAmount();

                if (var20.getID() == Item.itemModifierUUID)
                {
                    var9 += (double)EnchantmentHelper.func_152377_a(this, EnumCreatureAttribute.UNDEFINED);
                }

                double var11;

                if (var20.getOperation() != 1 && var20.getOperation() != 2)
                {
                    var11 = var9;
                }
                else
                {
                    var11 = var9 * 100.0D;
                }

                if (var9 > 0.0D)
                {
                    var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var20.getOperation(), new Object[] {field_111284_a.format(var11), StatCollector.translateToLocal("attribute.name." + (String)var18.getKey())}));
                }
                else if (var9 < 0.0D)
                {
                    var11 *= -1.0D;
                    var3.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var20.getOperation(), new Object[] {field_111284_a.format(var11), StatCollector.translateToLocal("attribute.name." + (String)var18.getKey())}));
                }
            }
        }

        if (this.hasTagCompound() && this.getTagCompound().getBoolean("Unbreakable"))
        {
            var3.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("item.unbreakable"));
        }

        if (p_82840_2_ && this.isItemDamaged())
        {
            var3.add("Durability: " + (this.getMaxDurability() - this.getCurrentDurability()) + " / " + this.getMaxDurability());
        }

        return var3;
    }

    public boolean hasEffect()
    {
        return this.getItem().hasEffect(this);
    }

    public EnumRarity getRarity()
    {
        return this.getItem().getRarity(this);
    }

    public boolean isItemEnchantable()
    {
        return !this.getItem().isItemTool(this) ? false : !this.isItemEnchanted();
    }

    public void addEnchantment(Enchantment p_77966_1_, int p_77966_2_)
    {
        if (this.stackTagCompound == null)
        {
            this.setTagCompound(new NBTTagCompound());
        }

        if (!this.stackTagCompound.hasKey("ench", 9))
        {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList var3 = this.stackTagCompound.getTagList("ench", 10);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", (short)p_77966_1_.effectId);
        var4.setShort("lvl", (short)((byte)p_77966_2_));
        var3.appendTag(var4);
    }

    public boolean isItemEnchanted()
    {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9);
    }

    public void setTagInfo(String p_77983_1_, NBTBase p_77983_2_)
    {
        if (this.stackTagCompound == null)
        {
            this.setTagCompound(new NBTTagCompound());
        }

        this.stackTagCompound.setTag(p_77983_1_, p_77983_2_);
    }

    public boolean canEditBlocks()
    {
        return this.getItem().canItemEditBlocks();
    }

    public boolean isOnItemFrame()
    {
        return this.itemFrame != null;
    }

    public void setItemFrame(EntityItemFrame p_82842_1_)
    {
        this.itemFrame = p_82842_1_;
    }

    public EntityItemFrame getItemFrame()
    {
        return this.itemFrame;
    }

    public int getRepairCost()
    {
        return this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3) ? this.stackTagCompound.getInteger("RepairCost") : 0;
    }

    public void setRepairCost(int p_82841_1_)
    {
        if (!this.hasTagCompound())
        {
            this.stackTagCompound = new NBTTagCompound();
        }

        this.stackTagCompound.setInteger("RepairCost", p_82841_1_);
    }

    public Multimap getAttributeModifiers()
    {
        Object var1;

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9))
        {
            var1 = HashMultimap.create();
            NBTTagList var2 = this.stackTagCompound.getTagList("AttributeModifiers", 10);

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                AttributeModifier var5 = SharedMonsterAttributes.readAttributeModifierFromNBT(var4);

                if (var5.getID().getLeastSignificantBits() != 0L && var5.getID().getMostSignificantBits() != 0L)
                {
                    ((Multimap)var1).put(var4.getString("AttributeName"), var5);
                }
            }
        }
        else
        {
            var1 = this.getItem().getItemAttributeModifiers();
        }

        return (Multimap)var1;
    }

    public void setItem(Item p_150996_1_)
    {
        this.theItem = p_150996_1_;
    }

    public IChatComponent func_151000_E()
    {
        IChatComponent var1 = (new ChatComponentText("[")).appendText(this.getDisplayName()).appendText("]");

        if (this.theItem != null)
        {
            NBTTagCompound var2 = new NBTTagCompound();
            this.writeToNBT(var2);
            var1.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(var2.toString())));
            var1.getChatStyle().setColor(this.getRarity().rarityColor);
        }

        return var1;
    }
}
