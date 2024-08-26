package com.example.domaincheck.service.check;

public interface DomainService {

    public boolean checkDomain(String f5ip, String host, boolean is_http, int f5_code);

}
