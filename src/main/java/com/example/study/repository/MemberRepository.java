package com.example.study.repository;

import com.example.study.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    //쿼리를 실행하려는 예
    /*
    findUser(String name, int age); //매개 변수 두개를 만족하는 sql 칼럼을 조회

    @Query("SELECT m FROM Member m WHERE m.userName = :name AND m.age = :age")
    findUser(String name, int age);

    @Query("SELECT m FROM Member m WHERE m.userName = :name")
    findUser(String name);

    @Query("SELECT m FROM Member m WHERE m.age = :age")
    findUser(int age);

    @Query("SELECT m FROM Member m")
    findUser();
     */
}
