package com.altaec.jooq.recurse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectoryController {

    @Autowired
    DirectoryService directoryService;

    @GetMapping
    public void getDirectory() {
       directoryService.getAllRecurseDirectory();
    }

    @PostMapping
    public void postDirectory(){
      /*  this.directoryService.insertDirectory(book);*/
    }
}
