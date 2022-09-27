package com.kh.great.web.controller.member;

import com.kh.great.domain.entity.Member;
import com.kh.great.domain.entity.Product;
import com.kh.great.domain.svc.member.MemberSVC;
import com.kh.great.domain.svc.product.ProductSVC;
import com.kh.great.web.dto.member.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    final ProductSVC productSVC;
    private final MemberSVC memberSVC;

    @GetMapping
    public String home(HttpServletRequest request, Model model) {
        List<Product> list = productSVC.today_deadline();
        model.addAttribute("list", list);

        String view = null;
        HttpSession session = request.getSession(false);
        view = (session == null) ? "main/main" : "main/mainMember" ;

        return view;
//        return "main";
    }

    //회원가입 화면
    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("join", new Join());

        return "join";    //회원가입 화면
    }

    //회원가입 처리
    @PostMapping("/join")
    public String join(
            @Valid @ModelAttribute("join") Join join,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        //기본 검증
        //if (bindingResult.hasErrors()) {
        //    log.info("errors = {}", bindingResult);
        //    return "join";
        //}

        //필드 검증(field error)
        //if (join.getMemId().length() > 15) {
        //    bindingResult.rejectValue("memId", null, "아이디 길이는 15자 이하까지 가능합니다.");
        //    return "join";
        //}

        //오브젝트 검증(object error)
        //비밀번호-비밀번호 확인 일치
        //if (join.getMemPassword() == join.getMemPasswordCheck()) {
        //    bindingResult.reject(null, "비밀번호 일치합니다");
        //    return "join";
        //}


        Member member = new Member();
        member.setMemType(join.getMemType());
        member.setMemId(join.getMemId());
        member.setMemPassword(join.getMemPassword());
        member.setMemName(join.getMemName());
        member.setMemNickname(join.getMemNickname());
        member.setMemEmail(join.getMemEmail());
        member.setMemBusinessnumber(join.getMemBusinessnumber());
        member.setMemStoreName(join.getMemStoreName());
        member.setMemStorePhonenumber(join.getMemStorePhonenumber());
        member.setMemStoreLocation(join.getMemStoreLocation());
        member.setMemStoreIntroduce(join.getMemStoreIntroduce());
        member.setMemStoreSns(join.getMemStoreSns());
        Member joinedMember = memberSVC.join(member);

        Long id = joinedMember.getMemNumber();
        redirectAttributes.addAttribute("id", id);
        return "redirect:/joinComplete";  //가입완료화면
    }

    //회원가입 완료 화면
    @GetMapping("/joinComplete")
    public String joinComplete(
            @ModelAttribute("join") JoinComplete joinComplete
    ) {

        return "member/joinComplete";    //회원가입 화면
    }

    //아이디 찾기 화면
    @GetMapping("/findId")
    public String findId(Model model) {
        model.addAttribute("findId", new findId());

        return "findId";
    }

    //비밀번호 찾기 화면
    @GetMapping("/findPw")
    public String findPw(Model model) {
        model.addAttribute("findPw", new findPw());

        return "findPw";
    }

    //비밀번호 재설정 화면
    @GetMapping("/resetPw")
    public String resetPw(Model model) {
        model.addAttribute("resetPw", new resetPw());

        return "resetPw";
    }

    //로그인 화면
    @GetMapping("/login")
    public String login(@ModelAttribute("login") Login login){

        return "login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("login") Login login,
            BindingResult bindingResult,
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "/") String redirectUrl
    ){
        //기본 검증
        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "login";
        }

        //회원유무
        Optional<Member> member = memberSVC.login(login.getMemId(), login.getMemPassword());
        log.info("member = {}", member);
        if(member.isEmpty()){
            bindingResult.reject(null,"회원정보가 없습니다.");
            return "login";
        }

        //세션에 회원정보 저장
        com.kh.great3.web.session.LoginMember loginMember = new com.kh.great3.web.session.LoginMember(member.get().getMemNumber(), member.get().getMemType(),
                member.get().getMemId(), member.get().getMemNickname(), member.get().getMemStoreName());

        //세션 생성
        HttpSession session = request.getSession(true);
        session.setAttribute("loginMember", loginMember);
        session.setAttribute("memNumber", member.get().getMemNumber());
        session.setAttribute("memType", member.get().getMemType());
        session.setAttribute("memNickname", member.get().getMemNickname());

//        if(requestURI.equals("/")){
//            return "mainMember";
//        }

        return "redirect:" + redirectUrl;
    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        //세션 조회
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/"; //메인
    }

}