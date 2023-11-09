package ro.tuc.ds2020.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.PersonTabelDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.PersonTabel;
import ro.tuc.ds2020.services.DeviceService;
import ro.tuc.ds2020.services.PersonTableService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;
    private final PersonTableService personTableService;

    @Autowired
    public DeviceController(DeviceService deviceService, PersonTableService personTableService) {
        this.deviceService = deviceService;
        this.personTableService = personTableService;
    }

    @GetMapping(value = "/getAll/{personId}")
    public ResponseEntity<List<DeviceDTO>> getDevices(@PathVariable(name = "personId") UUID personId) {
        List<DeviceDTO> dtos = deviceService.findDevicesForPerson(personId);
        for (DeviceDTO dto : dtos) {
            Link deviceLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(deviceLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDetailsDTO personDTO, @PathVariable("id") UUID id) {
        UUID personID = deviceService.insert(id,personDTO);
        return new ResponseEntity<>(personID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDetailsDTO> getDevice(@PathVariable("id") UUID personId) {
        DeviceDetailsDTO dto = deviceService.findPersonById(personId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllDevices/{id}")
    public ResponseEntity<List<Device>> getDeviceListOfUser(@PathVariable("id") UUID personId) {

        List<Device> deviceList = deviceService.getAllDevicesForUser(personId);
        return new ResponseEntity<>(deviceList, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/{address}/{description}/{maxHours}")
    public ResponseEntity<Device> updateDevie(@PathVariable("id") UUID id, @PathVariable("address") String address, @PathVariable("description") String description, @PathVariable("maxHours") int MaxHours)
    {
        Device device = deviceService.updateDevice(id, address, description, MaxHours);
        return new ResponseEntity<>(device, HttpStatus.OK);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") UUID id)
    {
        deviceService.deleteDevice(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @PostMapping("/device")
    public ResponseEntity<UUID> insertDeviceForPerson(@RequestBody UUID personID) {
        // Create a new Device associated with the provided Person's ID
        DeviceDetailsDTO deviceDTO = new DeviceDetailsDTO();
        PersonTabel personTabel = personTableService.findPerson(personID);

        //deviceDTO.setId(personTabel);
      //  deviceDTO.setAddress("aaa");
       // deviceDTO.setName("bbb");
        // Set other device details

        UUID deviceID = deviceService.insertFromPerson(deviceDTO); // Implement this method

        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

}
