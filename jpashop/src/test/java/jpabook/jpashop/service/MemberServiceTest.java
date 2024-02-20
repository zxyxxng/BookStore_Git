package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)    // jUnit4 실행할때 스프링이랑 같이 엮어서 실행할래!
@SpringBootTest // 스프링부트를 띄운 상태에서 테스트하기 위해 필요한 것. 안그럼 autowired등 다 실패함. 스프링 컨테이너 내에서 해당 테스트를 돌리는 것
// 위 두가지가 있어야 스프링을 integration해서 스프링 부트를 실제로 올려서 테스트 가능
@Transactional // 데이터 변경할거니까 사용. 이게 있어야 테스트 끝나면 전부 rollback됨
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;    // 만약 인서트쿼리 확인하고 싶으면? 1. 엔티티 매니저를 받은 다음에

    @Test
//    @Rollback(value = false) // 이걸 사용하게 되면 테스트에서도 데이터 insert되는 것 확인할 수 있음 + 트랜젝션 어노테이션이 테스트 케이스에 있으면 롤백을 해버리기 때문에
//    // 기본적으로 jpa에서는 pesist만 해서는 insert되지 않음. commit을 해야 insert됨
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");

        // when
        Long saveId = memberService.join(member);
    
        // then
//        em.flush(); // 2. flush(db에 강제로 쿼리 나가는거)로 영속성 컨택스트에 있는 데이터를 db에 반영해줌. 확인해보면 스프링 테스트 끝날때 롤백은 되지만 그 전에 인서트문 날리는거 확인할 수 있음
        assertEquals(member, memberRepository.findOne(saveId));
    }
    
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        memberService.join(member2);    // 예외가 발생해야 한다!!!

        // then
        fail("예외가 발생해야 한다.");   // fail: Junit이 기본으로 제공하는 assert기능으로 해당 위치까지 코드가 실행되면 실패하는 것
    }
}