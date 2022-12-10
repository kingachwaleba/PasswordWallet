package com.example.passwordwallet.ip_address;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IpAddressRepository extends JpaRepository<IpAddress, Integer> {

    Optional<IpAddress> findByIpAddress(String ipAddress);
}
