package com.android.server.os;

import android.util.Log;
import android.util.Slog;
import android.util.Xml;
import huawei.cust.HwCfgFilePolicy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class UDIDModelWhiteConfig {
    private static final String TAG = "UDIDModelWhiteConfig";
    private static final int UDID_MODEL_TYPE = 0;
    private static final String UDID_MODEL_WHITE_LIST = "udid_model_whitelist.xml";
    private static final String XML_ATTRIBUTE_NAME = "name";
    private static final String XML_UDID_MODEL = "udid_model";
    private static UDIDModelWhiteConfig udidModelWhiteConfig;
    private List<String> whiteModelInfos = new ArrayList();

    public static UDIDModelWhiteConfig getInstance() {
        if (udidModelWhiteConfig == null) {
            udidModelWhiteConfig = new UDIDModelWhiteConfig();
        }
        return udidModelWhiteConfig;
    }

    private UDIDModelWhiteConfig() {
        loadWhiteModelWhiteList();
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0069 A[Catch:{ FileNotFoundException -> 0x005c, XmlPullParserException -> 0x0059, IOException -> 0x0057, all -> 0x0054 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x009a A[SYNTHETIC, Splitter:B:37:0x009a] */
    /* JADX WARNING: Removed duplicated region for block: B:73:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    private void loadWhiteModelWhiteList() {
        StringBuilder sb;
        String str;
        InputStream inputStream = null;
        File udidModeFile = null;
        try {
            udidModeFile = HwCfgFilePolicy.getCfgFile("xml/udid_model_whitelist.xml", 0);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("udidModeFile exits? ");
            sb2.append(udidModeFile != null ? Boolean.valueOf(udidModeFile.exists()) : "udidModeFile is null");
            Slog.w(TAG, sb2.toString());
        } catch (NoClassDefFoundError e) {
            Log.d(TAG, "HwCfgFilePolicy NoClassDefFoundError");
        } catch (Exception e2) {
            Log.d(TAG, "HwCfgFilePolicy get udid_model_whitelist exception");
        }
        if (udidModeFile != null) {
            try {
                if (udidModeFile.exists()) {
                    inputStream = new FileInputStream(udidModeFile);
                    if (inputStream != null) {
                        XmlPullParser xmlParser = Xml.newPullParser();
                        xmlParser.setInput(inputStream, null);
                        for (int xmlEventType = xmlParser.next(); xmlEventType != 1; xmlEventType = xmlParser.next()) {
                            if (xmlEventType == 2 && XML_UDID_MODEL.equals(xmlParser.getName())) {
                                addUDIDModelInfo(xmlParser.getAttributeValue(null, "name"));
                            }
                        }
                    }
                    if (inputStream == null) {
                        try {
                            inputStream.close();
                            return;
                        } catch (IOException e3) {
                            e = e3;
                            str = TAG;
                            sb = new StringBuilder();
                        }
                    } else {
                        return;
                    }
                }
            } catch (FileNotFoundException e4) {
                Log.e(TAG, "load udid model config: " + e4.getMessage());
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        return;
                    } catch (IOException e5) {
                        e = e5;
                        str = TAG;
                        sb = new StringBuilder();
                    }
                } else {
                    return;
                }
            } catch (XmlPullParserException e6) {
                Log.e(TAG, "load udid model config: " + e6.getMessage());
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        return;
                    } catch (IOException e7) {
                        e = e7;
                        str = TAG;
                        sb = new StringBuilder();
                    }
                } else {
                    return;
                }
            } catch (IOException e8) {
                Log.e(TAG, "load udid model config: " + e8.getMessage());
                if (inputStream != null) {
                    try {
                        inputStream.close();
                        return;
                    } catch (IOException e9) {
                        e = e9;
                        str = TAG;
                        sb = new StringBuilder();
                    }
                } else {
                    return;
                }
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e10) {
                        Log.e(TAG, "load udid model config: IO Exception while closing stream: " + e10.getMessage());
                    }
                }
                throw th;
            }
        }
        Slog.w(TAG, "udid_model_whitelist.xml is not exist");
        if (inputStream != null) {
        }
        if (inputStream == null) {
        }
        sb.append("load udid model config: IO Exception while closing stream: ");
        sb.append(e.getMessage());
        Log.e(str, sb.toString());
    }

    public boolean isWhiteModelForUDID(String model) {
        int size = this.whiteModelInfos.size();
        if (size <= 0) {
            loadWhiteModelWhiteList();
            size = this.whiteModelInfos.size();
        }
        for (int i = 0; i < size; i++) {
            String modelInWhiteList = this.whiteModelInfos.get(i);
            if (model != null && modelInWhiteList != null && model.indexOf(modelInWhiteList) == 0) {
                return true;
            }
        }
        return false;
    }

    private void addUDIDModelInfo(String model) {
        if (!this.whiteModelInfos.contains(model)) {
            this.whiteModelInfos.add(model);
        }
    }
}
