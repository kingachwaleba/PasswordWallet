package com.example.passwordwallet.ip_address;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@RestController
public class IpAddressController {

    private final IpAddressService ipAddressService;

    public IpAddressController(IpAddressService ipAddressService) {
        this.ipAddressService = ipAddressService;
    }

    @PostMapping("/unblock")
    public ResponseEntity<?> unblockIpAddress(HttpServletRequest request) throws UnknownHostException {
        String ipAddressValue = ipAddressService.getIpAddressValue(request);
        IpAddress ipAddress = ipAddressService.findOneByIpAddress(ipAddressValue).orElseThrow(RuntimeException::new);
        ipAddressService.unblock(ipAddress);

        return ResponseEntity.ok("This ip address was unblocked correctly");
    }
}
