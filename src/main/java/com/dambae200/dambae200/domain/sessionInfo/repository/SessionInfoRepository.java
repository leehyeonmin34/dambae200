package com.dambae200.dambae200.domain.sessionInfo.repository;

import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionInfoRepository extends JpaRepository<SessionInfo, String> {
}
