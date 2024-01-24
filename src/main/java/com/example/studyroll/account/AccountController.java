package com.example.studyroll.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController{

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;

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



}
