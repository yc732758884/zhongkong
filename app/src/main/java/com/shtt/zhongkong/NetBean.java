package com.shtt.zhongkong;

import android.nfc.tech.NfcB;

public class NetBean {
   private static NetBean  nb;
    private NetBean() {

    }

    public  static NetBean getIntanse(){
        if (nb==null){
            nb=new NetBean();
        }
        return  nb;
    }

    private  String baseUrl="http://192.168.1.12:9090/";
    private  String xhjj="http://192.168.1.3:9090/";
    private  String xhjjcyl="http://192.168.1.4:9090/";
    private  String zss="http://192.168.1.7:9091/";
    private  String czws="http://192.168.1.9:9092/";
    private  String gyws="http://192.168.1.10:9093/";
    private  String fgcl="http://192.168.1.11:9090/";
    private  String xhzl="http://192.168.1.8:9090/";

    public String getXhzl() {
        return xhzl;
    }

    public void setXhzl(String xhzl) {
        this.xhzl = xhzl;
    }

    public String getFgcl() {
        return fgcl;
    }

    public void setFgcl(String fgcl) {
        this.fgcl = fgcl;
    }

    public String getGyws() {
        return gyws;
    }

    public void setGyws(String gyws) {
        this.gyws = gyws;
    }

    public String getCzws() {
        return czws;
    }

    public void setCzws(String czws) {
        this.czws = czws;
    }

    public String getZss() {
        return zss;
    }

    public void setZss(String zss) {
        this.zss = zss;
    }

    public String getXhjjcyl() {
        return xhjjcyl;
    }

    public void setXhjjcyl(String xhjjcyl) {
        this.xhjjcyl = xhjjcyl;
    }

    public static NetBean getNb() {
        return nb;
    }

    public static void setNb(NetBean nb) {
        NetBean.nb = nb;
    }

    public String getXhjj() {
        return xhjj;
    }

    public void setXhjj(String xhjj) {
        this.xhjj = xhjj;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
