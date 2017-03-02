
package cn.ifreedomer.com.softmanager.mail;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


/**
 * 该类用于发送邮件给指定的邮箱
 *
 * @author wanghexin
 * @creation 2015年8月26日
 */
public class MailThread extends Thread {
    private final Context mContext;
    private boolean mValidate = true;
    private String mMailContent;
    private String mSubject;
    private String mHost;
    private String mHostPort;
    private String mUserName;
    private String mPassword;
    private String mFromAddr;
    private String[] mToAddrList;
    private String mProtocol = "smtp";

    /**
     * 发送邮件线程
     *
     * @param ctx
     */
    public MailThread(Context ctx) {
        mContext = ctx;
    }

    public MailThread setSubject(String subject) {
        mSubject = subject;
        return this;
    }

    public MailThread setContent(String content) {
        mMailContent = content;
        return this;
    }

    public MailThread setHost(String host) {
        mHost = host;
        return this;
    }

    public MailThread setHostPort(String hostPort) {
        mHostPort = hostPort;
        return this;
    }

    public MailThread setValidate(boolean validate) {
        mValidate = validate;
        return this;
    }

    public MailThread setUserName(String userName) {
        mUserName = userName;
        return this;
    }

    public MailThread setPassword(String pwd) {
        mPassword = pwd;
        return this;
    }

    public MailThread setFromAddress(String fromAddr) {
        mFromAddr = fromAddr;
        return this;
    }

    public MailThread setToAddressList(String[] toAddrList) {
        mToAddrList = toAddrList;
        return this;
    }

    public MailThread setProtocol(String protocol) {
        mProtocol = protocol;
        return this;
    }

    private boolean mailInfoReady() {
        if (TextUtils.isEmpty(mHost) || TextUtils.isEmpty(mHostPort)
                || TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPassword)
                || TextUtils.isEmpty(mFromAddr)
                || mToAddrList == null || mToAddrList.length == 0) {

            Log.e("MailThread", "mail cannot be sent as missing key param: "
                    + "host: " + mHost + ", port: " + mHostPort + ", username: " + mUserName
                    + ", password is null? : " + TextUtils.isEmpty(mPassword)
                    + ", dest addr is null? : " + (mToAddrList == null || mToAddrList.length == 0));

            return false;
        }
        return true;
    }

    private static final String EXMAIL_QQ_SMTP_HOST = "smtp.exmail.qq.com";
    private static final String EXMAIL_PORT = "25";

    /**
     * 获取Imap协议发送的senderinfo，该对象封装了腾讯企业邮箱的host、port还有协议为imap
     *
     * @return
     */
    public MailThread initIMAPInfo() {
        this.mHost = EXMAIL_QQ_SMTP_HOST;
        this.mHostPort = EXMAIL_PORT;
        this.mProtocol = "imap";
        return this;
    }

    private void sendEmail() {
        if (!mailInfoReady())
            return;

        try {
            MailSenderInfo mailInfo = new MailSenderInfo();
            mailInfo.setSubject(mSubject);
            mailInfo.setMailServerHost(mHost);
            mailInfo.setMailServerPort(mHostPort);
            mailInfo.setValidate(mValidate);
            mailInfo.setUserName(mUserName);
            mailInfo.setPassword(mPassword);
            mailInfo.setFromAddress(mFromAddr);
            mailInfo.setToAddressList(mToAddrList);
            mailInfo.setProtocol(mProtocol);

            try {
                mailInfo.setContent("device_model:"
                        + Build.MODEL
                        + "\nversion_release:"
                        + Build.VERSION.RELEASE
                        + "\nVersion:"
                        + mContext.getPackageManager().getPackageInfo(
                                mContext.getPackageName(), 0).versionName
                        + "\nPackageName:" + mContext.getPackageName()
                        + "\nLogInfo:" + mMailContent);
            } catch (NameNotFoundException e) {
                mailInfo.setContent("device_model:" + Build.MODEL
                        + "\nversion_release:" + Build.VERSION.RELEASE
                        + "\nVersion:unKnown" + "\nPackageName:"
                        + mContext.getPackageName() + "\nLogInfo:" + mMailContent);
            }
            SimpleMailSender sms = new SimpleMailSender();
            sms.sendTextMail(mailInfo);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    @Override
    public void run() {
        sendEmail();
        super.run();
    }
}
