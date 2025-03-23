package org.skuhub.skuhub.repository.notice;

import org.skuhub.skuhub.model.notice.NotificationHistoryJpaEntity;
import org.skuhub.skuhub.model.user.UserInfoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistoryJpaEntity, Long> {

    List<NotificationHistoryJpaEntity> findByUserKey(UserInfoJpaEntity userKey);
}
