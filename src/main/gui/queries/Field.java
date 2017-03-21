package main.gui.queries;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Text;

public class Field {
	private String key;
	private CCombo operator;
	private Text filter;
	private CCombo andOr;

	public Field(String key, CCombo operator, Text filter, CCombo andOr) {
		this.key = key;
		this.operator = operator;
		this.filter = filter;
		this.andOr = andOr;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOperator() {
		return operator.getText();
	}

	public void setOperator(CCombo operator) {
		this.operator = operator;
	}

	public String getFilter() {
		return filter.getText();
	}

	public void setFilter(Text filter) {
		this.filter = filter;
	}

	public String getAndOr() {
		return andOr.getText();
	}
	
}
