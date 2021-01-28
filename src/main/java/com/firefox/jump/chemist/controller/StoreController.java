package com.firefox.jump.chemist.controller;

import com.firefox.jump.chemist.dto.ResultDto;
import com.firefox.jump.chemist.dto.SDto;
import com.firefox.jump.chemist.dto.StoreDto;
import com.firefox.jump.chemist.model.Store;
import com.firefox.jump.chemist.model.User;
import com.firefox.jump.chemist.repo.StoreRepo;
import com.firefox.jump.chemist.repo.UserRepo;
import com.firefox.jump.chemist.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@CrossOrigin
public class StoreController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StoreRepo storeRepo;

    @GetMapping("/store")
    public ResponseEntity<ResultDto> getStoreList(@RequestParam(value = "jwt") String input) {
        log.info(input);
        Integer userid = JwtTokenProvider.verifyJWT(input);
        Optional<User> opt = userRepo.findById(userid);
        if(opt.isPresent()){
            User user = opt.get();
            List<SDto> list = storeRepo.genGroupList(userid);
            List<StoreDto> result = new ArrayList<>();
            for(SDto ea : list){
                result.add(StoreDto.builder().storeName(ea.getName()).build());
            }
            return ResponseEntity.ok(ResultDto.success(result));
        }
        return ResponseEntity.ok(ResultDto.error("error"));
    }

    @PostMapping("/store")
    public ResponseEntity<ResultDto>  addQueryStore(@RequestBody StoreDto input) {
        Integer userid = JwtTokenProvider.verifyJWT(input.getJwt());
        Optional<User> opt = userRepo.findById(userid);
        Store store = null;
        if(opt.isPresent()){
            User user = opt.get();
            store = storeRepo.saveAndFlush(Store.builder().name(input.getStoreName()).userId(user.getId()).build());
            log.info("new Stor created:" + store.getName());
        }
        return ResponseEntity.ok(ResultDto.success(store));
    }

}
