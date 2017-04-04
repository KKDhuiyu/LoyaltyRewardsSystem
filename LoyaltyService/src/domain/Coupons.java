package domain;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "product_summaries")
public class Coupons {

	@XmlElement(name="product_summary")
	private final Collection<Transactions> summaries = new ArrayList<>();

	public Coupons() {
	}

	public Collection<Transactions> getSummaries() {
		return summaries;
	}

	public void add(Transactions productSummary) {
		summaries.add(productSummary);
	}

}
