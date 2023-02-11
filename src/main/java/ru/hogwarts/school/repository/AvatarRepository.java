package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Avatar;

import java.util.Collection;
import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findAvatarByStudent_Id(Long studentId);

//    @Query(value = "SELECT * FROM avatar LIMIT pageNumber OFFSET  pageSize", nativeQuery = true)
//    Collection<byte[]> getAvatarsOfStudents(Integer pageNumber, Integer pageSize);

}
