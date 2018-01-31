package com.kk.securityhttp.domain;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.kk.utils.PathUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by zhangkai on 16/9/19.
 */
public class GoagalInfo {


    @JSONField(name = "public_key")
    public String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA5KaI8l7xplShIEB0Pwgm\n" +
            "MRX/3uGG9BDLPN6wbMmkkO7H1mIOXWB/Jdcl4/IMEuUDvUQyv3P+erJwZ1rvNsto\n" +
            "hXdhp2G7IqOzH6d3bj3Z6vBvsXP1ee1SgqUNrjX2dn02hMJ2Swt4ry3n3wEWusaW\n" +
            "mev4CSteSKGHhBn5j2Z5B+CBOqPzKPp2Hh23jnIH8LSbXmW0q85a851BPwmgGEan\n" +
            "5HBPq04QUjo6SQsW/7dLaaAXfUTYETe0HnpLaimcHl741ftGyrQvpkmqF93WiZZX\n" +
            "wlcDHSprf8yW0L0KA5jIwq7qBeu/H/H5vm6yVD5zvUIsD7htX0tIcXeMVAmMXFLX\n" +
            "35duvYDpTYgO+DsMgk2Q666j6OcEDVWNBDqGHc+uPvYzVF6wb3w3qbsqTnD0qb/p\n" +
            "WxpEdgK2BMVz+IPwdP6hDsDRc67LVftYqHJLKAfQt5T6uRImDizGzhhfIfJwGQxI\n" +
            "7TeJq0xWIwB+KDUbFPfTcq0RkaJ2C5cKIx08c7lYhrsPXbW+J/W4M5ZErbwcdj12\n" +
            "hrfV8TPx/RgpJcq82otrNthI3f4QdG4POUhdgSx4TvoGMTk6CnrJwALqkGl8OTfP\n" +
            "KojOucENSxcA4ERtBw4It8/X39Mk0aqa8/YBDSDDjb+gCu/Em4yYvrattNebBC1z\n" +
            "ulK9uJIXxVPi5tNd7KlwLRMCAwEAAQ==\n" +
            "-----END PUBLIC KEY-----";


    public ChannelInfo channelInfo = null;
    public AppUtils.AppInfo appInfo = null;

    public String uuid = "";
    public String channel = "default";

    public String configPath = "";


    private static GoagalInfo goagalInfo = new GoagalInfo();

    public static GoagalInfo get() {
        return goagalInfo;
    }

    public void init(Context context) {

        configPath = PathUtils.makeConfigDir(context);

        String result1 = null;
        String result2 = null;
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze1 = zf.getEntry("META-INF/gamechannel.json");
            InputStream in1 = zf.getInputStream(ze1);
            result1 = FileIOUtils.readFile2String(in1);
            LogUtils.i("渠道->" + result1);

            ZipEntry ze2 = zf.getEntry("META-INF/rsa_public_key.pem");
            InputStream in2 = zf.getInputStream(ze2);
            result2 = FileIOUtils.readFile2String(in2);
            LogUtils.i("公钥->" + result2);
        } catch (Exception e) {
            LogUtils.w("apk中gamechannel或rsa_public_key文件不存在");
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }

        String name = gamechannelFilename;
        String path = configPath + "/" + name;
        if (result1 != null) {
            FileIOUtils.writeFileFromString(path, result1);
        } else {
            result1 = FileIOUtils.readFile2String(path);
        }

        if (result1 != null) {
            channel = result1;
        }

        name = rasPublickeylFilename;
        path = configPath + "/" + name;
        if (result2 != null) {
            publicKey = getPublicKey(result2);
            FileIOUtils.writeFileFromString(path, result2);
        } else {
            result2 = FileIOUtils.readFile2String(path);
            if (result2 != null) {
                publicKey = getPublicKey(result2);
            }
        }

        channelInfo = getChannelInfo();  //渠道信息
        uuid = getUid(context);  //唯一标识
        appInfo = AppUtils.getAppInfo();  //app信息

    }

    private String rasPublickeylFilename = "rsa_public_key.pem";
    private String gamechannelFilename = "gamechannel.json";

    public GoagalInfo setRasPublickeylFilename(String rasPublickeylFilename) {
        this.rasPublickeylFilename = rasPublickeylFilename;
        return this;
    }

    public GoagalInfo setGamechannelFilename(String gamechannelFilename) {
        this.gamechannelFilename = gamechannelFilename;
        return this;
    }

    private ChannelInfo getChannelInfo() {
        try {
            ChannelInfo channelInfo = JSON.parseObject(channel, ChannelInfo.class);
            return channelInfo;
        } catch (Exception e) {
            LogUtils.w("渠道信息解析错误->" + e.getMessage());
        }
        return null;
    }


    public String getPublicKey() {
        publicKey = getPublicKey(publicKey);
        return publicKey;
    }

    public String getPublicKey(String key) {
        return key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\r", "")
                .replace("\n", "");
    }

    public String getUid(Context context) {
        String uid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (StringUtils.isEmpty(uid) || uid.equals("02:00:00:00:00:00")) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            uid = wInfo.getMacAddress();
        }

        if (StringUtils.isEmpty(uid)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            uid = telephonyManager.getDeviceId();
        }

        if (uid == null) {
            uid = "";
        }

        return uid;
    }
}
