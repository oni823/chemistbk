package com.firefox.jump.chemist.repo;

import com.firefox.jump.chemist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    boolean existsByGithubId(String githubId);

    Optional<User> findByGithubId(String githubId);
}
