package com.smart.smartContactManager.controller;

import com.smart.helper.Message;
import com.smart.smartContactManager.entity.Contact;
import com.smart.smartContactManager.entity.User;
import com.smart.smartContactManager.repo.ContactRepository;
import com.smart.smartContactManager.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUsername(userName);
        model.addAttribute("user", user);
    }

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String openAddContactForm(Model model, HttpSession session) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());

        // ✅ Show and remove session message safely
        Message message = (Message) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }

        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact,
                                 @RequestParam("profileImage") MultipartFile file,
                                 Principal principal,
                                 HttpSession session) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUsername(name);

            if (!file.isEmpty()) {
                contact.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepository.save(user);

            session.setAttribute("message", new Message("Your contact is added! Add more...", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong! Try again...", "danger"));
        }

        return "redirect:/user/add-contact";
    }

    // 🔹 Show contact handler
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal, HttpSession session) {
        m.addAttribute("title", "Show user Contact");

        String userName = principal.getName();
        User user = this.userRepository.getUserByUsername(userName);

        Pageable pageable = PageRequest.of(page, 10);
        Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

        m.addAttribute("contacts", contacts);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", contacts.getTotalPages());

        // ✅ Add and remove session message safely
        Message message = (Message) session.getAttribute("message");
        if (message != null) {
            m.addAttribute("message", message);
            session.removeAttribute("message");
        }

        return "normal/show_contacts";
    }

    // 🔹 Show contact detail handler
    @RequestMapping("/{Cid}/contact")
    public String showContactDetail(@PathVariable("Cid") Integer Cid, Model model, Principal principal) {
        Optional<Contact> contactOptional = this.contactRepository.findById(Cid);
        Contact contact = contactOptional.get();

        String userName = principal.getName();
        User user = this.userRepository.getUserByUsername(userName);

        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }

        return "normal/contact_detail";
    }

    // 🔹 Delete contact handler
    @GetMapping("/delete/{Cid}")
    public String deleteContact(@PathVariable("Cid") Integer Cid, Model model, HttpSession session, Principal principal) {
        Contact contact = this.contactRepository.findById(Cid).get();

        String userName = principal.getName();
        User user = this.userRepository.getUserByUsername(principal.getName());

        if (user.getId() == contact.getUser().getId()) {
            user.getContacts().remove(contact);
            this.userRepository.save(user);
            //contact.setUser(null);
//            this.userRepository.save(user);
//            this.contactRepository.delete(contact);

            session.setAttribute("message", new Message("Contact Deleted Successfully....", "success"));
        } else {
            session.setAttribute("message", new Message("You are not authorized to delete this contact", "danger"));
        }

        return "redirect:/user/show-contacts/0";
    }
    //open update form handler
    @PostMapping("/update-contact/{Cid}")
    public String updateForm(@PathVariable("Cid") Integer Cid, Model m)
    {
        m.addAttribute("title","Update contact");
       Contact contact= this.contactRepository.findById(Cid).get();
       m.addAttribute("contact",contact);
        return "normal/update_form";
    }
    //update contact handler
    @RequestMapping(value = "/process-update", method = RequestMethod.POST)
    public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Model m,
                                HttpSession session, Principal principal) {


        try{

            //old contact details
           Contact oldContactDetail= this.contactRepository.findById(contact.getCid()).get();
            //image..
            if(!file.isEmpty())
            {
                //file work
                //rewrite
                //delete old photo
                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1=new File(deleteFile,oldContactDetail.getImage());
                file1.delete();


                //update new photo
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(file.getOriginalFilename());
            }else
            {
                contact.setImage(oldContactDetail.getImage());
            }
            User user = this.userRepository.getUserByUsername(principal.getName());
            contact.setUser(user);
            this.contactRepository.save(contact);
            session.setAttribute("message",new Message("Your contact is updated","success"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("CONTACT NAME: " + contact.getName());
        System.out.println("CONTACT ID: " + contact.getCid());
        return "redirect:/user/"+contact.getCid()+"/contact";
    }
    //your profile
    @GetMapping("/profile")
    public String yourProfile(Model model){
        model.addAttribute("title", "profile page");
        return "normal/profile";
    }
    // open setting handler
    @GetMapping("/settings")
    public  String openSetting()
    {
        return "normal/settings";
    }

    //change password
    @PostMapping("/change-password")
    public  String changePassword(@RequestParam("oldPassword")String oldPassword,
                                  @RequestParam("newPassword") String newPassword
            ,Principal principal, HttpSession session)
    {
        System.out.println("OLD PASSWORD :"  +oldPassword);
        System.out.println("NEW PASSWORD :"  +newPassword);
        String userName =principal.getName();
        User currentUser = this.userRepository.getUserByUsername(userName);
        System.out.println(currentUser.getPassword());
        if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
        {
            //change the password
            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);
            session.setAttribute("message", new Message("Your Password is successfully changed...","success"));
        }else
        {
            //error
            session.setAttribute("message", new Message("Please Enter correct old  Password...","danger"));
            return "redirect:/user/settings";
        }

        return "redirect:/user/index";
    }
}
