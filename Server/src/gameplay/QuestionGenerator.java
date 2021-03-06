package gameplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {
	private static QuestionGenerator instance = null;
	private ArrayList<CountryInformation> info;
	
	protected QuestionGenerator() {}
	
	public static QuestionGenerator getInstance() {
		if (instance == null) {
			instance = new QuestionGenerator();
		}
		return instance;
	}
	
	private String getQuestion(CountryInformation country, InformationType qtype) {
		switch(qtype) {
			case NAME:
				return "Which country has its capital on the " + country.getCapital() + " ?";
			case CODE:
				return "Which is the currency code of " + country.getName() + " ?";
			case POPULATION:
				return "How many people are in " + country.getName() + " ?";
			case CONTINENT:
				return "In which continent is located " + country.getName() + " ?";
			case AREA:
				return "The area size of " + country.getName() + " in square kilometers is approx ...";
			case CAPITAL:
				return country.getName() + " has the capital at ...";
			default:
				return "N/A";
		}
	}
	
	private String getAnswer(CountryInformation country, InformationType qtype) {
		switch(qtype) {
			case NAME:
				return country.getName();
			case CODE:
				return country.getCode();
			case POPULATION:
				return country.getPopulation();
			case CONTINENT:
				return country.getContinent();
			case AREA:
				return country.getArea();
			case CAPITAL:
				return country.getCapital();
			default:
				return "N/A";
		}
	}
	
	public Question generateQuestion() {
		InformationType qtype = InformationType.randomType();
		ArrayList<CountryInformation> variants = new ArrayList<CountryInformation>(4);
		Random rand = new Random();
		int i = 0;
		while (i < 4) {
			CountryInformation cInfo = getInfo().get(rand.nextInt(getInfo().size()));
			if (!variants.contains(cInfo)) {
				
				// If a question contains an empty value, then check another possible
				// question.
				String possibleQuestionField = getQuestionUnique(cInfo, qtype);
				if (possibleQuestionField == null || possibleQuestionField.isEmpty()) {
					continue;
				}
				
				// If a new CountryInformation contains qtype value that already exists
				// in variants, we must check for another CountryInformation
				// e.g. Varinats [Europe, Oceania]. If new possible variant contains
				// Europe continent, is not a valid variant because Europe already exists.
				// Also, an empty qtype value is not a valid variant
				String possibleVariant = getAnswer(cInfo, qtype);
				Boolean exists = false;
				
				if (possibleVariant == null || possibleVariant.isEmpty()) {
					exists = true;
				} else {
					for (CountryInformation el : variants) {
						String eachVariant = getAnswer(el, qtype);
						if (possibleVariant.equals(eachVariant)) {
							exists = true;
						}
					}
				}
				
				if (!exists) {
					variants.add(cInfo);
					i++;
				}
			}
			
		}
		
		ArrayList<String> variantsAnswers = new ArrayList<String>(4);
		String solution = getAnswer(variants.get(0), qtype);
		String question = getQuestion(variants.get(0), qtype);
		for ( i = 0; i < 4; i++) {
			int pos = rand.nextInt(4 - i);			
			CountryInformation cInfo = variants.get(pos);
			variantsAnswers.add(getAnswer(cInfo, qtype));
			variants.remove(pos);
		}
		
		return new Question(question, variantsAnswers, solution, qtype.toString());
	}
	
	public String getQuestionUnique(CountryInformation country, InformationType qtype) {
		switch(qtype) {
		case NAME:
			return country.getCapital();
		case CODE:
			return country.getName();
		case POPULATION:
			return country.getName();
		case CONTINENT:
			return country.getName();
		case AREA:
			return country.getName();
		case CAPITAL:
			return country.getName();
		default:
			return "N/A";
	}
	}

	public void printCountriesInformation() {
		for(Iterator<CountryInformation> it = info.iterator(); it.hasNext(); ) {
			System.out.println(it.next().toString());
		}
	}
		
	public ArrayList<CountryInformation> getInfo() {
		return info;
	}

	public void setInfo(ArrayList<CountryInformation> info) {
		this.info = info;
	}
}

enum InformationType {
	NAME, CODE, POPULATION,
	CAPITAL, CONTINENT, AREA;
	
	private static final List<InformationType> VALUES = 
			Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	
	public static InformationType randomType()  {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}