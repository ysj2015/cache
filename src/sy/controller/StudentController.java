package sy.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import sy.model.Student;
import sy.service.StudentService;

@Controller
@RequestMapping("/studentController")
public class StudentController {
    
    private StudentService studentService;
    
    public StudentService getStudentService() {
        return studentService;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @RequestMapping("/showStudent/{id}")
    public String showStudent(@PathVariable String id,HttpServletRequest request){
    	System.out.println("controller");
        Student u=studentService.getStudentById(Integer.valueOf(id));
        request.setAttribute("student", u);
        return "showStudent";
    }

}