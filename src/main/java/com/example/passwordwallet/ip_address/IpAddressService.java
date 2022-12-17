package com.example.passwordwallet.ip_address;


import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.Optional;

public interface IpAddressService {

    IpAddress save(IpAddress ipAddress);
    Optional<IpAddress> findOneByIpAddress(String ipAddress);
    void unblock(IpAddress ipAddress);
    String getIpAddressValue(HttpServletRequest request) throws UnknownHostException;
    IpAddress checkIpAddress(String ipAddressValue);
}
