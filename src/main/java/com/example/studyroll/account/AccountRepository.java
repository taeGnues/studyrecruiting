package com.example.studyroll.account;

import com.example.studyroll.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true) // 성능 이점얻기.
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    Account findByEmail(String mail);
}
