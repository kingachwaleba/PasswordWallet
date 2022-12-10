package com.example.passwordwallet.ip_address;


import java.util.Optional;

public interface IpAddressService {

    IpAddress save(IpAddress ipAddress);
    Optional<IpAddress> findOneByIpAddress(String ipAddress);
}
