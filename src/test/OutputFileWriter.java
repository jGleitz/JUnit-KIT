package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import test.framework.FrameworkException;

/**
 * Class to construct an output file giving information about the input and output of an test.
 * 
 * @author Joshua Gleitze
 * @version 1.0
 */
public class OutputFileWriter {
	private static final OutputFileWriter instance = new OutputFileWriter();
	private static final Map<String, Map<String, TestMethod>> CONTENT_MAP = new LinkedHashMap<>();
	private static final String DATETIME_FORMAT = "ddMMYYYY-HHmm";
	private static final String DATETIME_TAG = "%datetime";
	private static Date initDate;
	private static String outputFilePath;
	private static final OutputRunListener runListener = instance.new OutputRunListener();
	private static final String TEST_CLASS_NAME_TAG = "%testname";
	private static final String HTML_SPECIALCHARS = "[<>&\"]";
	private static String testClassName;
	private static boolean writeOutputFile;

	/**
	 * Tells the writer to document the last run.
	 * 
	 * @param args
	 *            The command line arguments the interactive console was called with.
	 */
	public static void documentRun(String[] args) {
		if (writeOutputFile) {
			String testMethodName = runListener.momentaryMethodName;
			String testMethodClassName = simpleName(runListener.momentaryClassName);

			// Generate the HTML
			String[] input = TestObject.getLastTerminal().getInput();
			String[][] output = TestObject.getLastTerminal().getOutputPerCommand();
			List<String> htmlElementList = new ArrayList<>();
			htmlElementList.add("<div class=\"session\">");
			htmlElementList.add("<pre class=\"terminal\">");
			htmlElementList.add(args(args));
			htmlElementList.addAll(output(output[0]));
			for (int i = 0; i < input.length; i++) {
				htmlElementList.add(input(input[i], (i == 0) ? "first" : ""));
				htmlElementList.addAll(output(output[i + 1]));
			}
			htmlElementList.add("</pre>");
			htmlElementList.add("</div>");
			String[] htmlElement = htmlElementList.toArray(new String[htmlElementList.size()]);

			getMethod(testMethodClassName, testMethodName).sessions.add(htmlElement);
		}
	}

	public static RunListener getListener() {
		return runListener;
	}

	/**
	 * Initialises the writer for the given test.
	 * 
	 * @param testClass
	 *            The class this output file should be constructed for.
	 */
	public static void initFor(Class<?> testClass) {
		String outputFileConfig = System.getProperty("outputFile");
		writeOutputFile = (outputFileConfig != null);
		if (writeOutputFile) {
			initDate = new Date();
			testClassName = testClass.getSimpleName();
			outputFileConfig = outputFileConfig.replace(TEST_CLASS_NAME_TAG, testClassName);
			String datetime = new SimpleDateFormat(DATETIME_FORMAT).format(initDate);
			outputFileConfig = outputFileConfig.replace(DATETIME_TAG, datetime);
			outputFilePath = outputFileConfig + ".html";
		}
	}

	/**
	 * If the user has provided the {@code -DoutputFile} parameter, this method sgenerates the output file with the
	 * provided content at the specified location. Calling this method has no effect otherwise.
	 */
	public static void writeFile() {
		if (writeOutputFile) {
			PrintWriter outputWriter;
			try {
				outputWriter = new PrintWriter(outputFilePath, "UTF-8");
				write(head(), outputWriter);
				int id = 0;
				for (String testClass : CONTENT_MAP.keySet()) {
					Map<String, TestMethod> classMap = CONTENT_MAP.get(testClass);
					write(new String[] {
							"<div class=\"testClass\">",
							"<h3>" + testClass + "</h3>"
					}, outputWriter);

					for (TestMethod testMethod : classMap.values()) {
						String indicator = "<span class=\"successIndicator "
								+ ((testMethod.success) ? "succeeded" : "failed") + "\">⏺</span>";
						write(new String[] {
								"<div class=\"testMethod closed\" id=\"" + id + "\">",
								"<h4 onclick=\"toggleMethod(" + id + ");\"><span class=\"opener\">⏵</span>"
										+ testMethod.name + indicator + "</h4>",
								"<div class=\"sessions\">"
						}, outputWriter);
						for (String[] session : testMethod.sessions) {
							write(session, outputWriter);
						}
						if (!testMethod.success) {
							write(testMethod.failureHTML, outputWriter);
						}
						write(new String[] {
								"</div>",
								"</div>"
						}, outputWriter);
						id++;
					}
					write(new String[] {
						"</div>"
					}, outputWriter);
				}
				write(inputFiles(), outputWriter);
				write(tail(), outputWriter);
				outputWriter.close();
			} catch (FileNotFoundException e) {
				throw new FrameworkException("We cannot generate an output file, as we\"re unable to write the file "
						+ outputFilePath);
			} catch (UnsupportedEncodingException e) {
				throw new FrameworkException(
						"We cannot generate an output file, as your system does not support UTF-8!");
			}
		}
	}

