package com.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/helloWorld")
public class HelloWorldController {

  @RequestMapping(method = RequestMethod.GET)
  public String hello( ModelMap model ) {

    // add data to the model
    model.addAttribute( "message", "Hello World!" );

    // return the name of the view to use with this model
    return "helloWorld";
  }

}