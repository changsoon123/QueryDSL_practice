package com.example.study.repository;

import com.example.study.entity.Member;
import com.example.study.entity.Team;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.study.entity.QMember.member;
import static com.example.study.entity.QTeam.team;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    JPAQueryFactory factory;

    @BeforeEach
    void settingObject(){
        factory = new JPAQueryFactory(em);
    }


    void testInsertData(){

        // jpaqueryfactory는 queryDSL로 쿼리문을 작성하기 위한 핵심 객체. (JPA가 더 큰 개념)
        // JPA를 구현하고 있는 spring-data-jpa안에 존재하고 있는 JPAQueryFactory 핵심 객체를 사용.


//        Team teamA = Team.builder()
//                .name("teamA")
//                .build();
//
//        Team teamB = Team.builder()
//                .name("teamB")
//                .build();
//
//        teamRepository.save(teamA);
//        teamRepository.save(teamB);

        Member member1 = Member.builder()
                .userName("member5")
                .age(50)
                .build();
        Member member2 = Member.builder()
                .userName("member6")
                .age(60)
                .build();
        Member member3 = Member.builder()
                .userName("member7")
                .age(70)
                .build();
        Member member4 = Member.builder()
                .userName("member8")
                .age(80)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }
    
    @Test
    @DisplayName("testJPA")
    void testJPA() {
        //given
        List<Member> members = memberRepository.findAll();

        //when
        members.forEach(System.out::println);

        //then
    }
    
    @Test
    @DisplayName("JPQL")
    void testJPQL() {
        //given
        String jpqlQuery = "SELECT m FROM Member m WHERE m.userName = :userName";
        //when
        Member foundMember = em.createQuery(jpqlQuery, Member.class)
                .setParameter("userName", "member2")
                .getSingleResult();

        //then
        assertEquals("teamA",foundMember.getTeam().getName());

        System.out.println("\n\n\n");
        System.out.println("foundMember = " + foundMember);
        System.out.println("foundMember.getTeam() = " + foundMember.getTeam());
        System.out.println("\n\n\n");
    }
    
    @Test
    @DisplayName("QueryDSL")
    void testQueryDSL() {
        //given
//      QMember m = new QMember("m1");  직접 객체를 생성할 필요가 없다.
//        QMember m = QMember.member;

        //when
        Member foundMember = factory
//                .select(member)
//                .from(member)
                .selectFrom(member)
                .where(member.userName.eq("member3")) //eq = equals
                .fetchOne(); // 하나라서 fetchOne
        //then
        assertNotNull(foundMember);
        assertEquals("teamB", foundMember.getTeam().getName());

        System.out.println("\n\n\n");
        System.out.println("foundMember = " + foundMember);
        System.out.println("foundMember.getTeam() = " + foundMember.getTeam());
        System.out.println("\n\n\n");

    }

    @Test
    @DisplayName("search")
    void search() {
        //given
        String searchName = "member2";
        int searchAge = 20;

        //when
        Member foundMember = factory.selectFrom(member)
                .where(member.userName.eq(searchName)
//                        .and(member.age.eq(searchAge)))
                        ,member.age.eq(searchAge)
                )
                .fetchOne();

        //then
        assertNotNull(foundMember);
        assertEquals("teamA",foundMember.getTeam().getName());

        /*

        JPAQueryFactory를 이용해서 쿼리문을 조립한 후 반환 인자를 결정한다.
        - fetchOne(): 단일 건 조회. 여러 건 조회시 예외 발생
        - fetchFirst(): 단일 건 조회. 여러 개가 조회 되어도 첫 번째 값만 반환
        - fetch(): List 형태로 반환

        JPQL이 제공하는 모든 검색 조건을 queryDsl에서도 사용 가능
        member.userName.eq("member1")  // userName = 'member1' (같다면)
        member.userName.nq("member1")  // userName != 'member1' (같지 않다면)
        member.userName.eq("member1").not()  // userName != 'member1' (같지 않다면)
        member.userName.isNotNull("member1") // 이름이 is not null
        member.age.in(10, 20) // 10이나 20을 포함하는지
        member.age.notIn(10, 20) // 10이나 20을 포함 안하는지
        member.age.between(10, 20) // 10이나 20 사이 (범위 지정)
        (between 10 and 20 <- 오라클, between 10, 20 <- My sql)
        member.age.goe(30) // age >= 30 greater or equals
        member.age.gt(30) // age > 30 greater than
        member.age.loe(30) // age <= 30 less or equals
        member.age.lt(30) // age < 30 less than
        member.userName.like("_감%") // userName LIKE '_감%'
        member.userName.contains("%감%") // userName LIKE '%감%'
        member.userName.startsWith("김") // userName LIKE '김%' (like로도 사용가능)
        member.userName.endsWith("김") // userName LIKE '%김' (like로도 사용가능)

         */

    }

    @Test
    @DisplayName("결과 반환하기")
    void testFetchResult() {

        //fetch
        List<Member> fetch1 = factory.selectFrom(member).fetch();

        System.out.println("\n\n ======== fetch =========");
        fetch1.forEach(System.out::println);

        //fetchOne()
        Member fetch2 = factory.selectFrom(member)
                .where(member.id.eq(3L))
                .fetchOne();

        System.out.println("\n\n ======== fetch2 =========");
        System.out.println("fetch2 = " + fetch2);

        //fetchFirst
        Member fetch3 = factory.selectFrom(member).fetchFirst();

        System.out.println("\n\n ======== fetch3 =========");
        System.out.println("fetch3 = " + fetch3);
        //then
    }
    
    @Test
    @DisplayName("QueryDsl custom 설정 확인")
    void queryDslCustom() {
        //given
        String name = "member4";
        //when
        List<Member> result = memberRepository.findByName(name);

        //then
        assertEquals(1,result.size());
        assertEquals("teamB", result.get(0).getTeam().getName());
    }
    
    @Test
    @DisplayName("회원 정렬 조회")
    void sort() {
        //given

        //when
        List<Member> result = factory.selectFrom(member)
//                .where(원하는 조건)
                .orderBy(member.age.asc())
                .fetch();
        //then
        assertEquals(result.size(), 8);

        System.out.println("\n\n\n");
        result.forEach(System.out::println);
        System.out.println("\n\n\n");
    }
    
    @Test
    @DisplayName("queryDsl paging")
    void paging() {
        //given

        //when
        List<Member> result = factory.selectFrom(member)
                .orderBy(member.userName.desc())
                .offset(0)
                .limit(3)
                .fetch();

        //then
        assertEquals(result.size(), 3);
        assertEquals(result.get(2).getUserName(), "member6");
    }
    
    @Test
    @DisplayName("그룹 함수의 종류")
    void aggregation() {
        //given

        //when
        List<Tuple> result = factory.select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                ).from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertEquals(tuple.get(member.count()), 8);
        assertEquals(tuple.get(member.age.sum()), 360);
        assertEquals(tuple.get(member.age.avg()), 45);
        assertEquals(tuple.get(member.age.max()), 80);
        assertEquals(tuple.get(member.age.min()), 10);

        System.out.println("\n\n\n");
        System.out.println("tuple = " + tuple.toString());
        System.out.println("\n\n\n");

        //then
    }
    
    @Test
    @DisplayName("Group By, Having")
    void testGroupBy() {
        //given

        //when
        List<Long> result = factory.select(member.age.count())
                .from(member)
                .orderBy(member.age.asc())
                .groupBy(member.age)
                .having(member.age.count().goe(2))
                .fetch();

        //then
        assertEquals(result.size(),2);

        System.out.println("\n\n\n");
        result.forEach(System.out::println);
        System.out.println("\n\n\n");
    }
    
    @Test
    @DisplayName("join 해보기")
    void join() {
        //given

        //when
        List<Member> result = factory.selectFrom(member)
                .leftJoin(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();
        //then
        System.out.println("\n\n\n");
        result.forEach(System.out::println);
        System.out.println("\n\n\n");

    }
    
    /*
     ex) 회원과 팀을 조인하면서., 팀 이름이 teamA인 팀만 조회, 회원은 모두 조회
     SQL: SELECT m.* t.* FROM tbl_member m LEFT JOIN tbl_team t ON m.team_id = t.id AND t.name = 'teamA'
     JPQL: SELECT m, t FROM Member m LEFT JOIN m.team t ON t.name = 'teamA'
     */
    @Test
    @DisplayName("left outer join 테스트")
    void leftJoinTest() {
        //given
        
        //when
        List<Tuple> result = factory.select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();
        //then
        result.forEach(tuple -> System.out.println("tuple = " + tuple));
    }
    
}