package com.example.studyroll.account;

import com.example.studyroll.domain.Account;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class AccountController{

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    // account repository를 account과 동일한 도메인 레벨로 봤을때 controller에서 레포지토리를 가져옴.
    // 단, controller를 service에서 참조하지 않는다.

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
        model.addAttribute(new SignUpForm());
//        model.addAttribute("signUpForm", new SignUpForm()); 로 써도됨. camelCase로 자동으로 인식함.
        return "account/sign-up";
    }


    @PostMapping("/sign-up")
    public String singUpSubmit(@Valid SignUpForm signUpForm, Errors errors){
        if(errors.hasErrors()) {
            return "account/sign-up";
        }
        accountService.processNewAccount(signUpForm);

        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email); // 이메일 존재여부 확인
        String view = "account/checked-email";
        if(account==null){
            model.addAttribute("error", "wrong.email");
            return view;
        }

        if(!account.getEmailCheckToken().equals(token)){
            model.addAttribute("error", "wrong.token");
            return view;
        }

        account.completeSignUp();
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;
    }



}
