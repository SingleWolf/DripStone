package com.walker.crash;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Student stu = new Student(18, "aa");
        changeStudent(stu);
        System.out.println("student=" + stu.name + "," + stu.age);
    }

    private void changeStudent(Student stu) {
        stu=new Student(45,"cc");
    }

    class Student {
        public int age;
        public String name;

        public Student(int age, String name) {
            this.age = age;
            this.name = name;
        }
    }
}