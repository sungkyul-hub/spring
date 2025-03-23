package org.skuhub.skuhub.repository.users;


import org.skuhub.skuhub.model.user.UserSetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserSetRepository extends JpaRepository<UserSetEntity, Long> {
    Optional<UserSetEntity> findByEmail(String email);
}
