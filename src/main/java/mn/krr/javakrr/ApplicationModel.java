package mn.krr.javakrr;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class ApplicationModel {

	private static final String PREFIX = "http://discovery-automation#";

	private static final String APPLICATION_URI = withPrefix("application");

	private static final String PROP_OWNS = "owns";

	private Model model = ModelFactory.createDefaultModel();

	public ApplicationModel() {
		initModel();
	}

	private void initModel() {
		model.createResource(APPLICATION_URI);
	}

	public Resource getApplication() {
		return model.getResource(APPLICATION_URI);
	}

	public Resource createChildResource(Resource parent, String name) {
		var resource = createResource(name);
		parent.addProperty(createProperty(PROP_OWNS), resource);
		return resource;
	}

	public Resource createResource(String name) {
		return model.createResource(withPrefix(name));
	}

	public Property createProperty(String name) {
		return model.createProperty(withPrefix(name));
	}

	public void serializeModel() {
		model.write(System.out);
	}

	public static final String withPrefix(String uri) {
		return PREFIX + uri;
	}

}
