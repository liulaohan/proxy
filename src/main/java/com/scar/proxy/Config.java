package com.scar.proxy;

import lombok.Data;

/**
 * @description
 * @author: scar
 * @create 2018-11-18 23:40
 */
@Data
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
}
