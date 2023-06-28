package com.quinterodaniel.webcreditjava.controller;

import com.quinterodaniel.webcreditjava.dto.DeviceDTO;
import com.quinterodaniel.webcreditjava.service.impl.DevicesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DevicesController {

    private DevicesServiceImpl devicesService;

    @Autowired
    public DevicesController(DevicesServiceImpl devicesService) {
        this.devicesService = devicesService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody DeviceDTO deviceDTO) throws Exception {
        var existingDevice = devicesService.existsDeviceWithId(deviceDTO.getId());

        if (existingDevice) {
            throw new Exception("");
        }

        devicesService.createDevice(deviceDTO);
        return ResponseEntity.ok("");
    }

    @GetMapping
    public ResponseEntity getDevices() {
        return ResponseEntity.ok(devicesService.getAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity getDevice(@PathVariable String id) {
        return ResponseEntity.ok(devicesService.getDevice(Long.valueOf(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity changeDeviceStatus(@PathVariable String id) {
        try {
            devicesService.toggleDeviceStatus(Long.valueOf(id));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/assignToUser")
    public ResponseEntity assignToUser(@RequestParam("id") String id, @RequestParam("email") String email,
                                       @RequestParam("journey") String journey) {
        try {
            devicesService.assignDeviceToUser(Long.valueOf(id), email, journey);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deassign/{id}")
    public ResponseEntity deAssignDevice(@PathVariable("id") String id) {
        try {
            devicesService.deAssignDeviceToUser(Long.valueOf(id));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
