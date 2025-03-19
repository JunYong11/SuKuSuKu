package com.web.sukusuku.repository;
import com.web.sukusuku.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // username 기준으로 조회 → 기본 제공됨 (findById)

    // 추가적으로 email 기준 조회하고 싶으면 예시로!
    User findByEmail(String email);

    // 혹시 level 기준으로 찾고 싶으면 예시로!
    List<User> findByLevel(String level);
}
