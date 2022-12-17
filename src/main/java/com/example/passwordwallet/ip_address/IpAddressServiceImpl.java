package com.example.passwordwallet.ip_address;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
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

    @Override
    public void unblock(IpAddress ipAddress) {
        ipAddress.setIsPermLock(false);
        save(ipAddress);
    }

    @Override
    public String getIpAddressValue(HttpServletRequest request) throws UnknownHostException {
        String ipAddressValue = request.getRemoteAddr();
        if("0:0:0:0:0:0:0:1".equals(ipAddressValue) || "127.0.0.1".equals(ipAddressValue)) {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipAddressValue = inetAddress.getHostAddress();
        }

        return ipAddressValue;
    }

    @Override
    public IpAddress checkIpAddress(String ipAddressValue) {
        Optional<IpAddress> optionalIpAddress = findOneByIpAddress(ipAddressValue);
        IpAddress ipAddress;
        if (optionalIpAddress.isPresent()) {
            ipAddress = optionalIpAddress.get();
            if (ipAddress.getIsPermLock())
                throw new IllegalStateException("This ip address is permanently blocked!");
            if (ipAddress.getTempLockTime() != null && ipAddress.getTempLockTime().isAfter(LocalDateTime.now()))
                throw new IllegalStateException("This ip address is temporarily blocked!");
        }
        else {
            ipAddress = new IpAddress();
            ipAddress.setIpAddress(ipAddressValue);
            ipAddress.setIsPermLock(false);
        }

        return ipAddress;
    }
}
