package medical.diyoras.project.controller;

import medical.diyoras.project.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/main/")
public class MainController {
    @Autowired
    MainService service;

    @PostMapping("/upload/points")
    public ResponseEntity one(@RequestParam("file") MultipartFile file){
        service.importPoints(file);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/upload/drugs")
    public ResponseEntity two(@RequestParam("file") MultipartFile file){
        service.importDrugs(file);
        return ResponseEntity.ok("ok");
    }
}
