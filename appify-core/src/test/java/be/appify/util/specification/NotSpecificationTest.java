package be.appify.util.specification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import be.appify.util.specification.Specification;
import be.appify.util.specification.SpecificationExtender;

public class NotSpecificationTest {
    private Specification<Object> specification;
    private Specification<Object> inverse;
    private Object subject;

    @Before
    @SuppressWarnings("unchecked")
    public void before() {
        specification = Mockito.mock(Specification.class);
        inverse = SpecificationExtender.not(specification);
        subject = new Object();
    }

    @Test
    public void isSatisfiedShouldReturnTrueSpecificationsIsNotSatisfied() {
        Mockito.when(specification.isSatisfiedBy(subject)).thenReturn(false);

        assertTrue(inverse.isSatisfiedBy(subject));
    }

    @Test
    public void isSatisfiedShouldReturnFalseWhenSpecificationIsSatisfied() {
        Mockito.when(specification.isSatisfiedBy(subject)).thenReturn(true);

        assertFalse(inverse.isSatisfiedBy(subject));
    }
}
