package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author wuyihua
 * @Date 2017/10/30
 * @todo
 */

public class PackageUtil {
    private static final String ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android";



    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }



//    public static List getComponentModels(Context context, String str, int i) {
//        Exception e;
//        List arrayList = new ArrayList();
//        try {
//            Context createPackageContext = context.createPackageContext(str, 0);
//            AssetManager assets = createPackageContext.getAssets();
//            XmlResourceParser openXmlResourceParser = assets.openXmlResourceParser((AssetManager.class.getMethod("addAssetPath", new Class[]{String.class}).invoke(assets, new Object[]{context.getPackageManager().getApplicationInfo(str, 0).sourceDir})).intValue(), "AndroidManifest.xml");
//            Resources resources = new Resources(assets, createPackageContext.getResources().getDisplayMetrics(), null);
//            while (true) {
//                int next = openXmlResourceParser.next();
//                if (next == 1) {
//                    break;
//                } else if (next == 2) {
//                    if (!((i == 0 && openXmlResourceParser.getName().equals("service")) || ((i == 1 && openXmlResourceParser.getName().equals(DBHelper.RECEIVER_TABLE_NAME)) || (i == 2 && openXmlResourceParser.getName().equals("activity"))))) {
//                        if (((i == 3 ? 1 : 0) & openXmlResourceParser.getName().equals("provider")) == 0) {
//                            continue;
//                        }
//                    }
//                    String attributeValue = openXmlResourceParser.getAttributeValue(ANDROID_NAMESPACE, "name");
//                    if (attributeValue == null) {
//                        for (int i2 = 0; i2 < openXmlResourceParser.getAttributeCount(); i2++) {
//                            if (TextUtils.isEmpty(openXmlResourceParser.getAttributeName(i2))) {
//                                int attributeNameResource = openXmlResourceParser.getAttributeNameResource(i2);
//                                if (attributeNameResource != 0 && resources.getResourceEntryName(attributeNameResource).equals("name")) {
//                                    attributeValue = openXmlResourceParser.getAttributeValue(i2);
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (attributeValue != null) {
//                        ComponentModel componentModel = new ComponentModel();
//                        if (!attributeValue.contains(".")) {
//                            componentModel.className = str + "." + attributeValue;
//                        } else if (attributeValue.startsWith(".")) {
//                            if (attributeValue.startsWith(".")) {
//                                attributeValue = str + attributeValue;
//                            }
//                            componentModel.className = attributeValue;
//                        } else {
//                            componentModel.className = attributeValue;
//                        }
//                        componentModel.packageName = str;
//                        arrayList.add(componentModel);
//                    } else {
//                        continue;
//                    }
//                }
//            }
//        } catch (PackageManager.NameNotFoundException e2) {
//            e = e2;
//        } catch (NoSuchMethodException e3) {
//            e = e3;
//        } catch (IllegalAccessException e4) {
//            e = e4;
//        } catch (IllegalArgumentException e5) {
//            e = e5;
//        } catch (InvocationTargetException e6) {
//            e = e6;
//        } catch (IOException e7) {
//            e = e7;
//        } catch (XmlPullParserException e8) {
//            e = e8;
//        }
//        return arrayList;
//        e.printStackTrace();
//        return arrayList;
//    }



    private void installToSystem(final String pkg, final String cmd1) {
//        runWithWait(new Runnable() {
//            public void run() {
//                try {
//                    String str = "";
//                    Utils.kill(pkg);
//                    Utils.remount("/system", InternalZipConstants.WRITE_MODE);
//                    str = new Utils("").cmdRoot(cmd1);
//                    System.out.println(str);
//                    if (str.contains("In /system space not found!")) {
//                        listAppsFragment.this.runToMain(new Runnable() {
//                            public void run() {
//                                listAppsFragment.this.showMessageInfo(listAppsFragment.this.getContext(), Utils.getText(R.string.warning), Utils.getText(R.string.no_space_in_system));
//                                listAppsFragment.removeDialogLP(11);
//                            }
//                        });
//                        return;
//                    }
//                    String system_app_dir = "/system/app/";
//                    try {
//                        if (new File("/system/priv-app").exists() && new File("/system/priv-app").list() != null) {
//                            system_app_dir = "/system/priv-app/";
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (listAppsFragment.api >= 21) {
//                        system_app_dir = "/system/priv-app/" + pkg + "/";
//                    }
//                    Utils.run_all("chmod 0644 " + system_app_dir + pkg + ".apk");
//                    Utils.run_all("chown 0.0 " + system_app_dir + pkg + ".apk");
//                    Utils.run_all("chown 0:0 " + system_app_dir + pkg + ".apk");
//                    listAppsFragment.this.runToMain(new Runnable() {
//                        public void run() {
//                            Utils.showDialogYesNo(Utils.getText(R.string.warning), Utils.getText(R.string.system_app_change), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Utils.reboot();
//                                }
//                            }, null, null);
//                            listAppsFragment.removeDialogLP(11);
//                        }
//                    });
//                } catch (Exception e2) {
//                    System.out.println("Move to system " + e2);
//                    listAppsFragment.this.runToMain(new Runnable() {
//                        public void run() {
//                            listAppsFragment.this.showMessage(Utils.getText(R.string.warning), Utils.getText(R.string.unknown_error));
//                            listAppsFragment.removeDialogLP(11);
//                        }
//                    });
//                }
//            }
//        });
    }

    private void moveToSystem(final String pkg, final String cmd1) {



//        runWithWait(new Runnable() {
//            public void run() {
//                try {
//                    String str = "";
//                    Utils.kill(pkg);
//                    Utils.removePkgFromSystem(pkg);
//                    str = new Utils("").cmdRoot(cmd1);
//                    System.out.println(str);
//                    if (str.contains("In /system space not found!")) {
//                        listAppsFragment.this.runToMain(new Runnable() {
//                            public void run() {
//                                listAppsFragment.this.showMessageInfo(listAppsFragment.this.getContext(), Utils.getText(R.string.warning), Utils.getText(R.string.no_space_in_system));
//                                listAppsFragment.removeDialogLP(11);
//                            }
//                        });
//                        return;
//                    }
//                    String system_app_dir = "/system/app/";
//                    try {
//                        if (new File("/system/priv-app").exists() && new File("/system/priv-app").list() != null) {
//                            system_app_dir = "/system/priv-app/";
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    Utils.run_all("chmod 0644 " + system_app_dir + pkg + ".apk");
//                    Utils.run_all("chown 0.0 " + system_app_dir + pkg + ".apk");
//                    Utils.run_all("chown 0:0 " + system_app_dir + pkg + ".apk");
//                    listAppsFragment.this.runToMain(new Runnable() {
//                        public void run() {
//                            listAppsFragment.getConfig().edit().remove(listAppsFragment.pli.pkgName).commit();
//                            listAppsFragment.plia.remove(listAppsFragment.pli.pkgName);
//                            listAppsFragment.plia.notifyDataSetChanged();
//                            Utils.showDialogYesNo(Utils.getText(R.string.warning), Utils.getText(R.string.system_app_change), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    Utils.reboot();
//                                }
//                            }, null, null);
//                            listAppsFragment.removeDialogLP(11);
//                        }
//                    });
//                } catch (Exception e2) {
//                    System.out.println("Move to system " + e2);
//                    listAppsFragment.this.runToMain(new Runnable() {
//                        public void run() {
//                            listAppsFragment.this.showMessage(Utils.getText(R.string.warning), Utils.getText(R.string.unknown_error));
//                            listAppsFragment.removeDialogLP(11);
//                        }
//                    });
//                }
//            }
//        });
    }


}
