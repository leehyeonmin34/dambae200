package com.dambae200.dambae200.global.cache;

import antlr.collections.impl.IntRange;
import com.dambae200.dambae200.domain.user.domain.User;
import com.dambae200.dambae200.global.cache.config.CacheType;
import com.dambae200.dambae200.global.cache.service.CacheModule;
import com.dambae200.dambae200.global.cache.service.CacheableRepository;
import com.dambae200.dambae200.global.domain.TestDomain;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.font.NumericShaper;
import java.time.LocalDateTime;
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

    private static final int TEST_DATA_NUM = 10;
    private long before;
    private long after;
    private List<TestDomain> entities = buildTestDomains();
    private List<Long> objectIdList = entities.stream()
            .map(TestDomain::getId).collect(Collectors.toList());;
    private List<Integer> integerList = IntStream.range(0,TEST_DATA_NUM).boxed().collect(Collectors.toList());

    @BeforeEach
    public void init(){
        before = System.currentTimeMillis();
    }

    @AfterEach
    public void end(){
        after = System.currentTimeMillis();
        System.out.println("소요시간 : " + (after - before) + "ms");

        cacheModule.evictAllByKeys(CacheType.TEST, integerList);
        cacheModule.evictAllByKeys(CacheType.TEST, objectIdList);
    }

    @Test
    @DisplayName("각각, Object")
    public void writeAllManuallyObjectTest(){
        entities.forEach(entity -> cacheModule.put(CacheType.TEST, entity.getId(), entity));
    }

    @Test
    @DisplayName("파이프라인, Object")
    public void writeAllPipelinedObjectTest(){
        cacheModule.putAll(CacheType.TEST, entities, e -> e.getId());
    }

    @Test
    @DisplayName("각각, Integer")
    public void writeAllManuallyIntegerTest(){
        integerList.forEach(i -> cacheModule.put(CacheType.TEST, i, i.intValue()));
    }

    @Test
    @DisplayName("파이프라인, Integer")
    public void writeAllPipelinedIntegerTest(){
        cacheModule.putAll(CacheType.TEST, integerList, i -> i.intValue());
    }

//               각각          파이프라인
//         Integer Object  Integer Object
// 3만개      2845   2078    358     321
// 1만개      1340    803    173     127
// 5천개      1116    543    173     128
// 2천개       699    254     87      44
// 1천개       967    285     98      34
// 100개      635     86     117     27
// 10개       425     34      69     20


    private List<TestDomain> buildTestDomains() {

        return IntStream.range(0, TEST_DATA_NUM)
                .mapToObj(i -> {
                    TestDomain entity = TestDomain.builder().createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .id(7000L + i)
                            .pw("password" + i)
                            .email("email" + i + "@example.com")
                            .nickname("nickname" + i)
                            .build();

                    return entity;
                })
                .collect(Collectors.toList());
    }



}
