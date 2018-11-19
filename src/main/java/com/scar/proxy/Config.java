package com.scar.proxy;

/**
 * @description
 * @author: scar
 * @create 2018-11-18 23:40
 */
public class Config {
    private String remarks = "";
    private String id = "";
    private String server;
    private long server_port;
    private long server_udp_prot;
    private String password;
    private String method;
    private String protocal = "";
    private String protocalparam = "";
    private String obfs = "";
    private String obfsparam = "";
    private String remarks_base64 = "";
    private String group = "";
    private boolean enable = true;
    private boolean udp_over_tcp;

    public Config() {}
    public Config(String server, long server_port, String password, String method) {
        this.server = server;
        this.server_port = server_port;
        this.password = password;
        this.method = method;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public long getServer_port() {
        return server_port;
    }

    public void setServer_port(long server_port) {
        this.server_port = server_port;
    }

    public long getServer_udp_prot() {
        return server_udp_prot;
    }

    public void setServer_udp_prot(long server_udp_prot) {
        this.server_udp_prot = server_udp_prot;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getProtocalparam() {
        return protocalparam;
    }

    public void setProtocalparam(String protocalparam) {
        this.protocalparam = protocalparam;
    }

    public String getObfs() {
        return obfs;
    }

    public void setObfs(String obfs) {
        this.obfs = obfs;
    }

    public String getObfsparam() {
        return obfsparam;
    }

    public void setObfsparam(String obfsparam) {
        this.obfsparam = obfsparam;
    }

    public String getRemarks_base64() {
        return remarks_base64;
    }

    public void setRemarks_base64(String remarks_base64) {
        this.remarks_base64 = remarks_base64;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isUdp_over_tcp() {
        return udp_over_tcp;
    }

    public void setUdp_over_tcp(boolean udp_over_tcp) {
        this.udp_over_tcp = udp_over_tcp;
    }
}
