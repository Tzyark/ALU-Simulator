import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AdderTest.class, AriRightShiftTest.class, ClaAdderTest.class, FloatAdditionTest.class,
		FloatDivisionTest.class, FloatMultiplicationTest.class, FloatRepresentationTest.class,
		FloatSubtractionTest.class, FloatTrueValueTest.class, FullAdderTest.class, Ieee754Test.class,
		IntegerAdditionTest.class, IntegerDivisionTest.class, IntegerMultiplicationTest.class,
		IntegerRepresentationTest.class, IntegerSubtractionTest.class, IntegerTrueValueTest.class, LeftShiftTest.class,
		LogRightShiftTest.class, NegationTest.class, OneAdderTest.class, SignedAdditionTest.class })
public class AllTests {

}