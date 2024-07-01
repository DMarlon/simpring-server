package unit.core.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import core.http.URI;
import core.http.URIProcessor;

public class URIProcessorTest {

	@Test
	void isMatchShouldReturnFalseWhenPatternIsNull() {
		URIProcessor processor = URIProcessor.create(null);

		Assertions.assertFalse(processor.isMatch(URI.create("")));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());
	}

	@Test
	void isMatchShouldReturnFalseWhenURIIsNull() {
		URIProcessor processor = URIProcessor.create(null);

		Assertions.assertFalse(processor.isMatch(null));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());
	}

	@Test
	void isMatchShouldReturnTrueWhenBothAreEmpty() {
		URIProcessor processor = URIProcessor.create("");

		Assertions.assertTrue(processor.isMatch(URI.create("")));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());
	}

	@Test
	void isMatchShouldReturnTrueAndIgnoreLastSlash() {
		URIProcessor processor = URIProcessor.create("/value");

		Assertions.assertTrue(processor.isMatch(URI.create("/value/")));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());
	}

	@Test
	void isMatchShouldReturnFalseWhenSizeIsDifferent() {
		URIProcessor processor = URIProcessor.create("/1/2/");

		Assertions.assertFalse(processor.isMatch(URI.create("/1/")));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());
	}

	@Test
	void isMatchShouldConsiderEmptyBracketsAsPath() {
		URIProcessor processor = URIProcessor.create("/{}");

		Assertions.assertFalse(processor.isMatch(URI.create("/1/")));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());

		Assertions.assertTrue(processor.isMatch(URI.create("/{}/")));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());
	}

	@Test
	void isMatchShouldReturnTrueAndReturnPathVariablesValuesWhenMatch() {
		String variable = "a";
		String value = "1";
		URIProcessor processor = URIProcessor.create("/{"+variable+"}");

		Assertions.assertTrue(processor.isMatch(URI.create("/" + value + "/")));
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());
		Assertions.assertFalse(processor.getPathVariables().isEmpty());
		Assertions.assertEquals(value, processor.getPathVariables().get(variable));

	}

	@Test
	void isMatchShouldReturnTrueAndReturnQueryParametersValuesWhenMatch() {
		String parameter = "param";
		URIProcessor processor = URIProcessor.create("/");

		Assertions.assertTrue(processor.isMatch(URI.create("/?"+parameter)));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertFalse(processor.getQueryParameters().isEmpty());
		Assertions.assertEquals("", processor.getQueryParameters().get(parameter));

		String value = "value";
		Assertions.assertTrue(processor.isMatch(URI.create("/?"+parameter+"="+value)));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertFalse(processor.getQueryParameters().isEmpty());
		Assertions.assertEquals(value, processor.getQueryParameters().get(parameter));
	}

	@Test
	void isMatchShouldCleanPathVariablesAndQueryParametersWhenCalledIsMatchAgain() {
		URIProcessor processor = URIProcessor.create("/{var}");

		Assertions.assertTrue(processor.isMatch(URI.create("/value1?param=value2")));
		Assertions.assertFalse(processor.getPathVariables().isEmpty());
		Assertions.assertFalse(processor.getQueryParameters().isEmpty());
		Assertions.assertEquals("value1", processor.getPathVariables().get("var"));
		Assertions.assertEquals("value2", processor.getQueryParameters().get("param"));

		Assertions.assertFalse(processor.isMatch(URI.create("/")));
		Assertions.assertTrue(processor.getPathVariables().isEmpty());
		Assertions.assertTrue(processor.getQueryParameters().isEmpty());

	}
}
