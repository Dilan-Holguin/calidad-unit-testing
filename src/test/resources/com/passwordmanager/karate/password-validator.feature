Feature: Validación de contraseñas

  Background:
    * def PasswordValidator = Java.type('com.passwordmanager.model.PasswordValidator')
    * def validator = new PasswordValidator()

  Scenario: Contraseña vacía
    When def result = validator.validate('')
    Then match result.getStrength().toString() == 'DEBIL'
    And match result.getScore() == 0

  Scenario: Contraseña corta
    When def result = validator.validate('Ab1!')
    Then match result.getStrength().toString() == 'DEBIL'
    And match result.getScore() == 4

  Scenario: Contraseña sin mayúscula
    When def result = validator.validate('password1!')
    Then match result.getStrength().toString() == 'MEDIA'
    And match result.getScore() == 4

  Scenario: Contraseña sin minúscula
    When def result = validator.validate('PASSWORD1!')
    Then match result.getStrength().toString() == 'MEDIA'
    And match result.getScore() == 4

  Scenario: Contraseña sin números
    When def result = validator.validate('Password!')
    Then match result.getStrength().toString() == 'MEDIA'
    And match result.getScore() == 4

  Scenario: Contraseña sin caracteres especiales
    When def result = validator.validate('Password1')
    Then match result.getStrength().toString() == 'MEDIA'
    And match result.getScore() == 4

  Scenario: Contraseña fuerte
    When def result = validator.validate('Password1!')
    Then match result.getStrength().toString() == 'FUERTE'
    And match result.getScore() == 5