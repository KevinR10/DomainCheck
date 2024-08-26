package com.example.domaincheck.controller.check;

import com.example.domaincheck.param.check.DomainCheckParam;
import com.example.domaincheck.service.check.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Component
@RequestMapping("/domain")
public class domainController {

    @Autowired
    private DomainService domainService;

    @PostMapping("/check")
    public String checkDomain(@RequestBody DomainCheckParam domainCheckParam){
        String domain = domainCheckParam.getDomain();
        String[] f5IP = domainCheckParam.getF5_ip().split(",");
        int f5Code = domainCheckParam.getF5_code();

        int successCount = 0;
        int totalRequests = 3;

        for (String ip : f5IP){
            for (int i = 0; i < totalRequests; i++){
                if (domainService.checkDomain(ip, domain, domainCheckParam.is_http, f5Code)) {
                    successCount++;
                }
                // 如果成功次数达到两次或更多，可以提前退出
                if (successCount >= 2) {
                    break;
                }
            }
        }
        return domain;
    }

}
