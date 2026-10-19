package com.passwordmanager.controller;

import com.passwordmanager.model.PasswordGenerator;
import com.passwordmanager.model.PasswordValidator;
import com.passwordmanager.view.PasswordView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PasswordControllerTest {

    private PasswordView view;
    private EventHandler<ActionEvent> onGenerate;
    private EventHandler<ActionEvent> onValidate;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        view = mock(PasswordView.class);
        new PasswordController(view, new PasswordGenerator(), new PasswordValidator());
        ArgumentCaptor<EventHandler<ActionEvent>> gen = ArgumentCaptor.forClass(EventHandler.class);
        ArgumentCaptor<EventHandler<ActionEvent>> val = ArgumentCaptor.forClass(EventHandler.class);
        verify(view).setOnGenerateAction(gen.capture());
        verify(view).setOnValidateAction(val.capture());
        onGenerate = gen.getValue();
        onValidate = val.getValue();
    }

    private void configurarGeneracion(int longitud, boolean u, boolean l, boolean d, boolean s) {
        when(view.getPasswordLength()).thenReturn(longitud);
        when(view.isUpperSelected()).thenReturn(u);
        when(view.isLowerSelected()).thenReturn(l);
        when(view.isDigitsSelected()).thenReturn(d);
        when(view.isSpecialSelected()).thenReturn(s);
    }

    @Test
    void generaYValidaAutomaticamente() {
        configurarGeneracion(16, true, true, true, true);
        onGenerate.handle(null);
        verify(view).setPasswordText(anyString());
        verify(view).showStrengthResult(anyString(), anyString(), anyDouble(), anyList(), anyDouble());
    }

    @Test
    void longitudInvalidaMuestraError() {
        configurarGeneracion(0, true, true, true, true);
        onGenerate.handle(null);
        verify(view).showError(anyString());
    }

    @Test
    void sinCategoriasMuestraError() {
        configurarGeneracion(16, false, false, false, false);
        onGenerate.handle(null);
        verify(view).showError(anyString());
    }

    @Test
    void errorInesperadoMuestraMensajeGenerico() {
        when(view.getPasswordLength()).thenThrow(new RuntimeException("falla"));
        onGenerate.handle(null);
        verify(view).showError(startsWith("Ha ocurrido un error"));
    }

    @Test
    void validaContrasenaFuerte() {
        when(view.getPasswordText()).thenReturn("Password1!");
        onValidate.handle(null);
        verify(view).showStrengthResult(eq("FUERTE"), eq("#21bf73"), eq(1.0), anyList(), anyDouble());
    }

    @Test
    void validaContrasenaMedia() {
        when(view.getPasswordText()).thenReturn("Password1");
        onValidate.handle(null);
        verify(view).showStrengthResult(eq("MEDIA"), eq("#ff9f43"), eq(0.8), anyList(), anyDouble());
    }

    @Test
    void validaContrasenaDebil() {
        when(view.getPasswordText()).thenReturn("");
        onValidate.handle(null);
        verify(view).showStrengthResult(eq("DÉBIL"), eq("#ff2e63"), eq(0.0), anyList(), anyDouble());
    }
}