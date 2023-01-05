package net.minecraft.world.biome;

import com.google.common.collect.Sets;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BiomeGenBase
{
    private static final Logger logger = LogManager.getLogger();
    protected static final BiomeGenBase.Height height_Default = new BiomeGenBase.Height(0.1F, 0.2F);
    protected static final BiomeGenBase.Height height_ShallowWaters = new BiomeGenBase.Height(-0.5F, 0.0F);
    protected static final BiomeGenBase.Height height_Oceans = new BiomeGenBase.Height(-1.0F, 0.1F);
    protected static final BiomeGenBase.Height height_DeepOceans = new BiomeGenBase.Height(-1.8F, 0.1F);
    protected static final BiomeGenBase.Height height_LowPlains = new BiomeGenBase.Height(0.125F, 0.05F);
    protected static final BiomeGenBase.Height height_MidPlains = new BiomeGenBase.Height(0.2F, 0.2F);
    protected static final BiomeGenBase.Height height_LowHills = new BiomeGenBase.Height(0.45F, 0.3F);
    protected static final BiomeGenBase.Height height_HighPlateaus = new BiomeGenBase.Height(1.5F, 0.025F);
    protected static final BiomeGenBase.Height height_MidHills = new BiomeGenBase.Height(1.0F, 0.5F);
    protected static final BiomeGenBase.Height height_Shores = new BiomeGenBase.Height(0.0F, 0.025F);
    protected static final BiomeGenBase.Height height_RockyWaters = new BiomeGenBase.Height(0.1F, 0.8F);
    protected static final BiomeGenBase.Height height_LowIslands = new BiomeGenBase.Height(0.2F, 0.3F);
    protected static final BiomeGenBase.Height height_PartiallySubmerged = new BiomeGenBase.Height(-0.2F, 0.1F);
    private static final BiomeGenBase[] biomeList = new BiomeGenBase[256];
    public static final Set explorationBiomesList = Sets.newHashSet();
    public static final BiomeGenBase ocean = (new BiomeGenOcean(0)).setColor(112).setBiomeName("Ocean").setHeight(height_Oceans);
    public static final BiomeGenBase plains = (new BiomeGenPlains(1)).setColor(9286496).setBiomeName("Plains");
    public static final BiomeGenBase desert = (new BiomeGenDesert(2)).setColor(16421912).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowPlains);
    public static final BiomeGenBase extremeHills = (new BiomeGenHills(3, false)).setColor(6316128).setBiomeName("Extreme Hills").setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
    public static final BiomeGenBase forest = (new BiomeGenForest(4, 0)).setColor(353825).setBiomeName("Forest");
    public static final BiomeGenBase taiga = (new BiomeGenTaiga(5, 0)).setColor(747097).setBiomeName("Taiga").func_76733_a(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_MidPlains);
    public static final BiomeGenBase swampland = (new BiomeGenSwamp(6)).setColor(522674).setBiomeName("Swampland").func_76733_a(9154376).setHeight(height_PartiallySubmerged).setTemperatureRainfall(0.8F, 0.9F);
    public static final BiomeGenBase river = (new BiomeGenRiver(7)).setColor(255).setBiomeName("River").setHeight(height_ShallowWaters);
    public static final BiomeGenBase hell = (new BiomeGenHell(8)).setColor(16711680).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);
    public static final BiomeGenBase sky = (new BiomeGenEnd(9)).setColor(8421631).setBiomeName("Sky").setDisableRain();
    public static final BiomeGenBase frozenOcean = (new BiomeGenOcean(10)).setColor(9474208).setBiomeName("FrozenOcean").setEnableSnow().setHeight(height_Oceans).setTemperatureRainfall(0.0F, 0.5F);
    public static final BiomeGenBase frozenRiver = (new BiomeGenRiver(11)).setColor(10526975).setBiomeName("FrozenRiver").setEnableSnow().setHeight(height_ShallowWaters).setTemperatureRainfall(0.0F, 0.5F);
    public static final BiomeGenBase icePlains = (new BiomeGenSnow(12, false)).setColor(16777215).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(height_LowPlains);
    public static final BiomeGenBase iceMountains = (new BiomeGenSnow(13, false)).setColor(10526880).setBiomeName("Ice Mountains").setEnableSnow().setHeight(height_LowHills).setTemperatureRainfall(0.0F, 0.5F);
    public static final BiomeGenBase mushroomIsland = (new BiomeGenMushroomIsland(14)).setColor(16711935).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9F, 1.0F).setHeight(height_LowIslands);
    public static final BiomeGenBase mushroomIslandShore = (new BiomeGenMushroomIsland(15)).setColor(10486015).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9F, 1.0F).setHeight(height_Shores);
    public static final BiomeGenBase beach = (new BiomeGenBeach(16)).setColor(16440917).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F).setHeight(height_Shores);
    public static final BiomeGenBase desertHills = (new BiomeGenDesert(17)).setColor(13786898).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setHeight(height_LowHills);
    public static final BiomeGenBase forestHills = (new BiomeGenForest(18, 0)).setColor(2250012).setBiomeName("ForestHills").setHeight(height_LowHills);
    public static final BiomeGenBase taigaHills = (new BiomeGenTaiga(19, 0)).setColor(1456435).setBiomeName("TaigaHills").func_76733_a(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(height_LowHills);
    public static final BiomeGenBase extremeHillsEdge = (new BiomeGenHills(20, true)).setColor(7501978).setBiomeName("Extreme Hills Edge").setHeight(height_MidHills.attenuate()).setTemperatureRainfall(0.2F, 0.3F);
    public static final BiomeGenBase jungle = (new BiomeGenJungle(21, false)).setColor(5470985).setBiomeName("Jungle").func_76733_a(5470985).setTemperatureRainfall(0.95F, 0.9F);
    public static final BiomeGenBase jungleHills = (new BiomeGenJungle(22, false)).setColor(2900485).setBiomeName("JungleHills").func_76733_a(5470985).setTemperatureRainfall(0.95F, 0.9F).setHeight(height_LowHills);
    public static final BiomeGenBase jungleEdge = (new BiomeGenJungle(23, true)).setColor(6458135).setBiomeName("JungleEdge").func_76733_a(5470985).setTemperatureRainfall(0.95F, 0.8F);
    public static final BiomeGenBase deepOcean = (new BiomeGenOcean(24)).setColor(48).setBiomeName("Deep Ocean").setHeight(height_DeepOceans);
    public static final BiomeGenBase stoneBeach = (new BiomeGenStoneBeach(25)).setColor(10658436).setBiomeName("Stone Beach").setTemperatureRainfall(0.2F, 0.3F).setHeight(height_RockyWaters);
    public static final BiomeGenBase coldBeach = (new BiomeGenBeach(26)).setColor(16445632).setBiomeName("Cold Beach").setTemperatureRainfall(0.05F, 0.3F).setHeight(height_Shores).setEnableSnow();
    public static final BiomeGenBase birchForest = (new BiomeGenForest(27, 2)).setBiomeName("Birch Forest").setColor(3175492);
    public static final BiomeGenBase birchForestHills = (new BiomeGenForest(28, 2)).setBiomeName("Birch Forest Hills").setColor(2055986).setHeight(height_LowHills);
    public static final BiomeGenBase roofedForest = (new BiomeGenForest(29, 3)).setColor(4215066).setBiomeName("Roofed Forest");
    public static final BiomeGenBase coldTaiga = (new BiomeGenTaiga(30, 0)).setColor(3233098).setBiomeName("Cold Taiga").func_76733_a(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_MidPlains).func_150563_c(16777215);
    public static final BiomeGenBase coldTaigaHills = (new BiomeGenTaiga(31, 0)).setColor(2375478).setBiomeName("Cold Taiga Hills").func_76733_a(5159473).setEnableSnow().setTemperatureRainfall(-0.5F, 0.4F).setHeight(height_LowHills).func_150563_c(16777215);
    public static final BiomeGenBase megaTaiga = (new BiomeGenTaiga(32, 1)).setColor(5858897).setBiomeName("Mega Taiga").func_76733_a(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_MidPlains);
    public static final BiomeGenBase megaTaigaHills = (new BiomeGenTaiga(33, 1)).setColor(4542270).setBiomeName("Mega Taiga Hills").func_76733_a(5159473).setTemperatureRainfall(0.3F, 0.8F).setHeight(height_LowHills);
    public static final BiomeGenBase extremeHillsPlus = (new BiomeGenHills(34, true)).setColor(5271632).setBiomeName("Extreme Hills+").setHeight(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
    public static final BiomeGenBase savanna = (new BiomeGenSavanna(35)).setColor(12431967).setBiomeName("Savanna").setTemperatureRainfall(1.2F, 0.0F).setDisableRain().setHeight(height_LowPlains);
    public static final BiomeGenBase savannaPlateau = (new BiomeGenSavanna(36)).setColor(10984804).setBiomeName("Savanna Plateau").setTemperatureRainfall(1.0F, 0.0F).setDisableRain().setHeight(height_HighPlateaus);
    public static final BiomeGenBase mesa = (new BiomeGenMesa(37, false, false)).setColor(14238997).setBiomeName("Mesa");
    public static final BiomeGenBase mesaPlateau_F = (new BiomeGenMesa(38, false, true)).setColor(11573093).setBiomeName("Mesa Plateau F").setHeight(height_HighPlateaus);
    public static final BiomeGenBase mesaPlateau = (new BiomeGenMesa(39, false, false)).setColor(13274213).setBiomeName("Mesa Plateau").setHeight(height_HighPlateaus);
    protected static final NoiseGeneratorPerlin temperatureNoise;
    protected static final NoiseGeneratorPerlin plantNoise;
    protected static final WorldGenDoublePlant genTallFlowers;
    public String biomeName;
    public int color;
    public int field_150609_ah;
    public Block topBlock;
    public int topBlockMetadata;
    public Block fillerBlock;
    public int fillerBlockMetadata;
    public float minHeight;
    public float maxHeight;
    public float temperature;
    public float rainfall;
    public int waterColorMultiplier;
    public BiomeDecorator theBiomeDecorator;
    protected List spawnableMonsterList;
    protected List spawnableCreatureList;
    protected List spawnableWaterCreatureList;
    protected List spawnableCaveCreatureList;
    protected boolean enableSnow;
    protected boolean enableRain;
    public final int biomeID;
    protected WorldGenTrees worldGeneratorTrees;
    protected WorldGenBigTree worldGeneratorBigTree;
    protected WorldGenSwamp worldGeneratorSwamp;
    private static final String __OBFID = "CL_00000158";

    protected BiomeGenBase(int p_i1971_1_)
    {
        this.topBlock = Blocks.grass;
        this.topBlockMetadata = 0;
        this.fillerBlock = Blocks.dirt;
        this.fillerBlockMetadata = 5169201;
        this.minHeight = height_Default.rootHeight;
        this.maxHeight = height_Default.variation;
        this.temperature = 0.5F;
        this.rainfall = 0.5F;
        this.waterColorMultiplier = 16777215;
        this.spawnableMonsterList = new ArrayList();
        this.spawnableCreatureList = new ArrayList();
        this.spawnableWaterCreatureList = new ArrayList();
        this.spawnableCaveCreatureList = new ArrayList();
        this.enableRain = true;
        this.worldGeneratorTrees = new WorldGenTrees(false);
        this.worldGeneratorBigTree = new WorldGenBigTree(false);
        this.worldGeneratorSwamp = new WorldGenSwamp();
        this.biomeID = p_i1971_1_;
        biomeList[p_i1971_1_] = this;
        this.theBiomeDecorator = this.createBiomeDecorator();
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySheep.class, 12, 4, 4));
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityPig.class, 10, 4, 4));
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 10, 4, 4));
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityCow.class, 8, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySpider.class, 100, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityZombie.class, 100, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySkeleton.class, 100, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityCreeper.class, 100, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 100, 4, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityEnderman.class, 10, 1, 4));
        this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityWitch.class, 5, 1, 1));
        this.spawnableWaterCreatureList.add(new BiomeGenBase.SpawnListEntry(EntitySquid.class, 10, 4, 4));
        this.spawnableCaveCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityBat.class, 10, 8, 8));
    }

    protected BiomeDecorator createBiomeDecorator()
    {
        return new BiomeDecorator();
    }

    protected BiomeGenBase setTemperatureRainfall(float p_76732_1_, float p_76732_2_)
    {
        if (p_76732_1_ > 0.1F && p_76732_1_ < 0.2F)
        {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        }
        else
        {
            this.temperature = p_76732_1_;
            this.rainfall = p_76732_2_;
            return this;
        }
    }

    protected final BiomeGenBase setHeight(BiomeGenBase.Height p_150570_1_)
    {
        this.minHeight = p_150570_1_.rootHeight;
        this.maxHeight = p_150570_1_.variation;
        return this;
    }

    protected BiomeGenBase setDisableRain()
    {
        this.enableRain = false;
        return this;
    }

    public WorldGenAbstractTree func_150567_a(Random p_150567_1_)
    {
        return (WorldGenAbstractTree)(p_150567_1_.nextInt(10) == 0 ? this.worldGeneratorBigTree : this.worldGeneratorTrees);
    }

    public WorldGenerator getRandomWorldGenForGrass(Random p_76730_1_)
    {
        return new WorldGenTallGrass(Blocks.tallgrass, 1);
    }

    public String func_150572_a(Random p_150572_1_, int p_150572_2_, int p_150572_3_, int p_150572_4_)
    {
        return p_150572_1_.nextInt(3) > 0 ? BlockFlower.field_149858_b[0] : BlockFlower.field_149859_a[0];
    }

    protected BiomeGenBase setEnableSnow()
    {
        this.enableSnow = true;
        return this;
    }

    protected BiomeGenBase setBiomeName(String p_76735_1_)
    {
        this.biomeName = p_76735_1_;
        return this;
    }

    protected BiomeGenBase func_76733_a(int p_76733_1_)
    {
        this.fillerBlockMetadata = p_76733_1_;
        return this;
    }

    protected BiomeGenBase setColor(int p_76739_1_)
    {
        this.func_150557_a(p_76739_1_, false);
        return this;
    }

    protected BiomeGenBase func_150563_c(int p_150563_1_)
    {
        this.field_150609_ah = p_150563_1_;
        return this;
    }

    protected BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_)
    {
        this.color = p_150557_1_;

        if (p_150557_2_)
        {
            this.field_150609_ah = (p_150557_1_ & 16711422) >> 1;
        }
        else
        {
            this.field_150609_ah = p_150557_1_;
        }

        return this;
    }

    public int getSkyColorByTemp(float p_76731_1_)
    {
        p_76731_1_ /= 3.0F;

        if (p_76731_1_ < -1.0F)
        {
            p_76731_1_ = -1.0F;
        }

        if (p_76731_1_ > 1.0F)
        {
            p_76731_1_ = 1.0F;
        }

        return Color.getHSBColor(0.62222224F - p_76731_1_ * 0.05F, 0.5F + p_76731_1_ * 0.1F, 1.0F).getRGB();
    }

    public List getSpawnableList(EnumCreatureType p_76747_1_)
    {
        return p_76747_1_ == EnumCreatureType.monster ? this.spawnableMonsterList : (p_76747_1_ == EnumCreatureType.creature ? this.spawnableCreatureList : (p_76747_1_ == EnumCreatureType.waterCreature ? this.spawnableWaterCreatureList : (p_76747_1_ == EnumCreatureType.ambient ? this.spawnableCaveCreatureList : null)));
    }

    public boolean getEnableSnow()
    {
        return this.func_150559_j();
    }

    public boolean canSpawnLightningBolt()
    {
        return this.func_150559_j() ? false : this.enableRain;
    }

    public boolean isHighHumidity()
    {
        return this.rainfall > 0.85F;
    }

    public float getSpawningChance()
    {
        return 0.1F;
    }

    public final int getIntRainfall()
    {
        return (int)(this.rainfall * 65536.0F);
    }

    public final float getFloatRainfall()
    {
        return this.rainfall;
    }

    public final float getFloatTemperature(int p_150564_1_, int p_150564_2_, int p_150564_3_)
    {
        if (p_150564_2_ > 64)
        {
            float var4 = (float)temperatureNoise.func_151601_a((double)p_150564_1_ * 1.0D / 8.0D, (double)p_150564_3_ * 1.0D / 8.0D) * 4.0F;
            return this.temperature - (var4 + (float)p_150564_2_ - 64.0F) * 0.05F / 30.0F;
        }
        else
        {
            return this.temperature;
        }
    }

    public void decorate(World p_76728_1_, Random p_76728_2_, int p_76728_3_, int p_76728_4_)
    {
        this.theBiomeDecorator.decorateChunk(p_76728_1_, p_76728_2_, this, p_76728_3_, p_76728_4_);
    }

    public int getBiomeGrassColor(int p_150558_1_, int p_150558_2_, int p_150558_3_)
    {
        double var4 = (double)MathHelper.clamp_float(this.getFloatTemperature(p_150558_1_, p_150558_2_, p_150558_3_), 0.0F, 1.0F);
        double var6 = (double)MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
        return ColorizerGrass.getGrassColor(var4, var6);
    }

    public int getBiomeFoliageColor(int p_150571_1_, int p_150571_2_, int p_150571_3_)
    {
        double var4 = (double)MathHelper.clamp_float(this.getFloatTemperature(p_150571_1_, p_150571_2_, p_150571_3_), 0.0F, 1.0F);
        double var6 = (double)MathHelper.clamp_float(this.getFloatRainfall(), 0.0F, 1.0F);
        return ColorizerFoliage.getFoliageColor(var4, var6);
    }

    public boolean func_150559_j()
    {
        return this.enableSnow;
    }

    public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_, Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_, int p_150573_6_, double p_150573_7_)
    {
        this.genBiomeTerrain(p_150573_1_, p_150573_2_, p_150573_3_, p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
    }

    public final void genBiomeTerrain(World p_150560_1_, Random p_150560_2_, Block[] p_150560_3_, byte[] p_150560_4_, int p_150560_5_, int p_150560_6_, double p_150560_7_)
    {
        boolean var9 = true;
        Block var10 = this.topBlock;
        byte var11 = (byte)(this.topBlockMetadata & 255);
        Block var12 = this.fillerBlock;
        int var13 = -1;
        int var14 = (int)(p_150560_7_ / 3.0D + 3.0D + p_150560_2_.nextDouble() * 0.25D);
        int var15 = p_150560_5_ & 15;
        int var16 = p_150560_6_ & 15;
        int var17 = p_150560_3_.length / 256;

        for (int var18 = 255; var18 >= 0; --var18)
        {
            int var19 = (var16 * 16 + var15) * var17 + var18;

            if (var18 <= 0 + p_150560_2_.nextInt(5))
            {
                p_150560_3_[var19] = Blocks.bedrock;
            }
            else
            {
                Block var20 = p_150560_3_[var19];

                if (var20 != null && var20.getMaterial() != Material.air)
                {
                    if (var20 == Blocks.stone)
                    {
                        if (var13 == -1)
                        {
                            if (var14 <= 0)
                            {
                                var10 = null;
                                var11 = 0;
                                var12 = Blocks.stone;
                            }
                            else if (var18 >= 59 && var18 <= 64)
                            {
                                var10 = this.topBlock;
                                var11 = (byte)(this.topBlockMetadata & 255);
                                var12 = this.fillerBlock;
                            }

                            if (var18 < 63 && (var10 == null || var10.getMaterial() == Material.air))
                            {
                                if (this.getFloatTemperature(p_150560_5_, var18, p_150560_6_) < 0.15F)
                                {
                                    var10 = Blocks.ice;
                                    var11 = 0;
                                }
                                else
                                {
                                    var10 = Blocks.water;
                                    var11 = 0;
                                }
                            }

                            var13 = var14;

                            if (var18 >= 62)
                            {
                                p_150560_3_[var19] = var10;
                                p_150560_4_[var19] = var11;
                            }
                            else if (var18 < 56 - var14)
                            {
                                var10 = null;
                                var12 = Blocks.stone;
                                p_150560_3_[var19] = Blocks.gravel;
                            }
                            else
                            {
                                p_150560_3_[var19] = var12;
                            }
                        }
                        else if (var13 > 0)
                        {
                            --var13;
                            p_150560_3_[var19] = var12;

                            if (var13 == 0 && var12 == Blocks.sand)
                            {
                                var13 = p_150560_2_.nextInt(4) + Math.max(0, var18 - 63);
                                var12 = Blocks.sandstone;
                            }
                        }
                    }
                }
                else
                {
                    var13 = -1;
                }
            }
        }
    }

    protected BiomeGenBase createMutation()
    {
        return new BiomeGenMutated(this.biomeID + 128, this);
    }

    public Class getBiomeClass()
    {
        return this.getClass();
    }

    public boolean isEqualTo(BiomeGenBase p_150569_1_)
    {
        return p_150569_1_ == this ? true : (p_150569_1_ == null ? false : this.getBiomeClass() == p_150569_1_.getBiomeClass());
    }

    public BiomeGenBase.TempCategory getTempCategory()
    {
        return (double)this.temperature < 0.2D ? BiomeGenBase.TempCategory.COLD : ((double)this.temperature < 1.0D ? BiomeGenBase.TempCategory.MEDIUM : BiomeGenBase.TempCategory.WARM);
    }

    public static BiomeGenBase[] getBiomeGenArray()
    {
        return biomeList;
    }

    public static BiomeGenBase getBiome(int p_150568_0_)
    {
        if (p_150568_0_ >= 0 && p_150568_0_ <= biomeList.length)
        {
            return biomeList[p_150568_0_];
        }
        else
        {
            logger.warn("Biome ID is out of bounds: " + p_150568_0_ + ", defaulting to 0 (Ocean)");
            return ocean;
        }
    }

    static
    {
        plains.createMutation();
        desert.createMutation();
        forest.createMutation();
        taiga.createMutation();
        swampland.createMutation();
        icePlains.createMutation();
        jungle.createMutation();
        jungleEdge.createMutation();
        coldTaiga.createMutation();
        savanna.createMutation();
        savannaPlateau.createMutation();
        mesa.createMutation();
        mesaPlateau_F.createMutation();
        mesaPlateau.createMutation();
        birchForest.createMutation();
        birchForestHills.createMutation();
        roofedForest.createMutation();
        megaTaiga.createMutation();
        extremeHills.createMutation();
        extremeHillsPlus.createMutation();
        biomeList[megaTaigaHills.biomeID + 128] = biomeList[megaTaiga.biomeID + 128];
        BiomeGenBase[] var0 = biomeList;
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            BiomeGenBase var3 = var0[var2];

            if (var3 != null && var3.biomeID < 128)
            {
                explorationBiomesList.add(var3);
            }
        }

        explorationBiomesList.remove(hell);
        explorationBiomesList.remove(sky);
        explorationBiomesList.remove(frozenOcean);
        explorationBiomesList.remove(extremeHillsEdge);
        temperatureNoise = new NoiseGeneratorPerlin(new Random(1234L), 1);
        plantNoise = new NoiseGeneratorPerlin(new Random(2345L), 1);
        genTallFlowers = new WorldGenDoublePlant();
    }

    public static class Height
    {
        public float rootHeight;
        public float variation;
        private static final String __OBFID = "CL_00000159";

        public Height(float p_i45371_1_, float p_i45371_2_)
        {
            this.rootHeight = p_i45371_1_;
            this.variation = p_i45371_2_;
        }

        public BiomeGenBase.Height attenuate()
        {
            return new BiomeGenBase.Height(this.rootHeight * 0.8F, this.variation * 0.6F);
        }
    }

    public static class SpawnListEntry extends WeightedRandom.Item
    {
        public Class entityClass;
        public int minGroupCount;
        public int maxGroupCount;
        private static final String __OBFID = "CL_00000161";

        public SpawnListEntry(Class p_i1970_1_, int p_i1970_2_, int p_i1970_3_, int p_i1970_4_)
        {
            super(p_i1970_2_);
            this.entityClass = p_i1970_1_;
            this.minGroupCount = p_i1970_3_;
            this.maxGroupCount = p_i1970_4_;
        }

        public String toString()
        {
            return this.entityClass.getSimpleName() + "*(" + this.minGroupCount + "-" + this.maxGroupCount + "):" + this.itemWeight;
        }
    }

    public static enum TempCategory
    {
        OCEAN("OCEAN", 0),
        COLD("COLD", 1),
        MEDIUM("MEDIUM", 2),
        WARM("WARM", 3);

        private static final BiomeGenBase.TempCategory[] $VALUES = new BiomeGenBase.TempCategory[]{OCEAN, COLD, MEDIUM, WARM};
        private static final String __OBFID = "CL_00000160";

        private TempCategory(String p_i45372_1_, int p_i45372_2_) {}
    }
}
