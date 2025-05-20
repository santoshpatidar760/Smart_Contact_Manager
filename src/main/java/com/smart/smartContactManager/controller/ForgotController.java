package com.smart.smartContactManager.controller;


import com.smart.helper.Message;
import com.smart.smartContactManager.entity.User;
import com.smart.smartContactManager.repo.UserRepository;
import com.smart.smartContactManager.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class ForgotController {
    Random random=new Random(1000);
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // email id from open handler
    @RequestMapping("/forgot")
    public String openEmailForm(){
        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email , HttpSession session){
        System.out.println("EMAIL :" +email);
       // generating 4 digit opt
       int otp= random.nextInt(999999);
       System.out.println("OTP :" +otp);
       // write  code for send otp to your email

        String subject ="OTP From SCM";
        String message =""
                + "<div style='border:1px solid #e2e2e2; padding:20px'>"
                + "<h1 >"
                + "OTP is :"
                + "<b>" +otp
                + "</n>"
                + "</h1>"
                + "</div>"
                ;
        String to =email;
        boolean flag= this.emailService.sendEmail(subject, message, to);
        if (flag)
        {
            session.setAttribute("myotp", otp);
            session.setAttribute("email", email);
            return "verify_otp";
        }
        else
        {
            session.setAttribute("message", new Message("check your email id !!", "error"));
            return "forgot_email_form";

        }

        }
        //verify otp
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") int
                                        otp, HttpSession session){

        int myotp=(int)session.getAttribute("myotp");
        String email = (String)session.getAttribute("email");
        if (myotp ==otp)
        {
            //password cxhange form
            User user= this.userRepository.getUserByUsername(email);
            if (user==null)
            {
                //send error message

                session.setAttribute("message", "User does not exist with this email");
                return "forgot_email_form";

            }else
            {
                //send change password form
            }


            return "password_change_form";
        }else
        {
            session.setAttribute("message","You have entered wrong otp !!");
            return "verify-otp";
        }

    }
    //change password
    @PostMapping("/change_password")
    public String changePassword(@RequestParam("newpassword")String newpassword, HttpSession session){
        String email = (String)session.getAttribute("email");
        User user = this.userRepository.getUserByUsername(email);
        user.setPassword(this.passwordEncoder.encode(newpassword));
        this.userRepository.save(user);
         return "redirect:/signin?change=password changed successfully..";
    }
}
