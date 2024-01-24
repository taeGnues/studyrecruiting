package com.example.studyroll.account;

import com.example.studyroll.domain.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

//    @MockBean // 외부서비스
//    private JavaMailSender javaMailSender;

//    @DisplayName("회원 가입 화면 보이는지 테스트") => junit5 기능임.
    @Test
    public void signUpForm() throws Exception {
        this.mockMvc.perform(get("/sign-up"))
                .andDo(print()) // 내용물 실제로 프린트하기
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up")) // 실제 뷰가 sign-up 인지 확인함
                .andExpect(model().attributeExists("signUpForm"));// 해당 모델이 존재하지는지 확인함

    }

//    @DisplayName("회원 가입 처리 - 입력값 오류")
    @Test
    public void signUpSubmit_with_wrong_input() throws Exception{
        //given
        mockMvc.perform(post("/sign-up")
                .param("nickname", "keesun")
                .param("email", "email..") // 잘못된 이메일
                .param("password", "12345")
                .with(csrf()) // spring security는 view에 post하는것에대해 csrf 토큰을 기본적으로 지원. 내가 만든 뷰인지를 확인하기 위해 사용함. 만약 토큰이 없다면 안전하지 않는 토큰이기 때문에 거부함.
                ) // 잘못된 비번
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"));
    }

//    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    public void signUpSubmit_with_correct_input() throws Exception{
        //given
        mockMvc.perform(post("/sign-up")
                        .param("nickname", "keesun")
                        .param("email", "hi563@naver.com")
                        .param("password", "12345678")
                        .with(csrf()) // spring security는 view에 post하는것에대해 csrf 토큰을 기본적으로 지원. 내가 만든 뷰인지를 확인하기 위해 사용함. 만약 토큰이 없다면 안전하지 않는 토큰이기 때문에 거부함.
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

//        assertTrue(accountRepository.existsByEmail("hi563@naver.com")); // 해당값 존재하는지 확인
        Account account = accountRepository.findByEmail("hi563@naver.com");
        assertNotNull(account.getEmailCheckToken());
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "12345678");

//        then(javaMailSender).should().send(any(SimpleMailMessage.class)); // 호출이 실제로 됐는지 확인.

    }

    // 인증 메일 확인 - 인증값 오류
    @Test
    public void checkEmailToken_with_wrong() throws Exception{
        //given
        mockMvc.perform(get("/check-email-token")
                .param("token", "afagaaaaaaaf")
                .param("email", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"));
        
    }
    // 인증메일 확인 - 입력값 정상
    @Test
    @Transactional // test에 기본적으로 transactional이 없기때문에 써줘야함.
    public void checkEmailToken_with_correct() throws Exception{

        Account account = Account.builder()
                .email("hi563@naver.com")
                .password("12345678")
                .nickname("gnues")
                .build();

        Account savedAccount = accountRepository.save(account);
        savedAccount.generateEmailCheckToken();


        //given
        mockMvc.perform(get("/check-email-token")
                        .param("token", savedAccount.getEmailCheckToken())
                        .param("email", savedAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"));

    }
}