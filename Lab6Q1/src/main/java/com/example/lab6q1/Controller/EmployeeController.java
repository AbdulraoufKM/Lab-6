package com.example.lab6q1.Controller;

import com.example.lab6q1.Api.ApiResponse;
import com.example.lab6q1.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ArrayList<Employee> getEmployees() {
        return employees;
    }


    @PostMapping("/add")
    public ResponseEntity addEmployees(@RequestBody @Valid Employee employee, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        employees.add(employee);

        return ResponseEntity.status(200).body( new ApiResponse("Employee is added successfully"));
    }

    @PutMapping("/update/{index}")
    public ResponseEntity updateEmployee(@PathVariable int index  ,Employee employee, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        if (index >= employees.size() || index < 0){
            return ResponseEntity.status(400).body( new ApiResponse("index out of bounds"));
        }
        if (employee == null){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(404).body(message);
        }else
            employees.set(index,employee);

        return ResponseEntity.status(200).body( new ApiResponse("Updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEmployee(@PathVariable String id, Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        for (Employee employee : employees){
            if (employee.getId().equals(id)){
                employees.remove(employee);
                return ResponseEntity.status(200).body( new ApiResponse("Deleted successfully"));
            }
        }
        String message = errors.getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body( message);
    }


    @GetMapping("/searchByPos/{position}")
    public ResponseEntity searchByPos(@PathVariable String position){
        ArrayList<Employee> employeePos = new ArrayList<>();

        if (position.equals("supervisor") || position.equals("coordinator")) {
            for (Employee employee : employees) {
                if (employee.getPosition().equals(position)) {
                    employeePos.add(employee);
                }
                    return ResponseEntity.status(200).body(employeePos);
            }
        }
        return ResponseEntity.status(400).body("invalid input , enter either supervisor or coordinator");
    }

    @GetMapping("/searchByAge/{max}/{min}")
    public ResponseEntity searchByAge(@PathVariable int max, @PathVariable int min){

        ArrayList<Employee> employees1 = new ArrayList<>();
        if (max > min || min <0 || min>150){
            for (Employee employee : employees){
                if (employee.getAge() <=max && employee.getAge() >= min){
                    employees1.add(employee);
                }
                return ResponseEntity.status(200).body(employee);

            }
        }
        return ResponseEntity.status(400).body("invalid range of age, enter max then min ");
    }

    @PutMapping("/annualLeave/{id}")
    public ResponseEntity annualLeave(@PathVariable String id)
    {
        for (Employee employee : employees) {
            if (employee.isOnLeave())
                return ResponseEntity.status(400).body("Employee is on leave");
            if (employee.getId().equals(id) && employee.getAnnualLeave() > 0) {
                employee.setOnLeave(true);
                employee.setAnnualLeave(employee.getAnnualLeave() - 1);
                return ResponseEntity.status(200).body( new ApiResponse("Annul leave is given"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("something went wrong !"));
    }

    @GetMapping("/noAnnualLeave")
    public ResponseEntity noAnnualLeave(){
       ArrayList<Employee> employees1 = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getAnnualLeave() <= 0) {
                employees1.add(employee);
            }
        }
         if (employees1.isEmpty()){
             return ResponseEntity.status(400).body(new ApiResponse("There is no employee without annual leave"));
         }else
             return ResponseEntity.status(200).body(employees1);
    }

    @PutMapping("/promote/{idSup}/{id}")
    public ResponseEntity promote(@PathVariable String idSup , @PathVariable String id , Errors errors){
        for (Employee employee : employees){
            if (employee.getId().equals(idSup) && employee.getPosition().equals("supervisor")){
                for (Employee employee1 : employees){
                    if (employee1.getId().equals(id) && employee1.getAge() >=30 && ! employee1.isOnLeave()){
                        employee1.setPosition("supervisor");
                        return ResponseEntity.status(200).body(new ApiResponse("Employee promoted !"));
                    }
                }
            }
        }
        String message = errors.getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body(message);
    }




}
