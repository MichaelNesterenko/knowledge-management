package mn.krr.javakrr.parser;

import java.nio.file.Paths;
import java.util.Objects;

import org.apache.jena.rdf.model.Resource;

import mn.krr.javakrr.ApplicationModel;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;

public class JavaSourceParser {

	private ApplicationModel applicationModel;

	public JavaSourceParser(ApplicationModel applicationModel) {
		this.applicationModel = Objects.requireNonNull(applicationModel);
	}

	public void parse() {
		var javaModel = createJavaModel();

		javaModel.getAllTypes().stream()
			.filter(s -> "mn.krr.javakrr.fortest.TestClass".equals(s.getQualifiedName()))
			.forEach(s -> fillJavaClassInfo(s));
	}

	private CtModel createJavaModel() {
		var l = new Launcher();

		l.addInputResource(Paths.get(".", "sources").toString());

		return l.buildModel();
	}

	private void fillJavaClassInfo(CtType<?> javaType) {
		var javaClassResource = toJavaClassResource(javaType);

		javaType.getAllMethods().stream()
			.filter(m -> !m.getDeclaringType().getQualifiedName().startsWith("java."))
			.forEach(m -> fillJavaMethodInfo(m, javaClassResource));
	}

	private Resource toJavaClassResource(CtType<?> javaType) {
		return applicationModel.createChildResource(
			applicationModel.getApplication(),
			"javaClass:" + javaType.getQualifiedName()
		);
	}

	private void fillJavaMethodInfo(CtMethod<?> javaMethod, Resource javaClassResource) {
		var javaMethodResource = toJavaMethodResource(javaMethod, javaClassResource);

		javaMethod.asIterable().forEach(mi -> fillMethodInternalsInfo(mi, javaMethodResource));
	}

	private Resource toJavaMethodResource(CtMethod<?> javaMethod, Resource javaClassResource) {
		return applicationModel.createChildResource(
			javaClassResource,
			"javaMethod:" + javaMethod.getDeclaringType().getQualifiedName() + "." + javaMethod.getSimpleName()
		);
	}

	@SuppressWarnings("rawtypes")
	private void fillMethodInternalsInfo(CtElement methodInstruction, Resource javaMethodResource) {
		if (methodInstruction instanceof CtTypeReference) {
			var targetType = ((CtTypeReference) methodInstruction).getTypeDeclaration();

			if (targetType != null) {
				javaMethodResource.addProperty(
					applicationModel.createProperty("hasDependency"),
					toJavaClassResource(targetType)
				);
			}
		} else if (methodInstruction instanceof CtAnnotation) {
			var annotation = (CtAnnotation) methodInstruction;
			javaMethodResource.addProperty(
				applicationModel.createProperty("hasAnnotation"),
				annotation.getType().getQualifiedName()
			);
		}
	}
}
