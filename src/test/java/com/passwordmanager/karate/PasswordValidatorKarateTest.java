package com.passwordmanager.karate;

import com.intuit.karate.junit5.Karate;

class PasswordValidatorKarateTest {

    @Karate.Test
    Karate testPasswordValidator() {
        return Karate.run("password-validator").relativeTo(getClass());
    }
}