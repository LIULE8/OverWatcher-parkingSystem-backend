package com.oocl.overwatcher.controller;

import com.oocl.overwatcher.dto.ChangeParkingLotDTO;
import com.oocl.overwatcher.dto.EmployeeDto;
import com.oocl.overwatcher.dto.ParkingLotDTO;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping

    public ResponseEntity<List<EmployeeDto>> findAllUser() {
        List<EmployeeDto> employeeDtos = userService.findAllUser().stream().map(EmployeeDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(employeeDtos);
    }

    @PostMapping

    public ResponseEntity<User> addUser(@RequestBody User user) {
        user.getRoleList().forEach(role -> {
            role.getUsers().add(user);
        });
        User afterSaveUser = userService.addUser(user);
        if (afterSaveUser!=null) {
            return ResponseEntity.ok(afterSaveUser);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/{parkingBoyId}/parkingLotId/{parkingLotId}")

    public ResponseEntity addParkingLotToParkingBoy(@PathVariable Long parkingBoyId,@PathVariable Long parkingLotId){
        if (userService.addParkingLotToParkingBoy(parkingBoyId,parkingLotId)){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("{id}/parkingLots")

    public ResponseEntity<List<ParkingLot>> finAllParkingLotByEmployeeId(@PathVariable Long id){
        return ResponseEntity.ok(userService.finAllParkingLotByEmployeeId(id));
    }

    @PutMapping("/status/{id}")

    public User updateUserStatus(@PathVariable Long id) {
        userService.updateStatus(id);
        return userService.findUserById(id);
    }
    @GetMapping("/onWork")

    public List<User> findAllEmployeesOnWork(){
        return userService.findAllEmployeesOnWork();
    }

    @GetMapping("/{id}")

    public ResponseEntity<EmployeeDto> findOne(@PathVariable("id") Long id) {
        try {
            User user = userService.findOne(id).orElseThrow(() -> new Exception("找不到该用户"));
            return ResponseEntity.ok(new EmployeeDto(user));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/name")

    public ResponseEntity<List<EmployeeDto>> findUsersByName(String name) {
        try {
            if (StringUtils.isNotBlank(name)) {
                List<User> users = userService.findByName(name);
                List<EmployeeDto> collect = users.stream().map(EmployeeDto::new).collect(Collectors.toList());
                return ResponseEntity.ok(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @GetMapping("/email")

    public ResponseEntity<List<EmployeeDto>> findUsersByEmail(String email) {
        try {
            if (StringUtils.isNotBlank(email)) {
                List<User> users = userService.findByEmail(email);
                List<EmployeeDto> collect = users.stream().map(EmployeeDto::new).collect(Collectors.toList());
                return ResponseEntity.ok(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @GetMapping("/phone")

    public ResponseEntity<List<EmployeeDto>> findUsersByPhone(String phone) {
        try {
            if (StringUtils.isNotBlank(phone)) {
                List<User> users = userService.findByPhone(phone);
                List<EmployeeDto> collect = users.stream().map(EmployeeDto::new).collect(Collectors.toList());
                return ResponseEntity.ok(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/role")

    public ResponseEntity<List<EmployeeDto>> findUsersByRole(String role) {
        try {
            if (StringUtils.isNotBlank(role)) {
                List<User> users = userService.findAllUser();
                List<EmployeeDto> collect = users.stream()
                        .filter(user -> user.getRoleList() != null && user.getRoleList().size() > 0 && role.equals(user.getRoleList().get(0).getName()))
                        .map(EmployeeDto::new).collect(Collectors.toList());
                return ResponseEntity.ok(collect);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{id}/alive")
    public ResponseEntity updateAliveMessageOfEmployee(@RequestBody User user) {
        if (StringUtils.isNotBlank(user.getId() + "")) {
            if (userService.updateAlive(user)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/changeParkingLotOwner")
    public ResponseEntity<List<ParkingLotDTO>> findAllParkingBoysByCondition(@RequestBody ChangeParkingLotDTO changeParkingLotDTO) {
        String direction = changeParkingLotDTO.getDirection();
        List<Long> parkingLotIds = changeParkingLotDTO.getParkingLotId();
        Long parkingBoyId = changeParkingLotDTO.getUserId();
        if(StringUtils.isNotBlank(direction)&&"left".equals(direction)){
            return ResponseEntity.ok(userService.changeParking(parkingLotIds).stream().map(parkingLot -> new ParkingLotDTO(parkingLot)).collect(Collectors.toList()));
            //TODO 从停车小弟把该parkingLotId的停车场的userId改null
        }else if(StringUtils.isNotBlank(direction)&&"right".equals(direction)){
            //TODO 从停车小弟把该parkingLotId的停车场的userId改为参数的userId
           return ResponseEntity.ok(userService.addParkingLotToPakingBoy(parkingBoyId,parkingLotIds).stream().map(parkingLot -> new ParkingLotDTO(parkingLot)).collect(Collectors.toList()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{id}")
     public ResponseEntity<EmployeeDto> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        System.out.println("============== id ================");
        System.out.println(id);
        User newer=userService.updateBasicMessageOfEmployees(id,user);
        if (newer==null){
            return new ResponseEntity<EmployeeDto>(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<EmployeeDto>(new EmployeeDto(newer),HttpStatus.OK);
        }
    }
}
