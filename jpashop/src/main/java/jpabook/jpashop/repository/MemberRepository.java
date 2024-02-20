package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 자동으로 스프링 빈 관리해줌
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;   // 엔티티매니저를 생성자로 만들어서 인젝션 해주는것
    // 원래는 @persistenceContext 있어야 하는데 스프링부트가 @AutoWired도 인젝션 되게 지원해주기 때문에 롬복으로 @RequiredArgsConstructor로 이용해서 쓸 수 있는 것
    // 만약 스프링 데이터 jpa가 없으면 이렇게 쓸 수 없음

    // JPA가 저장하는 로직
    public void save(Member member) {
        em.persist(member);
    }

    // JPA 단건조회 로직
    public Member findOne(Long id) {
        return em.find(Member.class, id); // jpa에서 제공하는 find메소드 활용
    }

    // JPA 다건조회 로직
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();    // createQuery로 (JPQL, 반환타입) 작성해야함 / getResultList 사용하면 Member를 List로 반환
    // + sql은 테이블을 대상으로 쿼리 작성, jpql은 엔티티 객체를 대상으로 작성
    }

    // 파라미터 바인딩해서 특정 회원의 name으로 조회하는 법
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)  // :로 파라미터 받아줌
                .setParameter("name", name) // 파라미터 매핑해주는 것
                .getResultList();
    }
}
