package be.appify.util.specification;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import be.appify.util.specification.Specification;
import be.appify.util.specification.SpecificationExtender;

public class OrSpecificationTest {
    private Specification<Object> specification1;
    private Specification<Object> specification2;
    private Specification<Object> both;
    private Object subject;

    @Before
    @SuppressWarnings("unchecked")
    public void before() {
        specification1 = Mockito.mock(Specification.class);
        specification2 = Mockito.mock(Specification.class);
        both = SpecificationExtender.or(specification1, specification2);
        subject = new Object();
    }

    @Test
    public void isSatisfiedShouldReturnTrueWhenBothSpecificationsAreSatisfied() {
        Mockito.when(specification1.isSatisfiedBy(subject)).thenReturn(true);
        Mockito.when(specification2.isSatisfiedBy(subject)).thenReturn(true);

        assertTrue(both.isSatisfiedBy(subject));
    }

    @Test
    public void isSatisfiedShouldReturnTrueWhenOneSpecificationIsNotSatisfied() {
        Mockito.when(specification1.isSatisfiedBy(subject)).thenReturn(true);
        Mockito.when(specification2.isSatisfiedBy(subject)).thenReturn(false);

        assertTrue(both.isSatisfiedBy(subject));
    }

    @Test
    public void isSatisfiedShouldReturnFalseWhenBothSpecificationsAreNotSatisfied() {
        Mockito.when(specification1.isSatisfiedBy(subject)).thenReturn(false);
        Mockito.when(specification2.isSatisfiedBy(subject)).thenReturn(false);

        assertFalse(both.isSatisfiedBy(subject));
    }
}
