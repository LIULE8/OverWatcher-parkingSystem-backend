package com.oocl.overwatcher.service;

import com.oocl.overwatcher.dto.EmployeeDto;
import com.oocl.overwatcher.entities.ParkingLot;
import com.oocl.overwatcher.entities.User;
import com.oocl.overwatcher.repositories.ParkingLotRepository;
import com.oocl.overwatcher.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public UserService(UserRepository repository) {
        this.userRepository=repository;
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }
    public User findUserById(Long id){
        return userRepository.findById(id).get();
    }
    public User findUserByParkingBoy(Long id){
        return userRepository.findById(id).get();
    }
    public User  addUser(User user){
        String randomUsername = getRandomString(3);
        String randomPassword = getRandomString(3);
        user.setUserName(randomUsername);
        String encodePassword = new BCryptPasswordEncoder().encode(randomPassword);
        user.setPassword(encodePassword);
        user.setStatus("上班");
        userRepository.save(user);
        User user1 = new User();
        user1.setUserName(randomUsername);
        user1.setPassword(randomPassword);
        return user1;

    }
    public void updateStatus(Long id) {
        User user = userRepository.findById(id).get();
        if(user.getStatus().equals("下班")||user.getStatus().equals("早退")){
            if(ZonedDateTime.now().getHour()<10){
                userRepository.updateStatusById(id,"上班");
            }
        }
        else if (user.getStatus().equals("上班")){
            if(ZonedDateTime.now().getHour()>10){
                    userRepository.updateStatusById(id,"下班");
            }else {
                    userRepository.updateStatusById(id,"早退");
            }
        }else
            {
                userRepository.updateStatusById(id,user.getStatus());
            }
    }

    public boolean updateAlive(User user) {
        int updateNum = userRepository.updateAliveById(user.getId(),user.getAlive());
        return updateNum!=0;
    }
    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

    public User updateBasicMessageOfEmployees(Long id,User user){
        System.out.println("============== id ================");
        System.out.println(id);
        Optional<User> finder=userRepository.findById(id);
        if (finder.isPresent()){
            User old=finder.get();
            old.setName(user.getName());
            old.setEmail(user.getEmail());
            old.setPhone(user.getPhone());
            old.setUserName(user.getUserName());
            old.setRoleList(user.getRoleList());
            userRepository.save(old);
            return old;
        }else {
            return null;
        }

    }


    public List<User> findByName(String name) {
        return userRepository.findEmployeeByName(name);
    }

    public List<ParkingLot> finAllParkingLotByEmployeeId(Long employeeId){
       return userRepository.findById(employeeId).get().getParkingLotList();
    }

    public List<User> findByEmail(String email) {
        return userRepository.findEmployeeByEmail(email);
    }

    public List<User> findByPhone(String phone) {
        return userRepository.findEmployeeByPhone(phone);
    }

    public List<User> findAllEmployeesOnWork() {
        List<User> userList= userRepository.findAllEmployeesOnWork().stream().filter(user -> (user.getRoleList().get(0).getName().equals("员工")&&user.getParkingLotList().size()!=0)
        ).collect(Collectors.toList());
       for (int i=0; i<userList.size();i++){
           for (int j=0; j<userList.get(i).getParkingLotList().size();j++){
               if (userList.get(i).getParkingLotList().get(j).getSize()<=0){
                   userList.get(i).getParkingLotList().remove(userList.get(i).getParkingLotList().get(j));
               }
           }
       }
        return userList;
    }
    public boolean addParkingLotToParkingBoy(Long parkingBoyId,Long parkingLotId){
        User parkingBoy = userRepository.findById(parkingBoyId).get();
        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId).get();
        if (parkingBoy!=null&&parkingLot!=null&&parkingLot.getUser()==null){
            parkingLot.setUser(parkingBoy);
            parkingBoy.getParkingLotList().add(parkingLot);
            User saveParkingBoy = userRepository.save(parkingBoy);
            return saveParkingBoy!=null;
        }
        return false;
    }
    public  String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public List<ParkingLot> changeParking(List<Long> pakingLotIds) {
       for (Long id :pakingLotIds){
           ParkingLot parkingLot = parkingLotRepository.findById(id).get();
           parkingLot.setUser(null);
           parkingLotRepository.save(parkingLot);
       }
        return parkingLotRepository.findAll();
    }
    public List<ParkingLot> addParkingLotToPakingBoy(Long pakingBoyId,List<Long> pakingLotIds) {
        for (Long id :pakingLotIds){
        ParkingLot parkingLot = parkingLotRepository.findById(id).get();
        User user = userRepository.findById(pakingBoyId).get();
        parkingLot.setUser(user);
        parkingLotRepository.save(parkingLot);
        }
        return parkingLotRepository.findAll();
    }
}
