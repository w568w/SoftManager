package cn.ifreedomer.com.softmanager.util;

import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.bean.PermissionDetail;
import cn.ifreedomer.com.softmanager.bean.PermissionGroup;

/**
 * @author:eavawu
 * @since: 04/11/2017.
 * TODO:
 */

public class XmlUtil {
    private static final String TAG = XmlUtil.class.getSimpleName();

    public static List<PermissionGroup> parsePermissionGroup(XmlResourceParser xmlParser) {
        List<PermissionGroup> permissionGroupDetailList = new ArrayList<>();
        try {
            PermissionGroup permissionGroup = null;
            int event = xmlParser.getEventType();   //先获取当前解析器光标在哪
            while (event != XmlPullParser.END_DOCUMENT) {    //如果还没到文档的结束标志，那么就继续往下处理
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "xml解析开始");
                        break;
                    case XmlPullParser.START_TAG:
                        //一般都是获取标签的属性值，所以在这里数据你需要的数据
                        Log.d(TAG, "当前标签是：" + xmlParser.getName());
                        if (xmlParser.getName().equals("permission_group")) {
                            permissionGroup = new PermissionGroup();

                            String name = xmlParser.getAttributeValue(null, "name");
                            LogUtil.e(TAG, "permission_group name = " + name);
                            permissionGroup.setName(name);
                        }

                        if (xmlParser.getName().equals("permission")) {
//                            xmlParser.getAttribute
                            //两种方法获取属性值
                            PermissionDetail permissionDetail = new PermissionDetail();
                            permissionDetail.setPermission(xmlParser.getAttributeValue(null, "name"));
                            permissionDetail.setTitle(xmlParser.getAttributeValue(null, "title"));
                            permissionDetail.setPermissionDes(xmlParser.getAttributeValue(null, "des"));
                            permissionDetail.setGroup(permissionGroup.getName());
                            permissionGroup.getPermissionDetailList().add(permissionDetail);
                            LogUtil.e(TAG, permissionGroup.toString());

//                            Log.d(TAG, "permission name=" + xmlParser.getAttributeName(0)
//                                    + ": " + xmlParser.getAttributeValue(0));
                        }
                        break;
                    case XmlPullParser.TEXT:
                        Log.d(TAG, "Text:" + xmlParser.getText());
                        break;
                    case XmlPullParser.END_TAG:
                        permissionGroupDetailList.add(permissionGroup);
                        break;
                    default:
                        break;
                }
                event = xmlParser.next();   //将当前解析器光标往下一步移
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, permissionGroupDetailList.toString());
        return permissionGroupDetailList;
    }

}
