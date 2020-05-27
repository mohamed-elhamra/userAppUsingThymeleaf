package com.example.userApp.controller;


import com.example.userApp.model.User;
import com.example.userApp.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Value("${file}")
    private String imageDir;

    @GetMapping("/newUser")
    public String newUser(Model model){
        model.addAttribute("user", new User());
        return "userForm";
    }


    @RequestMapping("/saveUser")
    public String saveUser(@Valid User user,
                           BindingResult bindingResult,
                           @RequestParam("picture") MultipartFile multipartFile) throws IOException {

        if (bindingResult.hasErrors()){
            return "userForm";
        }

        user.setPhoto(imageDir+user.getId());
        userRepository.save(user);

        if(!multipartFile.isEmpty()){
            System.out.println("drrfrf");
            multipartFile.transferTo(new File(imageDir+user.getId()));
        }
        return "redirect:/User/home";
    }

    @GetMapping("/home")
    public String listUser(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "keyWord", defaultValue = "") String keyWord){
        Page<User> userPage = userRepository.searchByName(keyWord, PageRequest.of(page,3));

        int pageCount = userPage.getTotalPages();
        int[] pages = new int[pageCount];
        for (int i = 0; i < pageCount; i++){
            pages[i] = i;
        }

        model.addAttribute("userPage", userPage);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page);
        return "index";
    }

    @GetMapping(value = "/getPhoto", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPhoto(@RequestParam("id") Long id) throws IOException {
        File file = new File(imageDir + id);
        return IOUtils.toByteArray(new FileInputStream(file));
    }

    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id){
        User user = userRepository.getOne(id);
        userRepository.delete(user);
        return "redirect:/User/home";
    }

    @GetMapping("/editUser")
    public String editUser(@RequestParam("id") Long id, Model model){
        User user = userRepository.getOne(id);
        model.addAttribute("user", user);
        return "userForm";
    }

}
