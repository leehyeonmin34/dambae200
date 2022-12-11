package com.dambae200.dambae200.domain.user.service;


import java.util.Random;

public class PasswordGenerator {

    public static String generatePw(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();
        String alpahbets = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        String num = String.valueOf(random.nextInt(10));
        System.out.println(alpahbets + " " + num);
        return alpahbets + num;

    }

}
