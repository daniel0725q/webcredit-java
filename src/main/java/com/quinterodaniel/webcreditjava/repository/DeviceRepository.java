package com.quinterodaniel.webcreditjava.repository;

import com.quinterodaniel.webcreditjava.entity.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
    List<Device> findAll();
    Optional<Device> findDeviceById(Long id);
}
