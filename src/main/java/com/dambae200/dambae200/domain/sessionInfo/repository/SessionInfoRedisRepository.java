package com.dambae200.dambae200.domain.sessionInfo.repository;


import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfoRedis;
import org.springframework.data.repository.CrudRepository;

public interface SessionInfoRedisRepository extends CrudRepository<SessionInfoRedis, String> {
}
