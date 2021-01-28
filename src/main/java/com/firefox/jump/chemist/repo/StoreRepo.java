package com.firefox.jump.chemist.repo;


import com.firefox.jump.chemist.dto.SDto;
import com.firefox.jump.chemist.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepo extends JpaRepository<Store, Integer> {

    @Query(value="select name, COUNT(*) as num from sys_store where user_id = ?1 group by name order by num desc", nativeQuery=true)
    List<SDto> genGroupList(Integer userid);
}
