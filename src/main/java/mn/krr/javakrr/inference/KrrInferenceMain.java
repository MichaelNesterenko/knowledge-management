package mn.krr.javakrr.inference;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

public class KrrInferenceMain {
	public static void main(String[] args) {
		var inferenceModel = ModelFactory.createInfModel(
			createReasoner("/inference.rules"),
			createDataModel("/model.ttl")
		);

		var conclusion = inferenceModel.getResource("http://discovery.inference#conclusion");

		inferenceModel.listStatements(
			new SimpleSelector(conclusion, (Property)null, (RDFNode)null)
		).forEach(System.out::println);
	}

	private static Reasoner createReasoner(String rulesResource) {
		return new GenericRuleReasoner(Rule.rulesFromURL(resourceUrl(rulesResource)));
	}

	private static Model createDataModel(String modelTtlResource) {
		var model = ModelFactory.createDefaultModel();
		model.read(resourceUrl(modelTtlResource), "TURTLE");
		return model;
	}

	private static String resourceUrl(String resource) {
		return KrrInferenceMain.class.getResource(resource).toString();
	}
}
