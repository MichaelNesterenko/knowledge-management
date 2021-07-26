package mn.krr.javakrr;

import mn.krr.javakrr.parser.JavaSourceParser;

public class JavaKrrMain {
	public static void main(String[] args) {
		new JavaKrrMain().run();
	}

	private void run() {
		var model = createModel();

		var sourceParser = new JavaSourceParser(model);
		sourceParser.parse();

		model.serializeModel();
	}

	private ApplicationModel createModel() {
		return new ApplicationModel();
	}
}
