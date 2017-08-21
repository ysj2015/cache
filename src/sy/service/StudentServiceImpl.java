package sy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sy.dao.StudentMapper;
import sy.model.Student;

@Service("studentService")
public class StudentServiceImpl implements StudentService {
    
    private StudentMapper studentMapper;

    public StudentMapper getStudentMapper() {
        return studentMapper;
    }

    @Autowired
    public void setStudentMapper(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @Override
    public Student getStudentById(Integer id) {
        // TODO Auto-generated method stub
        return studentMapper.selectByPrimaryKey(id);
    }

}