	private static void addAllElementsTo(String[] array, List<String> list) {
		list.addAll(Arrays.asList(array));
	}

	private static String args(String[] args) {
		StringBuilder argsLineBuilder = new StringBuilder();
		argsLineBuilder.append(testClassName);
		for (String a : args) {
			argsLineBuilder.append(" ");
			if (Input.isFile(a)) {
				argsLineBuilder.append("<span class=\"textFileLink\" onclick=\"showTextFile('");
				argsLineBuilder.append(getFileId(a));
				argsLineBuilder.append("');\">");
				argsLineBuilder.append(escapeHTML(a));
				argsLineBuilder.append("</span>");
			} else {
				argsLineBuilder.append(escapeHTML(a));
			}
		}
		return "<span class=\"args\">" + argsLineBuilder.toString() + "</span>";
	}

	private static String getFileId(String filePath) {
		String result = escapeHTML(filePath);
		result = result.replaceAll("[^a-z0-9\\-_:\\.]", "\\-");
		return result;
	}

	private static String[] head() {
		return new String[] {
				"<!DOCTYPE html>",
				"<html>",
				"	<head>",
				"               <meta charset=\"utf-8\">",
				"		<title>Test results of " + testClassName + "</title>",
				"               <link href=\"http://fonts.googleapis.com/css?family=Ubuntu:400,300,400italic|Ubuntu+Mono|Ubuntu+Condensed\" rel=\"stylesheet\" type=\"text/css\">",
				"               <style>",
				"                       * {",
				"                               font-family: \"Ubuntu\", sans-serif;",
				"                               box-sizing: border-box;",
				"                       }",
				"                       ",
				"                       .leftSide {",
				"                       		width: 60%;",
				"                       }",
				"                       ",
				"                       .rightSide {",
				"                       		width: 40%;",
				"                       		position: fixed;",
				"                       		right: 0px;",
				"                       		top: 180px;",
				"                       		bottom: 50px;",
				"                       }",
				"                       ",
				"                       h1 {",
				"                               font-weight: 300;",
				"                               font-size: 60px;",
				"                               margin-bottom: 10px;",
				"                       }",
				"                       ",
				"                       h2 {",
				"                               font-family: \"Ubuntu Condensed\", sans-serif;",
				"                               margin-top: 10px;",
				"                               font-size: 18px;",
				"                               margin-bottom: 50px;",
				"                       }",
				"                       ",
				"                       h3 {",
				"                               font-weight: 300;",
				"                               font-size: 30px;",
				"                               margin: 0;",
				"                               margin-bottom: 30px;",
				"                       }",
				"                       ",
				"                       h4 {",
				"                               font-weight: 700;",
				"                               font-size: 16px;",
				"                               margin: 0;",
				"                               cursor: pointer;",
				"                       }",
				"                       ",
				"                       h1, h2 {",
				"                               width: 100%;",
				"                               text-align: center;",
				"                       }",
				"                       ",
				"                       .testClass {",
				"                               margin: 25px 25px;",
				"                               border: 1px solid #666;",
				"                               border-radius: 30px;",
				"                               padding: 30px 40px;",
				"                               font-size: 15px;",
				"                       }",
				"                       ",
				"                       .testMethod {",
				"                               margin-top: 17px;",
				"                       }",
				"                       ",
				"                       .testMethod .sessions {",
				"                               overflow: hidden;",
				"                               padding-bottom: 20px;",
				"                               padding-left: 25px;",
				"                       }",
				"                       ",
				"                       .testMethod.closed .sessions {",
				"                               height: 0px;",
				"                               margin-bottom: 0px;",
				"                               padding-bottom: 0px;",
				"                       }",
				"                       ",
				"                       .input, .output, .args, .failure, .inputFile, .textFileLink {",
				"                               font-family: \"Ubuntu Mono\";",
				"                               display: inline-block;",
				"                       }",
				"                       ",
				"                       .terminal, .failure {",
				"                               background: #AAA;",
				"                               border: 2px solid #666;",
				"                               border-radius: 15px;",
				"                               padding: 20px 30px;",
				"                               overflow: auto;",
				"                       }",
				"                       ",
				"                       .args {",
				"                               color: #704;",
				"                               margin-bottom: 12px;",
				"                       }",
				"                       ",
				"                       .input {",
				"                               color: #070;",
				"                               margin-top: 8px;",
				"                       }",
				"                       ",
				"                       .input.first {",
				"                               margin-top: 0;",
				"                       }",
				"                       ",
				"                       .output {",
				"                               color: #000;",
				"                       }",
				"                       ",
				"                       .output.error {",
				"                               color: #A00;",
				"                       }",
				"                       ",
				"                       .successIndicator {",
				"                               display: inline-block;",
				"                               margin-left: 10px;",
				"                       }",
				"                       ",
				"                       .successIndicator.succeeded {",
				"                               color: green;",
				"                       }",
				"                       ",
				"                       .successIndicator.failed {",
				"                               color: red;",
				"                       }",
				"                       ",
				"                       .opener {",
				"                               display: inline-block;",
				"                               margin-right: 10px;",
				"                       }",
				"                       ",
				"                       .testMethod.closed .opener {",
				"                               transform: rotate(0deg);",
				"                       }",
				"                       ",
				"                       .testMethod .opener {",
				"                               transform: rotate(90deg);",
				"                       }",
				"                       ",
				"                       .failure {",
				"                               display: block;",
				"                               border: 2px solid red;",
				"                               background: #D99;",
				"                       }",
				"                       ",
				"                       .inputFile {",
				"                               display: none;",
				"                               border: 2px solid #666;",
				"                               background: #FFF;",
				"                               color: #000;",
				"                               position: absolute;",
				"                               left: 15px;",
				"                               right: 15px;",
				"                               border-radius: 15px;",
				"                               padding: 20px 30px;",
				"                               overflow: auto;",
				"                               max-height: 100%;",
				"                       }",
				"                       ",
				"                       .inputFile.visible {",
				"                               display: block;",
				"                       }",
				"                       ",
				"						.textFileLink {",
				"                       	cursor: pointer;",
				"                       }",
				"                       ",
				"						.textFileLink:hover {",
				"                       	text-decoration: underline;",
				"                       }",
				"                       ",
				"						.textFileDescription {",
				"                       	font-style: italic;",
				"                       	position: absolute;",
				"                       	left: 20px;",
				"                       }",
				"               </style>",
				"               <script>",
				"                       var momentaryTextFile;",
				"                       function toggleMethod(id) {",
				"                               var element = document.getElementById(id);",
				"                               element.className = (element.className.indexOf('closed') > -1) ? 'testMethod' : 'testMethod closed';",
				"                       }",
				"                       function showTextFile(path) {",
				"                               if (momentaryTextFile !== undefined) {",
				"                               	momentaryTextFile.className = 'inputFile';",
				"                               };",
				"                               momentaryTextFile = document.getElementById(path);",
				"                               momentaryTextFile.className = 'inputFile visible';",
				"                       }",
				"               </script>",
				"	</head>",
				"	<body>",
				"		<h1>" + testClassName + "</h1>",
				"		<h2> run on " + new SimpleDateFormat("EEEE, d.M.YYYY HH:mm:ss", Locale.ENGLISH).format(initDate)
						+ "</h2>",
				"		<div class=\"leftSide\">",
		};
	}

