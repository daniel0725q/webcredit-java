package com.quinterodaniel.webcreditjava.service.impl;

import com.quinterodaniel.webcreditjava.dto.DeviceDTO;
import com.quinterodaniel.webcreditjava.entity.Device;
import com.quinterodaniel.webcreditjava.repository.DeviceRepository;
import com.quinterodaniel.webcreditjava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class DevicesServiceImpl {
    private DeviceRepository deviceRepository;

    private UserRepository userRepository;

    @Autowired
    public DevicesServiceImpl(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }
    public void createDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        device.setId(deviceDTO.getId());
        device.setAvailable(Boolean.TRUE);
        device.setLastModified(new Date(System.currentTimeMillis()));

        deviceRepository.save(device);
    }

    public Optional<Device> getDevice(Long id) {
        return deviceRepository.findDeviceById(id);
    }

    public Boolean existsDeviceWithId(Long id) {
        return this.getDevice(id).isPresent();
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public void toggleDeviceStatus(Long id) throws Exception {
        var deviceOptional = getDevice(id);
        if (deviceOptional.isPresent()) {
            var device = deviceOptional.get();

            device.setAvailable(!device.getAvailable());
            device.setLastModified(new Date(System.currentTimeMillis()));
            deviceRepository.save(device);
        }
        throw new Exception("Device does not exist");
    }

    public void assignDeviceToUser(Long id, String email, String journey) throws Exception {
        var deviceOptional = getDevice(id);
        if (deviceOptional.isEmpty()) {
            throw new Exception("Device does not exist");
        }
        var user = userRepository.findByEmail(email);
        var device = deviceOptional.get();
        if (device.getUser() != null) {
            throw new Exception("Device has already an assign user. De-assign first");
        }
        device.setUser(user);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.DAY_OF_YEAR,1);
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(journey));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date sqlTomorrow = new Date(cal.getTimeInMillis());

        device.setLendUntil(sqlTomorrow);
        device.setAvailable(Boolean.FALSE);
        user.setDevice(device);
        deviceRepository.save(device);
        userRepository.save(user);
    }

    public void deAssignDeviceToUser(Long id) throws Exception {
        var deviceOptional = getDevice(id);
        if (deviceOptional.isEmpty()) {
            throw new Exception("Device does not exist");
        }
        var device = deviceOptional.get();
        device.setAvailable(Boolean.TRUE);
        var user = device.getUser();
        user.setDevice(null);
        device.setUser(null);
        deviceRepository.save(device);
        userRepository.save(user);
    }
}
