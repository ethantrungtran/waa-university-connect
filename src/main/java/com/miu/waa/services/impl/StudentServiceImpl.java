package com.miu.waa.services.impl;

import com.miu.waa.dto.request.StudentCreateDto;
import com.miu.waa.dto.response.StudentResponseDto;
import com.miu.waa.entities.Student;
import com.miu.waa.mapper.StudentDtoMapper;
import com.miu.waa.repositories.StudentRepository;
import com.miu.waa.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public List<StudentResponseDto> findAll() {
        try{
            List<Student> students=studentRepository.findAll();
            return students.stream()
                    .map(StudentDtoMapper.dtoMapper::studentToStudentResponseDto)
                    .collect(Collectors.toList());
        }
        catch(NoSuchElementException e){
            throw e;
        }
    }

    @Override
    public StudentResponseDto findById(Long id) {
        try{
            Optional<Student> optionalStudent=studentRepository.findByStudentId(id);
            if(optionalStudent.isEmpty()){
                throw new NoSuchElementException("Student not found!!");
            }
            return StudentDtoMapper.dtoMapper.
                    studentToStudentResponseDto(optionalStudent.get());
        }
        catch(NoSuchElementException e){
            throw e;
        }
    }

    @Override
    public StudentResponseDto createStudent(StudentCreateDto dto) {
        try{
            Student student=StudentDtoMapper.dtoMapper.studentCreateDtoToStudent(dto);
            student.setStudentId(studentRepository.generateStudentId());
            student.setCreatedAt(LocalDateTime.now());
            student = studentRepository.save(student);
            return StudentDtoMapper.dtoMapper.studentToStudentResponseDto(student);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public StudentResponseDto updateStudent(Long studentId,StudentCreateDto studentDto) {
        try{
            Optional<Student> optionalStudent=studentRepository.findByStudentId(studentId);
            if(optionalStudent.isEmpty()){
                throw new NoSuchElementException("Student not found!!");
            }
            Student student=optionalStudent.get();
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());
            student.setEmail(studentDto.getEmail());
            student.setPhone(studentDto.getPhone());
            student.setDateOfBirth(studentDto.getDateOfBirth());
            student.setProfilePictureURL(studentDto.getProfilePictureURL());
            student.setMajor(studentDto.getMajor());
            student.setAcademicAchievements(studentDto.getAcademicAchievements());
            student.setInterests(studentDto.getInterests());
            student.setExtracurricularActivities(studentDto.getExtracurricularActivities());
            student = studentRepository.save(student);
            return StudentDtoMapper.dtoMapper.studentToStudentResponseDto(student);
        }
        catch (Exception e){
            throw e;
        }
    }
}