	private static String[] inputFiles() {
		List<String> resultList = new ArrayList<>();
		addAllElementsTo(new String[] {
				"</div>",
				"<div class=\"rightSide\">",
				"<p class=\"textFileDescription\">Click on an input file to show it here</p>"
		}, resultList);
		for (String filePath : Input.getAllFilePaths()) {
			addAllElementsTo(new String[] {
				"<pre class=\"inputFile\" id=\"" + getFileId(filePath) + "\">"
			}, resultList);
			addAllElementsTo(escapeHTMLLines(Input.getFileContentOf(filePath)), resultList);
			addAllElementsTo(new String[] {
				"</pre>"
			}, resultList);
		}
		return resultList.toArray(new String[resultList.size()]);
	}

	private static String input(String inputCommand, String additionalClassName) {
		String className = "input" + ((additionalClassName.length() > 0) ? " " + additionalClassName : "");
		return "<span class=\"" + className + "\">" + escapeHTML(inputCommand) + "</span>";
	}

	private static List<String> output(String[] outputLines) {
		ArrayList<String> result = new ArrayList<>();
		if (outputLines.length > 0) {
			addAllElementsTo(escapeHTMLLines(outputLines), result);
			String spanclass = (outputLines[0].startsWith("Error,") ? "output error" : "output");
			result.set(0, "<span class=\"" + spanclass + "\">" + result.get(0));
			result.set(result.size() - 1, result.get(result.size() - 1) + "</span>");
		}
		return result;
	}

