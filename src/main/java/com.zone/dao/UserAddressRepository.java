package com.zone.dao;

import com.zone.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress,Integer> {
    public List<UserAddress> findAllByUserId(Integer userId);
}
