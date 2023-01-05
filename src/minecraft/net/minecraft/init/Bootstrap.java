package net.minecraft.init;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Bootstrap
{
    private static boolean alreadyRegistered = false;
    private static final String __OBFID = "CL_00001397";

    static void registerDispenserBehaviors()
    {
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.arrow, new BehaviorProjectileDispense()
        {
            private static final String __OBFID = "CL_00001398";
            protected IProjectile getProjectileEntity(World worldIn, IPosition position)
            {
                EntityArrow var3 = new EntityArrow(worldIn, position.getX(), position.getY(), position.getZ());
                var3.canBePickedUp = 1;
                return var3;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.egg, new BehaviorProjectileDispense()
        {
            private static final String __OBFID = "CL_00001404";
            protected IProjectile getProjectileEntity(World worldIn, IPosition position)
            {
                return new EntityEgg(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.snowball, new BehaviorProjectileDispense()
        {
            private static final String __OBFID = "CL_00001405";
            protected IProjectile getProjectileEntity(World worldIn, IPosition position)
            {
                return new EntitySnowball(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.experience_bottle, new BehaviorProjectileDispense()
        {
            private static final String __OBFID = "CL_00001406";
            protected IProjectile getProjectileEntity(World worldIn, IPosition position)
            {
                return new EntityExpBottle(worldIn, position.getX(), position.getY(), position.getZ());
            }
            protected float func_82498_a()
            {
                return super.func_82498_a() * 0.5F;
            }
            protected float func_82500_b()
            {
                return super.func_82500_b() * 1.25F;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.potionitem, new IBehaviorDispenseItem()
        {
            private final BehaviorDefaultDispenseItem field_150843_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001407";
            public ItemStack dispense(IBlockSource source, final ItemStack stack)
            {
                return ItemPotion.isSplash(stack.getMetadata()) ? (new BehaviorProjectileDispense()
                {
                    private static final String __OBFID = "CL_00001408";
                    protected IProjectile getProjectileEntity(World worldIn, IPosition position)
                    {
                        return new EntityPotion(worldIn, position.getX(), position.getY(), position.getZ(), stack.copy());
                    }
                    protected float func_82498_a()
                    {
                        return super.func_82498_a() * 0.5F;
                    }
                    protected float func_82500_b()
                    {
                        return super.func_82500_b() * 1.25F;
                    }
                }).dispense(source, stack): this.field_150843_b.dispense(source, stack);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.spawn_egg, new BehaviorDefaultDispenseItem()
        {
            private static final String __OBFID = "CL_00001410";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
                double var4 = source.getX() + (double)var3.getFrontOffsetX();
                double var6 = (double)((float)source.getYInt() + 0.2F);
                double var8 = source.getZ() + (double)var3.getFrontOffsetZ();
                Entity var10 = ItemMonsterPlacer.spawnCreature(source.getWorld(), stack.getMetadata(), var4, var6, var8);

                if (var10 instanceof EntityLivingBase && stack.hasDisplayName())
                {
                    ((EntityLiving)var10).setCustomNameTag(stack.getDisplayName());
                }

                stack.splitStack(1);
                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fireworks, new BehaviorDefaultDispenseItem()
        {
            private static final String __OBFID = "CL_00001411";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
                double var4 = source.getX() + (double)var3.getFrontOffsetX();
                double var6 = (double)((float)source.getYInt() + 0.2F);
                double var8 = source.getZ() + (double)var3.getFrontOffsetZ();
                EntityFireworkRocket var10 = new EntityFireworkRocket(source.getWorld(), var4, var6, var8, stack);
                source.getWorld().spawnEntityInWorld(var10);
                stack.splitStack(1);
                return stack;
            }
            protected void playDispenseSound(IBlockSource source)
            {
                source.getWorld().playAuxSFX(1002, source.getXInt(), source.getYInt(), source.getZInt(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.fire_charge, new BehaviorDefaultDispenseItem()
        {
            private static final String __OBFID = "CL_00001412";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
                IPosition var4 = BlockDispenser.getIPositionFromBlockSource(source);
                double var5 = var4.getX() + (double)((float)var3.getFrontOffsetX() * 0.3F);
                double var7 = var4.getY() + (double)((float)var3.getFrontOffsetX() * 0.3F);
                double var9 = var4.getZ() + (double)((float)var3.getFrontOffsetZ() * 0.3F);
                World var11 = source.getWorld();
                Random var12 = var11.rand;
                double var13 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetX();
                double var15 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetY();
                double var17 = var12.nextGaussian() * 0.05D + (double)var3.getFrontOffsetZ();
                var11.spawnEntityInWorld(new EntitySmallFireball(var11, var5, var7, var9, var13, var15, var17));
                stack.splitStack(1);
                return stack;
            }
            protected void playDispenseSound(IBlockSource source)
            {
                source.getWorld().playAuxSFX(1009, source.getXInt(), source.getYInt(), source.getZInt(), 0);
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.boat, new BehaviorDefaultDispenseItem()
        {
            private final BehaviorDefaultDispenseItem field_150842_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001413";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
                World var4 = source.getWorld();
                double var5 = source.getX() + (double)((float)var3.getFrontOffsetX() * 1.125F);
                double var7 = source.getY() + (double)((float)var3.getFrontOffsetY() * 1.125F);
                double var9 = source.getZ() + (double)((float)var3.getFrontOffsetZ() * 1.125F);
                int var11 = source.getXInt() + var3.getFrontOffsetX();
                int var12 = source.getYInt() + var3.getFrontOffsetY();
                int var13 = source.getZInt() + var3.getFrontOffsetZ();
                Material var14 = var4.getBlock(var11, var12, var13).getMaterial();
                double var15;

                if (Material.water.equals(var14))
                {
                    var15 = 1.0D;
                }
                else
                {
                    if (!Material.air.equals(var14) || !Material.water.equals(var4.getBlock(var11, var12 - 1, var13).getMaterial()))
                    {
                        return this.field_150842_b.dispense(source, stack);
                    }

                    var15 = 0.0D;
                }

                EntityBoat var17 = new EntityBoat(var4, var5, var7 + var15, var9);
                var4.spawnEntityInWorld(var17);
                stack.splitStack(1);
                return stack;
            }
            protected void playDispenseSound(IBlockSource source)
            {
                source.getWorld().playAuxSFX(1000, source.getXInt(), source.getYInt(), source.getZInt(), 0);
            }
        });
        BehaviorDefaultDispenseItem var0 = new BehaviorDefaultDispenseItem()
        {
            private final BehaviorDefaultDispenseItem field_150841_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001399";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                ItemBucket var3 = (ItemBucket)stack.getItem();
                int var4 = source.getXInt();
                int var5 = source.getYInt();
                int var6 = source.getZInt();
                EnumFacing var7 = BlockDispenser.getFacingDirection(source.getBlockMetadata());

                if (var3.tryPlaceContainedLiquid(source.getWorld(), var4 + var7.getFrontOffsetX(), var5 + var7.getFrontOffsetY(), var6 + var7.getFrontOffsetZ()))
                {
                    stack.setItem(Items.bucket);
                    stack.stackSize = 1;
                    return stack;
                }
                else
                {
                    return this.field_150841_b.dispense(source, stack);
                }
            }
        };
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.lava_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.water_bucket, var0);
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.bucket, new BehaviorDefaultDispenseItem()
        {
            private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
            private static final String __OBFID = "CL_00001400";
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
                World var4 = source.getWorld();
                int var5 = source.getXInt() + var3.getFrontOffsetX();
                int var6 = source.getYInt() + var3.getFrontOffsetY();
                int var7 = source.getZInt() + var3.getFrontOffsetZ();
                Material var8 = var4.getBlock(var5, var6, var7).getMaterial();
                int var9 = var4.getBlockMetadata(var5, var6, var7);
                Item var10;

                if (Material.water.equals(var8) && var9 == 0)
                {
                    var10 = Items.water_bucket;
                }
                else
                {
                    if (!Material.lava.equals(var8) || var9 != 0)
                    {
                        return super.dispenseStack(source, stack);
                    }

                    var10 = Items.lava_bucket;
                }

                var4.setBlockToAir(var5, var6, var7);

                if (--stack.stackSize == 0)
                {
                    stack.setItem(var10);
                    stack.stackSize = 1;
                }
                else if (((TileEntityDispenser)source.getBlockTileEntity()).func_146019_a(new ItemStack(var10)) < 0)
                {
                    this.field_150840_b.dispense(source, new ItemStack(var10));
                }

                return stack;
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.flint_and_steel, new BehaviorDefaultDispenseItem()
        {
            private boolean field_150839_b = true;
            private static final String __OBFID = "CL_00001401";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
                World var4 = source.getWorld();
                int var5 = source.getXInt() + var3.getFrontOffsetX();
                int var6 = source.getYInt() + var3.getFrontOffsetY();
                int var7 = source.getZInt() + var3.getFrontOffsetZ();

                if (var4.isAirBlock(var5, var6, var7))
                {
                    var4.setBlock(var5, var6, var7, Blocks.fire);

                    if (stack.attemptDamageItem(1, var4.rand))
                    {
                        stack.stackSize = 0;
                    }
                }
                else if (var4.getBlock(var5, var6, var7) == Blocks.tnt)
                {
                    Blocks.tnt.onBlockDestroyedByPlayer(var4, var5, var6, var7, 1);
                    var4.setBlockToAir(var5, var6, var7);
                }
                else
                {
                    this.field_150839_b = false;
                }

                return stack;
            }
            protected void playDispenseSound(IBlockSource source)
            {
                if (this.field_150839_b)
                {
                    source.getWorld().playAuxSFX(1000, source.getXInt(), source.getYInt(), source.getZInt(), 0);
                }
                else
                {
                    source.getWorld().playAuxSFX(1001, source.getXInt(), source.getYInt(), source.getZInt(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Items.dye, new BehaviorDefaultDispenseItem()
        {
            private boolean field_150838_b = true;
            private static final String __OBFID = "CL_00001402";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                if (stack.getMetadata() == 15)
                {
                    EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
                    World var4 = source.getWorld();
                    int var5 = source.getXInt() + var3.getFrontOffsetX();
                    int var6 = source.getYInt() + var3.getFrontOffsetY();
                    int var7 = source.getZInt() + var3.getFrontOffsetZ();

                    if (ItemDye.func_150919_a(stack, var4, var5, var6, var7))
                    {
                        if (!var4.isRemote)
                        {
                            var4.playAuxSFX(2005, var5, var6, var7, 0);
                        }
                    }
                    else
                    {
                        this.field_150838_b = false;
                    }

                    return stack;
                }
                else
                {
                    return super.dispenseStack(source, stack);
                }
            }
            protected void playDispenseSound(IBlockSource source)
            {
                if (this.field_150838_b)
                {
                    source.getWorld().playAuxSFX(1000, source.getXInt(), source.getYInt(), source.getZInt(), 0);
                }
                else
                {
                    source.getWorld().playAuxSFX(1001, source.getXInt(), source.getYInt(), source.getZInt(), 0);
                }
            }
        });
        BlockDispenser.dispenseBehaviorRegistry.putObject(Item.getItemFromBlock(Blocks.tnt), new BehaviorDefaultDispenseItem()
        {
            private static final String __OBFID = "CL_00001403";
            protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
            {
                EnumFacing var3 = BlockDispenser.getFacingDirection(source.getBlockMetadata());
                World var4 = source.getWorld();
                int var5 = source.getXInt() + var3.getFrontOffsetX();
                int var6 = source.getYInt() + var3.getFrontOffsetY();
                int var7 = source.getZInt() + var3.getFrontOffsetZ();
                EntityTNTPrimed var8 = new EntityTNTPrimed(var4, (double)((float)var5 + 0.5F), (double)((float)var6 + 0.5F), (double)((float)var7 + 0.5F), (EntityLivingBase)null);
                var4.spawnEntityInWorld(var8);
                --stack.stackSize;
                return stack;
            }
        });
    }

    public static void register()
    {
        if (!alreadyRegistered)
        {
            alreadyRegistered = true;
            Block.registerBlocks();
            BlockFire.func_149843_e();
            Item.registerItems();
            StatList.func_151178_a();
            registerDispenserBehaviors();
        }
    }
}
