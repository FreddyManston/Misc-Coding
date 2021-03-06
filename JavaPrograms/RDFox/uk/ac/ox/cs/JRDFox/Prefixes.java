// JRDFox(c) Copyright University of Oxford, 2013. All Rights Reserved.

package uk.ac.ox.cs.JRDFox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for abbreviating IRIs. The resulting IRIs can be either<br>
 * 1) &lt;uri&gt; or<br>
 * 2) prefix-name:local-name where prefix-name can be empty.<br>
 * Forms 1 and 2 are dependent upon a set of prefix declarations that associates prefix names with prefix IRIs.
 * A IRI abbreviated using form 2 that uses an unregistered prefix is invalid---expanding it will result in an exception.
 * Neither prefixes nor local names may contain colon characters. The grammar used for various parts of the IRIs is as follows:<br>
 * PN_CHARS_BASE ::= [A-Z] | [a-z] | [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x02FF] | [#x0370-#x037D] | [#x037F-#x1FFF] |
 *                   [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]<br>
 * PN_CHARS      ::= PN_CHARS_BASE | '_' | '-' | [0-9] | #x00B7 | [#x0300-#x036F] | [#x203F-#x2040]<br>
 * PN_LOCAL      ::= ( PN_CHARS_BASE | '_' | [0-9] ) ( ( PN_CHARS | '.' )* PN_CHARS )?<br>
 * PN_PREFIX     ::= PN_CHARS_BASE ( ( PN_CHARS | '.' )* PN_CHARS )?<br>
 */
public class Prefixes implements Serializable {
	private static final long serialVersionUID = -158185482289831766L;

	protected static final String PN_CHARS_BASE = "[A-Za-z\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";
	protected static final String PN_CHARS = "[A-Za-z0-9_\\u002D\\u00B7\\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0300-\\u036F\\u0370-\\u037D\\u037F-\\u1FFF\\u200C-\\u200D\\u203F-\\u2040\\u2070-\\u218F\\u2C00-\\u2FEF\\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD]";
	protected static final Pattern s_localNameChecker = Pattern.compile("(" + PN_CHARS_BASE + "|_|[0-9])((" + PN_CHARS + "|[.])*(" + PN_CHARS + "))?");
	public static final Map<String, String> s_semanticWebPrefixes;
	static {
		s_semanticWebPrefixes = new HashMap<String, String>();
		s_semanticWebPrefixes.put("rdf:", Namespaces.RDF_NS);
		s_semanticWebPrefixes.put("rdfs:", Namespaces.RDFS_NS);
		s_semanticWebPrefixes.put("owl:", Namespaces.OWL_NS);
		s_semanticWebPrefixes.put("xsd:", Namespaces.XSD_NS);
		s_semanticWebPrefixes.put("swrl:", Namespaces.SWRL_NS);
		s_semanticWebPrefixes.put("swrlb:", Namespaces.SWRLB_NS);
		s_semanticWebPrefixes.put("swrlx:", Namespaces.SWRLX_NS);
		s_semanticWebPrefixes.put("ruleml:", Namespaces.RULEML_NS);
	}

	public static final Prefixes DEFAULT_IMMUTABLE_INSTANCE;
	public static final Prefixes EMPTY_IMMUTABLE_INSTANCE;
	static {
		EMPTY_IMMUTABLE_INSTANCE = new Prefixes(new Prefixes()) {
			private static final long serialVersionUID = 1L;

			protected boolean declarePrefixRaw(String prefixName, String prefixIRI) {
				throw new UnsupportedOperationException("This Prefixes object is immutable.");
			}
		};
		Prefixes prefixes = new Prefixes();
		prefixes.declareSemanticWebPrefixes();
		DEFAULT_IMMUTABLE_INSTANCE = new Prefixes(prefixes) {
			private static final long serialVersionUID = 1L;

			protected boolean declarePrefixRaw(String prefixName, String prefixIRI) {
				throw new UnsupportedOperationException("This Prefixes object is immutable.");
			}
		};
	}

	protected final Map<String, String> m_prefixIRIsByPrefixName;
	protected final Map<String, String> m_prefixNamesByPrefixIRI;
	protected Pattern m_prefixIRIMatchingPattern;

	public Prefixes() {
		m_prefixIRIsByPrefixName = new TreeMap<String, String>();
		m_prefixNamesByPrefixIRI = new TreeMap<String, String>();
		buildPrefixIRIMatchingPattern();
	}

	public Prefixes(Prefixes prefixes) {
		m_prefixIRIsByPrefixName = new TreeMap<String, String>(prefixes.m_prefixIRIsByPrefixName);
		m_prefixNamesByPrefixIRI = new TreeMap<String, String>(prefixes.m_prefixNamesByPrefixIRI);
		buildPrefixIRIMatchingPattern();
	}

	protected void buildPrefixIRIMatchingPattern() {
		List<String> list = new ArrayList<String>(m_prefixNamesByPrefixIRI.keySet());
		// Sort the prefix IRIs, longest first
		Collections.sort(list, new Comparator<String>() {
			public int compare(String lhs, String rhs) {
				return rhs.length() - lhs.length();
			}
		});
		StringBuilder pattern = new StringBuilder("^(");
		boolean didOne = false;
		for (String prefixIRI : list) {
			if (didOne)
				pattern.append("|(");
			else {
				pattern.append("(");
				didOne = true;
			}
			pattern.append(Pattern.quote(prefixIRI));
			pattern.append(")");
		}
		pattern.append(")");
		if (didOne)
			m_prefixIRIMatchingPattern = Pattern.compile(pattern.toString());
		else
			m_prefixIRIMatchingPattern = null;
	}

	public String abbreviateIRI(String iri) {
		if (m_prefixIRIMatchingPattern != null) {
			Matcher matcher = m_prefixIRIMatchingPattern.matcher(iri);
			if (matcher.find()) {
				String localName = iri.substring(matcher.end());
				if (isValidLocalName(localName)) {
					String prefix = m_prefixNamesByPrefixIRI.get(matcher.group(1));
					return prefix + localName;
				}
			}
		}
		return "<" + iri + ">";
	}
	
	public void appendAbbreviatedIRI(StringBuilder builder, String iri) {
		if (m_prefixIRIMatchingPattern != null) {
			Matcher matcher = m_prefixIRIMatchingPattern.matcher(iri);
			if (matcher.find()) {
				String localName = iri.substring(matcher.end());
				if (isValidLocalName(localName)) {
					String prefix = m_prefixNamesByPrefixIRI.get(matcher.group(1));
					builder.append(prefix);
					builder.append(localName);
					return;
				}
			}
		}
		builder.append('<');
		builder.append(iri);
		builder.append('>');
	}

	public String expandAbbreviatedIRI(String abbreviation) {
		if (abbreviation.length() > 0 && abbreviation.charAt(0) == '<') {
			if (abbreviation.charAt(abbreviation.length() - 1) != '>')
				throw new IllegalArgumentException("The string '" + abbreviation + "' is not a valid abbreviation: IRIs must be enclosed in '<' and '>'.");
			return abbreviation.substring(1, abbreviation.length() - 1);
		}
		else {
			int pos = abbreviation.indexOf(':');
			if (pos != -1) {
				String prefix = abbreviation.substring(0, pos + 1);
				String prefixIRI = m_prefixIRIsByPrefixName.get(prefix);
				if (prefixIRI == null) {
					// Catch the common error of not quoting IRIs starting with
					// http:
					if (prefix == "http:")
						throw new IllegalArgumentException("The IRI '" + abbreviation + "' must be enclosed in '<' and '>' to be used as an abbreviation.");
					throw new IllegalArgumentException("The string '" + prefix + "' is not a registered prefix name.");
				}
				return prefixIRI + abbreviation.substring(pos + 1);
			}
			else
				throw new IllegalArgumentException("The abbreviation '" + abbreviation + "' is not valid (it does not start with a colon).");
		}
	}

	public boolean canBeExpanded(String iri) {
		if (iri.length() > 0 && iri.charAt(0) == '<')
			return false;
		else {
			int pos = iri.indexOf(':');
			if (pos != -1) {
				String prefix = iri.substring(0, pos + 1);
				return m_prefixIRIsByPrefixName.get(prefix) != null;
			}
			else
				return false;
		}
	}

	public boolean declarePrefix(String prefixName, String prefixIRI) {
		boolean prefixNameIsNew = declarePrefixRaw(prefixName, prefixIRI);
		buildPrefixIRIMatchingPattern();
		return prefixNameIsNew;
	}

	protected boolean declarePrefixRaw(String prefixName, String prefixIRI) {
		if (!prefixName.endsWith(":"))
			throw new IllegalArgumentException("Prefix name '" + prefixName + "' should end with a colon character.");
		String existingPrefixName = m_prefixNamesByPrefixIRI.get(prefixIRI);
		if (existingPrefixName != null && !prefixName.equals(existingPrefixName))
			throw new IllegalArgumentException("The prefix IRI '" + prefixIRI + "' has already been associated with the prefix name '" + existingPrefixName + "'.");
		m_prefixNamesByPrefixIRI.put(prefixIRI, prefixName);
		return m_prefixIRIsByPrefixName.put(prefixName, prefixIRI) == null;
	}

	public String guessPrefixIRIAndRegisterIfNeeded(String iri, String prefixName) {
		int breakingPoint = iri.lastIndexOf('#');
		if (breakingPoint == -1 || !isValidLocalName(iri.substring(breakingPoint + 1))) {
			breakingPoint = iri.lastIndexOf('/');
			if (breakingPoint == -1 || !isValidLocalName(iri.substring(breakingPoint + 1)))
				return null;
		}
		String prefixIRI = iri.substring(0, breakingPoint + 1);
		if (prefixIRI.equals("") || m_prefixNamesByPrefixIRI.containsKey(prefixIRI))
			return null;
		else {
			declarePrefix(prefixName, prefixIRI);
			return prefixIRI;
		}
	}

	public boolean declareDefaultPrefix(String defaultPrefixIRI) {
		return declarePrefix(":", defaultPrefixIRI);
	}

	public Map<String, String> getPrefixIRIsByPrefixName() {
		return java.util.Collections.unmodifiableMap(m_prefixIRIsByPrefixName);
	}

	public String getPrefixIRI(String prefixName) {
		return m_prefixIRIsByPrefixName.get(prefixName);
	}

	public boolean isPrefixNameRegistered(String prefixName) {
		return m_prefixIRIsByPrefixName.containsKey(prefixName);
	}

	public String getPrefixName(String prefixIRI) {
		return m_prefixNamesByPrefixIRI.get(prefixIRI);
	}

	public Set<Map.Entry<String, String>> getPrefixNameToPrefixIRIMappings() {
		return Collections.unmodifiableSet(m_prefixIRIsByPrefixName.entrySet());
	}

	public boolean declareSemanticWebPrefixes() {
		boolean somePrefixNameIsNew = false;
		for (Map.Entry<String, String> entry : s_semanticWebPrefixes.entrySet())
			if (declarePrefixRaw(entry.getKey(), entry.getValue()))
				somePrefixNameIsNew = true;
		buildPrefixIRIMatchingPattern();
		return somePrefixNameIsNew;
	}

	public boolean addPrefixes(Prefixes prefixes) {
		boolean somePrefixNameIsNew = false;
		for (Map.Entry<String, String> entry : prefixes.m_prefixIRIsByPrefixName.entrySet())
			if (declarePrefixRaw(entry.getKey(), entry.getValue()))
				somePrefixNameIsNew = true;
		buildPrefixIRIMatchingPattern();
		return somePrefixNameIsNew;
	}

	public String toString() {
		return m_prefixIRIsByPrefixName.toString();
	}

	public static boolean isValidLocalName(String localName) {
		return s_localNameChecker.matcher(localName).matches();
	}
}