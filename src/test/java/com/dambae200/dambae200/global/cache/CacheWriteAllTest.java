package com.dambae200.dambae200.global.cache;

import antlr.collections.impl.IntRange;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.font.NumericShaper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class CacheWriteAllTest {

    @Autowired
    private CacheableRepository userCacheableRepository;

    @Autowired
    private JpaRepository userRepository;

    @Autowired
    private CacheModule cacheModule;

    private long before;
    private long after;
    private List<User> users;
    private List<Integer> idList = IntStream.range(0,20000).boxed().collect(Collectors.toList());;

    @BeforeEach
    public void init(){
//        users = buildUsers();
//        userRepository.deleteAll(users);
        before = System.currentTimeMillis();
    }

    @AfterEach
    public void end(){
        after = System.currentTimeMillis();
        System.out.println("소요시간 : " + (after - before) + "ms");

//        List<Long> idList = users.stream()
//                .map(User::getId).collect(Collectors.toList());
//        cacheModule.evictAllByKeys(CacheType.USER, idList);
//        userCacheableRepository.deleteAllThrough(idList);
        cacheModule.evictAllByKeys(CacheType.TEST, idList);
    }

    @Test
    public void writeAllManuallyTest(){
        idList.forEach(i -> cacheModule.put(CacheType.TEST, i, i));
    }

    @Test
    public void writeAllPipelinedTest(){
        cacheModule.putAll(CacheType.TEST, idList, i -> i);
    }

    // Wrtie-All-Through
    // 갯수   각각    파이프라인
    // 5개   66ms     755ms
    // 100개  611ms  1082ms
    // 1000개 3717ms 3046ms
    // 2000개 4420ms 4089ms

    // Put Cache (User)
    // 갯수   각각    파이프라인
    // 2000개 425ms 872ms

    // Put Cache (Integer)
    // 갯수    각각    파이프라인
    //  5개   8ms 510ms
    // 1천개  256ms 698ms
    // 2만개  2573ms 1262ms

    private List<User> buildUsers() {

        List<User> lst = IntStream.range(0, 5)
                .mapToObj(i -> new User("email" + i, "pw" + i, "nickname" + i))
                .collect(Collectors.toList());
        List<User> users = userRepository.saveAll(lst);

        return users;
    }



}
