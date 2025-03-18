package com.example.YONDU.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.YONDU.models.Member;
import com.example.YONDU.repository.MemberRepository;

@Service
public class MemberDetailsServiceImpl implements UserDetailsService {
    @Autowired
    MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + identifier));

        return MemberDetailsImpl.build(member);
    }

}
