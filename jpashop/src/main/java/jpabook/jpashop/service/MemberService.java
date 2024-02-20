package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// 기본적으로 DB변경되는 것은 반드시 트랜젝션 어노테이션 내에서 수행되어야함, spring내에서 제공하는 것 있고 javax(jakarta)에서 제공하는것 있는데 spring에서 제공하는게 더 사용가능한 옵션 많으므로 추천
@Transactional(readOnly = true) // 트랜젝션에 read-only기능을 주면 jpa가 좀더 트랜젝션 기능 최적화함
@RequiredArgsConstructor    // final이 있는 필드만 가지고 생성자로 만들어줌
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional(readOnly = false)    // 전체는 true로 하고 db변경 필요한 메소드만 false로 하면 성능 최적화
    public Long join(Member member) {

        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);  // 문제 없으면 회원 저장(save메소드에서 em.persis를 타면서 멤버 객체를 영속성 컨텍스트에 올림. 그럼 pk가 id가 되고 id값이 항상 생성되는 것이 보장됨.)
        return member.getId();  // 저장 후 멤버 아이디 반환(id라도 돌려줘야 뭐가 저장됐는지 알 수 있음)
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());   // 동일 회원이름 있는지 찾음
        if (!findMembers.isEmpty()) {   // 조회 건수로 조건걸어도 됨.
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        // (+) 실무에서는 was가 여러대이기 때문에 동시에 동일한 멤버a를 저장하면 이 검증로직도 문제될 수 있음.
        // 따라서 방어조건으로 멤버name을 unique 제약조건으로 걸어주는 것 필요
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
