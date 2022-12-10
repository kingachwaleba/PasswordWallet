package com.example.passwordwallet.ip_address;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IpAddressServiceImpl implements IpAddressService {

    private final IpAddressRepository ipAddressRepository;

    public IpAddressServiceImpl(IpAddressRepository ipAddressRepository) {
        this.ipAddressRepository = ipAddressRepository;
    }

    @Override
    public IpAddress save(IpAddress ipAddress) {
        return ipAddressRepository.save(ipAddress);
    }

    @Override
    public Optional<IpAddress> findOneByIpAddress(String ipAddress) {
        return ipAddressRepository.findByIpAddress(ipAddress);
    }
}
