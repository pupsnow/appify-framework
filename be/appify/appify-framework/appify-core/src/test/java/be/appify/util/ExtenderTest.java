package be.appify.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import be.appify.util.Extender;

public class ExtenderTest {
    @Test
    public void extendedShouldForwardInvocationsToBase() {
        SomeInterface base = Mockito.mock(SomeInterface.class);
        AnotherInterface extension = Mockito.mock(AnotherInterface.class);

        SomeInterface extended = Extender.extend(SomeInterface.class, base, extension);

        extended.someMethod();
        Mockito.verify(base).someMethod();
    }

    @Test
    public void extendedShouldForwardInvocationsToExtension() {
        SomeInterface base = Mockito.mock(SomeInterface.class);
        AnotherInterface extension = Mockito.mock(AnotherInterface.class);

        AnotherInterface extended = Extender.extend(AnotherInterface.class, base, extension);

        Mockito.when(extension.anotherMethod(1)).thenReturn("returned");
        String result = extended.anotherMethod(1);
        assertEquals("returned", result);
    }

    @Test
    public void extendedShouldPreferExtensionOverBase() {
        SomeInterface base = Mockito.mock(SomeInterface.class);
        SomeInterface extension = Mockito.mock(SomeInterface.class);

        SomeInterface extended = Extender.extend(SomeInterface.class, base, extension);

        extended.someMethod();
        Mockito.verify(extension).someMethod();
    }

    @Test
    public void extendedCanBeNewInterface() {
        SomeInterface base = Mockito.mock(SomeInterface.class);
        AnotherInterface extension = Mockito.mock(AnotherInterface.class);

        JoinedInterface extended = Extender.extend(JoinedInterface.class, base, extension);

        Mockito.when(extension.anotherMethod(1)).thenReturn("returned");
        String result = extended.anotherMethod(1);
        assertEquals("returned", result);

        extended.someMethod();
        Mockito.verify(base).someMethod();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenMethodNotImplementedByBaseOrExtension() {
        SomeInterface base = Mockito.mock(SomeInterface.class);
        SomeInterface extension = Mockito.mock(SomeInterface.class);

        Extender.extend(JoinedInterface.class, base, extension);
    }

    @Test
    public void multipleExtendsShouldImplementAllInterfaces() {
        SomeInterface base = Mockito.mock(SomeInterface.class);
        AnotherInterface extension1 = Mockito.mock(AnotherInterface.class);
        YetAnotherInterface extension2 = Mockito.mock(YetAnotherInterface.class);

        JoinedInterface extended = Extender.extend(JoinedInterface.class, base, extension1);
        extended = Extender.extend(JoinedInterface.class, extended, extension2);

        assertTrue(extended instanceof YetAnotherInterface);

        Mockito.when(extension1.anotherMethod(1)).thenReturn("returned");
        String result = extended.anotherMethod(1);
        assertEquals("returned", result);

        extended.someMethod();
        Mockito.verify(base).someMethod();

        ((YetAnotherInterface) extended).yetAnotherMethod("argument");
        Mockito.verify(extension2).yetAnotherMethod("argument");
    }

    private static interface SomeInterface {
        void someMethod();
    }

    private static interface AnotherInterface {
        String anotherMethod(int argument);
    }

    private static interface YetAnotherInterface {
        void yetAnotherMethod(String argument);
    }

    private static interface JoinedInterface extends SomeInterface, AnotherInterface {
    }
}
