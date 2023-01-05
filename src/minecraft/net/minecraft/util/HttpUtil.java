package net.minecraft.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtil
{
    private static final AtomicInteger downloadThreadsStarted = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final String __OBFID = "CL_00001485";

    public static String buildPostString(Map data)
    {
        StringBuilder var1 = new StringBuilder();
        Iterator var2 = data.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (var1.length() > 0)
            {
                var1.append('&');
            }

            try
            {
                var1.append(URLEncoder.encode((String)var3.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException var6)
            {
                var6.printStackTrace();
            }

            if (var3.getValue() != null)
            {
                var1.append('=');

                try
                {
                    var1.append(URLEncoder.encode(var3.getValue().toString(), "UTF-8"));
                }
                catch (UnsupportedEncodingException var5)
                {
                    var5.printStackTrace();
                }
            }
        }

        return var1.toString();
    }

    public static String postMap(URL url, Map data, boolean skipLoggingErrors)
    {
        return post(url, buildPostString(data), skipLoggingErrors);
    }

    private static String post(URL url, String content, boolean skipLoggingErrors)
    {
        try
        {
            Proxy var3 = MinecraftServer.getServer() == null ? null : MinecraftServer.getServer().getServerProxy();

            if (var3 == null)
            {
                var3 = Proxy.NO_PROXY;
            }

            HttpURLConnection var4 = (HttpURLConnection)url.openConnection(var3);
            var4.setRequestMethod("POST");
            var4.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            var4.setRequestProperty("Content-Length", "" + content.getBytes().length);
            var4.setRequestProperty("Content-Language", "en-US");
            var4.setUseCaches(false);
            var4.setDoInput(true);
            var4.setDoOutput(true);
            DataOutputStream var5 = new DataOutputStream(var4.getOutputStream());
            var5.writeBytes(content);
            var5.flush();
            var5.close();
            BufferedReader var6 = new BufferedReader(new InputStreamReader(var4.getInputStream()));
            StringBuffer var8 = new StringBuffer();
            String var7;

            while ((var7 = var6.readLine()) != null)
            {
                var8.append(var7);
                var8.append('\r');
            }

            var6.close();
            return var8.toString();
        }
        catch (Exception var9)
        {
            if (!skipLoggingErrors)
            {
                logger.error("Could not post to " + url, var9);
            }

            return "";
        }
    }

    public static void downloadResourcePack(final File packFile, final String packName, final HttpUtil.DownloadListener completionListener, final Map requestData, final int maxSize, final IProgressUpdate loadingScreen, final Proxy proxy)
    {
        Thread var7 = new Thread(new Runnable()
        {
            private static final String __OBFID = "CL_00001486";
            public void run()
            {
                URLConnection var1 = null;
                InputStream var2 = null;
                DataOutputStream var3 = null;

                if (loadingScreen != null)
                {
                    loadingScreen.resetProgressAndMessage("Downloading Texture Pack");
                    loadingScreen.displayLoadingString("Making Request...");
                }

                try
                {
                    try
                    {
                        byte[] var4 = new byte[4096];
                        URL var5 = new URL(packName);
                        var1 = var5.openConnection(proxy);
                        float var6 = 0.0F;
                        float var7 = (float)requestData.entrySet().size();
                        Iterator var8 = requestData.entrySet().iterator();

                        while (var8.hasNext())
                        {
                            Entry var9 = (Entry)var8.next();
                            var1.setRequestProperty((String)var9.getKey(), (String)var9.getValue());

                            if (loadingScreen != null)
                            {
                                loadingScreen.setLoadingProgress((int)(++var6 / var7 * 100.0F));
                            }
                        }

                        var2 = var1.getInputStream();
                        var7 = (float)var1.getContentLength();
                        int var28 = var1.getContentLength();

                        if (loadingScreen != null)
                        {
                            loadingScreen.displayLoadingString(String.format("Downloading file (%.2f MB)...", new Object[] {Float.valueOf(var7 / 1000.0F / 1000.0F)}));
                        }

                        if (packFile.exists())
                        {
                            long var29 = packFile.length();

                            if (var29 == (long)var28)
                            {
                                completionListener.onDownloadComplete(packFile);

                                if (loadingScreen != null)
                                {
                                    loadingScreen.setDoneWorking();
                                }

                                return;
                            }

                            HttpUtil.logger.warn("Deleting " + packFile + " as it does not match what we currently have (" + var28 + " vs our " + var29 + ").");
                            packFile.delete();
                        }
                        else if (packFile.getParentFile() != null)
                        {
                            packFile.getParentFile().mkdirs();
                        }

                        var3 = new DataOutputStream(new FileOutputStream(packFile));

                        if (maxSize > 0 && var7 > (float)maxSize)
                        {
                            if (loadingScreen != null)
                            {
                                loadingScreen.setDoneWorking();
                            }

                            throw new IOException("Filesize is bigger than maximum allowed (file is " + var6 + ", limit is " + maxSize + ")");
                        }

                        boolean var30 = false;
                        int var31;

                        while ((var31 = var2.read(var4)) >= 0)
                        {
                            var6 += (float)var31;

                            if (loadingScreen != null)
                            {
                                loadingScreen.setLoadingProgress((int)(var6 / var7 * 100.0F));
                            }

                            if (maxSize > 0 && var6 > (float)maxSize)
                            {
                                if (loadingScreen != null)
                                {
                                    loadingScreen.setDoneWorking();
                                }

                                throw new IOException("Filesize was bigger than maximum allowed (got >= " + var6 + ", limit was " + maxSize + ")");
                            }

                            var3.write(var4, 0, var31);
                        }

                        completionListener.onDownloadComplete(packFile);

                        if (loadingScreen != null)
                        {
                            loadingScreen.setDoneWorking();
                            return;
                        }
                    }
                    catch (Throwable var26)
                    {
                        var26.printStackTrace();
                    }
                }
                finally
                {
                    try
                    {
                        if (var2 != null)
                        {
                            var2.close();
                        }
                    }
                    catch (IOException var25)
                    {
                        ;
                    }

                    try
                    {
                        if (var3 != null)
                        {
                            var3.close();
                        }
                    }
                    catch (IOException var24)
                    {
                        ;
                    }
                }
            }
        }, "File Downloader #" + downloadThreadsStarted.incrementAndGet());
        var7.setDaemon(true);
        var7.start();
    }

    public static int getSuitableLanPort() throws IOException
    {
        ServerSocket var0 = null;
        boolean var1 = true;
        int var10;

        try
        {
            var0 = new ServerSocket(0);
            var10 = var0.getLocalPort();
        }
        finally
        {
            try
            {
                if (var0 != null)
                {
                    var0.close();
                }
            }
            catch (IOException var8)
            {
                ;
            }
        }

        return var10;
    }

    public static String get(URL url) throws IOException
    {
        HttpURLConnection var1 = (HttpURLConnection)url.openConnection();
        var1.setRequestMethod("GET");
        BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.getInputStream()));
        StringBuilder var4 = new StringBuilder();
        String var3;

        while ((var3 = var2.readLine()) != null)
        {
            var4.append(var3);
            var4.append('\r');
        }

        var2.close();
        return var4.toString();
    }

    public interface DownloadListener
    {
        void onDownloadComplete(File p_148522_1_);
    }
}
