//
//package com.smart.smartContactManager.controller;
//
//import com.smart.helper.Message;
//import com.smart.smartContactManager.entity.User;
//import com.smart.smartContactManager.repo.UserRepository;
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//
//@Controller
//public class HomeController {
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Home page route
//    @RequestMapping("/")
//    public String home(Model model) {
//        model.addAttribute("title", "Home- Smart Contact Manager");
//        return "home";
//    }
//
//    // About page route
//    @RequestMapping("/about")
//    public String about(Model model) {
//        model.addAttribute("title", "About- Smart Contact Manager");
//        return "about";
//    }
//
//    // Signup page route
//    @RequestMapping("/signup")
//    public String signup(Model model, HttpSession session) {
//        model.addAttribute("title", "Register - Smart Contact Manager");
//        model.addAttribute("user", new User());
//
//        // Check if there is a message in the session
//        Message message = (Message) session.getAttribute("message");
//        if (message != null) {
//            model.addAttribute("message", message);
//            session.removeAttribute("message"); // Clear the message after showing
//        }
//
//        return "signup";
//    }
//
//    // Handler for registering user
//    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
//    public String registerUser(@Valid @ModelAttribute("user") User user,
//                               BindingResult result1,
//                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
//                               Model model,
//                               HttpSession session) {
//
//        try {
//            // Check if terms and conditions are agreed
//            if (!agreement) {
//                throw new Exception("You have not agreed to the terms and conditions");
//            }
//
//            // Check for validation errors
//            if (result1.hasErrors()) {
//                System.out.println("ERROR" +result1.toString());
//                model.addAttribute("user", user);
//                return "signup";
//            }
//
//            // Set user properties and save
//            user.setRole("ROLE_USER");
//            user.setEnabled(true);
//            user.setImageUrl("default.png");
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//
//
//            // Save user to the database
//            this.userRepository.save(user);
//
//            // Set session message for successful registration
//            session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
//
//            // Redirect to signup page to show the success message
//            return "redirect:/signup";
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Set error message in session if something goes wrong
//            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
//            return "redirect:/signup";
//        }
//    }
//
//
//    // login page route
//    @RequestMapping("/signin")
//    public String login(Model model, HttpSession session) {
//        model.addAttribute("title", "Login - Smart Contact Manager");
//        model.addAttribute("user", new User());
//
//        // Check if there is a message in the session
//        Message message = (Message) session.getAttribute("message");
//        if (message != null) {
//            model.addAttribute("message", message);
//            session.removeAttribute("message"); // Clear the message after showing
//        }
//
//        return "login";
//    }
//}


package com.smart.smartContactManager.controller;

import com.smart.helper.Message;
import com.smart.smartContactManager.entity.User;
import com.smart.smartContactManager.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // Home page route
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home- Smart Contact Manager");
        return "home";
    }

    // About page route
    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About- Smart Contact Manager");
        return "about";
    }

    // Signup page route
    @RequestMapping("/signup")
    public String signup(Model model, HttpSession session) {
        model.addAttribute("title", "Register - Smart Contact Manager");
        model.addAttribute("user", new User());

        Object obj = session.getAttribute("message");
        if (obj instanceof Message) {
            model.addAttribute("message", (Message) obj);
            session.removeAttribute("message"); // Clear after showing
        } else if (obj instanceof String) {
            model.addAttribute("message", new Message((String) obj, "alert-info"));
            session.removeAttribute("message");
        }

        return "signup";
    }

    // Handler for registering user
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result1,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               Model model,
                               HttpSession session) {

        try {
            // Check if terms and conditions are agreed
            if (!agreement) {
                throw new Exception("You have not agreed to the terms and conditions");
            }

            // Check for validation errors
            if (result1.hasErrors()) {
                System.out.println("ERROR" + result1.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            // Set user properties and save
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save user to the database
            this.userRepository.save(user);

            // Set session message for successful registration
            session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));

            // Redirect to signup page to show the success message
            return "redirect:/signup";

        } catch (Exception e) {
            e.printStackTrace();
            // Set error message in session if something goes wrong
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
            return "redirect:/signup";
        }
    }

    // login page route
    @RequestMapping("/signin")
    public String login(Model model, HttpSession session) {
        model.addAttribute("title", "Login - Smart Contact Manager");
        model.addAttribute("user", new User());

        Object obj = session.getAttribute("message");
        if (obj instanceof Message) {
            model.addAttribute("message", (Message) obj);
            session.removeAttribute("message"); // Clear after showing
        } else if (obj instanceof String) {
            model.addAttribute("message", new Message((String) obj, "alert-info"));
            session.removeAttribute("message");
        }

        return "login";
    }
}

