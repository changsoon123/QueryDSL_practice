package com.example.study.repository;

import com.example.study.entity.Member;
import com.example.study.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager em; // 스프링 jpa에서는 사용하지 않는다. JPA 관리 핵심 객체이다.

    JPAQueryFactory factory = new JPAQueryFactory(em);

    @BeforeEach
    void testInsertData(){

        // jpaqueryfactory는 queryDSL로 쿼리문을 작성하기 위한 핵심 객체. (JPA가 더 큰 개념)
        // JPA를 구현하고 있는 spring-data-jpa안에 존재하고 있는 JPAQueryFactory 핵심 객체를 사용.


        Team teamA = Team.builder()
                .name("teamA")
                .build();

        Team teamB = Team.builder()
                .name("teamB")
                .build();

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = Member.builder()
                .userName("member1")
                .age(10)
                .team(teamA)
                .build();
        Member member2 = Member.builder()
                .userName("member2")
                .age(20)
                .team(teamA)
                .build();
        Member member3 = Member.builder()
                .userName("member3")
                .age(30)
                .team(teamB)
                .build();
        Member member4 = Member.builder()
                .userName("member4")
                .age(40)
                .team(teamB)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }
    
    @Test
    @DisplayName("start QueryDSL")
    void startQueryDSL() {
        //given
        
        //when
        
        //then
    }
}