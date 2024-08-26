package com.example.domaincheck.param.check;

import lombok.Data;

@Data

public class DomainCheckParam {
    private String domain;
    private boolean is_internet;
    public boolean is_http;
    private String f5_ip;
    private int f5_code;
}
