package net.minecraft.crash;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrashReport
{
    private static final Logger logger = LogManager.getLogger();
    private final String description;
    private final Throwable cause;
    private final CrashReportCategory theReportCategory = new CrashReportCategory(this, "System Details");
    private final List crashReportSections = new ArrayList();
    private File crashReportFile;
    private boolean field_85059_f = true;
    private StackTraceElement[] stacktrace = new StackTraceElement[0];
    private static final String __OBFID = "CL_00000990";

    public CrashReport(String p_i1348_1_, Throwable p_i1348_2_)
    {
        this.description = p_i1348_1_;
        this.cause = p_i1348_2_;
        this.populateEnvironment();
    }

    private void populateEnvironment()
    {
        this.theReportCategory.addCrashSectionCallable("Minecraft Version", new Callable()
        {
            private static final String __OBFID = "CL_00001197";
            public String call()
            {
                return "1.7.10";
            }
        });
        this.theReportCategory.addCrashSectionCallable("Operating System", new Callable()
        {
            private static final String __OBFID = "CL_00001222";
            public String call()
            {
                return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Java Version", new Callable()
        {
            private static final String __OBFID = "CL_00001248";
            public String call()
            {
                return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Java VM Version", new Callable()
        {
            private static final String __OBFID = "CL_00001275";
            public String call()
            {
                return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }
        });
        this.theReportCategory.addCrashSectionCallable("Memory", new Callable()
        {
            private static final String __OBFID = "CL_00001302";
            public String call()
            {
                Runtime var1 = Runtime.getRuntime();
                long var2 = var1.maxMemory();
                long var4 = var1.totalMemory();
                long var6 = var1.freeMemory();
                long var8 = var2 / 1024L / 1024L;
                long var10 = var4 / 1024L / 1024L;
                long var12 = var6 / 1024L / 1024L;
                return var6 + " bytes (" + var12 + " MB) / " + var4 + " bytes (" + var10 + " MB) up to " + var2 + " bytes (" + var8 + " MB)";
            }
        });
        this.theReportCategory.addCrashSectionCallable("JVM Flags", new Callable()
        {
            private static final String __OBFID = "CL_00001329";
            public String call()
            {
                RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
                List var2 = var1.getInputArguments();
                int var3 = 0;
                StringBuilder var4 = new StringBuilder();
                Iterator var5 = var2.iterator();

                while (var5.hasNext())
                {
                    String var6 = (String)var5.next();

                    if (var6.startsWith("-X"))
                    {
                        if (var3++ > 0)
                        {
                            var4.append(" ");
                        }

                        var4.append(var6);
                    }
                }

                return String.format("%d total; %s", new Object[] {Integer.valueOf(var3), var4.toString()});
            }
        });
        this.theReportCategory.addCrashSectionCallable("AABB Pool Size", new Callable()
        {
            private static final String __OBFID = "CL_00001355";
            public String call()
            {
                byte var1 = 0;
                int var2 = 56 * var1;
                int var3 = var2 / 1024 / 1024;
                byte var4 = 0;
                int var5 = 56 * var4;
                int var6 = var5 / 1024 / 1024;
                return var1 + " (" + var2 + " bytes; " + var3 + " MB) allocated, " + var4 + " (" + var5 + " bytes; " + var6 + " MB) used";
            }
        });
        this.theReportCategory.addCrashSectionCallable("IntCache", new Callable()
        {
            private static final String __OBFID = "CL_00001382";
            public String call() throws SecurityException, NoSuchFieldException, IllegalAccessException, IllegalArgumentException
            {
                return IntCache.getCacheSizes();
            }
        });
    }

    public String getDescription()
    {
        return this.description;
    }

    public Throwable getCrashCause()
    {
        return this.cause;
    }

    public void getSectionsInStringBuilder(StringBuilder p_71506_1_)
    {
        if ((this.stacktrace == null || this.stacktrace.length <= 0) && this.crashReportSections.size() > 0)
        {
            this.stacktrace = (StackTraceElement[])ArrayUtils.subarray(((CrashReportCategory)this.crashReportSections.get(0)).func_147152_a(), 0, 1);
        }

        if (this.stacktrace != null && this.stacktrace.length > 0)
        {
            p_71506_1_.append("-- Head --\n");
            p_71506_1_.append("Stacktrace:\n");
            StackTraceElement[] var2 = this.stacktrace;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                StackTraceElement var5 = var2[var4];
                p_71506_1_.append("\t").append("at ").append(var5.toString());
                p_71506_1_.append("\n");
            }

            p_71506_1_.append("\n");
        }

        Iterator var6 = this.crashReportSections.iterator();

        while (var6.hasNext())
        {
            CrashReportCategory var7 = (CrashReportCategory)var6.next();
            var7.appendToStringBuilder(p_71506_1_);
            p_71506_1_.append("\n\n");
        }

        this.theReportCategory.appendToStringBuilder(p_71506_1_);
    }

    public String getCauseStackTraceOrString()
    {
        StringWriter var1 = null;
        PrintWriter var2 = null;
        Object var3 = this.cause;

        if (((Throwable)var3).getMessage() == null)
        {
            if (var3 instanceof NullPointerException)
            {
                var3 = new NullPointerException(this.description);
            }
            else if (var3 instanceof StackOverflowError)
            {
                var3 = new StackOverflowError(this.description);
            }
            else if (var3 instanceof OutOfMemoryError)
            {
                var3 = new OutOfMemoryError(this.description);
            }

            ((Throwable)var3).setStackTrace(this.cause.getStackTrace());
        }

        String var4 = ((Throwable)var3).toString();

        try
        {
            var1 = new StringWriter();
            var2 = new PrintWriter(var1);
            ((Throwable)var3).printStackTrace(var2);
            var4 = var1.toString();
        }
        finally
        {
            IOUtils.closeQuietly(var1);
            IOUtils.closeQuietly(var2);
        }

        return var4;
    }

    public String getCompleteReport()
    {
        StringBuilder var1 = new StringBuilder();
        var1.append("---- Minecraft Crash Report ----\n");
        var1.append("// ");
        var1.append(getWittyComment());
        var1.append("\n\n");
        var1.append("Time: ");
        var1.append((new SimpleDateFormat()).format(new Date()));
        var1.append("\n");
        var1.append("Description: ");
        var1.append(this.description);
        var1.append("\n\n");
        var1.append(this.getCauseStackTraceOrString());
        var1.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int var2 = 0; var2 < 87; ++var2)
        {
            var1.append("-");
        }

        var1.append("\n\n");
        this.getSectionsInStringBuilder(var1);
        return var1.toString();
    }

    public File getFile()
    {
        return this.crashReportFile;
    }

    public boolean saveToFile(File p_147149_1_)
    {
        if (this.crashReportFile != null)
        {
            return false;
        }
        else
        {
            if (p_147149_1_.getParentFile() != null)
            {
                p_147149_1_.getParentFile().mkdirs();
            }

            try
            {
                FileWriter var2 = new FileWriter(p_147149_1_);
                var2.write(this.getCompleteReport());
                var2.close();
                this.crashReportFile = p_147149_1_;
                return true;
            }
            catch (Throwable var3)
            {
                logger.error("Could not save crash report to " + p_147149_1_, var3);
                return false;
            }
        }
    }

    public CrashReportCategory getCategory()
    {
        return this.theReportCategory;
    }

    public CrashReportCategory makeCategory(String p_85058_1_)
    {
        return this.makeCategoryDepth(p_85058_1_, 1);
    }

    public CrashReportCategory makeCategoryDepth(String p_85057_1_, int p_85057_2_)
    {
        CrashReportCategory var3 = new CrashReportCategory(this, p_85057_1_);

        if (this.field_85059_f)
        {
            int var4 = var3.getPrunedStackTrace(p_85057_2_);
            StackTraceElement[] var5 = this.cause.getStackTrace();
            StackTraceElement var6 = null;
            StackTraceElement var7 = null;
            int var8 = var5.length - var4;

            if (var8 < 0)
            {
                System.out.println("Negative index in crash report handler (" + var5.length + "/" + var4 + ")");
            }

            if (var5 != null && 0 <= var8 && var8 < var5.length)
            {
                var6 = var5[var8];

                if (var5.length + 1 - var4 < var5.length)
                {
                    var7 = var5[var5.length + 1 - var4];
                }
            }

            this.field_85059_f = var3.firstTwoElementsOfStackTraceMatch(var6, var7);

            if (var4 > 0 && !this.crashReportSections.isEmpty())
            {
                CrashReportCategory var9 = (CrashReportCategory)this.crashReportSections.get(this.crashReportSections.size() - 1);
                var9.trimStackTraceEntriesFromBottom(var4);
            }
            else if (var5 != null && var5.length >= var4 && 0 <= var8 && var8 < var5.length)
            {
                this.stacktrace = new StackTraceElement[var8];
                System.arraycopy(var5, 0, this.stacktrace, 0, this.stacktrace.length);
            }
            else
            {
                this.field_85059_f = false;
            }
        }

        this.crashReportSections.add(var3);
        return var3;
    }

    private static String getWittyComment()
    {
        String[] var0 = new String[] {"Who set us up the TNT?", "Everything\'s going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I\'m sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don\'t be sad. I\'ll do better next time, I promise!", "Don\'t be sad, have a hug! <3", "I just don\'t know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn\'t worry myself about that.", "I bet Cylons wouldn\'t have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I\'m Minecraft, and I\'m a crashaholic.", "Ooh. Shiny.", "This doesn\'t make any sense!", "Why is it breaking :(", "Don\'t do that.", "Ouch. That hurt :(", "You\'re mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

        try
        {
            return var0[(int)(System.nanoTime() % (long)var0.length)];
        }
        catch (Throwable var2)
        {
            return "Witty comment unavailable :(";
        }
    }

    public static CrashReport makeCrashReport(Throwable p_85055_0_, String p_85055_1_)
    {
        CrashReport var2;

        if (p_85055_0_ instanceof ReportedException)
        {
            var2 = ((ReportedException)p_85055_0_).getCrashReport();
        }
        else
        {
            var2 = new CrashReport(p_85055_1_, p_85055_0_);
        }

        return var2;
    }
}
