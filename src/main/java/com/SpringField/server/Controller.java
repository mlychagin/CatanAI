package com.SpringField.server;

import com.SpringField.ui.DrawBoard;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RestController
public class Controller {

    @RequestMapping("/")
    public String index() {
        return "It worked! spring boot is a go!";
    }

    @RequestMapping("/home")
    public String home() throws IOException {
        try {
            String[] args = new String[]{"", ""};
            DrawBoard.main(args);
            return "It worked! spring boot is a go!";
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("IO exceptions");
        }
        return "couldnt draw board";
    }
}