	private static String[] tail() {
		return new String[] {
				"		</div>",
				"	</body>",
				"</html>"
		};
	}

	private static TestMethod getMethod(String className, String methodName) {
		Map<String, TestMethod> classMap = CONTENT_MAP.get(className);
		if (classMap == null) {
			classMap = new LinkedHashMap<String, TestMethod>();
			CONTENT_MAP.put(className, classMap);
		}
		TestMethod testMethod = CONTENT_MAP.get(className).get(methodName);
		if (testMethod == null) {
			testMethod = instance.new TestMethod(methodName);
			CONTENT_MAP.get(className).put(methodName, testMethod);
		}
		return testMethod;
	}

	private static void write(String[] lines, PrintWriter writer) {
		for (String line : lines) {
			writer.println(line);
		}
	}

	private static String[] failureHTML(String failureMessage) {
		return new String[] {
				"<pre class=\"failure\">",
				escapeHTML(failureMessage),
				"</pre>"
		};
	}

	private static String escapeHTML(String s) {
		StringBuilder out = new StringBuilder();
		for (char c : s.toCharArray()) {
			if (Character.toString(c).matches(HTML_SPECIALCHARS)) {
				out.append("&#");
				out.append((int) c);
				out.append(';');
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}

	private static String[] escapeHTMLLines(String[] lines) {
		String[] result = new String[lines.length];
		for (int i = 0; i < lines.length; i++) {
			result[i] = escapeHTML(lines[i]);
		}
		return result;
	}

	private static String simpleName(String className) {
		return className.substring(className.lastIndexOf(".") + 1);
	}

	private class OutputRunListener extends RunListener {
		private String momentaryClassName;
		private String momentaryMethodName;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.junit.runner.notification.RunListener#testFailure(org.junit.runner.notification.Failure)
		 */
		@Override
		public void testFailure(Failure failure) throws Exception {
			String className = simpleName(failure.getDescription().getClassName());
			String methodName = failure.getDescription().getMethodName();
			TestMethod method = getMethod(className, methodName);
			method.success = false;
			method.failureHTML = failureHTML(failure.getMessage());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.junit.runner.notification.RunListener#testStarted(org.junit.runner.Description)
		 */
		@Override
		public void testStarted(Description description) throws Exception {
			momentaryMethodName = description.getMethodName();
			momentaryClassName = description.getClassName();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.junit.runner.notification.RunListener#testRunFinished(org.junit.runner.Result)
		 */
		@Override
		public void testRunFinished(Result result) throws Exception {
			writeFile();
		}
	}

	private class TestMethod {
		private String name;
		private boolean success = true;
		private List<String[]> sessions = new ArrayList<>();
		private String[] failureHTML;

		private TestMethod(String name) {
			this.name = name;
		}
	}

}